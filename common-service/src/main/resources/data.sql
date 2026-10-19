
INSERT INTO client_profiles (user_id, company_name, company_address, vat_number)
VALUES (1005, 'Test Company', 'Sofia', 'BG123456789')
ON CONFLICT DO NOTHING;


