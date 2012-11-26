use C32;

--drop table if exists C32_DOCUMENT;

--drop table if exists hibernate_sequence;

create table C32_DOCUMENT (
AUDIT_MESSAGE_ID integer not null,
CREATE_DATE date,
DOCUMENT blob,
DOCUMENT_PATIENT_ID varchar(255),
PATIENT_ID varchar(255),
primary key (AUDIT_MESSAGE_ID)) ENGINE = InnoDB;

alter table C32_DOCUMENT
add index `PATIENT_ID` (`PATIENT_ID` ASC);

create table hibernate_sequence (next_val bigint) ENGINE = InnoDB;

insert into hibernate_sequence values ( 1 );
