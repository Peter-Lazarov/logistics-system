import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment";

export interface VehicleLocation {
  id: string;
  shipmentId: string;
  vehicleId: string;
  latitude: number;
  longitude: number;
  timestamp: string;
}

@Injectable({ providedIn: "root" })
export class TrackingService {
  private readonly apiUrl = `${environment.trackingUrl}${environment.tracking.locations}`;

  constructor(private readonly http: HttpClient) {}

  getHistory(shipmentId: string): Observable<VehicleLocation[]> {
    return this.http.get<VehicleLocation[]>(`${this.apiUrl}/${shipmentId}/history`);
  }

  getLatest(shipmentId: string): Observable<VehicleLocation> {
    return this.http.get<VehicleLocation>(`${this.apiUrl}/${shipmentId}/latest`);
  }
}