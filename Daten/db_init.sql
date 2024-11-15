CREATE TABLE userx (
    username VARCHAR(100) NOT NULL PRIMARY KEY,
    create_user_username VARCHAR(100) NOT NULL,
    create_date DATETIME NOT NULL,
    update_user_username VARCHAR(100),
    update_date DATETIME,
    password VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    enabled BOOLEAN,
    FOREIGN KEY (create_user_username) REFERENCES userx(username),
    FOREIGN KEY (update_user_username) REFERENCES userx(username)
);

CREATE TABLE userx_userx_role (
    userx_username VARCHAR(100) NOT NULL,
    roles VARCHAR(50) NOT NULL,
    PRIMARY KEY (userx_username, roles),
    FOREIGN KEY (userx_username) REFERENCES userx(username)
);

CREATE TABLE room (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    capacity INT NOT NULL,
    computers_available BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    UNIQUE KEY idx_room_id (id)
);

CREATE TABLE time_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    semester TINYINT,
    academic_year INT NOT NULL,
    status TINYINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME
);

CREATE TABLE room_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id VARCHAR(255),
    capacity INT NOT NULL,
    computers_available BOOLEAN NOT NULL,
    time_table_id BIGINT,
    FOREIGN KEY (time_table_id) REFERENCES time_table(id)
);

CREATE TABLE course (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    course_type TINYINT,
    study_type TINYINT,
    name VARCHAR(255),
    lecturer VARCHAR(255),
    semester INT NOT NULL,
    duration INT NOT NULL,
    number_of_participants INT NOT NULL,
    elective BOOLEAN NOT NULL,
    computers_necessary BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME
);

CREATE TABLE timing (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    timing_type TINYINT,
    day_of_the_week TINYINT,
    course_id VARCHAR(255),
    room_table_id BIGINT,
    FOREIGN KEY (course_id) REFERENCES course(id),
    FOREIGN KEY (room_table_id) REFERENCES room_table(id)
);

CREATE TABLE course_session (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    lecturer VARCHAR(255),
    study_type TINYINT,
    semester INT NOT NULL,
    number_of_participants INT NOT NULL,
    computers_necessary BOOLEAN NOT NULL,
    elective BOOLEAN NOT NULL,
    is_assigned BOOLEAN NOT NULL,
    is_fixed BOOLEAN NOT NULL,
    duration INT NOT NULL,
    timing_id BIGINT,
    course_id VARCHAR(255),
    time_table_id BIGINT,
    room_table_id BIGINT,
    FOREIGN KEY (timing_id) REFERENCES timing(id),
    FOREIGN KEY (course_id) REFERENCES course(id),
    FOREIGN KEY (time_table_id) REFERENCES time_table(id),
    FOREIGN KEY (room_table_id) REFERENCES room_table(id)
);

CREATE TABLE global_table_change (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255),
    change_type TINYINT,
    time_table VARCHAR(255),
    date TIME NOT NULL,
    change_user VARCHAR(255)
);


CREATE TABLE room_table_seq (
    next_val BIGINT NOT NULL
);

INSERT INTO room_table_seq VALUES (1);

CREATE TABLE course_session_seq (
    next_val BIGINT NOT NULL
);

INSERT INTO course_session_seq VALUES (1);

CREATE TABLE time_table_seq (
    next_val BIGINT NOT NULL
);

INSERT INTO time_table_seq VALUES (1);

CREATE TABLE timing_seq (
    next_val BIGINT NOT NULL
);

INSERT INTO timing_seq VALUES (1);

CREATE TABLE global_table_change_seq (
    next_val BIGINT NOT NULL
);

INSERT INTO global_table_change_seq VALUES (1);


-- users
INSERT INTO userx (enabled,first_name,last_name,password,username,email,create_user_username,create_date)VALUES(TRUE,'Admin','Istrator','$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6','admin','admin@memori.com','admin','2016-01-01 00:00:00');
INSERT INTO userx_userx_role (userx_username, roles)VALUES ('admin', 'ADMIN');
INSERT INTO userx_userx_role (userx_username, roles)VALUES ('admin', 'USER');
INSERT INTO userx (enabled,first_name,last_name,password,username,email,create_user_username,create_date)VALUES(TRUE,'Susi','Kaufgern','$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6','user1','susi.kaufgern@memori.com','admin','2016-01-01 00:00:00');
INSERT INTO userx_userx_role (userx_username, roles)VALUES ('user1', 'USER');
INSERT INTO userx (enabled,first_name,last_name,password,username,email,create_user_username,create_date)VALUES(TRUE,'Max','Mustermann','$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6','user2','max.mustermann@memori.com','admin','2016-01-01 00:00:00');
INSERT INTO userx_userx_role (userx_username, roles)VALUES ('user2', 'USER');
INSERT INTO userx (enabled,first_name,last_name,password,username,email,create_user_username,create_date)VALUES(TRUE,'Elvis','The King','$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6','elvis','elvis@memori.com','elvis','2016-01-01 00:00:00');
INSERT INTO userx_userx_role (userx_username, roles)VALUES ('elvis', 'ADMIN');

-- rooms
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HS A', 400, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HS B', 400, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HS C', 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HS D', 140, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HS E', 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HS F', 140, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HS G', 130, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('Rechnerraum 20', 30, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('Rechnerraum 21', 30, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('Rechnerraum 22', 30, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('Rechnerraum 14', 30, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('Rechnerraum 15', 30, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('Rechnerraum 25', 30, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('Rechnerraum 26', 30, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('Rechnerraum 18', 30, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('Rechnerraum 19', 30, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HSB 1', 144, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HSB 2', 144, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HSB 4', 48, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HSB 6', 120, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HSB 7', 70, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HSB 8', 40, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HSB 9', 40, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HS 10', 70, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('HS 11', 65, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('GROSSER HOERSAAL', 250, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('SR 12', 40, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('SR 13', 40, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('3W03', 40, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('3W04', 40, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');
INSERT INTO room (id,capacity,computers_available,created_at,updated_at)VALUES('3W05', 40, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00');

-- timeTables
INSERT INTO time_table(id,name,semester,academic_year,status,created_at,updated_at) VALUES (-1, 'TestForManyGroups', 1, 2022, 0, '2024-04-20 12:00:00', '2024-05-25 07:30:00');
INSERT INTO time_table(id,name,semester,academic_year,status,created_at,updated_at) VALUES (-2, 'TestForNoRoomConstraints', 1, 2023, 0, '2024-04-20 12:00:00', '2024-05-24 08:15:00');
INSERT INTO time_table(id,name,semester,academic_year,status,created_at,updated_at) VALUES (-3, 'TestWithManyCollisions', 0, 2024, 0, '2024-04-20 12:00:00', '2024-05-25 07:30:00');
INSERT INTO time_table(id,name,semester,academic_year,status,created_at,updated_at) VALUES (-4, 'TestWithProblematicConstraints', 1, 2021, 0, '2024-04-20 12:00:00', '2024-05-25 07:30:00');
INSERT INTO time_table(id,name,semester,academic_year,status,created_at,updated_at) VALUES (-5, 'TestWithSplits', 1, 2019, 0, '2024-04-20 12:00:00', '2024-05-25 07:30:00');
INSERT INTO time_table(id,name,semester,academic_year,status,created_at,updated_at) VALUES (-6, 'RealisticTest', 1, 2024, 0, '2024-10-01 12:00:00', '2024-10-01 12:00:00');

-- roomTables of timeTable1
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-1, 400, FALSE, 'HS A', -1);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-2, 30, FALSE, 'HS 11', -1);

-- roomTables of timeTable2
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-6, 400, FALSE, 'HS A', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-7, 400, FALSE, 'HS B', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-8, 150, FALSE, 'HS C', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-9, 140, FALSE, 'HS D', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-10, 150, FALSE, 'HS E', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-11, 140, FALSE, 'HS F', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-12, 130, FALSE, 'HS G', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-13, 30, TRUE, 'Rechnerraum 20', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-14, 30, TRUE, 'Rechnerraum 21', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-15, 30, TRUE, 'Rechnerraum 22', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-16, 30, TRUE, 'Rechnerraum 14', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-17, 30, TRUE, 'Rechnerraum 15', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-18, 30, TRUE, 'Rechnerraum 25', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-19, 30, TRUE, 'Rechnerraum 26', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-20, 30, TRUE, 'Rechnerraum 18', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-21, 30, TRUE, 'Rechnerraum 19', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-22, 144, FALSE, 'HSB 1', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-23, 144, FALSE, 'HSB 2', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-24, 48, FALSE, 'HSB 4', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-25, 120, FALSE, 'HSB 6', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-26, 70, FALSE, 'HSB 7', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-27, 40, FALSE, 'HSB 8', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-28, 40, FALSE, 'HSB 9', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-29, 70, FALSE, 'HS 10', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-30, 65, FALSE, 'HS 11', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-31, 250, FALSE, 'GROSSER HOERSAAL', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-32, 40, FALSE, 'SR 12', -2);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-33, 40, FALSE, 'SR 13', -2);

-- roomTables of timeTable3
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-34, 400, FALSE, 'HS A', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-35, 400, FALSE, 'HS B', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-36, 150, FALSE, 'HS C', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-37, 140, FALSE, 'HS D', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-38, 150, FALSE, 'HS E', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-39, 140, FALSE, 'HS F', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-40, 130, FALSE, 'HS G', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-41, 30, TRUE, 'Rechnerraum 20', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-42, 30, TRUE, 'Rechnerraum 21', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-43, 30, TRUE, 'Rechnerraum 22', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-44, 30, TRUE, 'Rechnerraum 14', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-45, 30, TRUE, 'Rechnerraum 15', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-46, 30, TRUE, 'Rechnerraum 25', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-47, 30, TRUE, 'Rechnerraum 26', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-48, 30, TRUE, 'Rechnerraum 18', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-49, 30, TRUE, 'Rechnerraum 19', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-50, 144, FALSE, 'HSB 1', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-51, 144, FALSE, 'HSB 2', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-52, 48, FALSE, 'HSB 4', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-53, 120, FALSE, 'HSB 6', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-54, 70, FALSE, 'HSB 7', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-55, 40, FALSE, 'HSB 8', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-56, 40, FALSE, 'HSB 9', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-57, 70, FALSE, 'HS 10', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-58, 65, FALSE, 'HS 11', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-59, 250, FALSE, 'GROSSER HOERSAAL', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-60, 40, FALSE, 'SR 12', -3);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-61, 40, FALSE, 'SR 13', -3);

-- roomTables of timeTable4
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-62, 400, FALSE, 'HS A', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-63, 400, FALSE, 'HS B', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-64, 150, FALSE, 'HS C', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-65, 140, FALSE, 'HS D', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-66, 150, FALSE, 'HS E', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-67, 140, FALSE, 'HS F', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-68, 130, FALSE, 'HS G', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-69, 30, TRUE, 'Rechnerraum 20', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-70, 30, TRUE, 'Rechnerraum 21', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-71, 30, TRUE, 'Rechnerraum 22', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-72, 30, TRUE, 'Rechnerraum 14', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-73, 30, TRUE, 'Rechnerraum 15', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-74, 30, TRUE, 'Rechnerraum 25', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-75, 30, TRUE, 'Rechnerraum 26', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-76, 30, TRUE, 'Rechnerraum 18', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-77, 30, TRUE, 'Rechnerraum 19', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-78, 144, FALSE, 'HSB 1', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-79, 144, FALSE, 'HSB 2', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-80, 48, FALSE, 'HSB 4', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-81, 120, FALSE, 'HSB 6', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-82, 70, FALSE, 'HSB 7', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-83, 40, FALSE, 'HSB 8', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-84, 40, FALSE, 'HSB 9', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-85, 70, FALSE, 'HS 10', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-86, 65, FALSE, 'HS 11', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-87, 250, FALSE, 'GROSSER HOERSAAL', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-88, 40, FALSE, 'SR 12', -4);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-89, 40, FALSE, 'SR 13', -4);

-- roomTables of timeTable5
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-90, 400, FALSE, 'HS A', -5);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-91, 30, TRUE, 'Rechnerraum 20', -5);

-- roomTables of timeTable6
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-92, 400, FALSE, 'HS A', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-93, 400, FALSE, 'HS B', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-94, 150, FALSE, 'HS C', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-95, 140, FALSE, 'HS D', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-96, 150, FALSE, 'HS E', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-97, 140, FALSE, 'HS F', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-98, 130, FALSE, 'HS G', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-99, 144, FALSE, 'HSB 1', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-100, 144, FALSE, 'HSB 2', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-101, 48, FALSE, 'HSB 4', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-102, 120, FALSE, 'HSB 6', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-103, 70, FALSE, 'HSB 7', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-104, 40, FALSE, 'HSB 8', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-105, 40, FALSE, 'HSB 9', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-106, 70, FALSE, 'HS 10', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-107, 65, FALSE, 'HS 11', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-108, 250, FALSE, 'GROSSER HOERSAAL', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-109, 40, FALSE, 'SR 12', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-110, 40, FALSE, 'SR 13', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-111, 30, TRUE, 'Rechnerraum 14', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-112, 30, TRUE, 'Rechnerraum 15', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-113, 30, TRUE, 'Rechnerraum 18', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-114, 30, TRUE, 'Rechnerraum 19', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-115, 30, TRUE, 'Rechnerraum 20', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-116, 30, TRUE, 'Rechnerraum 21', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-117, 30, TRUE, 'Rechnerraum 22', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-118, 30, TRUE, 'Rechnerraum 25', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-119, 30, TRUE, 'Rechnerraum 26', -6);
INSERT INTO room_table(id,capacity,computers_available,room_id,time_table_id) VALUES (-120, 40, FALSE, '3W03', -6);

INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703003', 'Einführung in die Programmierung', 0, 'Michael Felderer', 1, 180, 300, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703004', 'Einführung in die Programmierung', 2, 'Lukas Kaltenbrunner', 1, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703025', 'Funktionale Programmierung', 2, 'Rene Thiemann', 1, 45, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703024', 'Funktionale Programmierung', 0, 'Rene Thiemann', 1, 120, 300, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703063', 'Rechnerarchitektur', 2, 'Rainer Böhme', 1, 45, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703064', 'Rechnerarchitektur', 0, 'Rainer Böhme', 1, 120, 300, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703062', 'Einführung in die theoretische Informatik', 2, 'Jamie Hochrainer', 1, 45, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703007', 'Einführung in die theoretische Informatik', 0, 'Georg Moser', 1, 120, 300, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703098', 'Lineare Algebra', 2, 'Tim Netzer', 1, 45, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703099', 'Lineare Algebra', 0, 'Tim Netzer', 1, 120, 300, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);

INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703016', 'Programmiermethodik', 2, 'Simon Priller', 2, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703017', 'Programmiermethodik', 0, 'Lukas Kaltenbrunner', 2, 180, 300, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703066', 'Angewandte Mathematik für die Informatik', 2, 'Niko Rauch', 2, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703065', 'Angewandte Mathematik für die Informatik', 0, 'Michael Harders', 2, 180, 300, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703011', 'Algorithmen und Datenstrukturen', 2, 'Laurenz Wischounig', 2, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703010', 'Algorithmen und Datenstrukturen', 0, 'Justus Piater', 2, 180, 300, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703012', 'Betriebssysteme', 2, 'Markus Reiter', 2, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703013', 'Betriebssysteme', 0, 'Univ.-Prof. Dipl.-Ing. Dr. Thomas Fahringer', 2, 180, 300, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);

INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703020', 'Datenbanksysteme', 0, 'Günther Specht', 3, 180, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703021', 'Datenbanksysteme', 2, 'David Stern', 3, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703033', 'Rechnernetze und Internettechnik', 0, 'Jan Beutel', 3, 180, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703034', 'Rechnernetze und Internettechnik', 2, 'Niko Rossberg', 3, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703067', 'Daten und Wahrscheinlichkeiten', 0, 'Adam Jatowt', 3, 120, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703068', 'Daten und Wahrscheinlichkeiten', 2, 'Nadja Gruber', 3, 45, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703069', 'Diskrete Strukturen', 0, 'Markus Eberl', 3, 120, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703070', 'Diskrete Strukturen', 2, 'Charlie Sheen', 3, 45, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703071', 'Softwarearchitektur', 0, 'Clemens Sauerwein', 3, 180, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703072', 'Softwarearchitektur', 2, 'Michael Breu', 3, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);

INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703026', 'Logik', 0, 'Aart Middeldorp', 4, 180, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703027', 'Logik', 2, 'Fabian Mitterwallner', 4, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703075', 'Maschinelles Lernen', 0, 'Samuelle Tosatto', 4, 180, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703076', 'Maschinelles Lernen', 2, 'Sayantan Auddy', 4, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703077', 'Parallele Programmierung', 0, 'Thomas Fahringer', 4, 120, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703078', 'Parallele Programmierung', 2, 'Simon Lechner', 4, 45, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703079', 'Software Engineering', 0, 'Ruth Breu', 4, 120, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703080', 'Software Engineering', 2, 'Anna Jäger', 4, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703073', 'Einführung in das wissenschaftliche Arbeiten', 1, 'Thomas Fahringer', 4, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);

INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703087', 'Verteilte Systeme', 0, 'Juan Aznar Poveda', 5, 180, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703088', 'Verteilte Systeme', 2, 'Juan Aznar Poveda', 5, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703089', 'Visual Computing', 0, 'Michael Harders', 5, 180, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703090', 'Visual Computing', 2, 'Niko Rauch', 5, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703135', 'Einführung in die Robotik', 1, 'Samuelle Tosatto', 5, 180, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703141', 'Termersetzungssysteme', 1, 'Fabian Mitterwallner', 5, 180, 35, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703134', 'Deep Learning', 1, 'Antonio Jose Rodriguez-Sanchez', 5, 180, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703136', 'Mensch Maschine Interaktion', 1, 'Pascal Knierim', 5, 180, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703042', 'Ethische Aspekte der Informatik', 0, 'Wilhelm Guggenberger', 5, 90, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, TRUE);

INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703081', 'Vertiefunsseminar', 3, 'Günther Specht', 6, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703082', 'Bachelorseminar', 3, 'All Profs', 6, 180, 40, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703133', 'Prinzipien von Blockchain-Systemen', 1, 'Rainer Böhme', 6, 180, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703146', 'Performance-oriented Computing', 1, 'Peter Thoman', 6, 180, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703083', 'Programmverifikation', 0, 'Rene Thiemann', 6, 180, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703084', 'Programmverifikation', 2, 'Rene Thiemann', 6, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703085', 'Softwarequalität', 0, 'Harald Victor Schweiger', 6, 180, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703086', 'Softwarequalität', 2, 'Harald Victor Schweiger', 6, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('702631', 'Informationstheorie und Kryptologie', 0, 'Arne Dür', 6, 120, 150, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('702632', 'Informationstheorie und Kryptologie', 2, 'Arne Dür', 6, 45, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 0, TRUE);

-- Master Info                                                                                                                                                      id            name         type    lect    sem dur part  comp
-- semester1
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703300', 'Aktuelle Themen der Informatik', 0, 'Georg Moser', 1, 90, 60, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703301', 'Aktuelle Themen der Informatik', 2, 'Georg Moser', 1, 90, 30, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703302', 'Automaten und Logik', 0, 'Aart Middeldorp', 1, 150, 30, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703303', 'Automaten und Logik', 2, 'Johannes Niederhauser', 1, 150, 30, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703306', 'Kryptographie', 0, 'Arnab Roy', 1, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703307', 'Kryptographie', 2, 'Arnab Roy', 1, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703308', 'Hochleistungsrechnen', 0, 'Philipp Gschwandter', 1, 105, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703309', 'Hochleistungsrechnen', 2, 'Philipp Gschwandter', 1, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703110', 'Optimierung und numerische Berechnung', 0, 'Matthias Harders', 1, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703111', 'Optimierung und numerische Berechnung', 2, 'Marcel Ritter', 1, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703112', 'Signalverarbeitung und algorithmische Geometrie', 0, 'Marcel Ritter', 1, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703113', 'Signalverarbeitung und algorithmische Geometrie', 2, 'Muthukumar Pandaram', 1, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);

-- semester 3                                                                                                                                                      id            name         type    lect    sem dur part  comp
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703316', 'A Program and Resource Analysis', 1, 'Georg Moser', 3, 150, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703608', 'Tree Automata', 1, 'Aart Middeldorp', 3, 150, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703333', 'Advanced C++', 1, 'Peter Thoman', 3, 150, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703356', 'Advanced Distributed Systems', 1, 'Zahra Najafabadi Samani', 3, 150, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703325', 'Forschungsseminar verteiltes Rechnen', 3, 'Thomas Fahringer', 3, 90, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703357', 'Distributed Applications in the Edge-Cloud Continuum', 1, 'Sashko Ristov', 3, 150, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703327', 'Physikbasierte Simulation', 1, 'Matthias Harders', 3, 135, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703328', 'Advanced Computer Vision', 1, 'Antonio Jose Rodriguez-Sanchez', 3, 150, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703330', 'Machine Learning for Interactive Systems', 1, 'Justus Piater', 3, 150, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703331', 'Forschungsseminar in Wahrnehmung, Interaktion und Robotik', 3, 'Matthias Harders', 3, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703354', 'Low-power System Design', 1, 'Jan Beutel', 3, 150, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703345', 'Knowledge Graphs', 1, 'Dieter Fensel', 3, 150, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703346', 'Forschungsseminar Knowledge Graphs', 3, 'Dieter Fensel', 3, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, TRUE);

-- Master SWE
-- semester 1                                                                                                                                                      id         name    type   lect     sem dur part  comp
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703500', 'Softwareentwurf und Softwareentwicklungsprozesse', 0, 'Philipp Zech', 1, 90, 50, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 2, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703501', 'Softwareentwurf und Softwareentwicklungsprozesse', 2, 'Melanie Ernst', 1, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 2, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703502', 'Software Security Engineering', 0, 'Rainer Böhme', 1, 90, 50, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 2, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703503', 'Software Security Engineering', 2, 'Rainer Böhme', 1, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 2, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703504', 'Data Engineering und Analytics', 0, 'Eva Zangerle', 1, 90, 50, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 2, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703505', 'Data Engineering und Analytics', 2, 'Amir Reza Mohammadi', 1, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 2, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703506', 'Forschungsmethoden des Software Engineering', 0, 'Valentina Golendukhina', 1, 90, 50, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 2, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703507', 'Forschungsmethoden des Software Engineering', 2, 'Valentina Golendukhina', 1, 180, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 2, FALSE);

-- semester 2                                                                                                                                                                id      name  type  lect   sem dur part  comp
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703508', 'Software Produktmanagement', 1, 'Philipp Zech', 2, 255, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703509', 'Usability Engineering und Interaktionsdesign', 1, 'Valentina Golendukhina', 2, 90, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703510', 'Methoden der Software Qualitätssicherung', 1, 'Michael Vierhauser', 2, 180, 25, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 1, FALSE);


-- semester 3                                                                                                                                                      id         name    type   lect     sem dur part  comp
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703512', 'Masterseminar Software Engineering', 3, 'Ruth Breu', 3, 150, 50, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 2, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703517', 'Vorbereitung der Masterarbeit', 3, 'Michael Vierhauser', 3, 105, 50, FALSE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 2, FALSE);
INSERT INTO course (id,name,course_type,lecturer,semester,duration,number_of_participants,computers_necessary,created_at,updated_at,study_type,elective)VALUES('703518', 'Data-Intensive Applications', 1, 'Sashko Ristov', 3, 180, 25, TRUE, '2024-04-20 12:00:00', '2024-04-20 12:00:00', 2, TRUE);


-- timingConstraints of roomTables of timeTable1

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-1, 0, '12:00:00', '15:00:00', NULL, -2, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-2, 1, '12:00:00', '15:00:00', NULL, -2, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-3, 2, '12:00:00', '15:00:00', NULL, -2, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-4, 3, '12:00:00', '15:00:00', NULL, -2, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-5, 4, '12:00:00', '15:00:00', NULL, -2, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-6, 0, '15:00:00', '20:30:00', NULL, -2, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-7, 1, '15:00:00', '20:30:00', NULL, -2, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-8, 2, '15:00:00', '20:30:00', NULL, -2, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-9, 3, '15:00:00', '20:30:00', NULL, -2, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-10, 4, '15:00:00', '20:30:00', NULL, -2, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-4960, 0, '08:15:00', '12:00:00', NULL, -2, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-4970, 1, '08:15:00', '12:00:00', NULL, -2, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-4980, 2, '08:15:00', '12:00:00', NULL, -2, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-4990, 3, '08:15:00', '12:00:00', NULL, -2, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-5000, 4, '08:15:00', '12:00:00', NULL, -2, 1);

-- timingConstraints for roomTables of timeTable4 (timingType 0 for COMPUTER_SCIENCE, timingType 1 for BLOCKED);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-201, 0, '08:15:00', '20:30:00', NULL, -62, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-202, 1, '08:15:00', '20:30:00', NULL, -62, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-203, 2, '08:15:00', '20:30:00', NULL, -62, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-204, 3, '08:15:00', '20:30:00', NULL, -62, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-205, 4, '08:15:00', '20:30:00', NULL, -62, 0);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-211, 0, '08:15:00', '16:00:00', NULL, -63, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-212, 1, '09:00:00', '11:00:00', NULL, -63, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-213, 2, '08:15:00', '12:00:00', NULL, -63, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-214, 3, '08:15:00', '12:00:00', NULL, -63, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-215, 4, '10:00:00', '13:00:00', NULL, -63, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-216, 0, '18:00:00', '20:00:00', NULL, -63, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-217, 1, '11:00:00', '15:00:00', NULL, -63, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-218, 2, '14:00:00', '20:30:00', NULL, -63, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-219, 3, '12:00:00', '16:00:00', NULL, -63, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-220, 4, '15:00:00', '20:00:00', NULL, -63, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-221, 0, '08:15:00', '16:00:00', NULL, -64, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-222, 1, '09:00:00', '11:00:00', NULL, -64, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-223, 2, '08:15:00', '12:00:00', NULL, -64, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-224, 3, '08:15:00', '12:00:00', NULL, -64, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-225, 4, '10:00:00', '13:00:00', NULL, -64, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-226, 0, '18:00:00', '20:00:00', NULL, -64, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-227, 1, '11:00:00', '15:00:00', NULL, -64, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-228, 2, '14:00:00', '20:30:00', NULL, -64, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-229, 3, '12:00:00', '16:00:00', NULL, -64, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-230, 4, '15:00:00', '20:00:00', NULL, -64, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-231, 0, '08:15:00', '10:00:00', NULL, -65, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-232, 1, '09:00:00', '11:00:00', NULL, -65, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-233, 2, '08:15:00', '12:00:00', NULL, -65, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-234, 3, '08:15:00', '18:00:00', NULL, -65, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-235, 4, '10:00:00', '13:00:00', NULL, -65, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-236, 0, '18:00:00', '20:00:00', NULL, -65, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-237, 1, '11:00:00', '15:00:00', NULL, -65, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-238, 2, '14:00:00', '20:30:00', NULL, -65, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-239, 3, '18:00:00', '20:30:00', NULL, -65, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-240, 4, '15:00:00', '20:00:00', NULL, -65, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-241, 0, '08:15:00', '10:00:00', NULL, -66, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-242, 1, '09:00:00', '11:00:00', NULL, -66, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-243, 2, '08:15:00', '12:00:00', NULL, -66, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-244, 3, '08:15:00', '12:00:00', NULL, -66, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-245, 4, '10:00:00', '13:00:00', NULL, -66, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-246, 0, '18:00:00', '20:00:00', NULL, -66, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-247, 1, '11:00:00', '15:00:00', NULL, -66, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-248, 2, '14:00:00', '20:30:00', NULL, -66, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-249, 3, '12:00:00', '16:00:00', NULL, -66, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-250, 4, '15:00:00', '20:00:00', NULL, -66, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-251, 0, '08:15:00', '10:00:00', NULL, -67, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-252, 1, '09:00:00', '11:00:00', NULL, -67, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-253, 2, '08:15:00', '12:00:00', NULL, -67, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-254, 3, '08:15:00', '12:00:00', NULL, -67, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-255, 4, '10:00:00', '13:00:00', NULL, -67, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-256, 0, '18:00:00', '20:00:00', NULL, -67, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-257, 1, '11:00:00', '15:00:00', NULL, -67, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-258, 2, '14:00:00', '20:30:00', NULL, -67, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-259, 3, '12:00:00', '16:00:00', NULL, -67, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-260, 4, '15:00:00', '20:00:00', NULL, -67, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-261, 0, '08:15:00', '10:00:00', NULL, -68, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-262, 1, '09:00:00', '11:00:00', NULL, -68, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-263, 2, '08:15:00', '12:00:00', NULL, -68, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-264, 3, '08:15:00', '12:00:00', NULL, -68, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-265, 4, '10:00:00', '13:00:00', NULL, -68, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-266, 0, '18:00:00', '20:00:00', NULL, -68, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-267, 1, '11:00:00', '15:00:00', NULL, -68, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-268, 2, '14:00:00', '20:30:00', NULL, -68, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-269, 3, '12:00:00', '16:00:00', NULL, -68, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-270, 4, '15:00:00', '20:00:00', NULL, -68, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-271, 0, '08:15:00', '20:30:00', NULL, -69, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-272, 1, '09:00:00', '20:30:00', NULL, -69, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-273, 2, '08:15:00', '20:30:00', NULL, -69, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-274, 3, '08:15:00', '20:30:00', NULL, -69, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-275, 4, '10:00:00', '20:30:00', NULL, -69, 0);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-281, 0, '08:15:00', '20:30:00', NULL, -70, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-282, 1, '09:00:00', '20:30:00', NULL, -70, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-283, 2, '08:15:00', '20:30:00', NULL, -70, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-284, 3, '08:15:00', '20:30:00', NULL, -70, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-285, 4, '10:00:00', '20:30:00', NULL, -70, 0);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-291, 0, '08:15:00', '10:00:00', NULL, -71, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-292, 1, '09:00:00', '11:00:00', NULL, -71, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-293, 2, '08:15:00', '12:00:00', NULL, -71, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-294, 3, '08:15:00', '12:00:00', NULL, -71, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-295, 4, '10:00:00', '13:00:00', NULL, -71, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-296, 0, '18:00:00', '20:00:00', NULL, -71, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-297, 1, '11:00:00', '15:00:00', NULL, -71, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-298, 2, '14:00:00', '20:30:00', NULL, -71, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-299, 3, '12:00:00', '16:00:00', NULL, -71, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-300, 4, '15:00:00', '20:00:00', NULL, -71, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-301, 0, '08:15:00', '10:00:00', NULL, -72, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-302, 1, '09:00:00', '11:00:00', NULL, -72, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-303, 2, '08:15:00', '12:00:00', NULL, -72, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-304, 3, '08:15:00', '12:00:00', NULL, -72, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-305, 4, '10:00:00', '13:00:00', NULL, -72, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-306, 0, '18:00:00', '20:00:00', NULL, -72, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-307, 1, '11:00:00', '15:00:00', NULL, -72, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-308, 2, '14:00:00', '20:30:00', NULL, -72, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-309, 3, '12:00:00', '16:00:00', NULL, -72, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-310, 4, '15:00:00', '20:00:00', NULL, -72, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-311, 0, '08:15:00', '10:00:00', NULL, -73, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-312, 1, '09:00:00', '11:00:00', NULL, -73, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-313, 2, '08:15:00', '12:00:00', NULL, -73, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-314, 3, '08:15:00', '12:00:00', NULL, -73, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-315, 4, '10:00:00', '13:00:00', NULL, -73, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-316, 0, '18:00:00', '20:00:00', NULL, -73, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-317, 1, '11:00:00', '15:00:00', NULL, -73, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-318, 2, '14:00:00', '20:30:00', NULL, -73, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-319, 3, '12:00:00', '16:00:00', NULL, -73, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-320, 4, '15:00:00', '20:00:00', NULL, -73, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-321, 0, '08:15:00', '10:00:00', NULL, -74, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-322, 1, '09:00:00', '11:00:00', NULL, -74, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-323, 2, '08:15:00', '12:00:00', NULL, -74, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-324, 3, '08:15:00', '12:00:00', NULL, -74, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-325, 4, '10:00:00', '13:00:00', NULL, -74, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-326, 0, '18:00:00', '20:00:00', NULL, -74, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-327, 1, '11:00:00', '15:00:00', NULL, -74, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-328, 2, '14:00:00', '20:30:00', NULL, -74, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-329, 3, '12:00:00', '16:00:00', NULL, -74, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-330, 4, '15:00:00', '20:00:00', NULL, -74, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-331, 0, '08:15:00', '10:00:00', NULL, -75, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-332, 1, '09:00:00', '11:00:00', NULL, -75, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-333, 2, '08:15:00', '12:00:00', NULL, -75, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-334, 3, '08:15:00', '12:00:00', NULL, -75, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-335, 4, '10:00:00', '13:00:00', NULL, -75, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-336, 0, '18:00:00', '20:00:00', NULL, -75, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-337, 1, '11:00:00', '15:00:00', NULL, -75, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-338, 2, '14:00:00', '20:30:00', NULL, -75, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-339, 3, '12:00:00', '16:00:00', NULL, -75, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-340, 4, '15:00:00', '20:00:00', NULL, -75, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-341, 0, '08:15:00', '10:00:00', NULL, -76, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-342, 1, '09:00:00', '11:00:00', NULL, -76, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-343, 2, '08:15:00', '12:00:00', NULL, -76, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-344, 3, '08:15:00', '12:00:00', NULL, -76, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-345, 4, '10:00:00', '13:00:00', NULL, -76, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-346, 0, '18:00:00', '20:00:00', NULL, -76, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-347, 1, '11:00:00', '15:00:00', NULL, -76, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-348, 2, '14:00:00', '20:30:00', NULL, -76, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-349, 3, '12:00:00', '16:00:00', NULL, -76, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-350, 4, '15:00:00', '20:00:00', NULL, -76, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-351, 0, '08:15:00', '10:00:00', NULL, -77, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-352, 1, '09:00:00', '11:00:00', NULL, -77, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-353, 2, '08:15:00', '12:00:00', NULL, -77, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-354, 3, '08:15:00', '12:00:00', NULL, -77, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-355, 4, '10:00:00', '13:00:00', NULL, -77, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-356, 0, '18:00:00', '20:00:00', NULL, -77, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-357, 1, '11:00:00', '15:00:00', NULL, -77, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-358, 2, '14:00:00', '20:30:00', NULL, -77, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-359, 3, '12:00:00', '16:00:00', NULL, -77, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-360, 4, '15:00:00', '20:00:00', NULL, -77, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-361, 0, '08:15:00', '10:00:00', NULL, -78, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-362, 1, '09:00:00', '11:00:00', NULL, -78, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-363, 2, '08:15:00', '12:00:00', NULL, -78, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-364, 3, '08:15:00', '12:00:00', NULL, -78, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-365, 4, '10:00:00', '13:00:00', NULL, -78, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-366, 0, '18:00:00', '20:00:00', NULL, -78, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-367, 1, '11:00:00', '15:00:00', NULL, -78, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-368, 2, '14:00:00', '20:30:00', NULL, -78, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-369, 3, '12:00:00', '16:00:00', NULL, -78, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-370, 4, '15:00:00', '20:00:00', NULL, -78, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-371, 0, '08:15:00', '10:00:00', NULL, -79, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-372, 1, '09:00:00', '11:00:00', NULL, -79, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-373, 2, '08:15:00', '12:00:00', NULL, -79, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-374, 3, '08:15:00', '12:00:00', NULL, -79, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-375, 4, '10:00:00', '13:00:00', NULL, -79, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-376, 0, '18:00:00', '20:00:00', NULL, -79, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-377, 1, '11:00:00', '15:00:00', NULL, -79, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-378, 2, '14:00:00', '20:30:00', NULL, -79, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-379, 3, '12:00:00', '16:00:00', NULL, -79, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-380, 4, '15:00:00', '20:00:00', NULL, -79, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-381, 0, '08:15:00', '10:00:00', NULL, -80, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-382, 1, '09:00:00', '11:00:00', NULL, -80, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-383, 2, '08:15:00', '12:00:00', NULL, -80, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-384, 3, '08:15:00', '12:00:00', NULL, -80, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-385, 4, '10:00:00', '13:00:00', NULL, -80, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-386, 0, '18:00:00', '20:00:00', NULL, -80, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-387, 1, '11:00:00', '15:00:00', NULL, -80, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-388, 2, '14:00:00', '20:30:00', NULL, -80, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-389, 3, '12:00:00', '16:00:00', NULL, -80, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-390, 4, '15:00:00', '20:00:00', NULL, -80, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-391, 0, '08:15:00', '10:00:00', NULL, -81, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-392, 1, '09:00:00', '11:00:00', NULL, -81, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-393, 2, '08:15:00', '12:00:00', NULL, -81, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-394, 3, '08:15:00', '12:00:00', NULL, -81, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-395, 4, '10:00:00', '13:00:00', NULL, -81, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-396, 0, '18:00:00', '20:00:00', NULL, -81, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-397, 1, '11:00:00', '15:00:00', NULL, -81, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-398, 2, '14:00:00', '20:30:00', NULL, -81, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-399, 3, '12:00:00', '16:00:00', NULL, -81, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-400, 4, '15:00:00', '20:00:00', NULL, -81, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-401, 0, '08:15:00', '10:00:00', NULL, -82, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-402, 1, '09:00:00', '11:00:00', NULL, -82, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-403, 2, '08:15:00', '12:00:00', NULL, -82, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-404, 3, '08:15:00', '12:00:00', NULL, -82, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-405, 4, '10:00:00', '13:00:00', NULL, -82, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-406, 0, '18:00:00', '20:00:00', NULL, -82, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-407, 1, '11:00:00', '15:00:00', NULL, -82, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-408, 2, '14:00:00', '20:30:00', NULL, -82, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-409, 3, '12:00:00', '16:00:00', NULL, -82, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-410, 4, '15:00:00', '20:00:00', NULL, -82, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-411, 0, '08:15:00', '10:00:00', NULL, -83, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-412, 1, '09:00:00', '11:00:00', NULL, -83, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-413, 2, '08:15:00', '12:00:00', NULL, -83, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-414, 3, '08:15:00', '12:00:00', NULL, -83, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-415, 4, '10:00:00', '13:00:00', NULL, -83, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-416, 0, '18:00:00', '20:00:00', NULL, -83, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-417, 1, '11:00:00', '15:00:00', NULL, -83, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-418, 2, '14:00:00', '20:30:00', NULL, -83, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-419, 3, '12:00:00', '16:00:00', NULL, -83, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-420, 4, '15:00:00', '20:00:00', NULL, -83, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-421, 0, '08:15:00', '10:00:00', NULL, -84, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-422, 1, '09:00:00', '11:00:00', NULL, -84, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-423, 2, '08:15:00', '12:00:00', NULL, -84, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-424, 3, '08:15:00', '12:00:00', NULL, -84, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-425, 4, '10:00:00', '13:00:00', NULL, -84, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-426, 0, '18:00:00', '20:00:00', NULL, -84, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-427, 1, '11:00:00', '15:00:00', NULL, -84, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-428, 2, '14:00:00', '20:30:00', NULL, -84, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-429, 3, '12:00:00', '16:00:00', NULL, -84, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-430, 4, '15:00:00', '20:00:00', NULL, -84, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-431, 0, '08:15:00', '10:00:00', NULL, -85, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-432, 1, '09:00:00', '11:00:00', NULL, -85, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-433, 2, '08:15:00', '12:00:00', NULL, -85, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-434, 3, '08:15:00', '12:00:00', NULL, -85, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-435, 4, '10:00:00', '13:00:00', NULL, -85, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-436, 0, '18:00:00', '20:00:00', NULL, -85, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-437, 1, '11:00:00', '15:00:00', NULL, -85, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-438, 2, '14:00:00', '20:30:00', NULL, -85, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-439, 3, '12:00:00', '16:00:00', NULL, -85, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-440, 4, '15:00:00', '20:00:00', NULL, -85, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-441, 0, '08:15:00', '10:00:00', NULL, -86, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-442, 1, '09:00:00', '11:00:00', NULL, -86, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-443, 2, '08:15:00', '12:00:00', NULL, -86, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-444, 3, '08:15:00', '12:00:00', NULL, -86, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-445, 4, '10:00:00', '13:00:00', NULL, -86, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-446, 0, '18:00:00', '20:00:00', NULL, -86, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-447, 1, '11:00:00', '15:00:00', NULL, -86, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-448, 2, '14:00:00', '20:30:00', NULL, -86, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-449, 3, '12:00:00', '16:00:00', NULL, -86, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-450, 4, '15:00:00', '20:00:00', NULL, -86, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-451, 0, '08:15:00', '10:00:00', NULL, -87, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-452, 1, '09:00:00', '11:00:00', NULL, -87, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-453, 2, '08:15:00', '12:00:00', NULL, -87, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-454, 3, '08:15:00', '12:00:00', NULL, -87, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-455, 4, '10:00:00', '13:00:00', NULL, -87, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-456, 0, '18:00:00', '20:00:00', NULL, -87, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-457, 1, '11:00:00', '15:00:00', NULL, -87, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-458, 2, '14:00:00', '20:30:00', NULL, -87, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-459, 3, '12:00:00', '16:00:00', NULL, -87, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-460, 4, '15:00:00', '20:00:00', NULL, -87, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-461, 0, '08:15:00', '10:00:00', NULL, -88, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-462, 1, '09:00:00', '11:00:00', NULL, -88, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-463, 2, '08:15:00', '12:00:00', NULL, -88, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-464, 3, '08:15:00', '12:00:00', NULL, -88, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-465, 4, '10:00:00', '13:00:00', NULL, -88, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-466, 0, '18:00:00', '20:00:00', NULL, -88, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-467, 1, '11:00:00', '15:00:00', NULL, -88, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-468, 2, '14:00:00', '20:30:00', NULL, -88, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-469, 3, '12:00:00', '16:00:00', NULL, -88, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-470, 4, '15:00:00', '20:00:00', NULL, -88, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-471, 0, '08:15:00', '20:30:00', NULL, -89, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-472, 1, '09:00:00', '20:30:00', NULL, -89, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-473, 2, '08:15:00', '20:30:00', NULL, -89, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-474, 3, '08:15:00', '20:30:00', NULL, -89, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-475, 4, '10:00:00', '20:30:00', NULL, -89, 0);

