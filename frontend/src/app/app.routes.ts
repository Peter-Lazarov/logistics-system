import { Routes } from "@angular/router";
import { Login } from "./core/login/login";
import { ShipmentList } from "./features/shipments/shipment-list/shipment-list";
import { ShipmentDetails } from "./features/shipments/shipment-details/shipment-details";
import { ShipmentCreate } from "./features/shipments/shipment-create/shipment-create";
import { ClientRequest } from "./features/client/client-request/client-request";
import { authGuard } from "./core/auth/auth-guard";
import { roleGuard } from "./core/auth/role-guard";
import { EmployeeRegister } from "./features/admin/employee-register/employee-register";

export const routes: Routes = [
  {
    path: "",
    redirectTo: "login",
    pathMatch: "full",
  },
  {
    path: "login",
    component: Login,
  },
  {
    path: "shipments",
    component: ShipmentList,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: ["ROLE_ADMIN", "ROLE_EMPLOYEE"],
    },
  },
  {
    path: "shipments/create",
    component: ShipmentCreate,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: ["ROLE_CLIENT"],
    },
  },
  {
    path: "shipments/:id",
    component: ShipmentDetails,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: ["ROLE_ADMIN", "ROLE_EMPLOYEE"],
    },
  },
  {
    path: "client/request",
    component: ClientRequest,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: ["ROLE_CLIENT"],
    },
  },
  {
    path: "admin/register-employee",
    component: EmployeeRegister,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: ["ROLE_ADMIN"],
    },
  },
  


];
