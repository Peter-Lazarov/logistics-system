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

  private refreshUsers$ = new BehaviorSubject<void>(undefined);
  users$!: Observable<AdminUser[]>;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.users$ = this.refreshUsers$.pipe(
      switchMap(() => 
        this.http.get<AdminUser[]>(`${environment.authenticationUrl}${environment.auth.users}`)
      )
    );
  }

  toggleUserStatus(user: AdminUser): void {
    const status = user.status === "ACTIVE" ? "DISABLED" : "ACTIVE";

    this.http
      .patch<AdminUser>(
        `${environment.authenticationUrl}${environment.auth.users}/${user.id}/status`,
        { status }
      )
      .subscribe({
        next: () => this.refreshUsers$.next(),
        error: (err) => console.error(err),
      });
  }

  createEmployee(): void {
    this.http
      .post(`${environment.authenticationUrl}${environment.auth.registerEmployee}`, {
        email: this.email,
        password: this.password,
        name: this.name,
        phone: this.phone,
      })
      .subscribe({
        next: () => {
          alert("Employee created");
          this.email = "";
          this.name = "";
          this.phone = "";
          this.password = "";
          this.refreshUsers$.next();
        },
        error: (err) => {
          console.error(err);
        },
      });
  }
}