-- timingConstraints of roomTables of timeTable 5

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-481, 0, '08:15:00', '11:15:00', NULL, -90, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-482, 0, '11:15:00', '20:30:00', NULL, -90, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-483, 1, '08:15:00', '11:15:00', NULL, -90, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-484, 1, '11:15:00', '20:30:00', NULL, -90, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-485, 2, '08:15:00', '20:30:00', NULL, -90, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-486, 3, '08:15:00', '20:30:00', NULL, -90, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-487, 4, '08:15:00', '20:30:00', NULL, -90, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-488, 0, '08:15:00', '20:30:00', NULL, -91, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-489, 1, '08:15:00', '20:30:00', NULL, -91, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-490, 2, '08:15:00', '11:15:00', NULL, -91, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-491, 2, '11:15:00', '20:30:00', NULL, -91, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-492, 3, '08:15:00', '11:15:00', NULL, -91, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-493, 3, '11:15:00', '20:30:00', NULL, -91, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-494, 4, '08:15:00', '11:15:00', NULL, -91, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-495, 4, '11:15:00', '20:30:00', NULL, -91, 1);

-- timingConstraints of roomTables of timeTable 6
-- HSA
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-496, 0, '08:15:00', '17:15:00', NULL, -92, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-497, 1, '08:15:00', '12:00:00', NULL, -92, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-498, 1, '13:45:00', '19:00:00', NULL, -92, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-499, 2, '08:15:00', '12:00:00', NULL, -92, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-500, 2, '15:30:00', '19:00:00', NULL, -92, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-501, 3, '08:15:00', '10:15:00', NULL, -92, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-502, 4, '08:15:00', '10:15:00', NULL, -92, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-503, 2, '12:00:00', '15:30:00', NULL, -92, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-504, 3, '10:15:00', '15:30:00', NULL, -92, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-505, 4, '10:15:00', '12:00:00', NULL, -92, 0);

-- HSB
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-506, 0, '08:15:00', '12:00:00', NULL, -93, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-507, 0, '13:45:00', '17:15:00', NULL, -93, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-508, 1, '08:15:00', '17:15:00', NULL, -93, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-509, 2, '08:15:00', '17:15:00', NULL, -93, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-510, 3, '08:15:00', '15:30:00', NULL, -93, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-511, 4, '08:15:00', '15:30:00', NULL, -93, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-512, 0, '12:00:00', '13:45:00', NULL, -93, 0);

-- HSC
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-513, 0, '10:15:00', '19:00:00', NULL, -94, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-514, 1, '08:15:00', '12:00:00', NULL, -94, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-515, 1, '13:45:00', '20:30:00', NULL, -94, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-516, 2, '08:15:00', '12:00:00', NULL, -94, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-517, 2, '13:45:00', '17:15:00', NULL, -94, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-518, 3, '08:15:00', '17:15:00', NULL, -94, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-519, 4, '08:15:00', '13:45:00', NULL, -94, 1);

-- HSD
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-520, 0, '10:15:00', '19:00:00', NULL, -95, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-521, 1, '08:15:00', '15:30:00', NULL, -95, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-522, 1, '17:15:00', '19:00:00', NULL, -95, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-523, 2, '10:15:00', '19:00:00', NULL, -95, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-524, 3, '08:15:00', '19:00:00', NULL, -95, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-525, 4, '10:15:00', '17:15:00', NULL, -95, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-526, 4, '08:15:00', '10:15:00', NULL, -95, 0);

-- HSE
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-527, 0, '08:15:00', '19:00:00', NULL, -96, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-528, 1, '08:15:00', '19:00:00', NULL, -96, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-529, 2, '08:15:00', '17:15:00', NULL, -96, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-530, 3, '08:15:00', '15:30:00', NULL, -96, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-531, 4, '08:15:00', '15:30:00', NULL, -96, 1);

-- HSF
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-532, 0, '08:15:00', '19:00:00', NULL, -97, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-533, 1, '08:15:00', '19:00:00', NULL, -97, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-534, 2, '08:15:00', '19:00:00', NULL, -97, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-535, 3, '08:15:00', '15:30:00', NULL, -97, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-536, 4, '08:15:00', '15:30:00', NULL, -97, 1);

-- HSG
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-537, 0, '08:15:00', '19:00:00', NULL, -98, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-538, 1, '10:15:00', '17:15:00', NULL, -98, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-539, 2, '08:15:00', '19:00:00', NULL, -98, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-540, 3, '08:15:00', '17:15:00', NULL, -98, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-541, 4, '08:15:00', '13:45:00', NULL, -98, 1);

-- HSB1
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-542, 0, '08:30:00', '20:30:00', NULL, -99, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-543, 1, '08:30:00', '12:00:00', NULL, -99, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-544, 1, '13:45:00', '19:00:00', NULL, -99, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-545, 2, '08:30:00', '13:45:00', NULL, -99, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-546, 3, '08:30:00', '17:15:00', NULL, -99, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-547, 4, '10:15:00', '12:00:00', NULL, -99, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-548, 4, '13:45:00', '17:15:00', NULL, -99, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-549, 2, '15:30:00', '19:00:00', NULL, -99, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-550, 4, '12:00:00', '13:45:00', NULL, -99, 0);

-- HSB2
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-551, 0, '08:30:00', '20:30:00', NULL, -100, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-552, 1, '08:30:00', '20:30:00', NULL, -100, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-553, 2, '08:30:00', '17:15:00', NULL, -100, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-554, 3, '08:30:00', '17:15:00', NULL, -100, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-555, 4, '08:30:00', '12:00:00', NULL, -100, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-556, 4, '13:45:00', '17:15:00', NULL, -100, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-557, 4, '12:00:00', '13:45:00', NULL, -100, 0);

-- HSB4
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-558, 0, '08:30:00', '17:15:00', NULL, -101, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-559, 1, '08:30:00', '19:00:00', NULL, -101, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-560, 2, '08:30:00', '10:15:00', NULL, -101, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-561, 2, '12:00:00', '19:00:00', NULL, -101, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-562, 3, '08:30:00', '12:00:00', NULL, -101, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-563, 3, '13:45:00', '17:15:00', NULL, -101, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-564, 4, '08:30:00', '12:00:00', NULL, -101, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-565, 2, '10:15:00', '12:00:00', NULL, -101, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-566, 4, '12:00:00', '13:45:00', NULL, -101, 0);

-- HSB6
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-567, 0, '08:30:00', '20:30:00', NULL, -102, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-568, 1, '08:30:00', '17:15:00', NULL, -102, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-569, 2, '08:30:00', '19:00:00', NULL, -102, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-570, 3, '08:30:00', '10:15:00', NULL, -102, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-571, 3, '12:00:00', '13:45:00', NULL, -102, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-572, 3, '17:15:00', '19:00:00', NULL, -102, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-573, 4, '10:15:00', '12:00:00', NULL, -102, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-574, 3, '10:15:00', '12:00:00', NULL, -102, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-575, 3, '13:45:00', '15:30:00', NULL, -102, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-576, 4, '08:30:00', '10:15:00', NULL, -102, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-577, 4, '12:00:00', '13:45:00', NULL, -102, 0);

-- HSB7
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-578, 0, '08:30:00', '12:00:00', NULL, -103, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-579, 0, '13:45:00', '19:00:00', NULL, -103, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-580, 1, '10:15:00', '17:15:00', NULL, -103, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-581, 2, '08:30:00', '17:15:00', NULL, -103, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-582, 3, '08:30:00', '19:00:00', NULL, -103, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-583, 4, '08:30:00', '10:15:00', NULL, -103, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-584, 4, '12:00:00', '13:45:00', NULL, -103, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-585, 0, '12:00:00', '13:45:00', NULL, -103, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-586, 1, '08:30:00', '10:15:00', NULL, -103, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-587, 4, '10:15:00', '12:00:00', NULL, -103, 0);

-- HSB8
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-588, 0, '08:30:00', '19:00:00', NULL, -104, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-589, 1, '08:30:00', '19:00:00', NULL, -104, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-590, 2, '08:30:00', '13:45:00', NULL, -104, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-591, 2, '15:30:00', '17:15:00', NULL, -104, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-592, 3, '08:30:00', '19:00:00', NULL, -104, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-593, 4, '08:30:00', '15:30:00', NULL, -104, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-594, 2, '13:45:00', '15:30:00', NULL, -104, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-595, 4, '15:30:00', '17:15:00', NULL, -104, 0);

-- HSB9
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-596, 0, '10:15:00', '19:00:00', NULL, -105, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-597, 1, '10:15:00', '19:00:00', NULL, -105, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-598, 2, '08:30:00', '15:30:00', NULL, -105, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-599, 3, '08:30:00', '17:15:00', NULL, -105, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-600, 4, '08:30:00', '15:30:00', NULL, -105, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-601, 0, '08:30:00', '10:15:00', NULL, -105, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-602, 2, '15:30:00', '17:15:00', NULL, -105, 0);

-- HS10
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-603, 1, '12:00:00', '15:30:00', NULL, -106, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-604, 2, '08:30:00', '13:45:00', NULL, -106, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-605, 4, '08:30:00', '12:00:00', NULL, -106, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-606, 0, '08:30:00', '17:15:00', NULL, -106, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-607, 1, '10:15:00', '12:00:00', NULL, -106, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-608, 1, '15:30:00', '19:00:00', NULL, -106, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-609, 2, '13:45:00', '17:15:00', NULL, -106, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-610, 3, '08:30:00', '19:00:00', NULL, -106, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-611, 4, '12:00:00', '19:00:00', NULL, -106, 0);

-- HS11
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-612, 0, '08:30:00', '12:00:00', NULL, -107, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-613, 0, '17:15:00', '19:00:00', NULL, -107, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-614, 1, '08:30:00', '10:15:00', NULL, -107, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-615, 1, '13:45:00', '15:30:00', NULL, -107, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-616, 2, '08:30:00', '17:15:00', NULL, -107, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-617, 3, '10:15:00', '13:45:00', NULL, -107, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-618, 3, '15:30:00', '17:15:00', NULL, -107, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-619, 0, '12:00:00', '17:15:00', NULL, -107, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-620, 3, '08:30:00', '10:15:00', NULL, -107, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-621, 3, '13:45:00', '15:30:00', NULL, -107, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-622, 3, '17:15:00', '19:00:00', NULL, -107, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-623, 4, '08:30:00', '17:15:00', NULL, -107, 1);

-- SR12
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-624, 0, '08:30:00', '10:15:00', NULL, -109, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-625, 0, '12:00:00', '13:45:00', NULL, -109, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-626, 1, '08:30:00', '10:15:00', NULL, -109, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-627, 1, '13:45:00', '17:15:00', NULL, -109, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-628, 2, '12:00:00', '13:45:00', NULL, -109, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-629, 2, '15:30:00', '19:00:00', NULL, -109, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-630, 3, '08:30:00', '13:45:00', NULL, -109, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-631, 3, '15:30:00', '17:15:00', NULL, -109, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-632, 4, '10:15:00', '12:00:00', NULL, -109, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-633, 0, '10:15:00', '12:00:00', NULL, -109, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-634, 0, '13:45:00', '17:15:00', NULL, -109, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-635, 1, '10:15:00', '13:45:00', NULL, -109, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-636, 2, '08:30:00', '12:00:00', NULL, -109, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-637, 2, '13:45:00', '15:30:00', NULL, -109, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-638, 3, '13:45:00', '15:30:00', NULL, -109, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-639, 4, '08:30:00', '10:15:00', NULL, -109, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-640, 4, '12:00:00', '17:15:00', NULL, -109, 0);

