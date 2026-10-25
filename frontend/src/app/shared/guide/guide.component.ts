import { Component, HostListener, signal } from "@angular/core";

@Component({
  selector: "app-guide",
  standalone: true,
  templateUrl: "./guide.component.html",
  styleUrl: "./guide.component.css",
})
export class GuideComponent {
  protected readonly isOpen = signal(false);

  protected readonly steps = [
    {
      number: 1,
      title: "Register a Client",
      description:
        "Open Register and create a new client account."
    },
    {
      number: 2,
      title: "Create a Shipment Request",
      description:
        "Log in as the client and create a shipment request.",
      items: [
        "Category",
        "Weight",
        "Volume",
        "Origin",
        "Destination"
      ]
    },
    {
      number: 3,
      title: "Log in as Administrator",
      description: "The administrator can create employee account.",
      note: "username: admin@sunny-times.com / password: test1234"
    },
    {
      number: 4,
      title: "Register an Employee",
      description:
        "Create an employee account and remember the login credentials."
    },
    {
      number: 5,
      title: "Manage a shipment",
      description:
        "Log in as the employee, open the shipment and assign a driver.",
      note:
        "Distance and transport price are calculated automatically during assignment."
    },
    {
      number: 6,
      title: "Start Transportation",
      description:
        "Process the shipment until it reaches IN_TRANSIT.",
      workflow: [
        "CREATED",
        "ASSIGNED",
        "LOADED",
        "IN_TRANSIT"
      ],
      note:
        "GPS simulation starts automatically when the shipment enters IN_TRANSIT."
    },
    {
      number: 7,
      title: "View Tracking",
      description:
        "Open View Tracking from Shipment Details.",
      items: [
        "Current vehicle position",
        "Traveled route",
        "Tracking history"
      ],
      note:
        "Wait 15-20 seconds to observe new tracking points."
    },
    {
      number: 8,
      title: "Complete Delivery",
      description:
        "Finish the transport process.",
      workflow: [
        "ARRIVED",
        "DELIVERED"
      ],
      note:
        "GPS simulation stops after delivery. Tracking history remains available."
    }
  ];

  protected open(): void {
    this.isOpen.set(true);
  }

  protected close(): void {
    this.isOpen.set(false);
  }

  @HostListener("document:keydown.escape")
  protected closeOnEscape(): void {
    if (this.isOpen()) {
      this.close();
    }
  }
}