--liquibase formatted sql

--changeset StephanSpindler:initialBackendData_4
----------------------- Values for DOMIBUS_CONNECTOR_BACKEND_INFO ------------------------------

DELETE FROM DOMIBUS_CONNECTOR_BACKEND_INFO;

INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS, BACKEND_ENABLED, BACKEND_DEFAULT)
VALUES (1, 'cn=bob', 'bob', '', '', 'a bob backend - no push', null, true, true);

INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS, BACKEND_ENABLED, BACKEND_DEFAULT)
VALUES (2, 'cn=alice', 'alice', '', '', 'a alice backend with push', 'http://localhost:8${lab.id}2/services/domibusConnectorDeliveryWebservice', true, false);

INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS, BACKEND_ENABLED, BACKEND_DEFAULT)
VALUES (3, 'cn=exec_ri', 'exec_ri', '', '', 'a alice backend with push', 'http://localhost:8${lab.id}3/case-service/services/connectorClientBackendDelivery', true, false);

DELETE FROM DOMIBUS_CONNECTOR_PARTY;

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('gw01', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('gw02', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('gw03', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('gw04', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('gw05', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('gw06', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('gw07', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('gw08', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('gw09', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('gw10', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');


INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('at', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('gw-at', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('bg', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('cz', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('de', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('ee', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('es', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('gr', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered');

DELETE FROM DOMIBUS_CONNECTOR_SERVICE;

INSERT INTO DOMIBUS_CONNECTOR_SERVICE (SERVICE, SERVICE_TYPE) VALUES ('Connector-TEST', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE (SERVICE, SERVICE_TYPE) VALUES ('System-TEST', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE (SERVICE, SERVICE_TYPE) VALUES ('Service1', 'urn:e-codex:services:');

DELETE FROM DOMIBUS_CONNECTOR_ACTION;

INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
VALUES ('Eio_AnnexA_Submission', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
VALUES ('Eio_AnnexB_Submission', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
VALUES ('Eio_Outcome_Submission', false);


INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
VALUES ('SubmissionAcceptanceRejection', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
VALUES ('RelayREMMDAcceptanceRejection', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
VALUES ('DeliveryNonDeliveryToRecipient', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
VALUES ('RetrievalNonRetrievalToRecipient', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
VALUES ('System-TEST', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
VALUES ('Connector-TEST', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
VALUES ('Action1', false);

INSERT INTO DOMIBUS_CONNECTOR_ACTION ("ACTION", PDF_REQUIRED)
VALUES ('Action2', false);