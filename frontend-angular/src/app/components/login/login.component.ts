import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [CommonModule, FormsModule]
})
export class LoginComponent {
  loginData = { email: '', password: '' };
  errorMessage: string = '';
  
  constructor(private apiService: ApiService, private router: Router) {}

  onLogin() {
    this.apiService.login(this.loginData).subscribe(
      (response: any) => {
        console.log('Login successful:', response);
        
        const tokenObject = JSON.parse(response.token); // ✅ Extract actual token
        const token = tokenObject.token; // ✅ Get the correct token value
  
        localStorage.setItem('token', token);
        localStorage.setItem('userId', response.userId);
        localStorage.setItem('userName', response.userName);
        localStorage.setItem('userEmail', response.userEmail);
        localStorage.setItem('userPhone', response.userPhone);
  
        // ✅ Navigate first, then refresh the page to update navbar
        this.router.navigate(['/home']).then(() => {
          window.location.reload(); // ✅ Refresh to immediately update navbar
        });
      },
      (error: any) => {
        console.error('Login failed:', error);
        this.errorMessage = 'Invalid email or password!';
      }
    );
  }  
}
