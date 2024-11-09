import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
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
