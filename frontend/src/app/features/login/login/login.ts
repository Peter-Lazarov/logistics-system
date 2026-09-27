import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {

  username = '';
  password = '';

  constructor(
    private authService: AuthService
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

        console.log('LOGIN SUCCESS');

        console.log(response);

      },

      error: err => {

        console.error('LOGIN ERROR');

        console.error(err);

      }

    });
  }
}