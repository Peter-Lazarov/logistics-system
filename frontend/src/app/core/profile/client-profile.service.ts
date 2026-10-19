import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment";

export interface ClientProfile {
  userId: number;
  companyName: string | null;
  companyAddress: string | null;
  vatNumber: string | null;
}

interface ApiResponse<T> {
  message: string;
  data: T;
}

@Injectable({ providedIn: "root" })
export class ClientProfileService {
  private readonly apiUrl = `${environment.commonUrl}${environment.common.clients}`;

  constructor(private http: HttpClient) {}

  getCurrent(): Observable<ClientProfile> {
    return this.http.get<ClientProfile>(`${this.apiUrl}/me`);
  }

  updateCurrent(profile: ClientProfile): Observable<ApiResponse<ClientProfile>> {
    return this.http.put<ApiResponse<ClientProfile>>(`${this.apiUrl}/me`, profile);
  }

  isComplete(profile: ClientProfile): boolean {
    return Boolean(
      profile.companyName?.trim() &&
      profile.companyAddress?.trim() &&
      profile.vatNumber?.trim(),
    );
  }
}