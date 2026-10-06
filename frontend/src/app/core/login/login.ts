import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {

  username = 'peter';
  password = '1234';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login(): void {

    this.authService.login({
      username: this.username,
      password: this.password
    }).subscribe({

      next: response => {

        localStorage.setItem(
          'accessToken',
          response.accessToken
        );

        localStorage.setItem(
          'refreshToken',
          response.refreshToken
        );

        localStorage.setItem(
          'role',
          response.role
        );

        switch (response.role) {

          case 'ROLE_CLIENT':
            this.router.navigate(['/client/request']);
            break;

          case 'ROLE_EMPLOYEE':
            this.router.navigate(['/shipments']);
            break;

          case 'ROLE_ADMIN':
            this.router.navigate(['/shipments']);
            break;

          default:
            this.router.navigate(['/login']);
        }
      },

      error: err => {

        console.error('LOGIN ERROR');
        console.error(err);

      }

    });

  }

}