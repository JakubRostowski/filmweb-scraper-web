DO
'
BEGIN
IF NOT EXISTS(SELECT *
FROM users) THEN
    INSERT INTO users (email, enabled, password, role, time_of_creation, time_of_modification, username)
    VALUES (''admin@example.com'', true, ''$2a$10$UG80r83ao4PPsv8vgI3G1OHfjaAfCYZS3/29y76b6gRMW74qJiUua'',
        ''ROLE_ADMIN'', ''2022-03-17 20:17:39.537000'', ''2022-03-17 20:17:39.537000'', ''admin''),
        (''user@example.com'', true, ''$2a$10$UG80r83ao4PPsv8vgI3G1OHfjaAfCYZS3/29y76b6gRMW74qJiUua'',
            ''ROLE_USER'', ''2022-03-17 20:17:39.537000'', ''2022-03-17 20:17:39.537000'', ''user'');
    END IF;
END;
' LANGUAGE PLPGSQL;