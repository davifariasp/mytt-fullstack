import { Component } from '@angular/core';
import { PostComponent } from "../../components/post/post.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [PostComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  //chamada para os twits
  // constructor(private helloWorldService: HelloWorldService, private router: Router) {
  //   this.helloWorldService.getHelloWorld().subscribe({
  //     next: (data) => {
  //       console.log(data);
  //     },
  //     error: (error) => {
  //       console.log(error);
  //     },
  //     complete: () => {
  //       console.log('Complete');
  //     },
  //   });
  // }
}
