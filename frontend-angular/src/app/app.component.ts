import { Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FullCalendarModule } from '@fullcalendar/angular';

@Component({
  standalone: true,
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [RouterModule, CommonModule, FormsModule, FullCalendarModule] 
})
export class AppComponent {
  isLoggedIn: boolean = !!localStorage.getItem('token'); // ✅ Check login status

  constructor(private router: Router) {}

  logout() {
    console.log("User logged out"); // ✅ Debugging log
    localStorage.removeItem('token'); // ✅ Clear auth token if using JWT
    this.isLoggedIn = false; // ✅ Update login status
    this.router.navigate(['/login']); // ✅ Redirect to login page
  }
}
