a.	Run the following script as root user to run maintenance on the core table.  It will remove all but the last two files in the database on a per user basis.

b.	db_maintenance_v1.sql

c.	This should be scheduled to run only after backups have executed, to ensure there is no loss of data.
