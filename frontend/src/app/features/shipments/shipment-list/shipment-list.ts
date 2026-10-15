import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { NgClass } from "@angular/common";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";

import { Shipment, ShipmentService } from "../shipment.service";

@Component({
  selector: "app-shipment-list",
  standalone: true,
  imports: [CommonModule, RouterLink, NgClass],
  templateUrl: "./shipment-list.html",
  styleUrl: "./shipment-list.scss",
})
export class ShipmentList implements OnInit {
  shipments$!: Observable<Shipment[]>;

  constructor(private shipmentService: ShipmentService) {}

  ngOnInit(): void {
    this.shipments$ = this.shipmentService.getAll().pipe(
      map(data => data.sort((a, b) => a.id.localeCompare(b.id)))
    );
  }

  isClient(): boolean {
    return localStorage.getItem("role") === "ROLE_CLIENT";
  }

  isAdmin(): boolean {
    return localStorage.getItem("role") === "ROLE_ADMIN";
  }
}
