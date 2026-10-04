import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ShipmentService } from '../../shipments/shipment.service';

interface DemoLoad {

  origin: string;
  destination: string;

  category: string;
  description: string;

  totalWeight: number;
  totalVolume: number;

}

@Component({
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  selector: 'app-client-request',
  styleUrl: './client-request.scss',
  templateUrl: './client-request.html',
})
export class ClientRequest implements OnInit {

  form;

  demoLoads: DemoLoad[] = [
    {
      origin: 'Божурище',
      destination: 'Логистичен парк',
      category: 'Furniture',
      description: 'Office furniture',
      totalWeight: 12000,
      totalVolume: 32
    },
    {
      origin: 'Карго терминал',
      destination: 'Гимназия Неофит Бозвели',
      category: 'Food',
      description: 'School supplies',
      totalWeight: 8000,
      totalVolume: 24
    },
    {
      origin: 'Казичене склад',
      destination: 'Карго терминал',
      category: 'Electronics',
      description: 'Electronic equipment',
      totalWeight: 3500,
      totalVolume: 12
    }
  ];

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private shipmentService: ShipmentService
  ) {

    this.form = this.fb.group({
      origin: ['', Validators.required],
      destination: ['', Validators.required],
      category: ['', Validators.required],
      description: ['', Validators.required],
      totalWeight: [0, Validators.required],
      totalVolume: [0, Validators.required],
    });

  }

  ngOnInit(): void {
  }

  loadDemo(
    load: DemoLoad
  ): void {
    this.form.patchValue({
      origin: load.origin,
      destination: load.destination,
      category: load.category,
      description: load.description,
      totalWeight: load.totalWeight,
      totalVolume: load.totalVolume
    });
  }

  submitRequest(): void {
    if (this.form.invalid) {
      return;
    }

    this.shipmentService
      .createRequest(
        this.form.getRawValue()
      )
      .subscribe({
        next: response => {
          console.log(response);
          alert(
            'Request submitted successfully'
          );
          this.form.reset();
          this.router.navigate([
            '/shipments'
          ]);
        },
        error: err => {
          console.error(err);
        }
      });
  }
  

}
