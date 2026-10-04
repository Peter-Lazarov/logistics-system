import { Routes } from '@angular/router';
import { Login } from './core/login/login';
import { ShipmentList } from './features/shipments/shipment-list/shipment-list';
import { ShipmentDetails } from './features/shipments/shipment-details/shipment-details'
import { ShipmentCreate } from './features/shipments/shipment-create/shipment-create'
import { ClientRequest } from './features/client/client-request/client-request'
import { authGuard } from './core/auth/auth-guard';
export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: Login
  },
  {
    path: 'shipments',
    component: ShipmentList,
    canActivate: [authGuard]
  },
  {
    path: 'shipments/create',
    component: ShipmentCreate,
    canActivate: [authGuard]
  },
  {
    path: 'shipments/:id',
    component: ShipmentDetails,
    canActivate: [authGuard]
  },
  {
    path: 'client/request',
    component: ClientRequest,
    canActivate: [authGuard]
  }

];
