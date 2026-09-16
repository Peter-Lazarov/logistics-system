-- CLIENTS
INSERT INTO clients (client_id, name, phone, email)
VALUES ('CUST-001', 'Stoyan Dimitrov', '+359888111222', 'stoyan@example.com')
ON CONFLICT (client_id) DO NOTHING;

INSERT INTO clients (client_id, name, phone, email)
VALUES ('CUST-002', 'Maria Koleva', '+359888333444', 'maria@example.com')
ON CONFLICT (client_id) DO NOTHING;

INSERT INTO clients (client_id, name, phone, email)
VALUES ('CUST-003', 'Logistics LTD', '+359888555666', 'office@logistics-ltd.bg')
ON CONFLICT (client_id) DO NOTHING;


-- ROUTES
INSERT INTO routes (path_id, start, destination, estimated_time)
VALUES ('1102', 'Sofia', 'Plovdiv', '2h 30m')
ON CONFLICT (path_id) DO NOTHING;


-- ROUTE POINTS
INSERT INTO route_points (lat, lon, route_path_id)
VALUES (42.6977, 23.3219, '1102')
ON CONFLICT DO NOTHING;

INSERT INTO route_points (lat, lon, route_path_id)
VALUES (42.7050, 23.3500, '1102')
ON CONFLICT DO NOTHING;

INSERT INTO route_points (lat, lon, route_path_id)
VALUES (42.7200, 23.4000, '1102')
ON CONFLICT DO NOTHING;

INSERT INTO route_points (lat, lon, route_path_id)
VALUES (42.7500, 23.5000, '1102')
ON CONFLICT DO NOTHING;

INSERT INTO route_points (lat, lon, route_path_id)
VALUES (42.8000, 23.6500, '1102')
ON CONFLICT DO NOTHING;

INSERT INTO route_points (lat, lon, route_path_id)
VALUES (42.9000, 23.9000, '1102')
ON CONFLICT DO NOTHING;

INSERT INTO route_points (lat, lon, route_path_id)
VALUES (42.1350, 24.7450, '1102')
ON CONFLICT DO NOTHING;


-- DRIVERS
INSERT INTO drivers (id, name, phone, license_number, assigned_vehicle_id)
VALUES ('DR-001', 'Ivan Petrov', '+359888123456', 'BG1234567', 'CB1234AB')
ON CONFLICT (id) DO NOTHING;
