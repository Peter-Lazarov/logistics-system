import { Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../../environments/environment";

@Component({
  selector: "app-employee-register",
  imports: [FormsModule],
  templateUrl: "./employee-register.html",
  styleUrl: "./employee-register.scss",
})
export class EmployeeRegister {
  username = "";
  password = "";
  users: any[] = [];

  constructor(private http: HttpClient) {}

  loadUsers(): void {
    this.http.get<any[]>(`${environment.authenticationUrl}${environment.auth.users}`).subscribe({
      next: (data) => {
        this.users = data;
      },

      error: (err) => {
        console.error(err);
      },
    });
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  createEmployee(): void {
    this.http
      .post(`${environment.authenticationUrl}${environment.auth.registerEmployee}`, {
        username: this.username,
        password: this.password,
      })
      .subscribe({
        next: () => {
          alert("Employee created");

          this.username = "";
          this.password = "";

          this.loadUsers();
        },

        error: (err) => {
          console.error(err);
        },
      });
  }
}
