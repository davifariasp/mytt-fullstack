import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss'
})
export class PostComponent {
  @Input() usuario: string = ''; // Dados do usuário
  @Input() conteudo: string = ''; // Conteúdo do post
  @Input() data: string = ''; // Data do post
}
