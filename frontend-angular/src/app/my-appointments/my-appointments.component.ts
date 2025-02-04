import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { CommonModule } from '@angular/common'; // ✅ Import CommonModule

@Component({
  selector: 'app-my-appointments',
  standalone: true,
  templateUrl: './my-appointments.component.html',
  styleUrls: ['./my-appointments.component.css'],
  imports: [CommonModule] // ✅ Include CommonModule for *ngIf and *ngFor
})
export class MyAppointmentsComponent implements OnInit {
  appointments: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchAppointments();
  }

  fetchAppointments() {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };

    this.http.get<any[]>(`${environment.apiUrl}/appointments/user/${userId}`, { headers }).subscribe({
      next: (response) => {
        this.appointments = response;
      },
      error: (error) => {
        console.error("❌ Error fetching appointments:", error);
      }
    });
  }

  deleteAppointment(appointmentId: number) {
    if (!confirm('Are you sure you want to cancel this appointment?')) return;
  
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
  
    this.http.delete(`${environment.apiUrl}/appointments/bookings/${appointmentId}`, { headers, responseType: 'text' })
      .subscribe({
        next: (response) => {
          console.log("✅ Response:", response);
          alert('Appointment deleted successfully!');
          this.fetchAppointments(); // Refresh the list
        },
        error: (error) => {
          console.error("❌ Error deleting appointment:", error);
          alert('Failed to delete appointment.');
        }
      });
  }
  
}
