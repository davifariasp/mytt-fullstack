import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserServiceService } from '../../services/user-service.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  myForm!: FormGroup;

  constructor(private route: Router, private userService: UserServiceService, private fb: FormBuilder) {
    this.initForm();
  }

  initForm() {
    this.myForm = this.fb.group({
      email: ['', Validators.required],
      password: ['',Validators.required],
    });
  }

  toRegister() {
    this.route.navigate(['/register']);
  }

  handleLogin() {
    //console.log(this.myForm);
    console.log("clicou para fazer login");

    this.userService.getHelloWorld().subscribe({
      next: (data: any) => {
        console.log(data);
        this.route.navigate(['/home']);
      },
      error: (error: any) => {
        console.log(error);
      },
      complete: () => {
        console.log('Complete');
      },
    });
  }

  handleLoginWithGoogle() {
    console.log("clicou para fazer login com google");
  }
}