-- SR13
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-641, 0, '08:30:00', '20:30:00', NULL, -110, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-642, 1, '08:30:00', '20:30:00', NULL, -110, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-643, 2, '08:30:00', '20:30:00', NULL, -110, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-644, 3, '08:30:00', '20:30:00', NULL, -110, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-645, 4, '08:30:00', '20:30:00', NULL, -110, 1);

-- GROSSER
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-646, 0, '10:15:00', '20:30:00', NULL, -108, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-647, 1, '08:30:00', '10:15:00', NULL, -108, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-648, 1, '12:00:00', '20:30:00', NULL, -108, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-649, 2, '08:30:00', '12:00:00', NULL, -108, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-650, 2, '13:45:00', '15:30:00', NULL, -108, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-651, 3, '08:30:00', '10:15:00', NULL, -108, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-652, 3, '17:15:00', '19:00:00', NULL, -108, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-653, 4, '08:30:00', '12:00:00', NULL, -108, 1);

INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-654, 0, '08:30:00', '10:15:00', NULL, -108, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-655, 1, '10:15:00', '12:00:00', NULL, -108, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-656, 2, '12:00:00', '13:45:00', NULL, -108, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-657, 3, '10:15:00', '12:00:00', NULL, -108, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-658, 3, '13:45:00', '17:15:00', NULL, -108, 0);

-- Rechnerraum 14
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-659, 0, '08:30:00', '20:30:00', NULL, -111, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-660, 1, '08:30:00', '20:30:00', NULL, -111, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-661, 2, '08:30:00', '20:30:00', NULL, -111, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-662, 3, '08:30:00', '20:30:00', NULL, -111, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-663, 4, '08:30:00', '20:30:00', NULL, -111, 0);

-- Rechnerraum 15
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-664, 0, '08:30:00', '20:30:00', NULL, -112, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-665, 1, '08:30:00', '20:30:00', NULL, -112, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-666, 2, '08:30:00', '20:30:00', NULL, -112, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-667, 3, '08:30:00', '20:30:00', NULL, -112, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-668, 4, '08:30:00', '20:30:00', NULL, -112, 0);

-- Rechnerraum 18
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-669, 0, '08:30:00', '20:30:00', NULL, -113, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-670, 1, '08:30:00', '20:30:00', NULL, -113, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-671, 2, '08:30:00', '20:30:00', NULL, -113, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-672, 3, '08:30:00', '20:30:00', NULL, -113, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-673, 4, '08:30:00', '20:30:00', NULL, -113, 0);

-- Rechnerraum 19
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-674, 0, '08:30:00', '20:30:00', NULL, -114, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-675, 1, '08:30:00', '20:30:00', NULL, -114, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-676, 2, '08:30:00', '20:30:00', NULL, -114, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-677, 3, '08:30:00', '20:30:00', NULL, -114, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-678, 4, '08:30:00', '20:30:00', NULL, -114, 0);

-- Rechnerraum 20
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-679, 0, '08:30:00', '20:30:00', NULL, -115, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-680, 1, '08:30:00', '20:30:00', NULL, -115, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-681, 2, '08:30:00', '20:30:00', NULL, -115, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-682, 3, '08:30:00', '20:30:00', NULL, -115, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-683, 4, '08:30:00', '20:30:00', NULL, -115, 0);

-- Rechnerraum 21
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-684, 0, '08:30:00', '20:30:00', NULL, -116, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-685, 1, '08:30:00', '20:30:00', NULL, -116, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-686, 2, '08:30:00', '20:30:00', NULL, -116, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-687, 3, '08:30:00', '20:30:00', NULL, -116, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-688, 4, '08:30:00', '20:30:00', NULL, -116, 0);

-- Rechnerraum 22
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-689, 0, '08:30:00', '20:30:00', NULL, -117, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-690, 1, '08:30:00', '20:30:00', NULL, -117, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-691, 2, '08:30:00', '20:30:00', NULL, -117, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-692, 3, '08:30:00', '20:30:00', NULL, -117, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-693, 4, '08:30:00', '20:30:00', NULL, -117, 0);

-- Rechnerraum 25
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-694, 0, '08:30:00', '20:30:00', NULL, -118, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-695, 1, '08:30:00', '20:30:00', NULL, -118, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-696, 2, '08:30:00', '20:30:00', NULL, -118, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-697, 3, '08:30:00', '20:30:00', NULL, -118, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-698, 4, '08:30:00', '20:30:00', NULL, -118, 0);

-- Rechnerraum 26
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-699, 0, '08:30:00', '20:30:00', NULL, -119, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-700, 1, '08:30:00', '20:30:00', NULL, -119, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-701, 2, '08:30:00', '20:30:00', NULL, -119, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-702, 3, '08:30:00', '20:30:00', NULL, -119, 0);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-703, 4, '08:30:00', '20:30:00', NULL, -119, 0);


-- timingConstraints for courses (10);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-11, 0, '08:15:00', '10:00:00', '703010', NULL, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-12, 1, '08:15:00', '10:00:00', '703011', NULL, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-13, 2, '08:15:00', '10:00:00', '703012', NULL, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-14, 3, '08:15:00', '10:00:00', '703013', NULL, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-15, 4, '08:15:00', '18:00:00', '703024', NULL, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-16, 1, '08:15:00', '18:00:00', '703025', NULL, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-17, 2, '12:00:00', '13:00:00', '703062', NULL, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-18, 3, '13:00:00', '14:00:00', '703098', NULL, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-19, 2, '09:00:00', '10:30:00', '703099', NULL, 1);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-20, 1, '12:00:00', '13:00:00', '703010', NULL, 1);

-- timings of assigned courseSessions of timeTable 3 (TestWithManyCollisions)
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-21, 3, '08:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-22, 1, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-23, 1, '09:45:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-24, 3, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-25, 4, '10:00:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-26, 0, '11:30:00', '13:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-27, 0, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-28, 0, '12:00:00', '13:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-29, 2, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-30, 2, '09:45:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-31, 0, '09:30:00', '10:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-32, 0, '08:15:00', '09:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-33, 0, '10:00:00', '10:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-34, 0, '09:00:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-35, 1, '08:15:00', '10:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-36, 2, '08:15:00', '09:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-37, 2, '09:00:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-38, 2, '09:45:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-39, 2, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-40, 2, '08:15:00', '09:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-41, 2, '09:00:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-42, 2, '09:45:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-43, 2, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-44, 0, '08:15:00', '10:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-45, 0, '08:15:00', '09:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-46, 4, '10:00:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-47, 3, '08:15:00', '08:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-48, 3, '09:45:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-49, 3, '09:00:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-50, 3, '09:45:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-51, 3, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-52, 3, '10:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-53, 0, '11:15:00', '13:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-54, 1, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-55, 1, '09:45:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-56, 1, '11:15:00', '12:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-57, 1, '12:45:00', '13:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-58, 1, '13:15:00', '14:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-59, 1, '10:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-60, 0, '10:00:00', '13:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-61, 3, '08:45:00', '10:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-62, 3, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-63, 1, '08:30:00', '10:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-64, 1, '10:00:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-65, 0, '10:00:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-66, 0, '08:30:00', '10:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-67, 3, '08:15:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-68, 3, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-69, 3, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-70, 2, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-71, 2, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-72, 1, '08:30:00', '10:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-73, 1, '10:00:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-74, 2, '09:15:00', '12:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-75, 0, '10:45:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-76, 0, '10:00:00', '10:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-77, 0, '10:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-78, 4, '08:30:00', '10:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-79, 0, '10:45:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-80, 0, '13:00:00', '14:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-81, 3, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-82, 4, '08:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-83, 3, '08:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-84, 2, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-85, 2, '09:45:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-86, 2, '11:15:00', '12:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-87, 2, '12:45:00', '13:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-88, 2, '13:15:00', '14:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-89, 4, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-90, 4, '09:45:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-91, 4, '11:15:00', '12:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-92, 1, '08:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-93, 0, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-94, 3, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-95, 3, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-96, 3, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-97, 3, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-98, 3, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-99, 3, '10:45:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-100, 4, '10:45:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-101, 0, '08:15:00', '10:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-102, 4, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-103, 4, '09:45:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-104, 4, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-105, 0, '10:00:00', '10:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-106, 0, '08:15:00', '09:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-107, 0, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-108, 0, '09:15:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-109, 4, '08:15:00', '09:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-110, 4, '09:00:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-111, 3, '09:00:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-112, 4, '09:45:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-113, 4, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-114, 4, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-115, 4, '12:00:00', '12:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-116, 2, '08:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-117, 1, '10:00:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-118, 4, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-119, 4, '10:00:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-120, 2, '10:30:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-121, 4, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-122, 4, '09:45:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-123, 4, '11:15:00', '12:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-124, 1, '08:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-125, 3, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-126, 3, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-127, 2, '08:45:00', '10:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-128, 3, '08:45:00', '10:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-129, 2, '08:30:00', '10:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-130, 2, '10:00:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-131, 1, '08:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-132, 3, '10:00:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-133, 1, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-134, 1, '10:00:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-135, 3, '10:30:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-136, 2, '08:30:00', '10:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-137, 2, '10:00:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-138, 4, '09:45:00', '11:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-139, 3, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-140, 3, '09:45:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-141, 1, '10:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-142, 4, '08:15:00', '09:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-143, 0, '09:45:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-144, 0, '09:00:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-145, 0, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-146, 4, '10:00:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-147, 2, '09:00:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-148, 2, '08:15:00', '09:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-149, 2, '09:30:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-150, 2, '09:00:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-151, 3, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-152, 3, '09:45:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-153, 3, '11:15:00', '12:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-154, 1, '11:30:00', '13:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-155, 2, '08:15:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-156, 4, '09:00:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-157, 2, '09:30:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-158, 3, '09:00:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-159, 1, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-160, 1, '09:45:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-161, 4, '09:30:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-162, 2, '09:00:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-163, 0, '08:15:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-164, 0, '11:15:00', '12:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-165, 0, '09:45:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-166, 0, '09:00:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-167, 0, '10:00:00', '11:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-168, 0, '09:00:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-169, 0, '10:30:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-170, 3, '08:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-171, 1, '08:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-172, 4, '08:15:00', '08:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-173, 4, '09:00:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-174, 4, '08:15:00', '10:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-175, 4, '08:15:00', '09:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-176, 1, '08:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-177, 4, '09:45:00', '12:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-178, 0, '08:15:00', '08:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-179, 2, '09:15:00', '12:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-180, 3, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-181, 3, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-182, 3, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-183, 3, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-184, 4, '09:30:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-185, 3, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-186, 3, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-187, 2, '08:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-188, 0, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-189, 0, '09:45:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-190, 0, '11:15:00', '12:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-191, 0, '08:15:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-192, 1, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-193, 1, '10:15:00', '11:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-194, 3, '08:15:00', '10:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-195, 4, '08:15:00', '09:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-196, 4, '09:00:00', '09:45:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-197, 4, '09:45:00', '10:30:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-198, 4, '10:30:00', '11:15:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-199, 4, '11:15:00', '12:00:00', NULL, NULL, 3);
INSERT INTO timing(id,day_of_the_week,start_time,end_time,course_id,room_table_id,timing_type) VALUES (-200, 4, '12:00:00', '12:45:00', NULL, NULL, 3);

-- courseSessions of timeTable1
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-420, 'PS Rechnerarchitektur - Group 1', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-421, 'PS Rechnerarchitektur - Group 2', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-422, 'PS Rechnerarchitektur - Group 3', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-423, 'PS Rechnerarchitektur - Group 4', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-424, 'PS Rechnerarchitektur - Group 5', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-425, 'PS Rechnerarchitektur - Group 6', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-426, 'PS Rechnerarchitektur - Group 7', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-427, 'PS Rechnerarchitektur - Group 8', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-428, 'PS Rechnerarchitektur - Group 9', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-429, 'PS Rechnerarchitektur - Group 10', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-430, 'PS Rechnerarchitektur - Group 11', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-431, 'PS Rechnerarchitektur - Group 12', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-432, 'PS Rechnerarchitektur - Group 13', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-433, 'PS Rechnerarchitektur - Group 14', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-434, 'PS Rechnerarchitektur - Group 15', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-435, 'PS Rechnerarchitektur - Group 16', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-436, 'PS Rechnerarchitektur - Group 17', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-437, 'PS Rechnerarchitektur - Group 18', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-438, 'PS Rechnerarchitektur - Group 19', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-439, 'PS Rechnerarchitektur - Group 20', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-440, 'VO Rechnerarchitektur - Split 1', 'Rainer Böhme', 1, 300, 120, false, false, false, '703064', NULL, -1, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-441, 'VO Rechnerarchitektur - Split 2', 'Rainer Böhme', 1, 300, 120, false, false, false, '703064', NULL, -1, NULL, 0, FALSE);

-- courseSessions of timeTable2

INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-181, 'VO Einführung in die Programmierung', 'Michael Felderer', 1, 300, 180, false, false, false, '703003', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-182, 'PS Einführung in die Programmierung - Group 1', 'Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-183, 'PS Einführung in die Programmierung - Group 2', 'Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-184, 'PS Einführung in die Programmierung - Group 3', 'Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-185, 'PS Einführung in die Programmierung - Group 4', 'Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-186, 'PS Einführung in die Programmierung - Group 5', 'Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-187, 'PS Einführung in die Programmierung - Group 6', 'Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-188, 'PS Funktionale Programmierung - Group 1', 'Rene Thiemann', 1, 25, 45, true, false, false, '703025', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-189, 'PS Funktionale Programmierung - Group 2', 'Rene Thiemann', 1, 25, 45, true, false, false, '703025', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-190, 'PS Funktionale Programmierung - Group 3', 'Rene Thiemann', 1, 25, 45, true, false, false, '703025', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-191, 'PS Funktionale Programmierung - Group 4', 'Rene Thiemann', 1, 25, 45, true, false, false, '703025', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-192, 'PS Funktionale Programmierung - Group 5', 'Rene Thiemann', 1, 25, 45, true, false, false, '703025', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-193, 'PS Funktionale Programmierung - Group 6', 'Rene Thiemann', 1, 25, 45, true, false, false, '703025', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-194, 'VO Funktionale Programmierung', 'Rene Thiemann', 1, 300, 120, false, false, false, '703024', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-195, 'PS Rechnerarchitektur - Group 1', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-196, 'PS Rechnerarchitektur - Group 2', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-197, 'PS Rechnerarchitektur - Group 3', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-198, 'PS Rechnerarchitektur - Group 4', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-199, 'PS Rechnerarchitektur - Group 5', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-200, 'VO Rechnerarchitektur', 'Rainer Böhme', 1, 300, 120, false, false, false, '703064', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-201, 'PS Einführung in die theoretische Informatik', 'Jamie Hochrainer', 1, 25, 45, false, false, false, '703062', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-202, 'VO Einführung in die theoretische Informatik', 'Georg Moser', 1, 300, 120, false, false, false, '703007', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-203, 'PS Lineare Algebra - Group 1', 'Tim Netzer', 1, 25, 45, false, false, false, '703098', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-204, 'PS Lineare Algebra - Group 2', 'Tim Netzer', 1, 25, 45, false, false, false, '703098', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-205, 'PS Lineare Algebra - Group 3', 'Tim Netzer', 1, 25, 45, false, false, false, '703098', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-206, 'VO Lineare Algebra', 'Tim Netzer', 1, 300, 120, false, false, false, '703099', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-207, 'PS Programmiermethodik - Group 1', 'Simon Priller', 2, 25, 90, true, false, false, '703016', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-208, 'PS Programmiermethodik - Group 2', 'Simon Priller', 2, 25, 90, true, false, false, '703016', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-209, 'PS Programmiermethodik - Group 3', 'Simon Priller', 2, 25, 90, true, false, false, '703016', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-210, 'VO Programmiermethodik', 'Lukas Kaltenbrunner', 2, 300, 180, false, false, false, '703017', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-211, 'PS Angewandte Mathematik für die Informatik - Group 1', 'Niko Rauch', 2, 25, 90, false, false, false, '703066', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-212, 'PS Angewandte Mathematik für die Informatik - Group 2', 'Niko Rauch', 2, 25, 90, false, false, false, '703066', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-213, 'PS Angewandte Mathematik für die Informatik - Group 3', 'Niko Rauch', 2, 25, 90, false, false, false, '703066', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-214, 'PS Angewandte Mathematik für die Informatik - Group 4', 'Niko Rauch', 2, 25, 90, false, false, false, '703066', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-215, 'PS Angewandte Mathematik für die Informatik - Group 5', 'Niko Rauch', 2, 25, 90, false, false, false, '703066', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-216, 'PS Angewandte Mathematik für die Informatik - Group 6', 'Niko Rauch', 2, 25, 90, false, false, false, '703066', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-217, 'PS Angewandte Mathematik für die Informatik - Group 7', 'Niko Rauch', 2, 25, 90, false, false, false, '703066', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-218, 'VO Angewandte Mathematik für die Informatik', 'Michael Harders', 2, 300, 180, false, false, false, '703065', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-219, 'PS Algorithmen und Datenstrukturen - Group 1', 'Laurenz Wischounig', 2, 25, 90, false, false, false, '703011', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-220, 'PS Algorithmen und Datenstrukturen - Group 2', 'Laurenz Wischounig', 2, 25, 90, false, false, false, '703011', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-221, 'PS Algorithmen und Datenstrukturen - Group 3', 'Laurenz Wischounig', 2, 25, 90, false, false, false, '703011', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-222, 'PS Algorithmen und Datenstrukturen - Group 4', 'Laurenz Wischounig', 2, 25, 90, false, false, false, '703011', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-223, 'PS Algorithmen und Datenstrukturen - Group 5', 'Laurenz Wischounig', 2, 25, 90, false, false, false, '703011', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-224, 'PS Algorithmen und Datenstrukturen - Group 6', 'Laurenz Wischounig', 2, 25, 90, false, false, false, '703011', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-225, 'PS Algorithmen und Datenstrukturen - Group 7', 'Laurenz Wischounig', 2, 25, 90, false, false, false, '703011', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-226, 'VO Algorithmen und Datenstrukturen', 'Justus Piater', 2, 300, 180, false, false, false, '703010', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-227, 'PS Betriebssysteme - Group 1', 'Markus Reiter', 2, 25, 90, true, false, false, '703012', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-228, 'PS Betriebssysteme - Group 2', 'Markus Reiter', 2, 25, 90, true, false, false, '703012', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-229, 'PS Betriebssysteme - Group 3', 'Markus Reiter', 2, 25, 90, true, false, false, '703012', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-230, 'PS Betriebssysteme - Group 4', 'Markus Reiter', 2, 25, 90, true, false, false, '703012', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-231, 'PS Betriebssysteme - Group 5', 'Markus Reiter', 2, 25, 90, true, false, false, '703012', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-232, 'PS Betriebssysteme - Group 6', 'Markus Reiter', 2, 25, 90, true, false, false, '703012', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-233, 'PS Betriebssysteme - Group 7', 'Markus Reiter', 2, 25, 90, true, false, false, '703012', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-234, 'PS Betriebssysteme - Group 8', 'Markus Reiter', 2, 25, 90, true, false, false, '703012', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-235, 'VO Betriebssysteme', 'Univ.-Prof. Dipl.-Ing. Dr. Thomas Fahringer', 2, 300, 180, false, false, false, '703013', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-236, 'VO Datenbanksysteme', 'Günther Specht', 3, 150, 180, false, false, false, '703020', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-237, 'PS Datenbanksysteme - Group 1', 'David Stern', 3, 25, 90, true, false, false, '703021', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-238, 'PS Datenbanksysteme - Group 2', 'David Stern', 3, 25, 90, true, false, false, '703021', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-239, 'PS Datenbanksysteme - Group 3', 'David Stern', 3, 25, 90, true, false, false, '703021', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-240, 'PS Datenbanksysteme - Group 4', 'David Stern', 3, 25, 90, true, false, false, '703021', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-241, 'PS Datenbanksysteme - Group 5', 'David Stern', 3, 25, 90, true, false, false, '703021', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-242, 'VO Rechnernetze und Internettechnik', 'Jan Beutel', 3, 150, 180, false, false, false, '703033', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-243, 'PS Rechnernetze und Internettechnik - Group 1', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-244, 'PS Rechnernetze und Internettechnik - Group 2', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-245, 'PS Rechnernetze und Internettechnik - Group 3', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-246, 'PS Rechnernetze und Internettechnik - Group 4', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-247, 'PS Rechnernetze und Internettechnik - Group 5', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-248, 'PS Rechnernetze und Internettechnik - Group 6', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-249, 'PS Rechnernetze und Internettechnik - Group 7', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-250, 'VO Daten und Wahrscheinlichkeiten', 'Adam Jatowt', 3, 150, 120, false, false, false, '703067', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-251, 'PS Daten und Wahrscheinlichkeiten - Group 1', 'Nadja Gruber', 3, 25, 45, true, false, false, '703068', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-252, 'PS Daten und Wahrscheinlichkeiten - Group 2', 'Nadja Gruber', 3, 25, 45, true, false, false, '703068', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-253, 'PS Daten und Wahrscheinlichkeiten - Group 3', 'Nadja Gruber', 3, 25, 45, true, false, false, '703068', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-254, 'VO Diskrete Strukturen', 'Markus Eberl', 3, 150, 120, false, false, false, '703069', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-255, 'PS Diskrete Strukturen - Group 1', 'Charlie Sheen', 3, 25, 45, false, false, false, '703070', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-256, 'PS Diskrete Strukturen - Group 2', 'Charlie Sheen', 3, 25, 45, false, false, false, '703070', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-257, 'PS Diskrete Strukturen - Group 3', 'Charlie Sheen', 3, 25, 45, false, false, false, '703070', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-258, 'PS Diskrete Strukturen - Group 4', 'Charlie Sheen', 3, 25, 45, false, false, false, '703070', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-259, 'VO Softwarearchitektur', 'Clemens Sauerwein', 3, 150, 180, false, false, false, '703071', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-260, 'PS Softwarearchitektur - Group 1', 'Michael Breu', 3, 25, 90, true, false, false, '703072', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-261, 'PS Softwarearchitektur - Group 2', 'Michael Breu', 3, 25, 90, true, false, false, '703072', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-262, 'PS Softwarearchitektur - Group 3', 'Michael Breu', 3, 25, 90, true, false, false, '703072', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-263, 'PS Softwarearchitektur - Group 4', 'Michael Breu', 3, 25, 90, true, false, false, '703072', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-264, 'PS Softwarearchitektur - Group 5', 'Michael Breu', 3, 25, 90, true, false, false, '703072', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-265, 'VO Logik', 'Aart Middeldorp', 4, 150, 180, false, false, false, '703026', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-266, 'PS Logik - Group 1', 'Fabian Mitterwallner', 4, 25, 90, false, false, false, '703027', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-267, 'PS Logik - Group 2', 'Fabian Mitterwallner', 4, 25, 90, false, false, false, '703027', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-268, 'PS Logik - Group 3', 'Fabian Mitterwallner', 4, 25, 90, false, false, false, '703027', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-269, 'PS Logik - Group 4', 'Fabian Mitterwallner', 4, 25, 90, false, false, false, '703027', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-270, 'VO Maschinelles Lernen', 'Samuelle Tosatto', 4, 150, 180, false, false, false, '703075', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-271, 'PS Maschinelles Lernen - Group 1', 'Sayantan Auddy', 4, 25, 90, true, false, false, '703076', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-272, 'PS Maschinelles Lernen - Group 2', 'Sayantan Auddy', 4, 25, 90, true, false, false, '703076', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-273, 'PS Maschinelles Lernen - Group 3', 'Sayantan Auddy', 4, 25, 90, true, false, false, '703076', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-274, 'PS Maschinelles Lernen - Group 4', 'Sayantan Auddy', 4, 25, 90, true, false, false, '703076', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-275, 'PS Maschinelles Lernen - Group 5', 'Sayantan Auddy', 4, 25, 90, true, false, false, '703076', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-276, 'PS Maschinelles Lernen - Group 6', 'Sayantan Auddy', 4, 25, 90, true, false, false, '703076', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-277, 'VO Parallele Programmierung', 'Thomas Fahringer', 4, 150, 120, false, false, false, '703077', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-278, 'PS Parallele Programmierung - Group 1', 'Simon Lechner', 4, 25, 45, true, false, false, '703078', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-279, 'PS Parallele Programmierung - Group 2', 'Simon Lechner', 4, 25, 45, true, false, false, '703078', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-280, 'PS Parallele Programmierung - Group 3', 'Simon Lechner', 4, 25, 45, true, false, false, '703078', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-281, 'PS Parallele Programmierung - Group 4', 'Simon Lechner', 4, 25, 45, true, false, false, '703078', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-282, 'PS Parallele Programmierung - Group 5', 'Simon Lechner', 4, 25, 45, true, false, false, '703078', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-283, 'VO Software Engineering', 'Ruth Breu', 4, 150, 120, false, false, false, '703079', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-284, 'PS Software Engineering - Group 1', 'Anna Jäger', 4, 25, 90, true, false, false, '703080', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-285, 'PS Software Engineering - Group 2', 'Anna Jäger', 4, 25, 90, true, false, false, '703080', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-286, 'PS Software Engineering - Group 3', 'Anna Jäger', 4, 25, 90, true, false, false, '703080', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-287, 'PS Software Engineering - Group 4', 'Anna Jäger', 4, 25, 90, true, false, false, '703080', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-288, 'PS Software Engineering - Group 5', 'Anna Jäger', 4, 25, 90, true, false, false, '703080', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-289, 'PS Software Engineering - Group 6', 'Anna Jäger', 4, 25, 90, true, false, false, '703080', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-290, 'PS Software Engineering - Group 7', 'Anna Jäger', 4, 25, 90, true, false, false, '703080', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-291, 'PS Software Engineering - Group 8', 'Anna Jäger', 4, 25, 90, true, false, false, '703080', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-292, 'VU Einführung in das wissenschaftliche Arbeiten', 'Thomas Fahringer', 4, 25, 90, true, false, false, '703073', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-293, 'VO Verteilte Systeme', 'Juan Aznar Poveda', 5, 150, 180, false, false, false, '703087', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-294, 'PS Verteilte Systeme - Group 1', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-295, 'PS Verteilte Systeme - Group 2', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-296, 'PS Verteilte Systeme - Group 3', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-297, 'PS Verteilte Systeme - Group 4', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-298, 'PS Verteilte Systeme - Group 5', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-299, 'PS Verteilte Systeme - Group 6', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-300, 'PS Verteilte Systeme - Group 7', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-301, 'VO Visual Computing', 'Michael Harders', 5, 150, 180, false, false, false, '703089', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-302, 'PS Visual Computing - Group 1', 'Niko Rauch', 5, 25, 90, true, false, false, '703090', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-303, 'PS Visual Computing - Group 2', 'Niko Rauch', 5, 25, 90, true, false, false, '703090', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-304, 'PS Visual Computing - Group 3', 'Niko Rauch', 5, 25, 90, true, false, false, '703090', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-305, 'PS Visual Computing - Group 4', 'Niko Rauch', 5, 25, 90, true, false, false, '703090', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-306, 'PS Visual Computing - Group 5', 'Niko Rauch', 5, 25, 90, true, false, false, '703090', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-307, 'PS Visual Computing - Group 6', 'Niko Rauch', 5, 25, 90, true, false, false, '703090', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-308, 'VU Einführung in die Robotik', 'Samuelle Tosatto', 5, 25, 180, false, false, false, '703135', NULL, -2, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-309, 'VU Termersetzungssysteme', 'Fabian Mitterwallner', 5, 35, 180, false, false, false, '703141', NULL, -2, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-310, 'VU Deep Learning', 'Antonio Jose Rodriguez-Sanchez', 5, 25, 180, true, false, false, '703134', NULL, -2, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-311, 'VU Mensch Maschine Interaktion', 'Pascal Knierim', 5, 25, 180, false, false, false, '703136', NULL, -2, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-312, 'VO Ethische Aspekte der Informatik', 'Wilhelm Guggenberger', 5, 150, 90, false, false, false, '703042', NULL, -2, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-313, 'SE Vertiefunsseminar', 'Günther Specht', 6, 25, 90, false, false, false, '703081', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-314, 'SE Bachelorseminar', 'All Profs', 6, 40, 180, false, false, false, '703082', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-315, 'VU Prinzipien von Blockchain-Systemen', 'Rainer Böhme', 6, 25, 180, false, false, false, '703133', NULL, -2, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-316, 'VU Performance-oriented Computing', 'Peter Thoman', 6, 25, 180, true, false, false, '703146', NULL, -2, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-317, 'VO Programmverifikation', 'Rene Thiemann', 6, 150, 180, false, false, false, '703083', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-318, 'PS Programmverifikation - Group 1', 'Rene Thiemann', 6, 25, 90, false, false, false, '703084', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-319, 'PS Programmverifikation - Group 2', 'Rene Thiemann', 6, 25, 90, false, false, false, '703084', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-320, 'PS Programmverifikation - Group 3', 'Rene Thiemann', 6, 25, 90, false, false, false, '703084', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-321, 'PS Programmverifikation - Group 4', 'Rene Thiemann', 6, 25, 90, false, false, false, '703084', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-322, 'PS Programmverifikation - Group 5', 'Rene Thiemann', 6, 25, 90, false, false, false, '703084', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-323, 'PS Programmverifikation - Group 6', 'Rene Thiemann', 6, 25, 90, false, false, false, '703084', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-324, 'PS Programmverifikation - Group 7', 'Rene Thiemann', 6, 25, 90, false, false, false, '703084', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-325, 'VO Softwarequalität', 'Harald Victor Schweiger', 6, 150, 180, false, false, false, '703085', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-326, 'PS Softwarequalität - Group 1', 'Harald Victor Schweiger', 6, 25, 90, false, false, false, '703086', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-327, 'PS Softwarequalität - Group 2', 'Harald Victor Schweiger', 6, 25, 90, false, false, false, '703086', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-328, 'PS Softwarequalität - Group 3', 'Harald Victor Schweiger', 6, 25, 90, false, false, false, '703086', NULL, -2, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-329, 'VO Informationstheorie und Kryptologie', 'Arne Dür', 6, 150, 120, false, false, false, '702631', NULL, -2, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-330, 'PS Informationstheorie und Kryptologie - Group 1', 'Arne Dür', 6, 25, 45, false, false, false, '702632', NULL, -2, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-331, 'PS Informationstheorie und Kryptologie - Group 2', 'Arne Dür', 6, 25, 45, false, false, false, '702632', NULL, -2, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-332, 'PS Informationstheorie und Kryptologie - Group 3', 'Arne Dür', 6, 25, 45, false, false, false, '702632', NULL, -2, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-333, 'PS Informationstheorie und Kryptologie - Group 4', 'Arne Dür', 6, 25, 45, false, false, false, '702632', NULL, -2, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-334, 'PS Informationstheorie und Kryptologie - Group 5', 'Arne Dür', 6, 25, 45, false, false, false, '702632', NULL, -2, NULL, 0, TRUE);

