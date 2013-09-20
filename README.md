oraProxyTest
============



Helpful SQL:

Oracle DB:

CREATE ROLE role1; 
GRANT SELECT ON Users TO role1;
CREATE USER prox IDENTIFIED BY prox;
GRANT CONNECT TO prox;


ALTER USER prox QUOTA 100M ON USERS
ALTER USER prox GRANT CONNECT THROUGH jtstest

GRANT role1 TO prox;
GRANT CREATE SEQUENCE TO prox|jtstest
GRANT CREATE TABLE TO prox|jtstest

jtstest (and prox):
create sequence HIBSEQUENCE;

create table BeanOne (
    id number(10,0) not null,
    meaningless varchar2(255 char) not null,
    primary key (id)
);

create table BeanTwo (
    id number(10,0) not null,
    meaningless varchar2(255 char) not null,
    primary key (id)
);


select * from BeanOne;

select * from BeanTwo;

truncate table BeanOne;

truncate table BeanTwo;
