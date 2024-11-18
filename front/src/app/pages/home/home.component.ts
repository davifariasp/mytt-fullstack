import { Component, HostListener } from '@angular/core';
import { PostComponent } from '../../components/post/post.component';
import { AuthService } from '../../services/auth/auth.service';
import { UserService } from '../../services/user/user.service';
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

  pagePosts = 0;

  totalPages!: number;
  
  username:String = "usuário";

  isLoading = false;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router,
    private fb: FormBuilder,
    private postService: PostService
  ) {
    this.initForm();
    
    this.getPosts();

    this.getUserInfo();
  }

  getUserInfo() {
    this.userService.getUserInfo().subscribe({
      next: (data: any) => {
        console.log(data.user.username);
        this.username = data.user.username;
      },
      error: (error: any) => {
        console.log('Erro ao carregar informações do usuário:', error);
      },
      complete: () => {
        console.log('Informações do usuário carregadas com sucesso');
      },
    });
  }

  getPosts() {
    if (this.isLoading) return; // Evita chamadas concorrentes
  
    this.isLoading = true;
  
    this.postService.getPosts(this.pagePosts).subscribe({
      next: (data: any) => {    
        // Adiciona os novos posts à lista existente
        const newPosts = data.posts.map((post: any) => {
          return {
            content: post.content,
            user: post.user,
            createdAt: this.formatDateTime(post.createdAt),
          };
        });

        this.totalPages = data.totalPaginas;
  
        // Concatena os novos posts com os existentes
        this.posts = [...this.posts, ...newPosts];
  
        // Incrementa a página para a próxima chamada
        this.pagePosts++;
  
        console.log(this.posts); // Verifica os posts carregados no console
      },
      error: (error: any) => {
        console.log('Erro ao carregar posts:', error);
      },
      complete: () => {
        this.isLoading = false; // Libera o indicador de carregamento
        console.log('Posts carregados com sucesso');
      },
    });
  }
  

  formatDateTime(postDate: string): string {
    const now = new Date();
    const postTime = new Date(postDate);
    const diffInMs = now.getTime() - postTime.getTime(); // Diferença em milissegundos
    const diffInMinutes = Math.floor(diffInMs / (1000 * 60)); // Conversão para minutos
    const diffInHours = Math.floor(diffInMinutes / 60); // Conversão para horas

    if (diffInMinutes < 60) {
        return `há ${diffInMinutes} minuto(s)`;
    } else if (diffInHours < 24) {
        return `há ${diffInHours} hora(s)`;
    } else {
        return postTime.toLocaleDateString('pt-BR'); // Exibe a data no formato "dd/mm/aaaa"
    }
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
        
        //resetando pesquisa de posts
        this.posts = [];
        this.pagePosts = 0;
        this.totalPages = 0;
        
        this.getPosts();
      },
      error: (error: any) => {
        console.log(error);
      },
      complete: () => {
        console.log('Complete');
      },
    });
  }

  @HostListener('window:scroll', [])
  onScroll() {
    const scrollPosition = window.innerHeight + window.scrollY;
    const scrollThreshold = document.body.offsetHeight - 100;

    if (this.pagePosts >= this.totalPages) {
      return; // Evita chamadas desnecessárias
    }

    if (scrollPosition >= scrollThreshold) {
      this.getPosts(); // Carrega mais posts
    }
  }
}