-- courseSessions of timeTable3 (TestWithManyCollisions)
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-1, 'VO Einführung in die Programmierung', 'Michael Felderer', 1, 300, 180, false, true, false, '703003', -34, -3, -21, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-2, 'PS Einführung in die Programmierung - Group 1', 'Lukas Kaltenbrunner', 1, 25, 90, true, true, false, '703004', -41, -3, -22, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-3, 'PS Einführung in die Programmierung - Group 2', 'Lukas Kaltenbrunner', 1, 25, 90, true, true, false, '703004', -41, -3, -23, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-4, 'PS Einführung in die Programmierung - Group 3', 'Lukas Kaltenbrunner', 1, 25, 90, true, true, false, '703004', -42, -3, -24, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-5, 'PS Einführung in die Programmierung - Group 4', 'Lukas Kaltenbrunner', 1, 25, 90, true, true, false, '703004', -43, -3, -25, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-6, 'PS Einführung in die Programmierung - Group 5', 'Lukas Kaltenbrunner', 1, 25, 90, true, true, false, '703004', -46, -3, -26, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-7, 'PS Einführung in die Programmierung - Group 6', 'Lukas Kaltenbrunner', 1, 25, 90, true, true, false, '703004', -47, -3, -27, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-8, 'PS Einführung in die Programmierung - Group 7', 'Lukas Kaltenbrunner', 1, 25, 90, true, true, false, '703004', -47, -3, -28, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-9, 'PS Funktionale Programmierung - Group 1', 'Rene Thiemann', 1, 25, 45, true, true, false, '703025', -41, -3, -29, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-10, 'PS Funktionale Programmierung - Group 2', 'Rene Thiemann', 1, 25, 45, true, true, false, '703025', -41, -3, -30, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-11, 'PS Funktionale Programmierung - Group 3', 'Rene Thiemann', 1, 25, 45, true, true, false, '703025', -42, -3, -31, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-12, 'PS Funktionale Programmierung - Group 4', 'Rene Thiemann', 1, 25, 45, true, true, false, '703025', -43, -3, -32, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-13, 'PS Funktionale Programmierung - Group 5', 'Rene Thiemann', 1, 25, 45, true, true, false, '703025', -46, -3, -33, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-14, 'PS Funktionale Programmierung - Group 6', 'Rene Thiemann', 1, 25, 45, true, true, false, '703025', -43, -3, -34, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-15, 'VO Funktionale Programmierung', 'Rene Thiemann', 1, 300, 120, false, true, false, '703024', -36, -3, -35, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-16, 'PS Rechnerarchitektur - Group 1', 'Rainer Böhme', 1, 25, 45, false, true, false, '703063', -39, -3, -36, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-17, 'PS Rechnerarchitektur - Group 2', 'Rainer Böhme', 1, 25, 45, false, true, false, '703063', -39, -3, -37, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-18, 'PS Rechnerarchitektur - Group 3', 'Rainer Böhme', 1, 25, 45, false, true, false, '703063', -39, -3, -38, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-19, 'PS Rechnerarchitektur - Group 4', 'Rainer Böhme', 1, 25, 45, false, true, false, '703063', -39, -3, -39, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-20, 'PS Rechnerarchitektur - Group 5', 'Rainer Böhme', 1, 25, 45, false, true, false, '703063', -40, -3, -40, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-21, 'PS Rechnerarchitektur - Group 6', 'Rainer Böhme', 1, 25, 45, false, true, false, '703063', -40, -3, -41, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-22, 'PS Rechnerarchitektur - Group 7', 'Rainer Böhme', 1, 25, 45, false, true, false, '703063', -40, -3, -42, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-23, 'PS Rechnerarchitektur - Group 8', 'Rainer Böhme', 1, 25, 45, false, true, false, '703063', -40, -3, -43, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-24, 'VO Rechnerarchitektur', 'Rainer Böhme', 1, 300, 120, false, true, false, '703064', -34, -3, -44, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-25, 'PS Einführung in die theoretische Informatik', 'Jamie Hochrainer', 1, 25, 45, false, true, false, '703062', -56, -3, -45, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-26, 'VO Einführung in die theoretische Informatik', 'Georg Moser', 1, 300, 120, false, true, false, '703007', -36, -3, -46, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-27, 'PS Lineare Algebra - Group 1', 'Tim Netzer', 1, 25, 45, false, true, false, '703098', -39, -3, -47, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-28, 'PS Lineare Algebra - Group 2', 'Tim Netzer', 1, 25, 45, false, true, false, '703098', -39, -3, -48, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-29, 'PS Lineare Algebra - Group 3', 'Tim Netzer', 1, 25, 45, false, true, false, '703098', -39, -3, -49, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-30, 'PS Lineare Algebra - Group 4', 'Tim Netzer', 1, 25, 45, false, true, false, '703098', -40, -3, -50, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-31, 'PS Lineare Algebra - Group 5', 'Tim Netzer', 1, 25, 45, false, true, false, '703098', -40, -3, -51, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-32, 'PS Lineare Algebra - Group 6', 'Tim Netzer', 1, 25, 45, false, true, false, '703098', -51, -3, -52, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-33, 'VO Lineare Algebra', 'Tim Netzer', 1, 300, 120, false, true, false, '703099', -35, -3, -53, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-34, 'PS Programmiermethodik - Group 1', 'Simon Priller', 2, 25, 90, true, true, false, '703016', -43, -3, -54, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-35, 'PS Programmiermethodik - Group 2', 'Simon Priller', 2, 25, 90, true, true, false, '703016', -43, -3, -55, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-36, 'PS Programmiermethodik - Group 3', 'Simon Priller', 2, 25, 90, true, true, false, '703016', -43, -3, -56, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-37, 'PS Programmiermethodik - Group 4', 'Simon Priller', 2, 25, 90, true, true, false, '703016', -43, -3, -57, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-38, 'PS Programmiermethodik - Group 5', 'Simon Priller', 2, 25, 90, true, true, false, '703016', -43, -3, -58, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-39, 'PS Programmiermethodik - Group 6', 'Simon Priller', 2, 25, 90, true, true, false, '703016', -45, -3, -59, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-40, 'VO Programmiermethodik', 'Lukas Kaltenbrunner', 2, 300, 180, false, true, false, '703017', -36, -3, -60, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-41, 'PS Angewandte Mathematik für die Informatik - Group 1', 'Niko Rauch', 2, 25, 90, false, true, false, '703066', -51, -3, -61, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-42, 'PS Angewandte Mathematik für die Informatik - Group 2', 'Niko Rauch', 2, 25, 90, false, true, false, '703066', -37, -3, -62, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-43, 'PS Angewandte Mathematik für die Informatik - Group 3', 'Niko Rauch', 2, 25, 90, false, true, false, '703066', -52, -3, -63, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-44, 'PS Angewandte Mathematik für die Informatik - Group 4', 'Niko Rauch', 2, 25, 90, false, true, false, '703066', -52, -3, -64, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-45, 'PS Angewandte Mathematik für die Informatik - Group 5', 'Niko Rauch', 2, 25, 90, false, true, false, '703066', -55, -3, -65, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-46, 'PS Angewandte Mathematik für die Informatik - Group 6', 'Niko Rauch', 2, 25, 90, false, true, false, '703066', -55, -3, -66, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-47, 'VO Angewandte Mathematik für die Informatik', 'Michael Harders', 2, 300, 180, false, true, false, '703065', -35, -3, -67, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-48, 'PS Algorithmen und Datenstrukturen - Group 1', 'Laurenz Wischounig', 2, 25, 90, false, true, false, '703011', -39, -3, -68, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-49, 'PS Algorithmen und Datenstrukturen - Group 2', 'Laurenz Wischounig', 2, 25, 90, false, true, false, '703011', -40, -3, -69, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-50, 'PS Algorithmen und Datenstrukturen - Group 3', 'Laurenz Wischounig', 2, 25, 90, false, true, false, '703011', -52, -3, -70, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-51, 'PS Algorithmen und Datenstrukturen - Group 4', 'Laurenz Wischounig', 2, 25, 90, false, true, false, '703011', -52, -3, -71, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-52, 'PS Algorithmen und Datenstrukturen - Group 5', 'Laurenz Wischounig', 2, 25, 90, false, true, false, '703011', -55, -3, -72, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-53, 'PS Algorithmen und Datenstrukturen - Group 6', 'Laurenz Wischounig', 2, 25, 90, false, true, false, '703011', -55, -3, -73, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-54, 'VO Algorithmen und Datenstrukturen', 'Justus Piater', 2, 300, 180, false, true, false, '703010', -34, -3, -74, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-55, 'PS Betriebssysteme - Group 1', 'Markus Reiter', 2, 25, 90, true, true, false, '703012', -41, -3, -75, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-56, 'PS Betriebssysteme - Group 2', 'Markus Reiter', 2, 25, 90, true, true, false, '703012', -41, -3, -76, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-57, 'PS Betriebssysteme - Group 3', 'Markus Reiter', 2, 25, 90, true, true, false, '703012', -42, -3, -77, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-58, 'PS Betriebssysteme - Group 4', 'Markus Reiter', 2, 25, 90, true, true, false, '703012', -43, -3, -78, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-59, 'PS Betriebssysteme - Group 5', 'Markus Reiter', 2, 25, 90, true, true, false, '703012', -46, -3, -79, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-60, 'PS Betriebssysteme - Group 6', 'Markus Reiter', 2, 25, 90, true, true, false, '703012', -46, -3, -80, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-61, 'PS Betriebssysteme - Group 7', 'Markus Reiter', 2, 25, 90, true, true, false, '703012', -47, -3, -81, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-62, 'VO Betriebssysteme', 'Univ.-Prof. Dipl.-Ing. Dr. Thomas Fahringer', 2, 300, 180, false, true, false, '703013', -34, -3, -82, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-63, 'VO Datenbanksysteme', 'Günther Specht', 3, 150, 180, false, true, false, '703020', -36, -3, -83, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-64, 'PS Datenbanksysteme - Group 1', 'David Stern', 3, 25, 90, true, true, false, '703021', -43, -3, -84, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-65, 'PS Datenbanksysteme - Group 2', 'David Stern', 3, 25, 90, true, true, false, '703021', -43, -3, -85, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-66, 'PS Datenbanksysteme - Group 3', 'David Stern', 3, 25, 90, true, true, false, '703021', -43, -3, -86, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-67, 'PS Datenbanksysteme - Group 4', 'David Stern', 3, 25, 90, true, true, false, '703021', -43, -3, -87, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-68, 'PS Datenbanksysteme - Group 5', 'David Stern', 3, 25, 90, true, true, false, '703021', -43, -3, -88, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-69, 'PS Datenbanksysteme - Group 6', 'David Stern', 3, 25, 90, true, true, false, '703021', -45, -3, -89, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-70, 'PS Datenbanksysteme - Group 7', 'David Stern', 3, 25, 90, true, true, false, '703021', -45, -3, -90, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-71, 'PS Datenbanksysteme - Group 8', 'David Stern', 3, 25, 90, true, true, false, '703021', -45, -3, -91, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-72, 'VO Rechnernetze und Internettechnik', 'Jan Beutel', 3, 150, 180, false, true, false, '703033', -38, -3, -92, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-73, 'PS Rechnernetze und Internettechnik - Group 1', 'Niko Rossberg', 3, 25, 90, true, true, false, '703034', -41, -3, -93, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-74, 'PS Rechnernetze und Internettechnik - Group 2', 'Niko Rossberg', 3, 25, 90, true, true, false, '703034', -41, -3, -94, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-75, 'PS Rechnernetze und Internettechnik - Group 3', 'Niko Rossberg', 3, 25, 90, true, true, false, '703034', -43, -3, -95, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-76, 'PS Rechnernetze und Internettechnik - Group 4', 'Niko Rossberg', 3, 25, 90, true, true, false, '703034', -43, -3, -96, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-77, 'PS Rechnernetze und Internettechnik - Group 5', 'Niko Rossberg', 3, 25, 90, true, true, false, '703034', -47, -3, -97, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-78, 'PS Rechnernetze und Internettechnik - Group 6', 'Niko Rossberg', 3, 25, 90, true, true, false, '703034', -49, -3, -98, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-79, 'PS Rechnernetze und Internettechnik - Group 7', 'Niko Rossberg', 3, 25, 90, true, true, false, '703034', -45, -3, -99, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-80, 'PS Rechnernetze und Internettechnik - Group 8', 'Niko Rossberg', 3, 25, 90, true, true, false, '703034', -47, -3, -100, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-81, 'VO Daten und Wahrscheinlichkeiten', 'Adam Jatowt', 3, 150, 120, false, true, false, '703067', -36, -3, -101, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-82, 'PS Daten und Wahrscheinlichkeiten - Group 1', 'Nadja Gruber', 3, 25, 45, true, true, false, '703068', -41, -3, -102, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-83, 'PS Daten und Wahrscheinlichkeiten - Group 2', 'Nadja Gruber', 3, 25, 45, true, true, false, '703068', -41, -3, -103, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-84, 'PS Daten und Wahrscheinlichkeiten - Group 3', 'Nadja Gruber', 3, 25, 45, true, true, false, '703068', -42, -3, -104, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-85, 'PS Daten und Wahrscheinlichkeiten - Group 4', 'Nadja Gruber', 3, 25, 45, true, true, false, '703068', -43, -3, -105, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-86, 'PS Daten und Wahrscheinlichkeiten - Group 5', 'Nadja Gruber', 3, 25, 45, true, true, false, '703068', -46, -3, -106, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-87, 'PS Daten und Wahrscheinlichkeiten - Group 6', 'Nadja Gruber', 3, 25, 45, true, true, false, '703068', -47, -3, -107, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-88, 'VO Diskrete Strukturen', 'Markus Eberl', 3, 150, 120, false, true, false, '703069', -38, -3, -108, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-89, 'PS Diskrete Strukturen - Group 1', 'Charlie Sheen', 3, 25, 45, false, true, false, '703070', -40, -3, -109, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-90, 'PS Diskrete Strukturen - Group 2', 'Charlie Sheen', 3, 25, 45, false, true, false, '703070', -40, -3, -110, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-91, 'PS Diskrete Strukturen - Group 3', 'Charlie Sheen', 3, 25, 45, false, true, false, '703070', -40, -3, -111, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-92, 'PS Diskrete Strukturen - Group 4', 'Charlie Sheen', 3, 25, 45, false, true, false, '703070', -40, -3, -112, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-93, 'PS Diskrete Strukturen - Group 5', 'Charlie Sheen', 3, 25, 45, false, true, false, '703070', -40, -3, -113, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-94, 'PS Diskrete Strukturen - Group 6', 'Charlie Sheen', 3, 25, 45, false, true, false, '703070', -40, -3, -114, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-95, 'PS Diskrete Strukturen - Group 7', 'Charlie Sheen', 3, 25, 45, false, true, false, '703070', -40, -3, -115, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-96, 'VO Softwarearchitektur', 'Clemens Sauerwein', 3, 150, 180, false, true, false, '703071', -38, -3, -116, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-97, 'PS Softwarearchitektur - Group 1', 'Michael Breu', 3, 25, 90, true, true, false, '703072', -42, -3, -117, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-98, 'PS Softwarearchitektur - Group 2', 'Michael Breu', 3, 25, 90, true, true, false, '703072', -47, -3, -118, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-99, 'PS Softwarearchitektur - Group 3', 'Michael Breu', 3, 25, 90, true, true, false, '703072', -49, -3, -119, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-100, 'PS Softwarearchitektur - Group 4', 'Michael Breu', 3, 25, 90, true, true, false, '703072', -44, -3, -120, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-101, 'PS Softwarearchitektur - Group 5', 'Michael Breu', 3, 25, 90, true, true, false, '703072', -46, -3, -121, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-102, 'PS Softwarearchitektur - Group 6', 'Michael Breu', 3, 25, 90, true, true, false, '703072', -46, -3, -122, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-103, 'PS Softwarearchitektur - Group 7', 'Michael Breu', 3, 25, 90, true, true, false, '703072', -46, -3, -123, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-104, 'VO Logik', 'Aart Middeldorp', 4, 150, 180, false, true, false, '703026', -35, -3, -124, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-105, 'PS Logik - Group 1', 'Fabian Mitterwallner', 4, 25, 90, false, true, false, '703027', -50, -3, -125, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-106, 'PS Logik - Group 2', 'Fabian Mitterwallner', 4, 25, 90, false, true, false, '703027', -51, -3, -126, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-107, 'PS Logik - Group 3', 'Fabian Mitterwallner', 4, 25, 90, false, true, false, '703027', -52, -3, -127, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-108, 'PS Logik - Group 4', 'Fabian Mitterwallner', 4, 25, 90, false, true, false, '703027', -52, -3, -128, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-109, 'PS Logik - Group 5', 'Fabian Mitterwallner', 4, 25, 90, false, true, false, '703027', -55, -3, -129, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-110, 'PS Logik - Group 6', 'Fabian Mitterwallner', 4, 25, 90, false, true, false, '703027', -55, -3, -130, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-111, 'VO Maschinelles Lernen', 'Samuelle Tosatto', 4, 150, 180, false, true, false, '703075', -37, -3, -131, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-112, 'PS Maschinelles Lernen - Group 1', 'Sayantan Auddy', 4, 25, 90, true, true, false, '703076', -42, -3, -132, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-113, 'PS Maschinelles Lernen - Group 2', 'Sayantan Auddy', 4, 25, 90, true, true, false, '703076', -47, -3, -133, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-114, 'PS Maschinelles Lernen - Group 3', 'Sayantan Auddy', 4, 25, 90, true, true, false, '703076', -49, -3, -134, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-115, 'PS Maschinelles Lernen - Group 4', 'Sayantan Auddy', 4, 25, 90, true, true, false, '703076', -44, -3, -135, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-116, 'PS Maschinelles Lernen - Group 5', 'Sayantan Auddy', 4, 25, 90, true, true, false, '703076', -46, -3, -136, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-117, 'PS Maschinelles Lernen - Group 6', 'Sayantan Auddy', 4, 25, 90, true, true, false, '703076', -46, -3, -137, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-118, 'VO Parallele Programmierung', 'Thomas Fahringer', 4, 150, 120, false, true, false, '703077', -37, -3, -138, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-119, 'PS Parallele Programmierung - Group 1', 'Simon Lechner', 4, 25, 45, true, true, false, '703078', -41, -3, -139, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-120, 'PS Parallele Programmierung - Group 2', 'Simon Lechner', 4, 25, 45, true, true, false, '703078', -41, -3, -140, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-121, 'PS Parallele Programmierung - Group 3', 'Simon Lechner', 4, 25, 45, true, true, false, '703078', -48, -3, -141, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-122, 'PS Parallele Programmierung - Group 4', 'Simon Lechner', 4, 25, 45, true, true, false, '703078', -42, -3, -142, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-123, 'PS Parallele Programmierung - Group 5', 'Simon Lechner', 4, 25, 45, true, true, false, '703078', -47, -3, -143, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-124, 'PS Parallele Programmierung - Group 6', 'Simon Lechner', 4, 25, 45, true, true, false, '703078', -46, -3, -144, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-125, 'PS Parallele Programmierung - Group 7', 'Simon Lechner', 4, 25, 45, true, true, false, '703078', -43, -3, -145, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-126, 'VO Software Engineering', 'Ruth Breu', 4, 150, 120, false, true, false, '703079', -35, -3, -146, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-127, 'PS Software Engineering - Group 1', 'Anna Jäger', 4, 25, 90, true, true, false, '703080', -42, -3, -147, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-128, 'PS Software Engineering - Group 2', 'Anna Jäger', 4, 25, 90, true, true, false, '703080', -47, -3, -148, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-129, 'PS Software Engineering - Group 3', 'Anna Jäger', 4, 25, 90, true, true, false, '703080', -49, -3, -149, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-130, 'PS Software Engineering - Group 4', 'Anna Jäger', 4, 25, 90, true, true, false, '703080', -44, -3, -150, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-131, 'PS Software Engineering - Group 5', 'Anna Jäger', 4, 25, 90, true, true, false, '703080', -46, -3, -151, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-132, 'PS Software Engineering - Group 6', 'Anna Jäger', 4, 25, 90, true, true, false, '703080', -46, -3, -152, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-133, 'PS Software Engineering - Group 7', 'Anna Jäger', 4, 25, 90, true, true, false, '703080', -46, -3, -153, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-134, 'VU Einführung in das wissenschaftliche Arbeiten', 'Thomas Fahringer', 4, 25, 90, true, true, false, '703073', -46, -3, -154, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-135, 'VO Verteilte Systeme', 'Juan Aznar Poveda', 5, 150, 180, false, true, false, '703087', -37, -3, -155, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-136, 'PS Verteilte Systeme - Group 1', 'Juan Aznar Poveda', 5, 25, 90, true, true, false, '703088', -42, -3, -156, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-137, 'PS Verteilte Systeme - Group 2', 'Juan Aznar Poveda', 5, 25, 90, true, true, false, '703088', -47, -3, -157, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-138, 'PS Verteilte Systeme - Group 3', 'Juan Aznar Poveda', 5, 25, 90, true, true, false, '703088', -44, -3, -158, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-139, 'PS Verteilte Systeme - Group 4', 'Juan Aznar Poveda', 5, 25, 90, true, true, false, '703088', -46, -3, -159, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-140, 'PS Verteilte Systeme - Group 5', 'Juan Aznar Poveda', 5, 25, 90, true, true, false, '703088', -46, -3, -160, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-141, 'PS Verteilte Systeme - Group 6', 'Juan Aznar Poveda', 5, 25, 90, true, true, false, '703088', -48, -3, -161, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-142, 'PS Verteilte Systeme - Group 7', 'Juan Aznar Poveda', 5, 25, 90, true, true, false, '703088', -48, -3, -162, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-143, 'VO Visual Computing', 'Michael Harders', 5, 150, 180, false, true, false, '703089', -35, -3, -163, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-144, 'PS Visual Computing - Group 1', 'Niko Rauch', 5, 25, 90, true, true, false, '703090', -42, -3, -164, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-145, 'PS Visual Computing - Group 2', 'Niko Rauch', 5, 25, 90, true, true, false, '703090', -48, -3, -165, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-146, 'PS Visual Computing - Group 3', 'Niko Rauch', 5, 25, 90, true, true, false, '703090', -49, -3, -166, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-147, 'PS Visual Computing - Group 4', 'Niko Rauch', 5, 25, 90, true, true, false, '703090', -44, -3, -167, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-148, 'PS Visual Computing - Group 5', 'Niko Rauch', 5, 25, 90, true, true, false, '703090', -45, -3, -168, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-149, 'PS Visual Computing - Group 6', 'Niko Rauch', 5, 25, 90, true, true, false, '703090', -45, -3, -169, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-150, 'VU Einführung in die Robotik', 'Samuelle Tosatto', 5, 25, 180, false, true, false, '703135', -55, -3, -170, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-151, 'VU Termersetzungssysteme', 'Fabian Mitterwallner', 5, 35, 180, false, true, false, '703141', -40, -3, -171, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-152, 'VU Deep Learning', 'Antonio Jose Rodriguez-Sanchez', 5, 25, 180, true, true, false, '703134', -51, -3, -172, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-153, 'VU Mensch Maschine Interaktion', 'Pascal Knierim', 5, 25, 180, false, true, false, '703136', -51, -3, -173, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-154, 'VO Ethische Aspekte der Informatik', 'Wilhelm Guggenberger', 5, 150, 90, false, true, false, '703042', -36, -3, -174, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-155, 'SE Vertiefunsseminar', 'Günther Specht', 6, 25, 90, false, true, false, '703081', -55, -3, -175, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-156, 'SE Bachelorseminar', 'All Profs', 6, 40, 180, false, true, false, '703082', -51, -3, -176, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-157, 'VU Prinzipien von Blockchain-Systemen', 'Rainer Böhme', 6, 25, 180, false, true, false, '703133', -53, -3, -177, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-158, 'VU Performance-oriented Computing', 'Peter Thoman', 6, 25, 180, true, true, false, '703146', -52, -3, -178, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-159, 'VO Programmverifikation', 'Rene Thiemann', 6, 150, 180, false, true, false, '703083', -36, -3, -179, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-160, 'PS Programmverifikation - Group 1', 'Rene Thiemann', 6, 25, 90, false, true, false, '703084', -53, -3, -180, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-161, 'PS Programmverifikation - Group 2', 'Rene Thiemann', 6, 25, 90, false, true, false, '703084', -53, -3, -181, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-162, 'PS Programmverifikation - Group 3', 'Rene Thiemann', 6, 25, 90, false, true, false, '703084', -52, -3, -182, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-163, 'PS Programmverifikation - Group 4', 'Rene Thiemann', 6, 25, 90, false, true, false, '703084', -52, -3, -183, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-164, 'PS Programmverifikation - Group 5', 'Rene Thiemann', 6, 25, 90, false, true, false, '703084', -55, -3, -184, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-165, 'PS Programmverifikation - Group 6', 'Rene Thiemann', 6, 25, 90, false, true, false, '703084', -55, -3, -185, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-166, 'PS Programmverifikation - Group 7', 'Rene Thiemann', 6, 25, 90, false, true, false, '703084', -56, -3, -186, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-167, 'VO Softwarequalität', 'Harald Victor Schweiger', 6, 150, 180, false, true, false, '703085', -35, -3, -187, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-168, 'PS Softwarequalität - Group 1', 'Harald Victor Schweiger', 6, 25, 90, false, true, false, '703086', -39, -3, -188, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-169, 'PS Softwarequalität - Group 2', 'Harald Victor Schweiger', 6, 25, 90, false, true, false, '703086', -40, -3, -189, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-170, 'PS Softwarequalität - Group 3', 'Harald Victor Schweiger', 6, 25, 90, false, true, false, '703086', -40, -3, -190, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-171, 'PS Softwarequalität - Group 4', 'Harald Victor Schweiger', 6, 25, 90, false, true, false, '703086', -40, -3, -191, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-172, 'PS Softwarequalität - Group 5', 'Harald Victor Schweiger', 6, 25, 90, false, true, false, '703086', -40, -3, -192, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-173, 'PS Softwarequalität - Group 6', 'Harald Victor Schweiger', 6, 25, 90, false, true, false, '703086', -50, -3, -193, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-174, 'VO Informationstheorie und Kryptologie', 'Arne Dür', 6, 150, 120, false, true, false, '702631', -38, -3, -194, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-175, 'PS Informationstheorie und Kryptologie - Group 1', 'Arne Dür', 6, 25, 45, false, true, false, '702632', -39, -3, -195, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-176, 'PS Informationstheorie und Kryptologie - Group 2', 'Arne Dür', 6, 25, 45, false, true, false, '702632', -39, -3, -196, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-177, 'PS Informationstheorie und Kryptologie - Group 3', 'Arne Dür', 6, 25, 45, false, true, false, '702632', -39, -3, -197, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-178, 'PS Informationstheorie und Kryptologie - Group 4', 'Arne Dür', 6, 25, 45, false, true, false, '702632', -39, -3, -198, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-179, 'PS Informationstheorie und Kryptologie - Group 5', 'Arne Dür', 6, 25, 45, false, true, false, '702632', -39, -3, -199, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-180, 'PS Informationstheorie und Kryptologie - Group 6', 'Arne Dür', 6, 25, 45, false, true, false, '702632', -39, -3, -200, 0, TRUE);

