import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [CommonModule, FullCalendarModule],
})
export class HomeComponent implements OnInit {
  calendarOptions: CalendarOptions = {
    initialView: 'timeGridWeek',
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
    selectable: true,
    slotMinTime: '09:00:00',
    slotMaxTime: '18:00:00',
    allDaySlot: false,
    events: [],
    eventClick: this.handleSlotClick.bind(this), // ✅ Handle clicking a slot
  };

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.fetchAppointments();
  }

  fetchAppointments() {
    const token = localStorage.getItem('token');
  
    if (!token) {
      console.error('❌ No token found. User must log in.');
      return;
    }
  
    this.http.get<any[]>('http://localhost:8080/api/appointments/available', {
      headers: { Authorization: `Bearer ${token}` }
    }).subscribe(
      data => {
        console.log('✅ Available appointments:', data);
        this.calendarOptions.events = data.map(appointment => ({
          id: appointment.id, // ✅ Ensure ID is present
          title: 'Available',
          start: `${appointment.date}T${appointment.time}`,
          extendedProps: { id: appointment.id } // ✅ Ensure ID is available in events
        }));
      },
      error => {
        console.error('❌ Error fetching appointments:', error);
      }
    );
  }
  

  handleSlotClick(event: any) {
    console.log('✅ Slot Clicked Event:', event);
  
    if (!event.event || !event.event.start) {
      console.error('❌ Invalid slot data:', event);
      return;
    }
  
    const appointmentId = event.event.id || event.event.extendedProps?.id; 
    const appointmentDate = event.event.startStr.split('T')[0]; 
    const appointmentTime = event.event.startStr.split('T')[1]; 
  
    if (!appointmentId) {
      console.error('❌ Missing appointment ID.');
      return;
    }
  
    console.log('✅ Extracted Appointment:', {
      appointmentId,
      appointmentDate,
      appointmentTime
    });
  
    // ✅ Store in localStorage as backup
    localStorage.setItem('appointmentId', appointmentId.toString());
    localStorage.setItem('appointmentDate', appointmentDate);
    localStorage.setItem('appointmentTime', appointmentTime);
  
    console.log('✅ Redirecting to /book...');
    this.router.navigate(['/book'], {
      queryParams: { appointmentId, date: appointmentDate, time: appointmentTime }
    });
  }  
}
