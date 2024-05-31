
DECLARE
    v_user_count NUMBER;
BEGIN
    -- Comprova si l'usuari ja existeix
    SELECT COUNT(*)
    INTO v_user_count
    FROM dba_users
    WHERE username = 'C##HR';

    -- Si l'usuari no existeix, el crea
    IF v_user_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE USER C##HR IDENTIFIED BY HR';
        EXECUTE IMMEDIATE 'GRANT CREATE SESSION TO C##HR';
        EXECUTE IMMEDIATE 'GRANT CREATE TABLE TO C##HR';
        EXECUTE IMMEDIATE 'GRANT CREATE TRIGGER TO C##HR';
        EXECUTE IMMEDIATE 'GRANT EXECUTE ON DBMS_OUTPUT TO C##HR';
        DBMS_OUTPUT.PUT_LINE('Usuari C##HR creat amb èxit.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('L''usuari C##HR ja existeix.');
    END IF;
END;
/


-- Procediment per crear la taula COTXES
CREATE OR REPLACE PROCEDURE crear_taula_cotxes AS
    v_count NUMBER;
BEGIN
    -- Comprova si la taula ja existeix
    SELECT COUNT(*)
    INTO v_count
    FROM user_tables
    WHERE table_name = 'COTXES';

    -- Si la taula no existeix, la crea
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE COTXES (
                ID NUMBER PRIMARY KEY,
                MARCA_I_MODEL VARCHAR2(100) NOT NULL,
                PES NUMBER(10) NOT NULL,
                ES_NOU NUMBER(1) NOT NULL
            )
        ';
        DBMS_OUTPUT.PUT_LINE('La taula COTXES ha estat creada.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('La taula COTXES ja existeix.');
    END IF;
END;
/

BEGIN
    crear_taula_cotxes;
END;
/

-- Trigger per generar automàticament la ID en la inserció
CREATE OR REPLACE TRIGGER TRG_COTXES_ID
BEFORE INSERT ON COTXES
FOR EACH ROW
BEGIN
    SELECT NVL(MAX(ID), 0) + 1 INTO :NEW.ID FROM COTXES;
END;
/