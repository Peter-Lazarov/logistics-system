export const environment = {

  authenticationUrl: 'http://localhost:8080',
  commonUrl: 'http://localhost:8081',
  shipmentUrl: 'http://localhost:8082',
  trackingUrl: 'http://localhost:8084',

  auth: {
    login: '/auth/login',
    register: '/auth/register',
    refresh: '/auth/refresh',

    registerEmployee: '/auth/admin/register-employee',
    users: '/auth/admin/users'
  },

  common: {
    clients: '/clients',
    drivers: '/drivers',
    routes: '/routes'
  },

  shipment: {
    root: '/shipments',
    requests: '/shipments/requests'
  },

  tracking: {
    locations: '/tracking/locations'
  }

};
