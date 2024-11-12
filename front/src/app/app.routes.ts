import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { authGuard } from './services/authGuard/auth.guard';
import { UnauthorizedComponent } from './pages/unauthorized/unauthorized.component';

export const routes: Routes = [
    {
        path: '',
        component: AppComponent,
        canActivate: [authGuard]
    },
    {
        path: 'home',
        component: HomeComponent,
        canActivate: [authGuard]
    },
    {
        path: 'login',
        component: LoginComponent,
        canActivate: [authGuard]
    },
    {
        path: 'register',
        component: RegisterComponent,
        canActivate: [authGuard]
    },
    {
        path: 'unauthorized',
        component: UnauthorizedComponent,
        canActivate: [authGuard]
    }
];
