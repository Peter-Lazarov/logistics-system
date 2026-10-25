import { Component, signal } from "@angular/core";
import { RouterOutlet, Router, RouterLink } from "@angular/router";
import { GuideComponent } from "./shared/guide/guide.component";

@Component({
  imports: [RouterOutlet, RouterLink, GuideComponent],
  selector: "app-root",
  styleUrl: "./app.scss",
  templateUrl: "./app.html",
})
export class App {
  protected readonly title = signal("frontend");

  constructor(private router: Router) {}

  get email(): string {
    const token = localStorage.getItem("accessToken");
    const encodedPayload = token?.split(".")[1];

    if (!encodedPayload) {
      return "";
    }

    try {
      const base64Payload = encodedPayload
        .replace(/-/g, "+")
        .replace(/_/g, "/");
      const payload = JSON.parse(
        atob(base64Payload.padEnd(Math.ceil(base64Payload.length / 4) * 4, "=")),
      ) as { sub?: unknown; email?: unknown };

      if (typeof payload.sub === "string") {
        return payload.sub;
      }

      return typeof payload.email === "string" ? payload.email : "";
    } catch {
      return "";
    }
  }

  get role(): string {
    switch (localStorage.getItem("role")) {
      case "ROLE_ADMIN":
        return "Admin";
      case "ROLE_EMPLOYEE":
        return "Employee";
      case "ROLE_CLIENT":
        return "Client";
      default:
        return "";
    }
  }

  logout(): void {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("role");

    this.router.navigate(["/login"]);
  }

  isAdmin(): boolean {
    return localStorage.getItem("role") === "ROLE_ADMIN";
  }

  isEmployee(): boolean {
    return localStorage.getItem("role") === "ROLE_EMPLOYEE";
  }

  isClient(): boolean {
    return localStorage.getItem("role") === "ROLE_CLIENT";
  }
}
