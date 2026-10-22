import { Component, OnDestroy, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ActivatedRoute } from "@angular/router";
import { ChangeDetectorRef } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { AuthService, UserProfile } from "../../../core/auth/auth.service";
import { TrackingComponent } from "../../tracking/tracking.component";

import {
  Shipment,
  ShipmentService,
  ShipmentHistory,
} from "../shipment.service";

@Component({
  selector: "app-shipment-details",
  imports: [CommonModule, FormsModule, TrackingComponent],
  templateUrl: "./shipment-details.html",
  styleUrl: "./shipment-details.scss",
})
export class ShipmentDetails implements OnDestroy, OnInit {
  shipment?: Shipment;
  history: ShipmentHistory[] = [];
  drivers: any[] = [];
  selectedDriverId = "";
  selectedDistanceKm = 0;
  stepPositions = [8, 25, 40, 68, 88, 98];
  client: any;
  clientUser?: UserProfile;
  errorMessage = "";
  successMessage = "";
  pendingEvent: string | null = null;
  private successMessageTimeout?: ReturnType<typeof setTimeout>;

  constructor(
    private route: ActivatedRoute,
    private shipmentService: ShipmentService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
  ) {}

  private loadData(id: string): void {
    this.shipmentService.getById(id).subscribe({
      next: (data) => {
        this.shipment = data;
        this.selectedDistanceKm = data.distanceKm ?? 0;
        this.loadClient(data.clientUserId);
        this.cdr.detectChanges();
      },
      error: (error) => this.showError(error),
    });

    this.shipmentService.getHistory(id).subscribe({
      next: (data) => {
        this.history = data;
        this.cdr.detectChanges();
      },
      error: (error) => this.showError(error),
    });
  }

