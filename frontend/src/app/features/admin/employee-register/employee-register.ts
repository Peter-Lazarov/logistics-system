import { Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { HttpClient } from "@angular/common/http";
import { CommonModule } from "@angular/common";
import { environment } from "../../../environments/environment";
import { BehaviorSubject, Observable, switchMap } from "rxjs";

interface AdminUser {
  id: number;
  email: string;
  name: string | null;
  phone: string | null;
  role: string;
  status: "ACTIVE" | "DISABLED";
}

type EmployeeField = "email" | "name" | "phone" | "password";

const emailPattern = /^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?(?:\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)+$/;
const namePattern = /^[\p{L}]+([ '-][\p{L}]+)*$/u;
const phonePattern = /^[+]?[0-9() -]+$/;

@Component({
  selector: "app-employee-register",
  imports: [FormsModule, CommonModule], 
  templateUrl: "./employee-register.html",
  styleUrl: "./employee-register.scss",
})
export class EmployeeRegister implements OnInit {
  email = "";
  name = "";
  phone = "";
  password = "";
  submitted = false;
  private readonly touchedFields: Record<EmployeeField, boolean> = {
    email: false,
    name: false,
    phone: false,
    password: false,
  };
  errorMessage = "";
  successMessage = "";

  private refreshUsers$ = new BehaviorSubject<void>(undefined);
  users$!: Observable<AdminUser[]>;

  constructor(private http: HttpClient) {}

  markFieldTouched(field: EmployeeField): void {
    this.touchedFields[field] = true;
  }

  fieldError(field: EmployeeField): boolean {
    return this.fieldValidationMessage(field) !== null;
  }

  fieldValidationMessage(field: EmployeeField): string | null {
    if (!this.touchedFields[field] && !this.submitted) return null;

    const value = this[field].trim();
    if (!value) {
      return {
        email: "Email is required",
        name: "Full name is required",
        phone: "Phone number is required",
        password: "Password is required",
      }[field];
    }

    switch (field) {
      case "email":
        if (!emailPattern.test(value)) return "Invalid email format";
        if (value.length < 5 || value.length > 100) return "Email must be between 5 and 100 characters";
        return null;
      case "name": {
        const normalized = value.replace(/\s+/g, " ");
        if (!namePattern.test(normalized)) return "Full name contains invalid characters";
        if (normalized.length < 2 || normalized.length > 100) return "Full name must be between 2 and 100 characters";
        return null;
      }
      case "phone":
        if (!phonePattern.test(value)) return "Invalid phone number format";
        if (value.length < 7 || value.length > 20) return "Phone number must contain between 7 and 20 characters";
        return null;
      case "password":
        if (value.length < 4 || value.length > 50) return "Password must be between 4 and 50 characters";
        return null;
    }
  }

  ngOnInit(): void {
    this.users$ = this.refreshUsers$.pipe(
      switchMap(() => 
        this.http.get<AdminUser[]>(`${environment.authenticationUrl}${environment.auth.users}`)
      )
    );
  }

  toggleUserStatus(user: AdminUser): void {
    this.errorMessage = "";
    this.successMessage = "";
    const status = user.status === "ACTIVE" ? "DISABLED" : "ACTIVE";

    this.http
      .patch<AdminUser>(
        `${environment.authenticationUrl}${environment.auth.users}/${user.id}/status`,
        { status }
      )
      .subscribe({
        next: () => {
          this.successMessage = "User status updated.";
          this.refreshUsers$.next();
        },
        error: (err) => {
          this.errorMessage = err?.error?.message ?? "Could not update the user status. Please try again.";
        },
      });
  }

  createEmployee(): void {
    this.submitted = true;
    this.errorMessage = "";
    this.successMessage = "";

    this.email = this.email.trim();
    this.name = this.name.trim().replace(/\s+/g, " ");
    this.phone = this.phone.trim();
    this.password = this.password.trim();
    if ((["email", "name", "phone", "password"] as EmployeeField[]).some((field) => this.fieldError(field))) {
      return;
    }

    this.http
      .post(`${environment.authenticationUrl}${environment.auth.registerEmployee}`, {
        email: this.email,
        password: this.password,
        name: this.name,
        phone: this.phone,
      })
      .subscribe({
        next: () => {
          this.successMessage = "Employee created successfully.";
          this.email = "";
          this.name = "";
          this.phone = "";
          this.password = "";
          this.refreshUsers$.next();
        },
        error: (err) => {
          this.errorMessage = err?.error?.message ?? "Could not create the employee. Please try again.";
        },
      });
  }
}
