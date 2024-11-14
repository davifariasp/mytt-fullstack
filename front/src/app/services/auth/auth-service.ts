import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor() { }

  isTokenExpended(token: string | null): boolean {
    if (!token) {
      return true;
    }

    try {
      const decodedToken: any = jwtDecode(token);
      const expirationDate = decodedToken.exp * 1000;
      return Date.now() >= expirationDate;
    } catch (error) {
      console.error(error);
      return true;
    }
  }

  logout() {
    localStorage.removeItem('acessToken');
    localStorage.removeItem('refreshToken');
  }
}
