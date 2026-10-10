import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  name: string;
  phone: string;
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

  register(request: RegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, request);
  }

  getRole(): string | null {
    return localStorage.getItem("role");
  }

  isAdmin(): boolean {
    return this.getRole() === "ROLE_ADMIN";
  }

  isEmployee(): boolean {
    return this.getRole() === "ROLE_EMPLOYEE";
  }

  isClient(): boolean {
    return this.getRole() === "ROLE_CLIENT";
  }

  logout(): void {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("role");
  }

  getMe() {
    return this.http.get<any>(`${this.apiUrl}/me`);
  }
}
