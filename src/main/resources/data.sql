INSERT INTO users(id, email, enabled, password, role, time_of_creation, time_of_modification, username)
VALUES (1, 'user@example.com', 1, '$2a$10$UG80r83ao4PPsv8vgI3G1OHfjaAfCYZS3/29y76b6gRMW74qJiUua', 'ROLE_USER',
        '2022-03-17 20:17:39.537000', '2022-03-17 20:17:39.537000', 'user')
ON CONFLICT DO NOTHING;

