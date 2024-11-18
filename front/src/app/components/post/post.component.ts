import { Component, Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [MatIconModule],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss'
})
export class PostComponent {
  @Input() usuario: string = ''; // Dados do usuário
  @Input() conteudo: string = ''; // Conteúdo do post
  @Input() data: string = ''; // Data do post
}
