import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { BookComponent } from './components/book/book.component'; // ✅ Import the BookComponent

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'home', component: HomeComponent },
  { path: 'book', component: BookComponent }, // ✅ Add the /book route
  { path: '', redirectTo: '/home', pathMatch: 'full' }
];
