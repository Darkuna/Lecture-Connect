-- users
INSERT INTO USERX (ENABLED,FIRST_NAME,LAST_NAME,PASSWORD,USERNAME,EMAIL,CREATE_USER_USERNAME,CREATE_DATE)VALUES(TRUE,'Admin','Istrator','$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6','admin','admin@memori.com','admin','2016-01-01 00:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES)VALUES ('admin', 'ADMIN')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES)VALUES ('admin', 'USER')
INSERT INTO USERX (ENABLED,FIRST_NAME,LAST_NAME,PASSWORD,USERNAME,EMAIL,CREATE_USER_USERNAME,CREATE_DATE)VALUES(TRUE,'Susi','Kaufgern','$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6','user1','susi.kaufgern@memori.com','admin','2016-01-01 00:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES)VALUES ('user1', 'USER')
INSERT INTO USERX (ENABLED,FIRST_NAME,LAST_NAME,PASSWORD,USERNAME,EMAIL,CREATE_USER_USERNAME,CREATE_DATE)VALUES(TRUE,'Max','Mustermann','$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6','user2','max.mustermann@memori.com','admin','2016-01-01 00:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES)VALUES ('user2', 'USER')
INSERT INTO USERX (ENABLED,FIRST_NAME,LAST_NAME,PASSWORD,USERNAME,EMAIL,CREATE_USER_USERNAME,CREATE_DATE)VALUES(TRUE,'Elvis','The King','$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6','elvis','elvis@memori.com','elvis','2016-01-01 00:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES)VALUES ('elvis', 'ADMIN')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, CREATE_USER_USERNAME, CREATE_DATE) VALUES (TRUE, 'Sophia', 'Anderson', '$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6', 'sophia_anderson', 'sophia@example.com', 'admin', '2024-04-10 09:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('sophia_anderson', 'ADMIN')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, CREATE_USER_USERNAME, CREATE_DATE) VALUES (FALSE, 'Matthew', 'Clark', '$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6', 'matthew_clark', 'matthew@example.com', 'admin', '2024-05-15 10:30:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('matthew_clark', 'USER')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, CREATE_USER_USERNAME, CREATE_DATE) VALUES (TRUE, 'Emma', 'Walker', '$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6', 'emma_walker', 'emma@example.com', 'admin', '2024-06-20 11:45:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('emma_walker', 'USER')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, CREATE_USER_USERNAME, CREATE_DATE) VALUES (FALSE, 'Ethan', 'Allen', '$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6', 'ethan_allen', 'ethan@example.com', 'admin', '2024-07-25 12:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('ethan_allen', 'ADMIN')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, CREATE_USER_USERNAME, CREATE_DATE) VALUES (TRUE, 'Isabella', 'Young', '$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6', 'isabella_young', 'isabella@example.com', 'admin', '2024-08-30 13:15:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('isabella_young', 'USER')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, CREATE_USER_USERNAME, CREATE_DATE) VALUES (TRUE, 'Michael', 'Brown', '$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6', 'michael_brown_1', 'michael@example.com', 'admin', '2023-11-15 10:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('michael_brown_1', 'ADMIN')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, CREATE_USER_USERNAME, CREATE_DATE) VALUES (FALSE, 'Emily', 'Davis', '$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6', 'emily_davis_2', 'emily@example.com', 'admin', '2023-12-20 11:30:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('emily_davis_2', 'USER')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, CREATE_USER_USERNAME, CREATE_DATE) VALUES (TRUE, 'William', 'Martinez', '$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6', 'william_martinez_3', 'william@example.com', 'admin', '2024-01-25 13:45:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('william_martinez_3', 'ADMIN')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, CREATE_USER_USERNAME, CREATE_DATE) VALUES (FALSE, 'Olivia', 'Garcia', '$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6', 'olivia_garcia_4', 'olivia@example.com', 'admin', '2024-02-10 15:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('olivia_garcia_4', 'USER')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, CREATE_USER_USERNAME, CREATE_DATE) VALUES (TRUE, 'James', 'Rodriguez', '$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6', 'james_rodriguez_5', 'james@example.com', 'admin', '2024-03-05 16:15:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('james_rodriguez_5', 'ADMIN')

-- rooms (20)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('HS A', 450, FALSE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('HS B', 300, FALSE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('HS C', 300, FALSE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('HS D', 150, FALSE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('HS E', 150, FALSE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('HS F', 100, FALSE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('HS G', 50, FALSE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('Rechnerraum 20', 30, TRUE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('Rechnerraum 21', 30, TRUE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('Rechnerraum 22', 30, TRUE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('Rechnerraum 23', 30, TRUE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('Rechnerraum 24', 30, TRUE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('Rechnerraum 25', 30, TRUE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('Rechnerraum 26', 30, TRUE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('Rechnerraum 27', 30, TRUE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('Rechnerraum 28', 30, TRUE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('HSB 3', 30, FALSE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('HSB 5', 30, FALSE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('HSB 6', 30, FALSE)
INSERT INTO ROOM (ID,CAPACITY,COMPUTERS_AVAILABLE)VALUES('GROSSER HOERSAAL', 30, FALSE)

-- timeTable
INSERT INTO TIME_TABLE(ID, SEMESTER, ACADEMIC_YEAR) VALUES (-1, 0, 2023)

-- roomTables of the timeTable
INSERT INTO ROOM_TABLE(ID, ROOM_ID, TIME_TABLE_ID) VALUES (-1, 'HS A', -1)
INSERT INTO ROOM_TABLE(ID, ROOM_ID, TIME_TABLE_ID) VALUES (-2, 'HS B', -1)
INSERT INTO ROOM_TABLE(ID, ROOM_ID, TIME_TABLE_ID) VALUES (-3, 'HS C', -1)
INSERT INTO ROOM_TABLE(ID, ROOM_ID, TIME_TABLE_ID) VALUES (-4, 'Rechnerraum 20', -1)
INSERT INTO ROOM_TABLE(ID, ROOM_ID, TIME_TABLE_ID) VALUES (-5, 'Rechnerraum 21', -1)

-- courses (18 for semesters 1 and 2)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703003', 'Einführung in die Programmierung', 0, 'Michael Felderer', 1, 180, 300, FALSE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703004', 'Einführung in die Programmierung', 2, 'Lukas Kaltenbrunner', 1, 90, 25, TRUE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703025', 'Funktionale Programmierung', 2, 'Renee Thiemann', 1, 45, 25, TRUE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703024', 'Funktionale Programmierung', 0, 'Renee Thiemann', 1, 120, 300, FALSE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703063', 'Rechnerarchitektur', 2, 'Rainer Böhme', 1, 45, 25, FALSE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703064', 'Rechnerarchitektur', 0, 'Rainer Böhme', 1, 120, 300, FALSE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703062', 'Einführung in die theoretische Informatik', 4, 'Jamie Hochrainer', 1, 45, 25, FALSE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703007', 'Einführung in die theoretische Informatik', 0, 'Georg Moser', 1, 120, 300, FALSE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703098', 'Lineare Algebra', 2, 'Tim Netzer', 1, 45, 25, FALSE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703099', 'Lineare Algebra', 0, 'Tim Netzer', 1, 120, 300, FALSE)

INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703016', 'Programmiermethodik', 2, 'Simon Priller', 2, 90, 25, TRUE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703017', 'Programmiermethodik', 0, 'Lukas Kaltenbrunner', 2, 180, 300, FALSE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703066', 'Angewandte Mathematik für die Informatik', 2, 'Niko Rauch', 2, 90, 25, FALSE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703065', 'Angewandte Mathematik für die Informatik', 0, 'Michael Harders', 2, 180, 300, FALSE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703011', 'Algorithmen und Datenstrukturen', 2, 'Laurenz Wischounig', 2, 90, 25, FALSE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703010', 'Algorithmen und Datenstrukturen', 0, 'Justus Piater', 2, 180, 300, FALSE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703012', 'Betriebssysteme', 2, 'Markus Reiter', 2, 90, 25, TRUE)
INSERT INTO COURSE (ID, NAME, COURSE_TYPE, LECTURER, SEMESTER, DURATION, NUMBER_OF_PARTICIPANTS, COMPUTERS_NECESSARY)VALUES('703013', 'Betriebssysteme', 0, 'Univ.-Prof. Dipl.-Ing. Dr. Thomas Fahringer', 2, 180, 300, FALSE)

-- timingConstraints for roomTables (10)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-1, 0, '08:00:00', '10:00:00', NULL, -1)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-2, 1, '08:00:00', '10:00:00', NULL, -1)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-3, 0, '08:00:00', '10:00:00', NULL, -2)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-4, 0, '08:00:00', '10:00:00', NULL, -3)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-5, 0, '16:00:00', '18:00:00', NULL, -4)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-6, 1, '16:00:00', '18:00:00', NULL, -1)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-7, 2, '08:00:00', '10:00:00', NULL, -1)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-8, 3, '08:00:00', '10:00:00', NULL, -2)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-9, 4, '08:00:00', '10:00:00', NULL, -1)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-10, 2, '08:00:00', '10:00:00', NULL, -4)

-- timingConstraints for courses (10)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-11, 0, '08:00:00', '10:00:00', '703010', NULL)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-12, 1, '08:00:00', '10:00:00', '703011', NULL)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-13, 2, '08:00:00', '10:00:00', '703012', NULL)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-14, 3, '08:00:00', '10:00:00', '703013', NULL)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-15, 4, '08:00:00', '18:00:00', '703024', NULL)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-16, 1, '08:00:00', '18:00:00', '703025', NULL)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-17, 2, '12:00:00', '13:00:00', '703062', NULL)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-18, 3, '13:00:00', '14:00:00', '703098', NULL)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-19, 2, '09:00:00', '10:30:00', '703099', NULL)
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-20, 1, '12:00:00', '13:00:00', '703010', NULL)

-- timing for assigned courseSession
INSERT INTO TIMING(ID, DAY_OF_THE_WEEK, START_TIME, END_TIME, COURSE_ID, ROOM_TABLE_ID) VALUES (-21, 0, '08:00:00', '10:00:00', NULL , NULL)

-- courseSessions of the timeTable (all of them are currently unassigned)
INSERT INTO COURSE_SESSION(ID, DURATION, IS_ASSIGNED, IS_FIXED, COURSE_ID, ROOM_TABLE_ID, TIME_TABLE_ID, TIMING_ID) VALUES (-1, 180, FALSE, FALSE, '703003', NULL, -1, NULL)
INSERT INTO COURSE_SESSION(ID, DURATION, IS_ASSIGNED, IS_FIXED, COURSE_ID, ROOM_TABLE_ID, TIME_TABLE_ID, TIMING_ID) VALUES (-2, 90, FALSE, FALSE, '703004', NULL, -1, NULL)
INSERT INTO COURSE_SESSION(ID, DURATION, IS_ASSIGNED, IS_FIXED, COURSE_ID, ROOM_TABLE_ID, TIME_TABLE_ID, TIMING_ID) VALUES (-3, 120, FALSE, FALSE, '703024', NULL, -1, NULL)
INSERT INTO COURSE_SESSION(ID, DURATION, IS_ASSIGNED, IS_FIXED, COURSE_ID, ROOM_TABLE_ID, TIME_TABLE_ID, TIMING_ID) VALUES (-4, 45, FALSE, FALSE, '703025', NULL, -1, NULL)
INSERT INTO COURSE_SESSION(ID, DURATION, IS_ASSIGNED, IS_FIXED, COURSE_ID, ROOM_TABLE_ID, TIME_TABLE_ID, TIMING_ID) VALUES (-5, 45, FALSE, FALSE, '703063', NULL, -1, NULL)
INSERT INTO COURSE_SESSION(ID, DURATION, IS_ASSIGNED, IS_FIXED, COURSE_ID, ROOM_TABLE_ID, TIME_TABLE_ID, TIMING_ID) VALUES (-6, 120, TRUE, FALSE, '703064', -1, -1, -21)
INSERT INTO COURSE_SESSION(ID, DURATION, IS_ASSIGNED, IS_FIXED, COURSE_ID, ROOM_TABLE_ID, TIME_TABLE_ID, TIMING_ID) VALUES (-7, 45, FALSE, FALSE, '703062', NULL, -1, NULL)
INSERT INTO COURSE_SESSION(ID, DURATION, IS_ASSIGNED, IS_FIXED, COURSE_ID, ROOM_TABLE_ID, TIME_TABLE_ID, TIMING_ID) VALUES (-8, 120, FALSE, FALSE, '703007', NULL, -1, NULL)
INSERT INTO COURSE_SESSION(ID, DURATION, IS_ASSIGNED, IS_FIXED, COURSE_ID, ROOM_TABLE_ID, TIME_TABLE_ID, TIMING_ID) VALUES (-9, 45, FALSE, FALSE, '703098', NULL, -1, NULL)
INSERT INTO COURSE_SESSION(ID, DURATION, IS_ASSIGNED, IS_FIXED, COURSE_ID, ROOM_TABLE_ID, TIME_TABLE_ID, TIMING_ID) VALUES (-10, 120, FALSE, FALSE, '703099', NULL, -1, NULL)