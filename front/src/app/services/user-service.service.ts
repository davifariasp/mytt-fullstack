import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  private apiUrl = "https://jsonplaceholder.typicode.com/posts";

  constructor(private client: HttpClient) { }

  getHelloWorld(): Observable<any> {

    return this.client.get(this.apiUrl)
  }
}
