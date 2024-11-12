import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router); // Injeta o serviço de roteamento Angular
  const token = localStorage.getItem("acessToken");
  
  const isAuthenticated = !!token; // Avalia como true se token existe, false caso contrário
  const isLoginOrRegisterPage = state.url === '/login' || state.url === '/register';

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
