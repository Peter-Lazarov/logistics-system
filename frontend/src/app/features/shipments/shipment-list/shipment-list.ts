import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgClass } from '@angular/common';

import {
  Shipment,
  ShipmentService
} from '../shipment.service';

@Component({
  selector: 'app-shipment-list',
  imports: [
  CommonModule,
  RouterLink,
  NgClass
  ],
  templateUrl: './shipment-list.html',
  styleUrl: './shipment-list.scss'
})
export class ShipmentList implements OnInit {

  shipments: Shipment[] = [];

  constructor(
    private shipmentService: ShipmentService,
    private cdr: ChangeDetectorRef
  ) {
  }

  ngOnInit(): void {

  this.shipmentService
      .getAll()
      .subscribe({

        next: data => {
          this.shipments = data.sort((a,b) => a.id.localeCompare(b.id));
          this.cdr.detectChanges();
        },

        error: err => {
          console.error('Shipment error');
          console.error(err);
        }

      });
  }
}
