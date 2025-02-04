import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs'; 
import { catchError } from 'rxjs/operators'; 

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(userData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, userData);
  }

  signup(data: any): Observable<any> {
    console.log("📤 Sending Signup Request to Backend:", data);
  
    return this.http.post(`${this.baseUrl}/signup`, data, { responseType: 'text' }).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error("❌ API Error Occurred:", error);
        console.error("🔴 Error Status:", error.status);
        console.error("🔴 Error Response:", error.error);
        return throwError(() => error);
      })
    );
  }
  

  getAppointments(): Observable<any> {
    return this.http.get(`${this.baseUrl}/appointments/available`);
  }

  bookAppointment(id: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/appointments/book/${id}`, {});
  }
}
