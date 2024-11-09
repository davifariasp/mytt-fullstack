import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserServiceService } from '../../services/user-service.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  myRegister!: FormGroup;

  constructor(private route: Router, private userService: UserServiceService, private fb: FormBuilder) {
    this.initForm();
  }

  initForm() {
    this.myRegister = this.fb.group({
      email: ['', Validators.required],
      password: ['',Validators.required],
    });
  }

  handleRegister() {
    //console.log(this.myForm);
    console.log("clicou para fazer registro");

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

  handleRegisterWithGoogle() {
    console.log("clicou para fazer registro com google");
  }

}