  private loadClient(clientUserId: number): void {
    this.shipmentService.getClientById(clientUserId).subscribe({
      next: (data) => {
        this.client = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.showError(err);
      },
    });

    this.authService.getUserById(clientUserId).subscribe({
      next: (data) => {
        this.clientUser = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.showError(err);
      },
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get("id");

    if (!id) {
      return;
    }

    this.shipmentService.getById(id).subscribe({
      next: (data) => {
        this.shipment = data;
        this.selectedDistanceKm = data.distanceKm ?? 0;
        this.loadClient(data.clientUserId);
        this.cdr.detectChanges();
      },

      error: (err) => {
        this.showError(err);
      },
    });

    this.shipmentService.getHistory(id).subscribe({
      next: (data) => {
        this.history = data;

        this.cdr.detectChanges();
      },

      error: (err) => {
        this.showError(err);
      },
    });

    this.shipmentService.getDrivers().subscribe({
      next: (data) => {
        this.drivers = data;
        this.cdr.detectChanges();
      },
      error: (err) => this.showError(err),
    });

  }

  ngOnDestroy(): void {
    if (this.successMessageTimeout) {
      clearTimeout(this.successMessageTimeout);
    }
  }

  getAvailableActions(): string[] {
    if (!this.shipment) {
      return [];
    }

    switch (this.shipment.status) {
      case "CREATED":
        if (this.selectedDriverId) {
          return ["ASSIGN", "CANCEL"];
        }

        return ["CANCEL"];

      case "ASSIGNED":
        return ["LOAD", "CANCEL"];

      case "LOADED":
        return ["DEPART", "CANCEL"];

      case "IN_TRANSIT":
        return ["ARRIVE", "DELAY"];

      case "DELAYED":
        return ["DEPART", "CANCEL"];

      case "ARRIVED":
        return ["DELIVER"];

      case "DELIVERED":
      case "CANCELLED":
        return [];

      default:
        return [];
    }
  }

  getCurrentStep(): number {
    if (!this.shipment) {
      return 0;
    }

    switch (this.shipment.status) {
      case "CREATED":
        return 0;

      case "ASSIGNED":
        return 1;

      case "LOADED":
        return 2;

      case "IN_TRANSIT":
        return 3;

      case "ARRIVED":
        return 4;

      case "DELIVERED":
        return 5;

      default:
        return 0;
    }
  }

  journeySteps = [
    "CREATED",
    "ASSIGNED",
    "LOADED",
    "IN_TRANSIT",
    "ARRIVED",
    "DELIVERED",
  ];

  getHistoryEntry(status: string): ShipmentHistory | undefined {
    return this.history.find((h) => h.status === status);
  }

  formatDateTime(value?: string): string {
    if (!value) {
      return "";
    }

    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
      return value;
    }

    const year = date.getUTCFullYear();
    const month = String(date.getUTCMonth() + 1).padStart(2, "0");
    const day = String(date.getUTCDate()).padStart(2, "0");
    const hours = String(date.getUTCHours()).padStart(2, "0");
    const minutes = String(date.getUTCMinutes()).padStart(2, "0");
    const seconds = String(date.getUTCSeconds()).padStart(2, "0");

    return `${year}-${month}-${day} - ${hours}:${minutes}:${seconds}`;
  }

  formatDistance(value: number | null | undefined): string {
    return `${(value ?? 0).toFixed(1)} km`;
  }

  formatPrice(value: number | null | undefined): string {
    return (value ?? 0).toFixed(2);
  }

  formatDate(value?: string): string {
    if (!value) {
      return "";
    }

    const date = new Date(value);

    const day = String(date.getDate()).padStart(2, "0");

    const month = String(date.getMonth() + 1).padStart(2, "0");

    const year = date.getFullYear();

    return `${day}.${month}.${year}`;
  }

  formatTime(value?: string): string {
    if (!value) {
      return "";
    }

    const date = new Date(value);

    const hours = String(date.getHours()).padStart(2, "0");

    const minutes = String(date.getMinutes()).padStart(2, "0");

    const seconds = String(date.getSeconds()).padStart(2, "0");

    return `${hours}:${minutes}:${seconds}`;
  }

  isCompleted(status: string): boolean {
    const currentIndex = this.getCurrentStep();

    const stepIndex = this.journeySteps.indexOf(status);

    return stepIndex <= currentIndex;
  }

  getPrimaryAction(): string | null {
    const actions = this.getAvailableActions();

    if (actions.length === 0) {
      return null;
    }

    return actions[0];
  }

  getSecondaryAction(): string | null {
    const actions = this.getAvailableActions();

    if (actions.length < 2) {
      return null;
    }

    return actions[1];
  }

  requestEvent(event: string): void {
    this.pendingEvent = event;
    this.errorMessage = "";
    this.clearSuccessMessageTimeout();
    this.successMessage = "";
  }

  confirmPendingEvent(): void {
    const event = this.pendingEvent;
    this.pendingEvent = null;

    if (event) {
      this.processEvent(event);
    }
  }

  processEvent(event: string): void {
    if (!this.shipment) {
      return;
    }

    const driver = this.getSelectedDriver();

    if (event === "ASSIGN") {
      this.shipmentService
        .update(this.shipment.id, {
          category: this.shipment.category,
          description: this.shipment.description,

          origin: this.shipment.origin,
          destination: this.shipment.destination,

          clientUserId: this.shipment.clientUserId,

          driverUserId: this.selectedDriverId,
          vehicleId: driver?.assignedVehicleId ?? "",
          simulationRoute: this.shipment.simulationRoute ?? null,
          distanceKm: this.selectedDistanceKm,

          totalWeight: this.shipment.totalWeight,
          totalVolume: this.shipment.totalVolume,

          price: this.shipment.price,
        })
        .subscribe({
          next: () => {
            this.shipmentService
              .processEvent(this.shipment!.id, event)
              .subscribe({
                next: (response) => {
                  this.showSuccess(response.message);
                  this.errorMessage = "";
                  this.loadData(this.shipment!.id);
                },
                error: (err) => {
                  this.showError(err);
                },
              });
          },
          error: (err) => {
            this.showError(err);
          },
        });
      return;
    }

    this.shipmentService.processEvent(this.shipment.id, event).subscribe({
      next: (response) => {
        this.showSuccess(response.message);
        this.errorMessage = "";
        this.loadData(this.shipment!.id);
      },

      error: (err) => {
        this.showError(err);
      },
    });
  }

  assignShipment(): void {
    if (!this.shipment || !this.selectedDriverId) {
      return;
    }
    
    this.shipmentService
      .update(this.shipment.id, {
        category: this.shipment.category,
        description: this.shipment.description,

        origin: this.shipment.origin,
        destination: this.shipment.destination,

        clientUserId: this.shipment.clientUserId,

        driverUserId: this.selectedDriverId,
        vehicleId: this.getSelectedDriver()?.assignedVehicleId ?? "",
        simulationRoute: this.shipment.simulationRoute ?? null,
        distanceKm: this.selectedDistanceKm,
        totalWeight: this.shipment.totalWeight,
        totalVolume: this.shipment.totalVolume,

        price: this.shipment.price,
      })
      .subscribe({
        next: (response) => {
          this.showSuccess(response.message);
          this.errorMessage = "";
          this.processEvent("ASSIGN");
        },

        error: (err) => {
          this.showError(err);
        },
      });
  }

  private getSelectedDriver(): any | undefined {
    return this.drivers.find(
      (driver) => String(driver.userId) === String(this.selectedDriverId),
    );
  }

  private showSuccess(message: string): void {
    this.clearSuccessMessageTimeout();

    this.successMessage = message;
    this.successMessageTimeout = setTimeout(() => {
      this.successMessage = "";
      this.cdr.detectChanges();
    }, 10_000);
  }

  private showError(error: { error?: { message?: string } }): void {
    this.clearSuccessMessageTimeout();
    this.errorMessage =
      error?.error?.message ?? "We could not complete this shipment action. Please try again.";
    this.successMessage = "";
    this.cdr.detectChanges();
  }

  private clearSuccessMessageTimeout(): void {
    if (this.successMessageTimeout) {
      clearTimeout(this.successMessageTimeout);
      this.successMessageTimeout = undefined;
    }
  }
}
