import { Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../core/auth/auth.service";

@Component({
  selector: "app-register",
  standalone: true,
  imports: [FormsModule],
  templateUrl: "./register.html",
  styleUrl: "./register.scss",
})
export class Register {
  email = "";
  password = "";
  name = "";
  phone = "";

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  register(): void {
    this.authService
      .register({
        email: this.email,
        password: this.password,
        name: this.name,
        phone: this.phone,
      })
      .subscribe({
        next: () => {
          this.router.navigate(["/login"]);
        },
        error: (err) => {
          console.error(err);
          alert("Registration failed");
        },
      });
  }
}
