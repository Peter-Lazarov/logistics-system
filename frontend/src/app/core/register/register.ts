import { ChangeDetectorRef, Component } from "@angular/core";
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
  errorMessage = "";
  successMessage = "";

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}

  register(): void {
    this.errorMessage = "";
    this.successMessage = "";
    this.authService
      .register({
        email: this.email,
        password: this.password,
        name: this.name,
        phone: this.phone,
      })
      .subscribe({
        next: (response) => {
          this.successMessage = response.message ?? "Registration successful.";
          setTimeout(() => this.router.navigate(["/login"]), 2000);
        },
        error: (err) => {
          this.errorMessage =
            err?.error?.message ?? "We could not complete your registration. Please try again.";
          this.cdr.markForCheck();
        },
      });
  }
}
