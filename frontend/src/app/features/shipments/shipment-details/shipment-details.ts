import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ActivatedRoute } from "@angular/router";
import { ChangeDetectorRef } from "@angular/core";
import { FormsModule } from "@angular/forms";

import {
  Shipment,
  ShipmentService,
  ShipmentHistory,
} from "../shipment.service";

@Component({
  selector: "app-shipment-details",
  imports: [CommonModule, FormsModule],
  templateUrl: "./shipment-details.html",
  styleUrl: "./shipment-details.scss",
})
export class ShipmentDetails implements OnInit {
  shipment?: Shipment;
  history: ShipmentHistory[] = [];
  drivers: any[] = [];
  routes: any[] = [];
  selectedDriverId = "";
  selectedRouteId = "";

  stepPositions = [8, 25, 40, 68, 88, 98];

  constructor(
    private route: ActivatedRoute,
    private shipmentService: ShipmentService,
    private cdr: ChangeDetectorRef,
  ) {}

  private loadData(id: string): void {
    this.shipmentService.getHistory(id).subscribe({
      next: (data) => {
        this.history = data;

        this.cdr.detectChanges();
      },
    });

    this.shipmentService.getById(id).subscribe({
      next: (data) => {
        this.shipment = data;

        this.cdr.detectChanges();
      },
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get("id");

    if (!id) {
      return;
    }

    this.shipmentService.getHistory(id).subscribe({
      next: (data) => {
        this.history = data;

        this.cdr.detectChanges();
      },

      error: (err) => {
        console.error(err);
      },
    });

    this.shipmentService.getById(id).subscribe({
      next: (data) => {
        this.shipment = data;

        this.cdr.detectChanges();
      },

      error: (err) => {
        console.error(err);
      },
    });

    this.shipmentService.getDrivers().subscribe({
      next: (data) => {
        this.drivers = data;
        this.cdr.detectChanges();
      },
    });

    this.shipmentService.getRoutes().subscribe({
      next: (data) => {
        this.routes = data;
        this.cdr.detectChanges();
      },
    });
  }

  getAvailableActions(): string[] {
    if (!this.shipment) {
      return [];
    }

    switch (this.shipment.status) {
      case "CREATED":
        if (this.selectedDriverId && this.selectedRouteId) {
          return ["ASSIGN_DRIVER", "CANCEL"];
        }

        return ["CANCEL"];

      case "ASSIGNED":
        return ["LOAD_SHIPMENT", "CANCEL"];

      case "LOADED":
        return ["START_ROUTE", "CANCEL"];

      case "IN_TRANSIT":
        return ["ARRIVE_DESTINATION", "DELAY"];

      case "DELAYED":
        return ["START_ROUTE", "CANCEL"];

      case "ARRIVED":
        return ["COMPLETE_DELIVERY"];

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

  processEvent(event: string): void {
    if (!this.shipment) {
      return;
    }

    const driver = this.drivers.find((d) => d.id === this.selectedDriverId);

    if (event === "ASSIGN_DRIVER") {
      this.shipmentService
        .update(this.shipment.id, {
          category: this.shipment.category,
          description: this.shipment.description,

          origin: this.shipment.origin,
          destination: this.shipment.destination,

          clientId: this.shipment.clientId,

          driverId: this.selectedDriverId,
          pathId: this.selectedRouteId,
          vehicleId: driver?.assignedVehicleId ?? "",

          totalWeight: this.shipment.totalWeight,
          totalVolume: this.shipment.totalVolume,

          price: this.shipment.price,
        })
        .subscribe({
          next: () => {
            this.shipmentService
              .processEvent(this.shipment!.id, event)
              .subscribe({
                next: () => {
                  this.loadData(this.shipment!.id);
                },
                error: (err) => {
                  console.error(err);
                },
              });
          },
          error: (err) => {
            console.error(err);
          },
        });
      return;
    }

    const confirmed = confirm(`Process event ${event}?`);

    if (!confirmed) {
      return;
    }

    this.shipmentService.processEvent(this.shipment.id, event).subscribe({
      next: () => {
        this.loadData(this.shipment!.id);
      },

      error: (err) => {
        console.error(err);
      },
    });
  }

  assignShipment(): void {
    if (!this.shipment || !this.selectedDriverId || !this.selectedRouteId) {
      return;
    }

    const driver = this.drivers.find((d) => d.id === this.selectedDriverId);
    const route = this.routes.find((r) => r.pathId === this.selectedRouteId);

    this.shipmentService
      .update(this.shipment.id, {
        category: this.shipment.category,
        description: this.shipment.description,

        origin: this.shipment.origin,
        destination: this.shipment.destination,

        clientId: this.shipment.clientId,

        driverId: this.selectedDriverId,
        vehicleId: this.shipment.vehicleId ?? "",
        pathId: this.selectedRouteId,

        totalWeight: this.shipment.totalWeight,
        totalVolume: this.shipment.totalVolume,

        price: this.shipment.price,
      })
      .subscribe({
        next: () => {
          this.processEvent("ASSIGN_DRIVER");
        },

        error: (err) => {
          console.error(err);
        },
      });
  }
}
