import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ShipmentService } from '../../shipments/shipment.service';
import { ClientProfileService } from '../../../core/profile/client-profile.service';

interface DemoLoad {
  origin: string;
  destination: string;
  simulationRoute: "BOTEVGRADSKO" | "BOJURISHTE" | "KAZICHENE";
  category: string;
  description: string;
  totalWeight: number;
  totalVolume: number;
}

@Component({
  selector: 'app-client-request',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
  ],
  styleUrl: './client-request.scss',
  templateUrl: './client-request.html',
})
export class ClientRequest implements OnInit {

  form;
  profileIncomplete = false;
  profileChecking = true;
  errorMessage = '';

  demoLoads: DemoLoad[] = [
    {
      origin: 'Божурище',
      destination: 'Логистичен парк',
      simulationRoute: 'BOJURISHTE',
      category: 'Furniture',
      description: 'Office furniture',
      totalWeight: 12000,
      totalVolume: 32
    },
    {
      origin: 'Карго терминал',
      destination: 'Гимназия Неофит Бозвели',
      simulationRoute: 'BOTEVGRADSKO',
      category: 'Food',
      description: 'School supplies',
      totalWeight: 8000,
      totalVolume: 24
    },
    {
      origin: 'Казичене склад',
      destination: 'Карго терминал',
      simulationRoute: 'KAZICHENE',
      category: 'Electronics',
      description: 'Electronic equipment',
      totalWeight: 3500,
      totalVolume: 12
    }
  ];

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private shipmentService: ShipmentService,
    private clientProfileService: ClientProfileService,
  ) {
    this.form = this.fb.group({
      origin: ['', Validators.required],
      destination: ['', Validators.required],
      category: ['', Validators.required],
      description: ['', Validators.required],
      totalWeight: [0, Validators.required],
      totalVolume: [0, Validators.required],
      simulationRoute: [null as string | null],
    });
  }

  ngOnInit(): void {
    this.clientProfileService.getCurrent().subscribe({
      next: (profile) => {
        this.profileIncomplete = !this.clientProfileService.isComplete(profile);
        this.profileChecking = false;
      },
      error: () => {
        this.profileIncomplete = true;
        this.profileChecking = false;
      },
    });
  }

  loadDemo(load: DemoLoad): void {
    this.form.patchValue({
      origin: load.origin,
      destination: load.destination,
      simulationRoute: load.simulationRoute,
      category: load.category,
      description: load.description,
      totalWeight: load.totalWeight,
      totalVolume: load.totalVolume
    });
  }

  clearPlannedRoute(): void {
    this.form.controls.simulationRoute.setValue(null);
  }

  submitRequest(): void {
    if (this.profileChecking || this.profileIncomplete) {
      if (this.profileIncomplete) {
        this.router.navigate(['/profile']);
      }
      return;
    }

    if (this.form.invalid) {
      return;
    }

    this.shipmentService
      .createRequest(this.form.getRawValue())
      .subscribe({
        next: () => {
          this.router.navigate(['/shipments/my']);
        },
        error: (err) => {
          this.errorMessage = err?.error?.message ??
            'Please complete your company, address, and VAT number in your profile before requesting a shipment.';
        }
      });
  }
}
