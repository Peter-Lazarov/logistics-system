import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { AuthService, UserProfile } from "../auth/auth.service";
import { Observable, BehaviorSubject, switchMap } from "rxjs";

@Component({
  selector: "app-profile",
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: "./profile.html",
  styleUrl: "./profile.scss",
})
export class Profile implements OnInit {
  private refreshProfile$ = new BehaviorSubject<void>(undefined);
  
  profile$!: Observable<UserProfile>;
  isEditing = false;
  isSaving = false;
  name = "";
  phone = "";
  errorMessage = "";
  successMessage = "";

  get bannerRoleClass(): string {
    switch (this.authService.getRole()) {
      case "ROLE_CLIENT":
        return "client-banner";
      case "ROLE_ADMIN":
        return "admin-banner";
      case "ROLE_EMPLOYEE":
      default:
        return "employee-banner";
    }
  }

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.profile$ = this.refreshProfile$.pipe(
      switchMap(() => this.authService.getMe())
    );
  }

  startEditing(profile: UserProfile): void {
    this.name = profile.name;
    this.phone = profile.phone;
    this.errorMessage = "";
    this.successMessage = "";
    this.isEditing = true;
  }

  cancelEditing(): void {
    this.isEditing = false;
    this.errorMessage = "";
  }

  saveProfile(): void {
    this.isSaving = true;
    this.errorMessage = "";
    this.successMessage = "";

    this.authService.updateProfile({
      name: this.name.trim(),
      phone: this.phone.trim(),
    }).subscribe({
      next: () => {
        this.refreshProfile$.next(); 
        
        this.isEditing = false;
        this.isSaving = false;
        this.successMessage = "Profile updated successfully.";
      },
      error: () => {
        this.isSaving = false;
        this.errorMessage = "Could not update your profile. Please try again.";
      },
    });
  }
}
