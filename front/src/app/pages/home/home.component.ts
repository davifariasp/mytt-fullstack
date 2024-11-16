import { Component } from '@angular/core';
import { PostComponent } from '../../components/post/post.component';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { PostService } from '../../services/post/post.service';

interface PostResponse {
  content: string;
  user: string;
  createdAt: string;
}
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [PostComponent, ReactiveFormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  posts: PostResponse[] = [];

  formPost!: FormGroup;

  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder,
    private postService: PostService
  ) {
    this.initForm();
    
    this.postService.getPosts().subscribe({
      next: (data: any) => {
        
        this.posts = data.map((post: any) => {
          return {
            content: post.content,
            user: post.user,
            createdAt: new Date(post.createdAt).toLocaleString(),
          };
        });

        console.log(data);
      },
      error: (error: any) => {
        console.log(error);
      },
      complete: () => {
        console.log('Complete');
      },
    });
  }

  initForm() {
    this.formPost = this.fb.group({
      content: ['', Validators.required],
    });
  }

  handleLogout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  handlePost() {
    this.postService.createPost(this.formPost.value.content).subscribe({
      next: (data: any) => {
        console.log(data);
        this.formPost.reset();
        this.postService.getPosts().subscribe({
          next: (data: any) => {
            this.posts = data;
            console.log(data);
          },
          error: (error: any) => {
            console.log(error);
          },
          complete: () => {
            console.log('Complete');
          },
        });
      },
      error: (error: any) => {
        console.log(error);
      },
      complete: () => {
        console.log('Complete');
      },
    });
  }
}
