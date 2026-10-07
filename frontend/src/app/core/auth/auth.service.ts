import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment";

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  role: string;
}

@Injectable({
  providedIn: "root",
})
export class AuthService {
  private readonly apiUrl = `${environment.authenticationUrl}/auth`;

  constructor(private http: HttpClient) {}

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request);
  }

  getRole(): string | null {
    return localStorage.getItem("role");
  }

  isAdmin(): boolean {
    return this.getRole() === "ADMIN";
  }

  isEmployee(): boolean {
    return this.getRole() === "EMPLOYEE";
  }

  isClient(): boolean {
    return this.getRole() === "CLIENT";
  }
}
