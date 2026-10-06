import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { CanActivateFn } from '@angular/router';

export const roleGuard: CanActivateFn = (
  route
) => {

  const router = inject(Router);

  const role = localStorage.getItem('role');

  const allowedRoles =
    route.data?.['roles'];

  if (
    role &&
    allowedRoles.includes(role)
  ) {
    return true;
  }

  router.navigate(['/shipments']);

  return false;
};
