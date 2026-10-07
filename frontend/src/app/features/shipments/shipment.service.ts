import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from '../../environments/environment';

export interface Shipment {
  id: string;
  category: string;
  description: string;
  origin: string;
  destination: string;
  clientId: string;
  driverId: string;
  vehicleId: string;
  pathId: string;
  totalWeight: number;
  totalVolume: number;
  price: number;
  status: string;
  createdAt: string;
  updatedAt: string;
}

export interface ShipmentHistory {
  id: number;
  shipmentId: string;
  status: string;
  changedBy: string;
  changedAt: string;
}

@Injectable({
  providedIn: "root",
})
export class ShipmentService {
  private readonly apiUrl = `${environment.shipmentUrl}${environment.shipment.root}`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Shipment[]> {
    return this.http.get<Shipment[]>(this.apiUrl);
  }

  getById(id: string): Observable<Shipment> {
    return this.http.get<Shipment>(`${this.apiUrl}/${id}`);
  }

  getHistory(shipmentId: string): Observable<ShipmentHistory[]> {
    return this.http.get<ShipmentHistory[]>(
      `${this.apiUrl}/${shipmentId}/history`,
    );
  }

  updateStatus(shipmentId: string, status: string): Observable<Shipment> {
    return this.http.patch<Shipment>(`${this.apiUrl}/${shipmentId}/status`, {
      status: status,
    });
  }

  processEvent(shipmentId: string, event: string): Observable<Shipment> {
    return this.http.patch<Shipment>(`${this.apiUrl}/${shipmentId}/event`, {
      event: event,
    });
  }

  create(request: any) {
    return this.http.post(this.apiUrl, request);
  }

  getClients() {
    return this.http.get<any[]>(`${environment.commonUrl}${environment.common.clients}`);
  }

  getDrivers() {
    return this.http.get<any[]>(`${environment.commonUrl}${environment.common.drivers}`);
  }

  getRoutes() {
    return this.http.get<any[]>(`${environment.commonUrl}${environment.common.routes}`);
  }

  createRequest(request: any) {
    return this.http.post(`${this.apiUrl}/requests`, request);
  }

  update(id: string, request: any) {
    return this.http.put(`${this.apiUrl}/${id}`, request);
  }
}
