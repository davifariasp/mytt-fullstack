import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router); // Injeta o serviço de roteamento Angular
  const token = localStorage.getItem("token");
  
  const isAuthenticated = !!token; // Avalia como true se token existe, false caso contrário
  const isLoginOrRegisterPage = state.url === '/login' || state.url === '/register';

  if (!isAuthenticated && !isLoginOrRegisterPage) {
    return router.parseUrl('/unauthorized');
  } else if (isAuthenticated && isLoginOrRegisterPage) {
    return router.parseUrl('/home');
  } else if (state.url === '/') {
    return router.parseUrl('/home');
  } else if (isAuthenticated && state.url === '/unauthorized') {
    return router.parseUrl('/home');
  }

  return true;
};
