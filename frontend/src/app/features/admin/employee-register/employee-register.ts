import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-employee-register',
  imports: [FormsModule],
  templateUrl: './employee-register.html',
  styleUrl: './employee-register.scss'
})
export class EmployeeRegister {

  username = '';
  password = '';

  constructor(
    private http: HttpClient
  ) {}

  createEmployee(): void {

    this.http.post(
      'http://localhost:8080/auth/admin/register-employee',
      {
        username: this.username,
        password: this.password
      }
    ).subscribe({

      next: () => {

        alert('Employee created');

        this.username = '';
        this.password = '';

      },

      error: err => {
        console.error(err);
      }

    });

  }

}
