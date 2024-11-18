import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@env';
import { UserRegister } from '../../models/user/userRegister';
import { UserLogin } from '../../models/user/userLogin';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private client: HttpClient) { }
  
  token = localStorage.getItem('acessToken');

  getHelloWorld(): Observable<any> {
    return this.client.get(environment.apiUrl + '/hello-world');
  }

  registerUser(user: UserRegister): Observable<any> {
    return this.client.post(environment.apiUrl + '/users', user);
  }

  login(user: UserLogin): Observable<any> {
    return this.client.post(environment.apiUrl + '/login', user);
  }

  getUserInfo(): Observable<String | any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`, // Substitua por como você obtém seu token
      'Content-Type': 'application/json' // Opcional, mas frequentemente necessário
    });

    return this.client.get(environment.apiUrl + '/users/me', {headers});
  }
}
