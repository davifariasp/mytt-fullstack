import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@env';
import { UserRegister } from '../models/user/userRegister';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  constructor(private client: HttpClient) { }

  getHelloWorld(): Observable<any> {
    return this.client.get(environment.apiUrl + '/hello-world');
  }

  registerUser(user: UserRegister): Observable<any> {
    return this.client.post(environment.apiUrl + '/users', user);
  }
}
