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
      title: "Register a Client Account",
      description:
        "Registration is available for everyone. Create a new client account using the Register page.",
    },
    {
      number: 2,
      title: "Log In and Create a Shipment",
      description: "Log in with the client account and create a shipment with:",
      items: ["Category", "Weight and volume", "Origin and destination"],
      note: "New shipments start with status CREATED.",
    },
    {
      number: 3,
      title: "Log In as Administrator",
      description: "Administrators can manage users, create employees, and enable or disable accounts.",
      note: "Username: admin@sunny-times.com, Password: 1234",
    },
    {
      number: 4,
      title: "Log In as Employee",
      description:
        "Employees can view all shipments, assign drivers and vehicles, and manage the shipment workflow.",
    },
    {
      number: 5,
      title: "Prepare the Shipment",
      description:
        "Assign a vehicle and a driver. The system automatically calculates the route distance and shipment price.",
    },
    {
      number: 6,
      title: "Start Transportation",
      description: "Move the shipment through the workflow:",
      workflow: ["CREATED", "ASSIGNED", "LOADED", "IN_TRANSIT"],
      note: "Executing DEPART starts GPS simulation, generates tracking points, and makes truck movement visible.",
    },
    {
      number: 7,
      title: "Track the Vehicle",
      description: "Open View Tracking from Shipment Details to see:",
      items: ["Current vehicle position", "Traveled route", "Tracking history"],
    },
    {
      number: 8,
      title: "Complete Delivery",
      description: "Complete the delivery workflow:",
      workflow: ["IN_TRANSIT", "ARRIVED", "DELIVERED"],
      note: "When DELIVERED is executed, GPS simulation stops. Route history and tracking data remain available.",
    },
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