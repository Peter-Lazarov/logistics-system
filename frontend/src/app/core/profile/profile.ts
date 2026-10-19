import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { AuthService, UserProfile } from "../auth/auth.service";
import { Observable, BehaviorSubject, combineLatest, of, shareReplay, switchMap, take } from "rxjs";
import { ClientProfile, ClientProfileService } from "./client-profile.service";

@Component({
  selector: "app-profile",
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: "./profile.html",
  styleUrl: "./profile.scss",
})
export class Profile implements OnInit {
  private refreshProfile$ = new BehaviorSubject<void>(undefined);
  private refreshClientProfile$ = new BehaviorSubject<void>(undefined);
  
  profile$!: Observable<UserProfile>;
  clientProfile$!: Observable<ClientProfile>;
  isEditing = false;
  isSaving = false;
  name = "";
  phone = "";
  companyName = "";
  companyAddress = "";
  vatNumber = "";
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

  constructor(
    private authService: AuthService,
    private clientProfileService: ClientProfileService,
  ) {}

  ngOnInit(): void {
    this.profile$ = this.refreshProfile$.pipe(
      switchMap(() => this.authService.getMe()),
      shareReplay({ bufferSize: 1, refCount: true }),
    );
    this.clientProfile$ = this.authService.isClient()
      ? this.refreshClientProfile$.pipe(
          switchMap(() => this.clientProfileService.getCurrent()),
          shareReplay({ bufferSize: 1, refCount: true }),
        )
      : of({ userId: 0, companyName: null, companyAddress: null, vatNumber: null });

    if (this.authService.isClient()) {
      combineLatest([this.profile$, this.clientProfile$])
        .pipe(take(1))
        .subscribe(([profile, clientProfile]) => {
          if (!this.clientProfileService.isComplete(clientProfile)) {
            this.startEditing(profile, clientProfile);
          }
        });
    }
  }

  isClientProfileComplete(profile: ClientProfile): boolean {
    return this.clientProfileService.isComplete(profile);
  }

  startEditing(profile: UserProfile, clientProfile: ClientProfile): void {
    this.name = profile.name;
    this.phone = profile.phone;
    this.companyName = clientProfile.companyName ?? "";
    this.companyAddress = clientProfile.companyAddress ?? "";
    this.vatNumber = clientProfile.vatNumber ?? "";
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
      next: () => this.saveClientProfile(),
      error: () => this.handleSaveError(),
    });
  }

  private saveClientProfile(): void {
    this.clientProfile$.pipe(
      take(1),
      switchMap((profile) => this.clientProfileService.updateCurrent({
        userId: profile.userId,
        companyName: this.companyName.trim(),
        companyAddress: this.companyAddress.trim(),
        vatNumber: this.vatNumber.trim(),
      })),
    ).subscribe({
      next: () => {
        this.refreshProfile$.next();
        this.refreshClientProfile$.next();
        this.isEditing = false;
        this.isSaving = false;
        this.successMessage = "Profile updated successfully.";
      },
      error: () => this.handleSaveError(),
    });
  }

  private handleSaveError(): void {
    this.isSaving = false;
    this.errorMessage = "Could not update your profile. Please try again.";
  }
}
