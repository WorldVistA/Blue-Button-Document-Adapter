-- *****************************************************************
-- Description:  Maintenance Script which backs out all entries 
--               except for the last one for a given patient.
-- Notes: Run as c32 user.
-- Input Parameters:  None
-- Output Parameters: None
-- Error Conditions Raised:  N/A
-- Author: Matthew.McCall2@va.gov, Steve.Monson@va.gov
-- Revision History
-- Date            Author                Reason for Change
-- ----------------------------------------------------------------
-- 07 Dec 2012     Matthew.McCall2@va.gov    Created script 
-- 24 Jan 2013     Steve.Monson@va.gov       Modified insert statement
-- 29 Jan 2013     Matthew.McCall2@va.gov    Revised to drop only (no archive table)
-- *****************************************************************

use c32;

-- Allows delete query to run.

set SQL_SAFE_UPDATES=0;

-- Creates the archive table if it doesn't exist.

create temporary table if not exists C32_DOCUMENT_REMOVAL AS select AUDIT_MESSAGE_ID from C32_DOCUMENT limit 0;

-- Reduce this list to retain the maximum value.

insert into C32_DOCUMENT_REMOVAL
	(select a.AUDIT_MESSAGE_ID
	from C32_DOCUMENT a
	where a.CREATE_DATE not in (select max(b.CREATE_DATE) 
							from C32_DOCUMENT b 
							where b.PATIENT_ID=a.PATIENT_ID)
	and a.CREATE_DATE not in (select max(c.CREATE_DATE)
								from C32_DOCUMENT c
								where c.PATIENT_ID=a.PATIENT_ID
								and c.CREATE_DATE not in 	(select max(d.CREATE_DATE)
															from C32_DOCUMENT d
															where d.PATIENT_ID=a.PATIENT_ID)));

-- Remove archived records from main table.

delete from C32_DOCUMENT where AUDIT_MESSAGE_ID in (select AUDIT_MESSAGE_ID from C32_DOCUMENT_REMOVAL);

drop table if exists C32_DOCUMENT_REMOVAL;