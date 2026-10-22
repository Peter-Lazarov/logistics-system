import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { map, Observable } from 'rxjs';
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
    this.shipments$ = this.shipmentService.getMyShipments().pipe(
      map((shipments) =>
        [...shipments].sort((left, right) =>
          left.id.localeCompare(right.id, undefined, { numeric: true }),
        ),
      ),
    );
  }
}
