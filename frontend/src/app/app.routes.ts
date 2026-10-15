import { Routes } from "@angular/router";
import { Register } from "./core/register/register";
import { Login } from "./core/login/login";
import { authGuard } from "./core/auth/auth-guard";
import { roleGuard } from "./core/auth/role-guard";
import { Profile } from "./core/profile/profile";
import { EmployeeRegister } from "./features/admin/employee-register/employee-register";
import { ShipmentCreate } from "./features/shipments/shipment-create/shipment-create";
import { ShipmentList } from "./features/shipments/shipment-list/shipment-list";
import { ShipmentDetails } from "./features/shipments/shipment-details/shipment-details";
import { MyShipmentsComponent } from "./features/client/my-shipments/my-shipments";

import { ClientRequest } from "./features/client/client-request/client-request";

export const routes: Routes = [
  {
    path: "",
    redirectTo: "login",
    pathMatch: "full",
  },
  {
    path: "register",
    component: Register,
  },
  {
    path: "login",
    component: Login,
  },
  {
    path: "shipments/request",
    component: ClientRequest,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: ["ROLE_CLIENT"],
    },
  },
  {
    path: "shipments/my",
    component: MyShipmentsComponent,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: ["ROLE_CLIENT"],
    },
  },
  {
    path: "shipments/create",
    component: ShipmentCreate,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: ["ROLE_ADMIN", "ROLE_EMPLOYEE"],
    },
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
    path: "shipments/:id",
    component: ShipmentDetails,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: ["ROLE_ADMIN", "ROLE_EMPLOYEE"],
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
  {
    path: "profile",
    component: Profile,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: ["ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN"],
    },
  },
];
