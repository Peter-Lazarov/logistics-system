import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { catchError, map, of } from "rxjs";
import { ClientProfileService } from "./client-profile.service";

export const clientProfileGuard: CanActivateFn = () => {
  const clientProfileService = inject(ClientProfileService);
  const router = inject(Router);

  return clientProfileService.getCurrent().pipe(
    map((profile) =>
      clientProfileService.isComplete(profile) ? true : router.parseUrl("/profile"),
    ),
    catchError(() => of(router.parseUrl("/profile"))),
  );
};