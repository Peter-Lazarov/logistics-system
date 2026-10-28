export const environment = {

  authenticationUrl: '/api/auth',
  commonUrl: '/api/common',
  shipmentUrl: '/api/shipments',
  trackingUrl: '/api/tracking',

  auth: {
    login: '/login',
    register: '/register',
    refresh: '/refresh',

    registerEmployee: '/admin/register-employee',
    users: '/admin/users'
  },

  common: {
    clients: '/clients',
    drivers: '/drivers',
  },

  shipment: {
    root: '',
    requests: '/requests'
  },

  tracking: {
    locations: '/tracking/shipments'
  }
};
