import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ShipmentService } from '../shipment.service';

@Component({
  selector: 'app-shipment-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './shipment-create.html',
  styleUrl: './shipment-create.scss'
})
export class ShipmentCreate implements OnInit{

  form;
  clients: any[] = [];
  drivers: any[] = [];
  routes: any[] = [];
  
  constructor(
    private fb: FormBuilder,
    private shipmentService: ShipmentService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {

    this.form = this.fb.group({
      category: ['', Validators.required],
      description: ['', Validators.required],
      origin: ['', Validators.required],
      destination: ['', Validators.required],
      clientId: ['', Validators.required],
      driverId: ['', Validators.required],
      vehicleId: ['', Validators.required],
      pathId: ['', Validators.required],
      totalWeight: [0, Validators.required],
      totalVolume: [0, Validators.required],
      price: [0, Validators.required]
    });

  }

  ngOnInit(): void {

    this.shipmentService
      .getClients()
      .subscribe({
        next: data => {
          this.clients = data;
          this.cdr.detectChanges();
        },
        error: err => {
          console.error(err);
        }
      });

    this.shipmentService
    .getDrivers()
    .subscribe({
      next: data => {
        this.drivers = data;
        this.cdr.detectChanges();
      }
    });

    this.shipmentService
    .getRoutes()
    .subscribe({
      next: data => {
        this.routes = data;
        this.cdr.detectChanges();
      }
    });
  }

  onDriverChange(
    driverId: string
  ): void {

    const driver =
      this.drivers.find(
        d => d.id === driverId
      );

    if (!driver) {
      return;
    }

    this.form.patchValue({
      vehicleId:
        driver.assignedVehicleId
    });

  }

  createShipment(): void {

    if (this.form.invalid) {
      return;
    }

    this.shipmentService
      .create(this.form.getRawValue())
      .subscribe({
        next: () => {
          this.router.navigate(['/shipments']);
        },
        error: err => {
          console.error(err);
        }
      });

  }

}

