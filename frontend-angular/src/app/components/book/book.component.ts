import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-book',
  standalone: true,
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css'],
})
export class BookComponent implements OnInit {
  appointmentId: number | null = null;
  appointmentDate: string = '';  // ✅ Declare missing properties
  appointmentTime: string = '';  // ✅ Declare missing properties
  userName: string = '';  // ✅ Declare missing properties
  userEmail: string = '';  // ✅ Declare missing properties
  userPhone: string = ''; 
  user = { name: '', phone: '', email: '', address: '' };
  appointmentDetails = { date: '', time: '' };

  constructor(private route: ActivatedRoute, public router: Router, private http: HttpClient) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      console.log('📌 Query Params:', params);
      
      this.appointmentId = params['appointmentId'] ? Number(params['appointmentId']) : null;
      this.appointmentDate = params['date'] || '';
      this.appointmentTime = params['time'] || '';
  
      // ✅ If query params are missing, try getting from localStorage
      if (!this.appointmentId) {
        console.warn('⚠️ No appointmentId in query params. Checking localStorage...');
        this.appointmentId = Number(localStorage.getItem('appointmentId'));
        this.appointmentDate = localStorage.getItem('appointmentDate') || '';
        this.appointmentTime = localStorage.getItem('appointmentTime') || '';
      }
  
      console.log('📌 Loaded Data:', {
        appointmentId: this.appointmentId,
        appointmentDate: this.appointmentDate,
        appointmentTime: this.appointmentTime
      });
    });
  
    this.userName = localStorage.getItem('userName') || '';
    this.userEmail = localStorage.getItem('userEmail') || '';
    this.userPhone = localStorage.getItem('userPhone') || '';
  
    console.log('📌 User Details:', {
      userName: this.userName,
      userEmail: this.userEmail,
      userPhone: this.userPhone
    });
  }

  fetchAppointmentDetails() {
    if (!this.appointmentId) {
      console.error('Invalid appointment ID.');
      return;
    }
  
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
  
    this.http.get<any>(`http://localhost:8080/api/appointments/${this.appointmentId}`, { headers })
      .subscribe(
        data => {
          if (!data || !data.date || !data.time) {
            console.error('No appointment data received or missing fields:', data);
            alert('Failed to load appointment details.');
            return;
          }
  
          console.log('Fetched appointment details:', data); // ✅ Debugging log
  
          this.appointmentDetails = {
            date: data.date,
            time: data.time
          };
        },
        error => {
          console.error('Error fetching appointment details:', error);
          alert('Failed to load appointment details.');
        }
      );
  }
  
  confirmBooking() {
    if (!this.appointmentId) {
      alert('Invalid appointment. Please try again.');
      return;
    }
  
    console.log('🚀 confirmBooking() triggered');
  
    const userId = localStorage.getItem('userId') || ''; 
    const userName = localStorage.getItem('userName') || '';
    const userEmail = localStorage.getItem('userEmail') || '';
    const userPhone = localStorage.getItem('userPhone') || '';
    const token = localStorage.getItem('token');
  
    if (!token) {
      console.error('❌ No token found. User must be logged in.');
      alert('User authentication error. Please log in again.');
      return;
    }
  
    const headers = { Authorization: `Bearer ${token}` };
  
    const bookingData = {
      appointmentId: this.appointmentId,
      userId: userId,
      guestName: userName,
      guestEmail: userEmail,
      guestPhone: userPhone
    };
  
    console.log('📤 Sending Booking Request:', bookingData, 'with headers:', headers);
  
    this.http.post(`http://localhost:8080/api/appointments/book/${this.appointmentId}`, bookingData, { headers,responseType: 'text' })
    .subscribe({
      next: (response) => {
        console.log('✅ Booking successful:', response);
        alert('Appointment booked successfully!');
        this.router.navigate(['/home']);
      },
      error: (error) => {
        console.error('❌ Booking failed:', error);
        if (error.error && typeof error.error === 'object') {
          alert(error.error.error || 'Booking failed. Please try again.');
        } else {
          alert('Unexpected error. Check console logs.');
        }
      }
    });
  
  }
  
}
