import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Observable, forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';
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
export class ShipmentCreate implements OnInit {

  form;
  errorMessage = "";
  successMessage = "";
  data$!: Observable<{ clients: any[], drivers: any[], routes: any[] }>;
  private rawDrivers: any[] = [];
  
  constructor(
    private fb: FormBuilder,
    private shipmentService: ShipmentService,
    private router: Router
  ) {
    this.form = this.fb.group({
      category: ['', Validators.required],
      description: ['', Validators.required],
      origin: ['', Validators.required],
      destination: ['', Validators.required],
      clientUserId: ['', Validators.required],
      driverUserId: ['', Validators.required],
      vehicleId: ['', Validators.required],
      pathId: ['', Validators.required],
      totalWeight: [0, Validators.required],
      totalVolume: [0, Validators.required],
      price: [0, Validators.required]
    });
  }

  ngOnInit(): void {
    this.data$ = forkJoin({
      clients: this.shipmentService.getClients(),
      drivers: this.shipmentService.getDrivers(),
      routes: this.shipmentService.getRoutes()
    }).pipe(
      map(res => {
        this.rawDrivers = res.drivers;
        return res;
      })
    );
  }

  onDriverChange(driverId: string): void {
    const driver = this.rawDrivers.find(d => d.id === driverId);
    if (!driver) {
      return;
    }
    this.form.patchValue({
      vehicleId: driver.assignedVehicleId
    });
  }

  createShipment(): void {
    this.errorMessage = "";
    this.successMessage = "";
    if (this.form.invalid) {
      return;
    }
    this.shipmentService
      .create(this.form.getRawValue())
      .subscribe({
        next: (response) => {
          this.successMessage = response.message ?? "Shipment created successfully.";
          setTimeout(() => this.router.navigate(['/shipments']), 1500);
        },
        error: err => {
          this.errorMessage =
            err?.error?.message ?? "We could not create the shipment. Please try again.";
        }
      });
  }
}
