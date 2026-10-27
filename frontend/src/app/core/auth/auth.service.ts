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
  message: string;
}

export interface UserProfile {
  id: number;
  name: string;
  email: string;
  phone: string;
}

export interface UpdateProfileRequest {
  name: string;
  phone: string;
}

@Injectable({
  providedIn: "root",
})
export class AuthService {
  private readonly apiUrl = `${environment.authenticationUrl}`;

  constructor(private http: HttpClient) {}

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request);
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request);
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

  getMe(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.apiUrl}/me`);
  }

  getUserById(id: number): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.apiUrl}/users/${id}`);
  }

  updateProfile(request: UpdateProfileRequest): Observable<UserProfile> {
    return this.http.put<UserProfile>(`${this.apiUrl}/me`, request);
  }
}
