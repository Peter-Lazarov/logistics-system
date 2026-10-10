import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AuthService } from "../auth/auth.service";
import { Observable } from "rxjs";

@Component({
  selector: "app-profile",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "./profile.html",
  styleUrl: "./profile.scss",
})
export class Profile implements OnInit {
  profile$!: Observable<any>;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.profile$ = this.authService.getMe();
  }
}
