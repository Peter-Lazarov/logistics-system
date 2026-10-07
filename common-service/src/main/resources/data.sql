
-- ROUTES
INSERT INTO routes (path_id, origin, destination, estimated_time)
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

INSERT INTO client_profiles (user_id, company_name, company_address, vat_number)
VALUES (1005, 'Test Company', 'Sofia', 'BG123456789')
ON CONFLICT DO NOTHING;
