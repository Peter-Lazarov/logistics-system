import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment";

export interface Shipment {
  id: string;

  category: string;
  description: string;

  origin: string;
  destination: string;

  clientUserId: number;
  driverUserId: number | null;

  vehicleId: string | null;
  pathId: string | null;

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

export interface ApiResponse<T> {
  message: string;
  data: T;
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

  processEvent(shipmentId: string, event: string): Observable<ApiResponse<Shipment>> {
    return this.http.patch<ApiResponse<Shipment>>(`${this.apiUrl}/${shipmentId}/event`, {
      event,
    });
  }

  create(request: any): Observable<ApiResponse<Shipment>> {
    return this.http.post<ApiResponse<Shipment>>(this.apiUrl, request);
  }

  getClients() {
    return this.http.get<any[]>(
      `${environment.commonUrl}${environment.common.clients}`,
    );
  }

  getDrivers() {
    return this.http.get<any[]>(
      `${environment.commonUrl}${environment.common.drivers}`,
    );
  }

  getRoutes() {
    return this.http.get<any[]>(
      `${environment.commonUrl}${environment.common.routes}`,
    );
  }

  createRequest(request: any): Observable<ApiResponse<Shipment>> {
    return this.http.post<ApiResponse<Shipment>>(`${this.apiUrl}/requests`, request);
  }

  update(id: string, request: any): Observable<ApiResponse<Shipment>> {
    return this.http.put<ApiResponse<Shipment>>(`${this.apiUrl}/${id}`, request);
  }

  getMyShipments() {
    return this.http.get<any[]>(`${environment.shipmentUrl}/shipments/my`);
  }

  getClientById(id: number) {
    return this.http.get<any>(
      `${environment.commonUrl}${environment.common.clients}/${id}`,
    );
  }
  
  
}
