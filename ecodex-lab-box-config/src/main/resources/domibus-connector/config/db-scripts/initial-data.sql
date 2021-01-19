--liquibase formatted sql

--changeset StephanSpindler:initialBackendData_4
----------------------- Values for DOMIBUS_CONNECTOR_BACKEND_INFO ------------------------------

DELETE FROM DOMIBUS_CONNECTOR_BACKEND_INFO;

INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS, BACKEND_ENABLED, BACKEND_DEFAULT)
VALUES (1, 'cn=bob', 'bob', '', '', 'a bob backend - no push', null, true, true);

INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS, BACKEND_ENABLED, BACKEND_DEFAULT)
VALUES (2, 'cn=alice', 'alice', '', '', 'a alice backend with push', 'http://localhost:8${lab.id}2/services/domibusConnectorDeliveryWebservice', true, false);

-- INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS, BACKEND_ENABLED, BACKEND_DEFAULT)
-- VALUES (3, 'cn=exec_ri', 'exec_ri', '', '', 'a alice backend with push', 'http://localhost:8${lab.id}3/case-service/services/connectorClientBackendDelivery', true, false);

DELETE FROM DOMIBUS_CONNECTOR_PARTY;

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  (1, 'gw01', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  (2, 'gw02', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  (3, 'gw03', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  (4, 'gw04', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  (5, 'gw05', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  (6, 'gw06', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  (7, 'gw07', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  (8, 'gw08', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  (9, 'gw09', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  (10, 'gw10', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');


-- INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
--   ('at', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');
-- 
-- INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
--   ('gw-at', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');
-- 
-- INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
--   ('bg', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');
-- 
-- INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
--   ('cz', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');
-- 
-- INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
--   ('de', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');
-- 
-- INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
--   ('ee', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');
-- 
-- INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
--   ('es', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');
-- 
-- INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
--   ('gr', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

DELETE FROM DOMIBUS_CONNECTOR_SERVICE;

INSERT INTO DOMIBUS_CONNECTOR_SERVICE (ID, SERVICE, SERVICE_TYPE) VALUES (1, 'Connector-TEST', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE (ID, SERVICE, SERVICE_TYPE) VALUES (2, 'System-TEST', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE (ID, SERVICE, SERVICE_TYPE) VALUES (3, 'Service1', 'urn:e-codex:services:');

DELETE FROM DOMIBUS_CONNECTOR_ACTION;

-- INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
-- VALUES ('Eio_AnnexA_Submission', false);
-- 
-- INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
-- VALUES ('Eio_AnnexB_Submission', false);
-- 
-- INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
-- VALUES ('Eio_Outcome_Submission', false);
-- 

INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, "ACTION", PDF_REQUIRED)
VALUES (1, 'SubmissionAcceptanceRejection', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, "ACTION", PDF_REQUIRED)
VALUES (2, 'RelayREMMDAcceptanceRejection', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, "ACTION", PDF_REQUIRED)
VALUES (3, 'DeliveryNonDeliveryToRecipient', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, "ACTION", PDF_REQUIRED)
VALUES (4, 'RetrievalNonRetrievalToRecipient', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, "ACTION", PDF_REQUIRED)
VALUES (5, 'System-TEST', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, "ACTION", PDF_REQUIRED)
VALUES (6, 'Connector-TEST', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, "ACTION", PDF_REQUIRED)
VALUES (7, 'Action1', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, "ACTION", PDF_REQUIRED)
VALUES (8, 'Action2', false);