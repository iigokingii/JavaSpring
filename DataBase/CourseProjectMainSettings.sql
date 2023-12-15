--ADMIN LOCAL
    --�� ������� ��� �������� admina ����� dbca ��� �������� ���� pdb_dba
    --� ������� ����� ���������� �� �������� pdb � session,
    SELECT USERNAME,CREATED FROM DBA_USERS ORDER BY CREATED DESC;
    SELECT *FROM V$TABLESPACE;
    
    SELECT default_tablespace FROM dba_users WHERE username='ADMIN';
    select * from session_privs;
    select * from cdb_role_privs where grantee=upper('admin');
    select * from role_sys_privs where role='PDB_DBA';
    select * from dba_role_privs where GRANTEE='PDB_DBA';
    select * from role_sys_privs where ROLE='CONNECT';
    --PRIVILEGES
    GRANT CREATE ANY TABLE TO PDB_DBA;
    GRANT CREATE VIEW TO PDB_DBA;
    GRANT CREATE PROCEDURE TO PDB_DBA;
    GRANT CREATE TABLESPACE TO PDB_DBA;
    GRANT CREATE ROLE TO PDB_DBA;
    GRANT CREATE PROFILE TO PDB_DBA;
    GRANT CREATE SESSION TO PDB_DBA WITH ADMIN OPTION;
    GRANT CREATE USER TO PDB_DBA;
    GRANT CREATE ANY DIRECTORY TO PDB_DBA;-- ������� ���������� ��� �������/��������
    GRANT WRITE ON DIRECTORY MY_DIRECTORY TO PDB_DBA;
    --GRANT RESOURCE TO admin; 
    --��� GENERATED ALWAYS AS IDENTITY
    GRANT CREATE ANY SEQUENCE TO PDB_DBA;
    GRANT SELECT ANY SEQUENCE TO PDB_DBA;
    --������ quot � TS
    ALTER USER admin QUOTA unlimited ON CP_TS;
    ALTER USER CP_USER QUOTA 100M ON CP_TS;
    COMMIT;
    
    CREATE TABLESPACE CP_TS
        DATAFILE 'CP_TS.DBF'
        SIZE 50M
        AUTOEXTEND ON NEXT 1M
        MAXSIZE 150M
        EXTENT MANAGEMENT LOCAL;
        
    CREATE TEMPORARY TABLESPACE CP_TEMP_TS
        TEMPFILE 'CP_TEMP_TS.DBF'
        SIZE 20M
        AUTOEXTEND ON NEXT 500K
        MAXSIZE 120M
        EXTENT MANAGEMENT LOCAL;
    
    COMMIT;

--USER PROFILE
    CREATE ROLE ROLE_USER;
    DROP ROLE ROLE_USER;
    GRANT CREATE SESSION TO ROLE_USER;
    GRANT EXECUTE ON ADMIN.SHAREDFUNCTIONS TO ROLE_USER;
    GRANT SELECT ON ALL_INFORMATION_ABOUT_FILM TO ROLE_USER;
    GRANT SELECT ON USER_PROFILE TO ROLE_USER;  
    GRANT SELECT ON USERS_REVIEWS_ON_MOVIE TO ROLE_USER;    
    GRANT SELECT ON USER_WATCH_LATER TO ROLE_USER; 
    GRANT SELECT ON USER_LIKE TO ROLE_USER;    
    GRANT EXECUTE,DEBUG ON GENRE_TYPE_INSTANCE TO ROLE_USER;    
    GRANT EXECUTE,DEBUG ON DIRECTOR_TYPE_INSTANCE TO ROLE_USER; 
    GRANT EXECUTE,DEBUG ON ACTOR_TYPE_INSTANCE TO ROLE_USER; 
    GRANT EXECUTE,DEBUG ON INTERESTING_FACT_TYPE_INSTANCE TO ROLE_USER; 
 
    CREATE PROFILE CP_USER_PROFILE LIMIT
            PASSWORD_LIFE_TIME 180
            SESSIONS_PER_USER 3
            FAILED_LOGIN_ATTEMPTS 7
            PASSWORD_LOCK_TIME 1
            PASSWORD_REUSE_TIME 10
            PASSWORD_GRACE_TIME DEFAULT
            CONNECT_TIME 180
            IDLE_TIME 30;
    
    CREATE USER CP_USER IDENTIFIED BY 1111
        DEFAULT TABLESPACE CP_TS
        TEMPORARY TABLESPACE CP_TEMP_TS
        PROFILE CP_USER_PROFILE
        ACCOUNT UNLOCK;
        
    GRANT ROLE_USER TO CP_USER;
    SELECT * FROM ADMIN.ALL_INFORMATION_ABOUT_FILM;
    
    
    DROP ROLE ROLE_USER;
DECLARE
  CURS SYS_REFCURSOR;
  SS ADMIN.ALL_INFORMATION_ABOUT_FILM%ROWTYPE; 
BEGIN
  CURS := ADMIN.SHAREDFUNCTIONS.FIND_BY_ID(1); 
  LOOP
    FETCH CURS INTO SS;
    EXIT WHEN CURS%NOTFOUND;
    DBMS_OUTPUT.PUT_LINE(SS.TITLE);
  END LOOP;
  CLOSE CURS;
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/

