import { Component, signal } from '@angular/core';
import { RouterOutlet, Router, RouterLink } from '@angular/router';

@Component({
  imports: [
    RouterOutlet,
    RouterLink
  ],
  selector: 'app-root',
  styleUrl: './app.scss',
  templateUrl: './app.html',
})
export class App {

  protected readonly title = signal('frontend');

  constructor(private router: Router) {
  }

  logout(): void {

    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');

    this.router.navigate(['/login']);

  }

}
