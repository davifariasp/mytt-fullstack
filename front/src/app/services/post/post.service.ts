import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@env';
import { delay, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private client: HttpClient) {}

  token = localStorage.getItem('acessToken');
  
  
  getPosts(pagina:number) : Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`, // Substitua por como você obtém seu token
      'Content-Type': 'application/json' // Opcional, mas frequentemente necessário
    });

    const params = { pagina: pagina};

    return this.client.get(environment.apiUrl + '/posts/user', {headers, params}).pipe(delay(2000));;
  }

  createPost(postContent: String) : Observable<any> {
    
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`, // Substitua por como você obtém seu token
      'Content-Type': 'application/json' // Opcional, mas frequentemente necessário
    });

    const body = { content: postContent}

    return this.client.post(environment.apiUrl + '/posts', body, {headers, responseType: 'text'});
  }
}
