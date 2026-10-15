import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { Shipment, ShipmentService } from '../../shipments/shipment.service';

@Component({
  selector: 'app-my-shipments',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-shipments.html',
  styleUrl: './my-shipments.scss'
})
export class MyShipmentsComponent implements OnInit {
  shipments$!: Observable<Shipment[]>;

  constructor(private shipmentService: ShipmentService) {}

  ngOnInit(): void {
    this.shipments$ = this.shipmentService.getMyShipments();
  }
}
