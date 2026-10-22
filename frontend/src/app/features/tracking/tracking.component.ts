import { AfterViewInit, Component, ElementRef, HostListener, Input, OnChanges, OnDestroy, SimpleChanges, ViewChild } from "@angular/core";
import * as L from "leaflet";
import { Subscription, catchError, of, switchMap, timer } from "rxjs";
import { ActivatedRoute, RouterLink } from "@angular/router";
import { Shipment, ShipmentService } from "../shipments/shipment.service";
import { TrackingService, VehicleLocation } from "./tracking.service";

@Component({
  selector: "app-tracking",
  standalone: true,
  imports: [RouterLink],
  templateUrl: "./tracking.component.html",
  styleUrl: "./tracking.component.css",
})
export class TrackingComponent implements AfterViewInit, OnChanges, OnDestroy {
  @Input() shipmentId?: string;
  @Input() assignedVehicleId: string | null = null;
  @Input() shipmentStatus: string | null = null;
  @Input() embedded = false;

  @ViewChild("mapContainer", { static: true })
  private mapContainer!: ElementRef<HTMLDivElement>;
  @ViewChild("trackingContainer", { static: true })
  private trackingContainer!: ElementRef<HTMLElement>;

  shipment: Shipment | null = null;
  history: VehicleLocation[] = [];
  latestPosition: VehicleLocation | null = null;
  historyLoaded = false;
  latestLoaded = false;
  isFullscreen = false;
  errorMessage = "";

