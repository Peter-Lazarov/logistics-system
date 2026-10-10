import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MyShipments } from './my-shipments';

describe('MyShipments', () => {
  let component: MyShipments;
  let fixture: ComponentFixture<MyShipments>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyShipments],
    }).compileComponents();

    fixture = TestBed.createComponent(MyShipments);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
