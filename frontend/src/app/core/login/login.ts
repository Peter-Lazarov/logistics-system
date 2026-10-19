import { ChangeDetectorRef, Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Router } from "@angular/router";

import { AuthService } from "../auth/auth.service";
import { ClientProfileService } from "../profile/client-profile.service";

@Component({
  selector: "app-login",
  imports: [FormsModule],
  templateUrl: "./login.html",
  styleUrl: "./login.scss",
})
export class Login {
  errorMessage = "";
  // email = "peter@sunny-times.com";
  email = "georgy@sunny-times.com"
  password = "1234";

  constructor(
    private authService: AuthService,
    private clientProfileService: ClientProfileService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}

  login(): void {
    this.errorMessage = "";
    this.authService
      .login({
        email: this.email,
        password: this.password,
      })
      .subscribe({
        next: (response) => {
          localStorage.setItem("accessToken", response.accessToken);

          localStorage.setItem("refreshToken", response.refreshToken);

          localStorage.setItem("role", response.role);

          switch (response.role) {
            case "ROLE_CLIENT":
              this.clientProfileService.getCurrent().subscribe({
                next: (profile) => {
                  this.router.navigate([
                    this.clientProfileService.isComplete(profile)
                      ? "/shipments/my"
                      : "/profile",
                  ]);
                },
                error: () => this.router.navigate(["/profile"]),
              });
              break;

            case "ROLE_EMPLOYEE":
              this.router.navigate(["/shipments"]);
              break;

            case "ROLE_ADMIN":
              this.router.navigate(["/shipments"]);
              break;

            default:
              this.router.navigate(["/login"]);
          }
        },

        error: (err) => {
          this.errorMessage =
            err?.error?.message ?? "We could not sign you in. Please try again.";
          this.cdr.markForCheck();
        },
      });
  }
}
