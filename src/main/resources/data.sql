-- users
INSERT INTO USERX (ENABLED,FIRST_NAME,LAST_NAME,PASSWORD,USERNAME,EMAIL,CREATE_USER_USERNAME,CREATE_DATE)VALUES(TRUE,'Admin','Istrator','$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6','admin','admin@memori.com','admin','2016-01-01 00:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES)VALUES ('admin', 'ADMIN')
INSERT INTO USERX (ENABLED,FIRST_NAME,LAST_NAME,PASSWORD,USERNAME,EMAIL,CREATE_USER_USERNAME,CREATE_DATE)VALUES(TRUE,'Susi','Kaufgern','$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6','user1','susi.kaufgern@memori.com','admin','2016-01-01 00:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES)VALUES ('user1', 'USER')
INSERT INTO USERX (ENABLED,FIRST_NAME,LAST_NAME,PASSWORD,USERNAME,EMAIL,CREATE_USER_USERNAME,CREATE_DATE)VALUES(TRUE,'Max','Mustermann','$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6','user2','max.mustermann@memori.com','admin','2016-01-01 00:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES)VALUES ('user2', 'USER')
INSERT INTO USERX (ENABLED,FIRST_NAME,LAST_NAME,PASSWORD,USERNAME,EMAIL,CREATE_USER_USERNAME,CREATE_DATE)VALUES(TRUE,'Elvis','The King','$2a$10$TfySssuPsjcaILwgdB403O2lOCXTS8m1pLy6sONcXDS9xXmkD8dx6','elvis','elvis@memori.com','elvis','2016-01-01 00:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES)VALUES ('elvis', 'ADMIN')

-- decks
INSERT INTO Deck (deckid, name, description, is_active, is_public, owner_username) VALUES ('93ce273a-fd41-4d7e-9fe0-e5d9d13bc995', 'Empty deck', 'for testing','true', 'true', 'user1')
INSERT INTO Deck (deckid, name, description, is_active, is_public, owner_username) VALUES ('22a0c39d-a2b0-460d-9e39-4dc2db3e7b36', 'SOAR Deck', 'Software Architektur Karten','true', 'true', 'user1')
INSERT INTO Deck (deckid, name, description, is_active, is_public, owner_username) VALUES ('cb5e5b13-fd0e-4be0-a29e-7d26800e94bb', 'DISTR Deck', 'Diskrete Struklkturen Karten','true','false', 'user1')
INSERT INTO Deck (deckid, name, description, is_active, is_public, owner_username) VALUES ('9ec2dda8-3850-4ffd-bc25-fabc732adaec', 'RNIT DECK', 'Rechnernetze und Internettechnik Karten','true', 'true', 'user1')
INSERT INTO Deck (deckid, name, description, is_active, is_public, owner_username) VALUES ('19534ec0-bf13-4317-bb3d-91c29603b06c', 'DAWA Deck', 'Daten und Wahrscheinlichkeiten Karten','true', 'true', 'user2')
INSERT INTO Deck (deckid, name, description, is_active, is_public, owner_username) VALUES ('4af32892-5e68-49be-9c7c-84fa54140880', 'DABS Deck', 'Datenbanken Karten','false','false', 'user2')

-- cards
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('291771c2-b76f-411a-b54d-cd9cd0c27f9f', 'front test 1', 'back test 1', TRUE, '22a0c39d-a2b0-460d-9e39-4dc2db3e7b36')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('130fa75a-33ed-417f-bdc5-e4e34482ad20', 'front test 2', 'back test 2', TRUE, 'cb5e5b13-fd0e-4be0-a29e-7d26800e94bb')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('21fbafe5-ad3d-47da-9b13-9ab2d5d290b0', 'front test 3', 'back test 3', TRUE, '22a0c39d-a2b0-460d-9e39-4dc2db3e7b36')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('70811d0f-061b-425c-82d9-0d8c7209ee9a', 'Wie heißt das Design Pattern, welches es als push und pull Variante gibt?', 'Observer Pattern', TRUE, '22a0c39d-a2b0-460d-9e39-4dc2db3e7b36')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('3264bc82-0b4a-4578-82c8-22ba6d9c1126', 'Aus welchen Schichten besteht die 3-Schichten-Architektur?', 'Persistents-, Anwendungs- und Präsentationsschicht', TRUE, '22a0c39d-a2b0-460d-9e39-4dc2db3e7b36')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('07f39c5f-cfc8-4397-9668-e129e9bec267', 'Kann ein DAG Zyklen enthalten?', 'Nein', TRUE, 'cb5e5b13-fd0e-4be0-a29e-7d26800e94bb')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('61c49c72-de90-4523-9388-96b3e09e52d0', 'Welche Schritte gibt es bei der Induktion?', 'Basisfall, Induktionsschritt und Induktionshypothese', TRUE, 'cb5e5b13-fd0e-4be0-a29e-7d26800e94bb')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('4d9c0fc3-cba6-4b2d-a976-7a4f400154bd', 'Wie heißt der Graph, welcher in der VO vorgestellt wurde und der aus einem Pentagon und einem innen liegenden Stern besteht?', 'Petersen Graph', TRUE, 'cb5e5b13-fd0e-4be0-a29e-7d26800e94bb')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('23a92b3d-2ebe-445b-984e-000acd711689', 'Wie viele IPv6 Addresen gibt es?', '2^128', TRUE, '9ec2dda8-3850-4ffd-bc25-fabc732adaec')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('cb79d4ae-f853-439e-b62a-77b834103ee6', 'Wie viele IPv4 Addresen gibt es?', '2^32', TRUE, '9ec2dda8-3850-4ffd-bc25-fabc732adaec')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('93806103-8575-4142-89af-f6b99142ce59', 'Wie funktionert das Benotungssystem im Proseminar?', 'Keine Ahnung', TRUE, '9ec2dda8-3850-4ffd-bc25-fabc732adaec')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('ccaa1a7e-49ab-4d9a-9ffc-28c6f5403b32', 'Wofür steht die Abkürzung Mbps?', 'Mega Bit pro Sekunde', TRUE, '9ec2dda8-3850-4ffd-bc25-fabc732adaec')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('439371b7-5850-4da6-bf28-c89cdd93049c', 'Wie lautet die Formel für die bedingte Wahrscheinlichkeit?', 'P(A und B)/P(B)', TRUE, '19534ec0-bf13-4317-bb3d-91c29603b06c')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('e28b9fbb-22f7-454e-bee7-a6f9174479af', 'Sind Zufallsvariablen Ereignisse?', 'Nein', TRUE, '19534ec0-bf13-4317-bb3d-91c29603b06c')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('acd39f43-fe2c-4e05-b2a6-92fe143c0df2', 'Wie lautet die Formel für ordered sampling with replacement?', 'n^k', TRUE, '19534ec0-bf13-4317-bb3d-91c29603b06c')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('78cdd252-65ac-473c-89f0-905032670257', 'Wofür steht SQL?', 'Structured Query Language', TRUE, '4af32892-5e68-49be-9c7c-84fa54140880')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('9590d3bf-d514-4946-8942-04c46b27756f', 'Was ist die Vorraussetzung für die 1.NF', 'Jedes Element eines Tables muss atomar sein', TRUE, '4af32892-5e68-49be-9c7c-84fa54140880')


-- userDeckData
INSERT INTO User_Deck_Data (user_deck_dataid, deck_deck_id, username) VALUES ('9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', '22a0c39d-a2b0-460d-9e39-4dc2db3e7b36', 'user1')
INSERT INTO User_Deck_Data (user_deck_dataid, deck_deck_id, username) VALUES ('344af511-e9b0-46b8-86c2-9fcf23353056', '22a0c39d-a2b0-460d-9e39-4dc2db3e7b36', 'user2')


-- userCardData
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('66dc13ab-7664-497c-b510-3be1b22156b6', 0, 0, '2023-01-08', 0, 6, '291771c2-b76f-411a-b54d-cd9cd0c27f9f', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('f6847309-f411-47e7-a013-efcc1d8d022b', 0, 0, '2023-01-08', 0, 6, '130fa75a-33ed-417f-bdc5-e4e34482ad20', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('a680a3d4-8805-488a-a3dc-d59c702a0a42', 0, 0, '2023-01-08', 0, 6, '21fbafe5-ad3d-47da-9b13-9ab2d5d290b0', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('b90cfa8c-3fb7-4de0-be26-08338efc659f', 0, 0, '2023-01-08', 0, 6, '70811d0f-061b-425c-82d9-0d8c7209ee9a', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('3ab15ff1-b788-4c7a-b684-6ff483285a42', 0, 0, '2023-01-08', 0, 6, '3264bc82-0b4a-4578-82c8-22ba6d9c1126', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('120e7885-3cf6-429c-bee9-b2ae67cb1e41', 0, 0, '2023-01-08', 0, 6, '07f39c5f-cfc8-4397-9668-e129e9bec267', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('711a75a9-afb3-4d81-af82-2773f191f657', 0, 0, '2023-01-08', 0, 6, '61c49c72-de90-4523-9388-96b3e09e52d0', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('22099c05-2b5e-42b4-af4d-7ba46ba61424', 0, 0, '2023-01-08', 0, 6, '4d9c0fc3-cba6-4b2d-a976-7a4f400154bd', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('66bb194e-23d3-4f6c-8699-7c1c6e0ae847', 0, 0, '2023-01-08', 0, 6, '23a92b3d-2ebe-445b-984e-000acd711689', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('b7d065a5-0da9-4b08-bbb2-bcf20ca7d0a7', 0, 0, '2023-01-08', 0, 6, 'cb79d4ae-f853-439e-b62a-77b834103ee6', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('6c27a05b-924e-4b9b-b9a6-63802b928bf4', 0, 0, '2023-01-08', 0, 6, '93806103-8575-4142-89af-f6b99142ce59', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('3f9287dc-6d14-41be-bdf5-ab986bfba071', 0, 0, '2023-01-08', 0, 6, 'ccaa1a7e-49ab-4d9a-9ffc-28c6f5403b32', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('2950c880-610b-423e-9477-c1b95954fcb2', 0, 0, '2023-01-08', 0, 6, '439371b7-5850-4da6-bf28-c89cdd93049c', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('5e5aa57f-5057-45a9-b2e9-0103e41cbd0a', 0, 0, '2023-01-08', 0, 6, 'e28b9fbb-22f7-454e-bee7-a6f9174479af', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('4d07ca02-5eb8-4d03-aeea-38cbabbd07dd', 0, 0, '2023-01-08', 0, 6, 'acd39f43-fe2c-4e05-b2a6-92fe143c0df2', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('0c7571ad-f98f-4656-961c-6eee50548b99', 0, 0, '2023-01-08', 0, 6, '78cdd252-65ac-473c-89f0-905032670257', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)
INSERT INTO User_Card_Data (user_Card_DataID, repetitions, e_Factor, next_Date, learn_Interval, difficulty_Rating, card_card_id, deck_data_id, reversed) VALUES ('6ffa08e1-0a66-4f8f-bb79-a084b33b5c23', 0, 0, '2023-01-08', 0, 6, '9590d3bf-d514-4946-8942-04c46b27756f', '9fdaf57d-d2c3-44d8-979b-8df0acf96cf7', false)


-- smoke test user
INSERT INTO USERX (ENABLED,FIRST_NAME,LAST_NAME,PASSWORD,USERNAME,EMAIL,CREATE_USER_USERNAME,CREATE_DATE)VALUES(TRUE,'smoke','user','$2a$10$nkj3gqqrItuA8uYQZJBmy.A9pNixv8teFIKT0z64n.vLtsi3Io2WK','smoke','smokeUser@memori.com','smoke','2016-01-01 00:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES)VALUES ('smoke', 'USER')
-- smoke test deck
INSERT INTO Deck (deckid, name, description, is_active, is_public, owner_username) VALUES ('439371b7-5850-4da6-bf28-c89cdd93049c', 'Test Deck', 'Test Deck für den Smoke Test','true', 'false', 'smoke')
-- smoke test Cards
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('6824c75a-abe6-4b50-8057-1839aac82bac', 'front smoke test 1', 'back smoke test 1', TRUE, '439371b7-5850-4da6-bf28-c89cdd93049c')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('fdecd824-6d17-4e68-b40f-4599d7504734', 'front smoke test 2', 'back smoke test 2', TRUE, '439371b7-5850-4da6-bf28-c89cdd93049c')
INSERT INTO CARD (cardID, front, back, is_reversible, deck_included_uuid) VALUES ('b2a5837f-2180-4efd-a43c-5608787a5bea', 'front smoke test 3', 'back smoke test 3', TRUE, '439371b7-5850-4da6-bf28-c89cdd93049c')

-- reports
INSERT INTO REPORT(REPORTID,REASON, STATE,DECK_DECK_ID, REPORTER_USERNAME) VALUES ('a20cea9f-b35e-411a-b15c-d31700d66a83', 'Suspicious deck', 0, '9ec2dda8-3850-4ffd-bc25-fabc732adaec', 'admin')
INSERT INTO REPORT(REPORTID,REASON, STATE,DECK_DECK_ID, REPORTER_USERNAME) VALUES ('a20cea9f-b35e-411a-b15c-d31700d66a82', 'Deck was not made by a studend but by chatGPT', 0, '9ec2dda8-3850-4ffd-bc25-fabc732adaec', 'user1')
INSERT INTO REPORT(REPORTID,REASON, STATE,DECK_DECK_ID, REPORTER_USERNAME) VALUES ('a20cea9f-b35e-411a-b15c-d31700d66a81', 'Reported from a User', 0, '9ec2dda8-3850-4ffd-bc25-fabc732adaec', 'admin')