-- courseSessions of timeTable4

INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-335, 'VO Einführung in die Programmierung', 'Michael Felderer', 1, 300, 180, false, false, false, '703003', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-336, 'PS Einführung in die Programmierung - Group 1', 'Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-337, 'PS Einführung in die Programmierung - Group 2', 'Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-338, 'PS Einführung in die Programmierung - Group 3', 'Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-339, 'PS Einführung in die Programmierung - Group 4', 'Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-340, 'PS Einführung in die Programmierung - Group 5', 'Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-341, 'PS Einführung in die Programmierung - Group 6', 'Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-342, 'PS Funktionale Programmierung - Group 1', 'Rene Thiemann', 1, 25, 45, true, false, false, '703025', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-343, 'PS Funktionale Programmierung - Group 2', 'Rene Thiemann', 1, 25, 45, true, false, false, '703025', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-344, 'PS Funktionale Programmierung - Group 3', 'Rene Thiemann', 1, 25, 45, true, false, false, '703025', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-345, 'PS Funktionale Programmierung - Group 4', 'Rene Thiemann', 1, 25, 45, true, false, false, '703025', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-346, 'PS Funktionale Programmierung - Group 5', 'Rene Thiemann', 1, 25, 45, true, false, false, '703025', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-347, 'PS Funktionale Programmierung - Group 6', 'Rene Thiemann', 1, 25, 45, true, false, false, '703025', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-348, 'VO Funktionale Programmierung', 'Rene Thiemann', 1, 300, 120, false, false, false, '703024', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-349, 'PS Rechnerarchitektur - Group 1', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-350, 'PS Rechnerarchitektur - Group 2', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-351, 'PS Rechnerarchitektur - Group 3', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-352, 'PS Rechnerarchitektur - Group 4', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-353, 'PS Rechnerarchitektur - Group 5', 'Rainer Böhme', 1, 25, 45, false, false, false, '703063', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-354, 'VO Rechnerarchitektur', 'Rainer Böhme', 1, 300, 120, false, false, false, '703064', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-355, 'PS Einführung in die theoretische Informatik', 'Jamie Hochrainer', 1, 25, 45, false, false, false, '703062', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-356, 'VO Einführung in die theoretische Informatik', 'Georg Moser', 1, 300, 120, false, false, false, '703007', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-357, 'PS Lineare Algebra - Group 1', 'Tim Netzer', 1, 25, 45, false, false, false, '703098', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-358, 'PS Lineare Algebra - Group 2', 'Tim Netzer', 1, 25, 45, false, false, false, '703098', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-359, 'PS Lineare Algebra - Group 3', 'Tim Netzer', 1, 25, 45, false, false, false, '703098', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-360, 'VO Lineare Algebra', 'Tim Netzer', 1, 300, 120, false, false, false, '703099', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-361, 'VO Datenbanksysteme', 'Günther Specht', 3, 150, 180, false, false, false, '703020', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-362, 'PS Datenbanksysteme - Group 1', 'David Stern', 3, 25, 90, true, false, false, '703021', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-363, 'PS Datenbanksysteme - Group 2', 'David Stern', 3, 25, 90, true, false, false, '703021', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-364, 'PS Datenbanksysteme - Group 3', 'David Stern', 3, 25, 90, true, false, false, '703021', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-365, 'PS Datenbanksysteme - Group 4', 'David Stern', 3, 25, 90, true, false, false, '703021', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-366, 'PS Datenbanksysteme - Group 5', 'David Stern', 3, 25, 90, true, false, false, '703021', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-367, 'VO Rechnernetze und Internettechnik', 'Jan Beutel', 3, 150, 180, false, false, false, '703033', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-368, 'PS Rechnernetze und Internettechnik - Group 1', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-369, 'PS Rechnernetze und Internettechnik - Group 2', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-370, 'PS Rechnernetze und Internettechnik - Group 3', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-371, 'PS Rechnernetze und Internettechnik - Group 4', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-372, 'PS Rechnernetze und Internettechnik - Group 5', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-373, 'PS Rechnernetze und Internettechnik - Group 6', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-374, 'PS Rechnernetze und Internettechnik - Group 7', 'Niko Rossberg', 3, 25, 90, true, false, false, '703034', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-375, 'VO Daten und Wahrscheinlichkeiten', 'Adam Jatowt', 3, 150, 120, false, false, false, '703067', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-376, 'PS Daten und Wahrscheinlichkeiten - Group 1', 'Nadja Gruber', 3, 25, 45, true, false, false, '703068', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-377, 'PS Daten und Wahrscheinlichkeiten - Group 2', 'Nadja Gruber', 3, 25, 45, true, false, false, '703068', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-378, 'PS Daten und Wahrscheinlichkeiten - Group 3', 'Nadja Gruber', 3, 25, 45, true, false, false, '703068', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-379, 'VO Diskrete Strukturen', 'Markus Eberl', 3, 150, 120, false, false, false, '703069', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-380, 'PS Diskrete Strukturen - Group 1', 'Charlie Sheen', 3, 25, 45, false, false, false, '703070', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-381, 'PS Diskrete Strukturen - Group 2', 'Charlie Sheen', 3, 25, 45, false, false, false, '703070', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-382, 'PS Diskrete Strukturen - Group 3', 'Charlie Sheen', 3, 25, 45, false, false, false, '703070', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-383, 'PS Diskrete Strukturen - Group 4', 'Charlie Sheen', 3, 25, 45, false, false, false, '703070', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-384, 'VO Softwarearchitektur', 'Clemens Sauerwein', 3, 150, 180, false, false, false, '703071', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-385, 'PS Softwarearchitektur - Group 1', 'Michael Breu', 3, 25, 90, true, false, false, '703072', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-386, 'PS Softwarearchitektur - Group 2', 'Michael Breu', 3, 25, 90, true, false, false, '703072', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-387, 'PS Softwarearchitektur - Group 3', 'Michael Breu', 3, 25, 90, true, false, false, '703072', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-388, 'PS Softwarearchitektur - Group 4', 'Michael Breu', 3, 25, 90, true, false, false, '703072', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-389, 'PS Softwarearchitektur - Group 5', 'Michael Breu', 3, 25, 90, true, false, false, '703072', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-390, 'VO Verteilte Systeme', 'Juan Aznar Poveda', 5, 150, 180, false, false, false, '703087', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-391, 'PS Verteilte Systeme - Group 1', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-392, 'PS Verteilte Systeme - Group 2', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-393, 'PS Verteilte Systeme - Group 3', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-394, 'PS Verteilte Systeme - Group 4', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-395, 'PS Verteilte Systeme - Group 5', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-396, 'PS Verteilte Systeme - Group 6', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-397, 'PS Verteilte Systeme - Group 7', 'Juan Aznar Poveda', 5, 25, 90, true, false, false, '703088', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-398, 'VO Visual Computing', 'Michael Harders', 5, 150, 180, false, false, false, '703089', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-399, 'PS Visual Computing - Group 1', 'Niko Rauch', 5, 25, 90, true, false, false, '703090', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-400, 'PS Visual Computing - Group 2', 'Niko Rauch', 5, 25, 90, true, false, false, '703090', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-401, 'PS Visual Computing - Group 3', 'Niko Rauch', 5, 25, 90, true, false, false, '703090', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-402, 'PS Visual Computing - Group 4', 'Niko Rauch', 5, 25, 90, true, false, false, '703090', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-403, 'PS Visual Computing - Group 5', 'Niko Rauch', 5, 25, 90, true, false, false, '703090', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-404, 'PS Visual Computing - Group 6', 'Niko Rauch', 5, 25, 90, true, false, false, '703090', NULL, -4, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-405, 'VU Einführung in die Robotik', 'Samuelle Tosatto', 5, 25, 180, false, false, false, '703135', NULL, -4, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-406, 'VU Termersetzungssysteme', 'Fabian Mitterwallner', 5, 35, 180, false, false, false, '703141', NULL, -4, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-407, 'VU Deep Learning', 'Antonio Jose Rodriguez-Sanchez', 5, 25, 180, true, false, false, '703134', NULL, -4, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-408, 'VU Mensch Maschine Interaktion', 'Pascal Knierim', 5, 25, 180, false, false, false, '703136', NULL, -4, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-409, 'VO Ethische Aspekte der Informatik', 'Wilhelm Guggenberger', 5, 150, 90, false, false, false, '703042', NULL, -4, NULL, 0, TRUE);

-- courseSessions of timeTable 5

INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-410, 'VO Einführung in die Programmierung - Split 1','Michael Felderer', 1, 300, 90, false, false, false, '703003', NULL, -5, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-411, 'PS Einführung in die Programmierung - Group 1','Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -5, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-412, 'PS Einführung in die Programmierung - Group 2','Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -5, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-413, 'PS Einführung in die Programmierung - Group 3','Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -5, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-414, 'PS Einführung in die Programmierung - Group 4','Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -5, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-415, 'PS Einführung in die Programmierung - Group 5','Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -5, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-416, 'PS Einführung in die Programmierung - Group 6','Lukas Kaltenbrunner', 1, 25, 90, true, false, false, '703004', NULL, -5, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-417, 'VO Datenbanksysteme - Split 1', 'Günther Specht', 3, 300, 90, false, false, false, '703020', NULL, -5, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-418, 'VO Datenbanksysteme - Split 2', 'Günther Specht', 3, 300, 90, false, false, false, '703020', NULL, -5, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-419, 'VO Einführung in die Programmierung - Split 2','Michael Felderer', 1, 300, 90, false, false, false, '703003', NULL, -5, NULL, 0, FALSE);

-- courseSessions of timeTable 6

INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-442, 'PS Einführung in die Programmierung - Group 1', 'Lukas Kaltenbrunner', 1, 25, 90, TRUE, FALSE, FALSE, '703004', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-443, 'PS Einführung in die Programmierung - Group 2', 'Lukas Kaltenbrunner', 1, 25, 90, TRUE, FALSE, FALSE, '703004', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-444, 'PS Einführung in die Programmierung - Group 3', 'Lukas Kaltenbrunner', 1, 25, 90, TRUE, FALSE, FALSE, '703004', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-445, 'PS Einführung in die Programmierung - Group 4', 'Lukas Kaltenbrunner', 1, 25, 90, TRUE, FALSE, FALSE, '703004', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-446, 'PS Einführung in die Programmierung - Group 5', 'Lukas Kaltenbrunner', 1, 25, 90, TRUE, FALSE, FALSE, '703004', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-447, 'PS Einführung in die Programmierung - Group 6', 'Lukas Kaltenbrunner', 1, 25, 90, TRUE, FALSE, FALSE, '703004', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-448, 'PS Einführung in die Programmierung - Group 7', 'Lukas Kaltenbrunner', 1, 25, 90, TRUE, FALSE, FALSE, '703004', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-449, 'PS Einführung in die Programmierung - Group 8', 'Lukas Kaltenbrunner', 1, 25, 90, TRUE, FALSE, FALSE, '703004', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-450, 'PS Einführung in die Programmierung - Group 9', 'Lukas Kaltenbrunner', 1, 25, 90, TRUE, FALSE, FALSE, '703004', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-451, 'PS Einführung in die Programmierung - Group 10', 'Lukas Kaltenbrunner', 1, 25, 90, TRUE, FALSE, FALSE, '703004', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-452, 'PS Funktionale Programmierung - Group 1', 'Rene Thiemann', 1, 25, 45, TRUE, FALSE, FALSE, '703025', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-453, 'PS Funktionale Programmierung - Group 2', 'Rene Thiemann', 1, 25, 45, TRUE, FALSE, FALSE, '703025', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-454, 'PS Funktionale Programmierung - Group 3', 'Rene Thiemann', 1, 25, 45, TRUE, FALSE, FALSE, '703025', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-455, 'PS Funktionale Programmierung - Group 4', 'Rene Thiemann', 1, 25, 45, TRUE, FALSE, FALSE, '703025', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-456, 'PS Funktionale Programmierung - Group 5', 'Rene Thiemann', 1, 25, 45, TRUE, FALSE, FALSE, '703025', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-457, 'PS Funktionale Programmierung - Group 6', 'Rene Thiemann', 1, 25, 45, TRUE, FALSE, FALSE, '703025', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-458, 'PS Funktionale Programmierung - Group 7', 'Rene Thiemann', 1, 25, 45, TRUE, FALSE, FALSE, '703025', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-459, 'PS Funktionale Programmierung - Group 8', 'Rene Thiemann', 1, 25, 45, TRUE, FALSE, FALSE, '703025', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-460, 'PS Funktionale Programmierung - Group 9', 'Rene Thiemann', 1, 25, 45, TRUE, FALSE, FALSE, '703025', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-461, 'PS Rechnerarchitektur - Group 1', 'Rainer Böhme', 1, 25, 45, FALSE, FALSE, FALSE, '703063', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-462, 'PS Rechnerarchitektur - Group 2', 'Rainer Böhme', 1, 25, 45, FALSE, FALSE, FALSE, '703063', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-463, 'PS Rechnerarchitektur - Group 3', 'Rainer Böhme', 1, 25, 45, FALSE, FALSE, FALSE, '703063', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-464, 'PS Rechnerarchitektur - Group 4', 'Rainer Böhme', 1, 25, 45, FALSE, FALSE, FALSE, '703063', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-465, 'PS Rechnerarchitektur - Group 5', 'Rainer Böhme', 1, 25, 45, FALSE, FALSE, FALSE, '703063', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-466, 'PS Rechnerarchitektur - Group 6', 'Rainer Böhme', 1, 25, 45, FALSE, FALSE, FALSE, '703063', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-467, 'PS Rechnerarchitektur - Group 7', 'Rainer Böhme', 1, 25, 45, FALSE, FALSE, FALSE, '703063', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-468, 'PS Rechnerarchitektur - Group 8', 'Rainer Böhme', 1, 25, 45, FALSE, FALSE, FALSE, '703063', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-469, 'PS Rechnerarchitektur - Group 9', 'Rainer Böhme', 1, 25, 45, FALSE, FALSE, FALSE, '703063', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-470, 'PS Einführung in die theoretische Informatik - Group 1', 'Jamie Hochrainer', 1, 25, 45, FALSE, FALSE, FALSE, '703062', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-471, 'PS Einführung in die theoretische Informatik - Group 2', 'Jamie Hochrainer', 1, 25, 45, FALSE, FALSE, FALSE, '703062', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-472, 'PS Einführung in die theoretische Informatik - Group 3', 'Jamie Hochrainer', 1, 25, 45, FALSE, FALSE, FALSE, '703062', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-473, 'PS Einführung in die theoretische Informatik - Group 4', 'Jamie Hochrainer', 1, 25, 45, FALSE, FALSE, FALSE, '703062', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-474, 'PS Einführung in die theoretische Informatik - Group 5', 'Jamie Hochrainer', 1, 25, 45, FALSE, FALSE, FALSE, '703062', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-475, 'PS Einführung in die theoretische Informatik - Group 6', 'Jamie Hochrainer', 1, 25, 45, FALSE, FALSE, FALSE, '703062', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-476, 'PS Einführung in die theoretische Informatik - Group 7', 'Jamie Hochrainer', 1, 25, 45, FALSE, FALSE, FALSE, '703062', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-477, 'PS Datenbanksysteme - Group 1', 'David Stern', 3, 25, 90, TRUE, FALSE, FALSE, '703021', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-478, 'PS Datenbanksysteme - Group 2', 'David Stern', 3, 25, 90, TRUE, FALSE, FALSE, '703021', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-479, 'PS Datenbanksysteme - Group 3', 'David Stern', 3, 25, 90, TRUE, FALSE, FALSE, '703021', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-480, 'PS Datenbanksysteme - Group 4', 'David Stern', 3, 25, 90, TRUE, FALSE, FALSE, '703021', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-481, 'PS Datenbanksysteme - Group 5', 'David Stern', 3, 25, 90, TRUE, FALSE, FALSE, '703021', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-482, 'PS Datenbanksysteme - Group 6', 'David Stern', 3, 25, 90, TRUE, FALSE, FALSE, '703021', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-483, 'PS Datenbanksysteme - Group 7', 'David Stern', 3, 25, 90, TRUE, FALSE, FALSE, '703021', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-484, 'PS Rechnernetze und Internettechnik - Group 1', 'Niko Rossberg', 3, 25, 90, TRUE, FALSE, FALSE, '703034', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-485, 'PS Rechnernetze und Internettechnik - Group 2', 'Niko Rossberg', 3, 25, 90, TRUE, FALSE, FALSE, '703034', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-486, 'PS Rechnernetze und Internettechnik - Group 3', 'Niko Rossberg', 3, 25, 90, TRUE, FALSE, FALSE, '703034', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-487, 'PS Rechnernetze und Internettechnik - Group 4', 'Niko Rossberg', 3, 25, 90, TRUE, FALSE, FALSE, '703034', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-488, 'PS Rechnernetze und Internettechnik - Group 5', 'Niko Rossberg', 3, 25, 90, TRUE, FALSE, FALSE, '703034', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-489, 'PS Rechnernetze und Internettechnik - Group 6', 'Niko Rossberg', 3, 25, 90, TRUE, FALSE, FALSE, '703034', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-490, 'PS Daten und Wahrscheinlichkeiten - Group 1', 'Nadja Gruber', 3, 25, 45, TRUE, FALSE, FALSE, '703068', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-491, 'PS Daten und Wahrscheinlichkeiten - Group 2', 'Nadja Gruber', 3, 25, 45, TRUE, FALSE, FALSE, '703068', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-492, 'PS Daten und Wahrscheinlichkeiten - Group 3', 'Nadja Gruber', 3, 25, 45, TRUE, FALSE, FALSE, '703068', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-493, 'PS Daten und Wahrscheinlichkeiten - Group 4', 'Nadja Gruber', 3, 25, 45, TRUE, FALSE, FALSE, '703068', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-494, 'PS Daten und Wahrscheinlichkeiten - Group 5', 'Nadja Gruber', 3, 25, 45, TRUE, FALSE, FALSE, '703068', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-495, 'PS Diskrete Strukturen - Group 1', 'Charlie Sheen', 3, 25, 45, FALSE, FALSE, FALSE, '703070', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-496, 'PS Diskrete Strukturen - Group 2', 'Charlie Sheen', 3, 25, 45, FALSE, FALSE, FALSE, '703070', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-497, 'PS Diskrete Strukturen - Group 3', 'Charlie Sheen', 3, 25, 45, FALSE, FALSE, FALSE, '703070', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-498, 'PS Diskrete Strukturen - Group 4', 'Charlie Sheen', 3, 25, 45, FALSE, FALSE, FALSE, '703070', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-499, 'PS Diskrete Strukturen - Group 5', 'Charlie Sheen', 3, 25, 45, FALSE, FALSE, FALSE, '703070', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-500, 'PS Softwarearchitektur - Group 1', 'Michael Breu', 3, 25, 90, TRUE, FALSE, FALSE, '703072', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-501, 'PS Softwarearchitektur - Group 2', 'Michael Breu', 3, 25, 90, TRUE, FALSE, FALSE, '703072', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-502, 'PS Softwarearchitektur - Group 3', 'Michael Breu', 3, 25, 90, TRUE, FALSE, FALSE, '703072', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-503, 'PS Softwarearchitektur - Group 4', 'Michael Breu', 3, 25, 90, TRUE, FALSE, FALSE, '703072', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-504, 'PS Softwarearchitektur - Group 5', 'Michael Breu', 3, 25, 90, TRUE, FALSE, FALSE, '703072', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-505, 'PS Softwarearchitektur - Group 6', 'Michael Breu', 3, 25, 90, TRUE, FALSE, FALSE, '703072', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-506, 'PS Softwarearchitektur - Group 7', 'Michael Breu', 3, 25, 90, TRUE, FALSE, FALSE, '703072', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-507, 'PS Verteilte Systeme - Group 1', 'Juan Aznar Poveda', 5, 25, 90, TRUE, FALSE, FALSE, '703088', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-508, 'PS Verteilte Systeme - Group 2', 'Juan Aznar Poveda', 5, 25, 90, TRUE, FALSE, FALSE, '703088', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-509, 'PS Verteilte Systeme - Group 3', 'Juan Aznar Poveda', 5, 25, 90, TRUE, FALSE, FALSE, '703088', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-510, 'PS Visual Computing - Group 1', 'Niko Rauch', 5, 25, 90, TRUE, FALSE, FALSE, '703090', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-511, 'PS Visual Computing - Group 2', 'Niko Rauch', 5, 25, 90, TRUE, FALSE, FALSE, '703090', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-512, 'PS Visual Computing - Group 3', 'Niko Rauch', 5, 25, 90, TRUE, FALSE, FALSE, '703090', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-513, 'PS Aktuelle Themen der Informatik - Group 1', 'Georg Moser', 1, 30, 90, FALSE, FALSE, FALSE, '703301', NULL, -6, NULL, 1, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-514, 'PS Aktuelle Themen der Informatik - Group 2', 'Georg Moser', 1, 30, 90, FALSE, FALSE, FALSE, '703301', NULL, -6, NULL, 1, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-515, 'PS Automaten und Logik', 'Johannes Niederhauser', 1, 30, 150, FALSE, FALSE, FALSE, '703303', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-516, 'PS Kryptographie', 'Arnab Roy', 1, 25, 90, FALSE, FALSE, FALSE, '703307', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-517, 'PS Hochleistungsrechnen', 'Philipp Gschwandter', 1, 25, 90, TRUE, FALSE, FALSE, '703309', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-518, 'PS Optimierung und numerische Berechnung', 'Marcel Ritter', 1, 25, 90, TRUE, FALSE, FALSE, '703111', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-519, 'PS Signalverarbeitung und algorithmische Geometrie', 'Muthukumar Pandaram', 1, 25, 90, TRUE, FALSE, FALSE, '703113', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-520, 'PS Softwareentwurf und Softwareentwicklungsprozesse - Group 1', 'Melanie Ernst', 1, 25, 90, FALSE, FALSE, FALSE, '703501', NULL, -6, NULL, 2, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-521, 'PS Softwareentwurf und Softwareentwicklungsprozesse - Group 2', 'Melanie Ernst', 1, 25, 90, FALSE, FALSE, FALSE, '703501', NULL, -6, NULL, 2, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-522, 'PS Software Security Engineering - Group 1', 'Rainer Böhme', 1, 25, 90, FALSE, FALSE, FALSE, '703503', NULL, -6, NULL, 2, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-523, 'PS Software Security Engineering - Group 2', 'Rainer Böhme', 1, 25, 90, FALSE, FALSE, FALSE, '703503', NULL, -6, NULL, 2, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-524, 'PS Data Engineering und Analytics - Group 1', 'Amir Reza Mohammadi', 1, 25, 90, FALSE, FALSE, FALSE, '703505', NULL, -6, NULL, 2, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-525, 'PS Data Engineering und Analytics - Group 2', 'Amir Reza Mohammadi', 1, 25, 90, FALSE, FALSE, FALSE, '703505', NULL, -6, NULL, 2, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-526, 'PS Forschungsmethoden des Software Engineering - Group 1', 'Valentina Golendukhina', 1, 25, 180, FALSE, FALSE, FALSE, '703507', NULL, -6, NULL, 2, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-527, 'PS Forschungsmethoden des Software Engineering - Group 2', 'Valentina Golendukhina', 1, 25, 180, FALSE, FALSE, FALSE, '703507', NULL, -6, NULL, 2, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-528, 'VU Data-Intensive Applications', 'Sashko Ristov', 3, 25, 180, TRUE, FALSE, FALSE, '703518', NULL, -6, NULL, 2, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-529, 'SE Forschungsseminar verteiltes Rechnen', 'Thomas Fahringer', 3, 25, 90, TRUE, FALSE, FALSE, '703325', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-530, 'SE Forschungsseminar in Wahrnehmung, Interaktion und Robotik', 'Matthias Harders', 3, 25, 90, FALSE, FALSE, FALSE, '703331', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-531, 'SE Forschungsseminar Knowledge Graphs', 'Dieter Fensel', 3, 25, 90, FALSE, FALSE, FALSE, '703346', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-532, 'SE Masterseminar Software Engineering', 'Ruth Breu', 3, 50, 150, FALSE, FALSE, FALSE, '703512', NULL, -6, NULL, 2, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-533, 'SE Vorbereitung der Masterarbeit', 'Michael Vierhauser', 3, 50, 105, FALSE, FALSE, FALSE, '703517', NULL, -6, NULL, 2, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-534, 'SE Vertiefunsseminar', 'Günther Specht', 6, 25, 90, FALSE, FALSE, FALSE, '703081', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-535, 'VO Einführung in die Programmierung', 'Michael Felderer', 1, 300, 180, FALSE, FALSE, FALSE, '703003', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-536, 'VO Funktionale Programmierung', 'Rene Thiemann', 1, 300, 120, FALSE, FALSE, FALSE, '703024', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-537, 'VO Rechnerarchitektur', 'Rainer Böhme', 1, 300, 120, FALSE, FALSE, FALSE, '703064', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-538, 'VO Einführung in die theoretische Informatik', 'Georg Moser', 1, 300, 120, FALSE, FALSE, FALSE, '703007', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-539, 'VO Datenbanksysteme', 'Günther Specht', 3, 150, 180, FALSE, FALSE, FALSE, '703020', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-540, 'VO Rechnernetze und Internettechnik', 'Jan Beutel', 3, 150, 180, FALSE, FALSE, FALSE, '703033', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-541, 'VO Daten und Wahrscheinlichkeiten', 'Adam Jatowt', 3, 150, 120, FALSE, FALSE, FALSE, '703067', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-542, 'VO Diskrete Strukturen', 'Markus Eberl', 3, 150, 120, FALSE, FALSE, FALSE, '703069', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-543, 'VO Softwarearchitektur', 'Clemens Sauerwein', 3, 150, 180, FALSE, FALSE, FALSE, '703071', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-544, 'VO Verteilte Systeme', 'Juan Aznar Poveda', 5, 150, 180, FALSE, FALSE, FALSE, '703087', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-545, 'VO Visual Computing', 'Michael Harders', 5, 150, 180, FALSE, FALSE, FALSE, '703089', NULL, -6, NULL, 0, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-546, 'VO Ethische Aspekte der Informatik', 'Wilhelm Guggenberger', 5, 150, 90, FALSE, FALSE, FALSE, '703042', NULL, -6, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-547, 'VO Aktuelle Themen der Informatik', 'Georg Moser', 1, 60, 90, FALSE, FALSE, FALSE, '703300', NULL, -6, NULL, 1, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-548, 'VO Automaten und Logik', 'Aart Middeldorp', 1, 30, 150, FALSE, FALSE, FALSE, '703302', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-549, 'VO Kryptographie', 'Arnab Roy', 1, 25, 90, FALSE, FALSE, FALSE, '703306', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-550, 'VO Hochleistungsrechnen', 'Philipp Gschwandter', 1, 25, 105, FALSE, FALSE, FALSE, '703308', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-551, 'VO Optimierung und numerische Berechnung', 'Matthias Harders', 1, 25, 90, FALSE, FALSE, FALSE, '703110', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-552, 'VO Signalverarbeitung und algorithmische Geometrie', 'Marcel Ritter', 1, 25, 90, FALSE, FALSE, FALSE, '703112', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-553, 'VO Softwareentwurf und Softwareentwicklungsprozesse', 'Philipp Zech', 1, 50, 90, FALSE, FALSE, FALSE, '703500', NULL, -6, NULL, 2, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-554, 'VO Software Security Engineering', 'Rainer Böhme', 1, 50, 90, FALSE, FALSE, FALSE, '703502', NULL, -6, NULL, 2, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-555, 'VO Data Engineering und Analytics', 'Eva Zangerle', 1, 50, 90, FALSE, FALSE, FALSE, '703504', NULL, -6, NULL, 2, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-556, 'VO Forschungsmethoden des Software Engineering', 'Valentina Golendukhina', 1, 50, 90, FALSE, FALSE, FALSE, '703506', NULL, -6, NULL, 2, FALSE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-557, 'VU Einführung in die Robotik', 'Samuelle Tosatto', 5, 25, 180, FALSE, FALSE, FALSE, '703135', NULL, -6, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-558, 'VU Termersetzungssysteme', 'Fabian Mitterwallner', 5, 35, 180, FALSE, FALSE, FALSE, '703141', NULL, -6, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-559, 'VU Deep Learning', 'Antonio Jose Rodriguez-Sanchez', 5, 25, 180, TRUE, FALSE, FALSE, '703134', NULL, -6, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-560, 'VU Mensch Maschine Interaktion', 'Pascal Knierim', 5, 25, 180, FALSE, FALSE, FALSE, '703136', NULL, -6, NULL, 0, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-561, 'VU A Program and Resource Analysis', 'Georg Moser', 3, 25, 150, FALSE, FALSE, FALSE, '703316', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-562, 'VU Tree Automata', 'Aart Middeldorp', 3, 25, 150, FALSE, FALSE, FALSE, '703608', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-563, 'VU Advanced C++', 'Peter Thoman', 3, 25, 150, FALSE, FALSE, FALSE, '703333', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-564, 'VU Advanced Distributed Systems', 'Zahra Najafabadi Samani', 3, 25, 150, FALSE, FALSE, FALSE, '703356', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-565, 'VU Distributed Applications in the Edge-Cloud Continuum', 'Sashko Ristov', 3, 25, 150, FALSE, FALSE, FALSE, '703357', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-566, 'VU Physikbasierte Simulation', 'Matthias Harders', 3, 25, 135, FALSE, FALSE, FALSE, '703327', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-567, 'VU Advanced Computer Vision', 'Antonio Jose Rodriguez-Sanchez', 3, 25, 150, FALSE, FALSE, FALSE, '703328', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-568, 'VU Machine Learning for Interactive Systems', 'Justus Piater', 3, 25, 150, FALSE, FALSE, FALSE, '703330', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-569, 'VU Low-power System Design', 'Jan Beutel', 3, 25, 150, FALSE, FALSE, FALSE, '703354', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-570, 'VU Knowledge Graphs', 'Dieter Fensel', 3, 25, 150, FALSE, FALSE, FALSE, '703345', NULL, -6, NULL, 1, TRUE);
INSERT INTO course_session(id,name,lecturer,semester,number_of_participants,duration,computers_necessary,is_assigned,is_fixed,course_id,room_table_id,time_table_id,timing_id,study_type,elective) VALUES (-571, 'VU Einführung in das wissenschaftliche Arbeiten', 'Thomas Fahringer', 4, 25, 90, TRUE, FALSE, FALSE, '703073', NULL, -6, NULL, 0, FALSE);
