# **📅 Appointment Booking System**  

A **full-stack** appointment booking system built with **Spring Boot (Backend) and Angular (Frontend)**. Users can **view available slots, book appointments, view their bookings, and cancel appointments**.

---

## **🛠️ Technologies Used**  

### **🔹 Backend (Spring Boot)**  
- **Spring Boot 3** (REST API)  
- **Spring Data JPA** (Database operations)  
- **MySQL / H2 Database**  
- **Spring Security & JWT** (Authentication & Authorization)  

### **🔹 Frontend (Angular)**  
- **Angular 16+**  
- **Angular Material** (UI Components)  
- **RxJS & HttpClient** (API Requests)  
- **Bootstrap & CSS** (Styling)  

---

## **📌 Features**  

### **✅ Backend Features (Spring Boot)**  
✔️ **Fetch Available Appointments** – Retrieve all unbooked slots for the next 7 days.  
✔️ **Book an Appointment** – Assign a slot to a user and mark it as booked.  
✔️ **View User Appointments** – List all booked appointments for a user.  
✔️ **Cancel an Appointment** – Delete a booking and make the slot available again.  

### **✅ Frontend Features (Angular)**  
✔️ **View Available Appointments** – Display all free slots fetched from the backend.  
✔️ **Book an Appointment** – Select a slot and confirm the booking.  
✔️ **View My Appointments** – Show all booked appointments for the logged-in user.  
✔️ **Cancel Appointment** – Allow users to delete a booking.  

---

## **📌 API Endpoints**  

| Method | Endpoint | Description |
|--------|---------|-------------|
| `GET` | `/api/appointments/available` | Fetch all available slots |
| `POST` | `/api/appointments/book/{id}` | Book an appointment |
| `GET` | `/api/appointments/user/{userId}` | View user's booked appointments |
| `DELETE` | `/api/appointments/bookings/{bookingId}` | Cancel an appointment |

---

## **🚀 How to Run the Project**  

### **1️⃣ Backend (Spring Boot)**  
#### **Prerequisites:**  
- Java 17+  
- Maven  
- MySQL or H2 Database  

#### **Steps to Run:**  
1. **Clone the repository**  
   ```sh
   git clone https://github.com/your-repo/appointment-booking.git
   cd appointment-booking/backend
   ```
2. **Configure the Database** (Optional: Edit `application.properties` for MySQL)  
3. **Run the Application**  
   ```sh
   mvn spring-boot:run
   ```
4. **Backend runs on:**  
   ```
   http://localhost:8080
   ```

---

### **2️⃣ Frontend (Angular)**  
#### **Prerequisites:**  
- Node.js & npm  
- Angular CLI  

#### **Steps to Run:**  
1. **Navigate to the frontend directory**  
   ```sh
   cd frontend
   ```
2. **Install dependencies**  
   ```sh
   npm install
   ```
3. **Start the Angular App**  
   ```sh
   ng serve
   ```
4. **Frontend runs on:**  
   ```
   http://localhost:4200
   ```

---

## **📌 Future Enhancements**  
- Add **rescheduling functionality** for booked appointments.  
- Implement **email notifications** for bookings and cancellations.  
- Improve **UI/UX with Angular Material**.  

