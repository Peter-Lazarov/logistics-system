import { ChangeDetectorRef, Component } from "@angular/core";
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
} from "@angular/forms";
import { Router } from "@angular/router";
import { finalize } from "rxjs";
import { AuthService } from "../../core/auth/auth.service";

const emailPattern = /^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?(?:\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)+$/;
const namePattern = /^[\p{L}]+([ '-][\p{L}]+)*$/u;
const phonePattern = /^[+]?[0-9() -]+$/;

const trimmedRequired: ValidatorFn = (control: AbstractControl): ValidationErrors | null =>
  typeof control.value !== "string" || control.value.trim().length === 0
    ? { required: true }
    : null;

function trimmedLength(minimum: number, maximum: number, collapseSpaces = false): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (typeof control.value !== "string") return null;
    let value = control.value.trim();
    if (collapseSpaces) value = value.replace(/\s+/g, " ");
    if (value.length < minimum) return { minlength: { requiredLength: minimum, actualLength: value.length } };
    if (value.length > maximum) return { maxlength: { requiredLength: maximum, actualLength: value.length } };
    return null;
  };
}

function trimmedPattern(pattern: RegExp, collapseSpaces = false): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (typeof control.value !== "string" || control.value.trim().length === 0) return null;
    let value = control.value.trim();
    if (collapseSpaces) value = value.replace(/\s+/g, " ");
    return pattern.test(value) ? null : { pattern: true };
  };
}

@Component({
  selector: "app-register",
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: "./register.html",
  styleUrl: "./register.scss",
})
export class Register {
  readonly form: FormGroup;
  submitted = false;
  isSubmitting = false;
  passwordFocused = false;
  errorMessage = "";
  successMessage = "";

  constructor(
    formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {
    this.form = formBuilder.group({
      email: ["", [trimmedRequired, trimmedLength(5, 100), trimmedPattern(emailPattern)]],
      password: ["", [trimmedRequired, trimmedLength(4, 50)]],
      name: ["", [trimmedRequired, trimmedLength(2, 100, true), trimmedPattern(namePattern, true)]],
      phone: ["", [trimmedRequired, trimmedLength(7, 20), trimmedPattern(phonePattern)]],
    });
  }

  fieldError(field: "email" | "password" | "name" | "phone"): string | null {
    const control = this.form.controls[field];
    if (!control || (!control.touched && !this.submitted)) return null;
    if (control.hasError("required")) {
      return {
        email: "Email is required",
        password: "Password is required",
        name: "Full name is required",
        phone: "Phone number is required",
      }[field];
    }
    if (control.hasError("pattern")) {
      return {
        email: "Invalid email format",
        password: null,
        name: "Full name contains invalid characters",
        phone: "Invalid phone number format",
      }[field];
    }
    if (control.hasError("minlength") || control.hasError("maxlength")) {
      return {
        email: "Email must be between 5 and 100 characters",
        password: "Password must be between 4 and 50 characters",
        name: "Full name must be between 2 and 100 characters",
        phone: "Phone number must contain between 7 and 20 characters",
      }[field];
    }
    return null;
  }

  register(): void {
    if (this.isSubmitting) return;
    this.submitted = true;
    this.form.markAllAsTouched();
    this.errorMessage = "";
    this.successMessage = "";

    const values = this.form.getRawValue();
    values.email = values.email.trim();
    values.password = values.password.trim();
    values.name = values.name.trim().replace(/\s+/g, " ");
    values.phone = values.phone.trim();
    this.form.patchValue(values, { emitEvent: false });
    this.form.updateValueAndValidity();
    if (this.form.invalid) return;

    this.isSubmitting = true;
    this.cdr.markForCheck();
    this.authService
      .register({
        email: values.email,
        password: values.password,
        name: values.name,
        phone: values.phone,
      })
      .pipe(
        finalize(() => {
          this.isSubmitting = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (response) => {
          this.successMessage = response.message ?? "Registration successful.";
          setTimeout(() => this.router.navigate(["/login"]), 2000);
        },
        error: (err) => {
          const fieldErrors = err?.error?.errors as Record<string, string> | undefined;
          this.errorMessage = fieldErrors
            ? Object.values(fieldErrors).join(" ")
            : err?.error?.message ?? "We could not complete your registration. Please try again.";
        },
      });
  }
}
