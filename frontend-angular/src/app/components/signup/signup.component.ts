import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { FullCalendarModule } from '@fullcalendar/angular';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, FormsModule, FullCalendarModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  signupData = {
    fullName: '',
    email: '',
    password: '',
    phone: '',
    address: '',
    role: 'USER' // ✅ Ensure role is included
  };
  

  successMessage: string = '';
  errorMessage: string = '';

  constructor(private apiService: ApiService) {}

  onSignup() {
    console.log("🔵 Attempting Signup with Data:", this.signupData);
    
    this.apiService.signup(this.signupData).subscribe({
      next: (response) => {
        console.log("✅ Signup successful:", response);
        this.successMessage = "Signup successful! You can now log in.";
        this.errorMessage = ""; // Clear any previous error
      },
      error: (error) => {
        console.error("❌ Signup failed, Error Object:", error);
        console.error("🔴 Error Status:", error.status);
        console.error("🔴 Error Message:", error.error?.message || "Unknown error");
  
        if (error.status === 400) {
          this.errorMessage = error.error?.message || "Email already registered!";
        } else {
          this.errorMessage = "Signup failed. Please try again later.";
        }
  
        this.successMessage = ""; // Clear success message if error occurs
      }
    });
  }
  
  
}
