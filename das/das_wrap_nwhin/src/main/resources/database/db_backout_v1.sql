-- *****************************************************************
-- Description:  Drops the base table and hibernate sequence for C32 project.
-- Notes: Run as root user.
-- Input Parameters:  None
-- Output Parameters: None
-- Error Conditions Raised:  N/A
-- Author: Matthew.McCall2@va.gov
-- Revision History
-- Date            Author                Reason for Change
-- ----------------------------------------------------------------
-- 27 Nov 2012     Matthew.McCall2@va.gov    Created script 
-- *****************************************************************

use c32;

drop table if exists C32_DOCUMENT;

drop table if exists hibernate_sequence;
