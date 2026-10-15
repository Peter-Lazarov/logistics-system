import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { MyShipmentsComponent } from './my-shipments';
import { ShipmentService } from '../../shipments/shipment.service';

describe('MyShipments', () => {
  let component: MyShipmentsComponent;
  let fixture: ComponentFixture<MyShipmentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyShipmentsComponent],
      providers: [
        provideRouter([]),
        {
          provide: ShipmentService,
          useValue: { getMyShipments: () => of([]) },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MyShipmentsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
