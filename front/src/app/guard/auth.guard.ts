import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router); // Injeta o serviço de roteamento Angular
  const token = localStorage.getItem('acessToken');

  var isAuthenticated = true;

  if (authService.isTokenExpended(token)) {
    localStorage.removeItem('acessToken');
    localStorage.removeItem('refreshToken');
    isAuthenticated = false;
    //alert('Sua sessão expirou, faça login novamente');
  }

  // Avalia como true se token existe, false caso contrário
  const isLoginOrRegisterPage =
    state.url === '/login' || state.url === '/register';

  if (!isAuthenticated && !isLoginOrRegisterPage) {
    return router.parseUrl('/login');
  } else if (isAuthenticated && isLoginOrRegisterPage) {
    console.log('Usuário autenticado');
    return router.parseUrl('/home');
  } else if (isAuthenticated && state.url === '/') {
    console.log('Usuário autenticado');
    return router.parseUrl('/home');
  }

  return true;
};