  private map?: L.Map;
  private routeLine?: L.Polyline;
  private truckMarker?: L.Marker;
  private routeSubscription?: Subscription;
  private shipmentSubscription?: Subscription;
  private historySubscription?: Subscription;
  private latestSubscription?: Subscription;
  private latestTimestamp: string | null = null;
  private viewInitialized = false;
  private mapResizeObserver?: ResizeObserver;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly shipmentService: ShipmentService,
    private readonly trackingService: TrackingService,
  ) {}

  get vehicleId(): string | null {
    if (this.embedded) {
      return this.assignedVehicleId?.trim() || null;
    }

    const vehicleId = this.shipment?.vehicleId?.trim();
    return vehicleId || null;
  }

  get noVehicleAssigned(): boolean {
    return this.embedded ? !this.vehicleId : Boolean(this.shipment && !this.vehicleId);
  }

  get noTrackingData(): boolean {
    return Boolean(
      (this.embedded || this.shipment) &&
        this.vehicleId &&
        this.isTrackingStatus(this.currentStatus) &&
        this.historyLoaded &&
        this.latestLoaded &&
        !this.latestPosition,
    );
  }

  private get currentStatus(): string | null {
    return this.embedded ? this.shipmentStatus : this.shipment?.status ?? null;
  }

  ngAfterViewInit(): void {
    this.viewInitialized = true;
    this.mapResizeObserver = new ResizeObserver(() => {
      this.map?.invalidateSize({ pan: false });
    });
    this.mapResizeObserver.observe(this.mapContainer.nativeElement);

    if (this.embedded) {
      if (this.shipmentId) {
        this.syncTrackingState(this.shipmentId);
      }
      return;
    }

    if (this.shipmentId) {
      this.loadShipment(this.shipmentId);
      return;
    }

    this.routeSubscription = this.route.paramMap.subscribe((params) => {
      const routeShipmentId = params.get("shipmentId");
      if (routeShipmentId) {
        this.loadShipment(routeShipmentId);
      }
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!this.embedded || !this.viewInitialized) {
      return;
    }

    const relevantInputChanged = Boolean(
      changes["shipmentId"] || changes["assignedVehicleId"] || changes["shipmentStatus"],
    );
    if (relevantInputChanged && this.shipmentId) {
      this.syncTrackingState(this.shipmentId);
    }
  }

  ngOnDestroy(): void {
    this.routeSubscription?.unsubscribe();
    this.shipmentSubscription?.unsubscribe();
    this.historySubscription?.unsubscribe();
    this.latestSubscription?.unsubscribe();
    this.mapResizeObserver?.disconnect();
    this.map?.remove();
    this.map = undefined;
  }

  async toggleFullscreen(): Promise<void> {
    const container = this.trackingContainer.nativeElement;
    if (document.fullscreenElement === container) {
      await document.exitFullscreen();
      return;
    }

    await container.requestFullscreen();
  }

  @HostListener("document:fullscreenchange")
  onFullscreenChange(): void {
    this.isFullscreen = document.fullscreenElement === this.trackingContainer.nativeElement;
    requestAnimationFrame(() => this.map?.invalidateSize());
  }

  private loadShipment(shipmentId: string): void {
    this.shipmentSubscription?.unsubscribe();
    this.historySubscription?.unsubscribe();
    this.latestSubscription?.unsubscribe();
    this.routeLine?.remove();
    this.truckMarker?.remove();
    this.routeLine = undefined;
    this.truckMarker = undefined;
    this.shipment = null;
    this.history = [];
    this.latestPosition = null;
    this.historyLoaded = false;
    this.latestLoaded = false;
    this.errorMessage = "";

    this.shipmentSubscription = this.shipmentService.getById(shipmentId).subscribe({
      next: (shipment) => {
        this.shipment = shipment;
        this.syncTrackingState(shipmentId);
      },
      error: () => {
        this.errorMessage = "Unable to load shipment details.";
      },
    });
  }

  private loadShipmentLocations(shipmentId: string): void {
    this.loadHistory(shipmentId);
    this.startLatestPolling(shipmentId);
  }

  private syncTrackingState(shipmentId: string): void {
    const hasVehicle = Boolean(this.vehicleId);

    if (!hasVehicle) {
      this.historySubscription?.unsubscribe();
      this.latestSubscription?.unsubscribe();
      this.routeLine?.remove();
      this.truckMarker?.remove();
      this.routeLine = undefined;
      this.truckMarker = undefined;
      this.history = [];
      this.latestPosition = null;
      this.latestTimestamp = null;
      this.historyLoaded = true;
      this.latestLoaded = true;
      this.map?.remove();
      this.map = undefined;
      return;
    }

    this.initializeMap();

    if (!this.isTrackingStatus(this.currentStatus)) {
      this.latestSubscription?.unsubscribe();
      this.latestSubscription = undefined;
      this.historyLoaded = false;
      this.latestLoaded = false;
      return;
    }

    if (this.currentStatus === "ARRIVED" || this.currentStatus === "DELIVERED") {
      this.latestSubscription?.unsubscribe();
      this.latestSubscription = undefined;
      this.loadHistory(shipmentId);
      this.loadLatestOnce(shipmentId);
    } else {
      this.loadShipmentLocations(shipmentId);
    }

    requestAnimationFrame(() => {
      requestAnimationFrame(() => this.map?.invalidateSize({ pan: false }));
    });
  }

  private isTrackingStatus(status: string | null): boolean {
    return status === "IN_TRANSIT" || status === "DELAYED" ||
      status === "ARRIVED" || status === "DELIVERED";
  }

  private initializeMap(): void {
    if (this.map) {
      return;
    }

    this.map = L.map(this.mapContainer.nativeElement, {
      zoomControl: false,
    }).setView([42.6977, 23.3219], 12);

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      maxZoom: 19,
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright" target="_blank" rel="noopener">OpenStreetMap</a> contributors',
    }).addTo(this.map);
    L.control.zoom({ position: "bottomright" }).addTo(this.map);
  }

  private loadHistory(shipmentId: string): void {
    this.historySubscription = this.trackingService.getHistory(shipmentId).subscribe({
      next: (locations) => {
        this.history = locations;
        this.historyLoaded = true;
        this.drawRoute();
      },
      error: () => {
        this.historyLoaded = true;
        this.history = [];
      },
    });
  }

  private startLatestPolling(shipmentId: string): void {
    this.latestSubscription?.unsubscribe();
    this.latestSubscription = timer(0, 5000)
      .pipe(
        switchMap(() =>
          this.trackingService.getLatest(shipmentId).pipe(
            catchError(() => of(null)),
          ),
        ),
      )
      .subscribe((position) => {
        this.latestLoaded = true;
        if (position) {
          this.latestPosition = position;
          this.updateTruckMarker(position);
          this.extendRoute(position);
        }
      });
  }

  private loadLatestOnce(shipmentId: string): void {
    this.latestSubscription?.unsubscribe();
    this.latestSubscription = this.trackingService.getLatest(shipmentId)
      .pipe(catchError(() => of(null)))
      .subscribe((position) => {
        this.latestLoaded = true;
        if (position) {
          this.latestPosition = position;
          this.updateTruckMarker(position);
          this.extendRoute(position);
        }
      });
  }

  private drawRoute(): void {
    if (!this.map || this.history.length === 0) {
      return;
    }

    const coordinates: L.LatLngExpression[] = this.history.map((point) => [
      point.latitude,
      point.longitude,
    ]);

    this.routeLine?.remove();
    this.routeLine = L.polyline(coordinates, {
      color: "#087f8c",
      weight: 5,
      opacity: 0.88,
      lineCap: "round",
      lineJoin: "round",
    }).addTo(this.map);
    this.latestTimestamp = this.history[this.history.length - 1].timestamp;
    this.map.fitBounds(this.routeLine.getBounds(), { padding: [72, 72], maxZoom: 15 });
  }

  private extendRoute(position: VehicleLocation): void {
    if (!this.map || position.timestamp === this.latestTimestamp) {
      return;
    }

    if (!this.routeLine) {
      this.routeLine = L.polyline([], {
        color: "#087f8c",
        weight: 5,
        opacity: 0.88,
        lineCap: "round",
        lineJoin: "round",
      }).addTo(this.map);
    }

    this.routeLine.addLatLng([position.latitude, position.longitude]);
    this.latestTimestamp = position.timestamp;
  }

  private updateTruckMarker(position: VehicleLocation): void {
    if (!this.map) {
      return;
    }

    const location: L.LatLngExpression = [position.latitude, position.longitude];

    if (!this.truckMarker) {
      const truckIcon = L.divIcon({
        className: "tracking-truck-icon",
        html: '<span style="display:grid;width:42px;height:42px;place-items:center;border:3px solid #fff;border-radius:50%;background:#d44f36;box-shadow:0 2px 9px #172b3a66"><svg viewBox="0 0 24 24" width="25" height="25" aria-hidden="true" fill="none" stroke="#fff" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h11v11H3zM14 10h4l3 3v4h-7z"/><circle cx="7.5" cy="18" r="1.5"/><circle cx="18" cy="18" r="1.5"/></svg></span>',
        iconSize: [48, 48],
        iconAnchor: [24, 24],
      });

      this.truckMarker = L.marker(location, { icon: truckIcon, title: this.vehicleId ?? undefined })
        .addTo(this.map)
        .bindTooltip(this.vehicleId ?? "Vehicle", { direction: "top", offset: [0, -22] });
      if (this.history.length === 0) {
        this.map.setView(location, 15);
      }
      return;
    }

    this.truckMarker.setLatLng(location);
  }
}