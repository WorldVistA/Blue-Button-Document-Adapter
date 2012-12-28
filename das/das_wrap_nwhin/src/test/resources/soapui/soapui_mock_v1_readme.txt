The files included in this directory can be used to stand up an alternative mock NwHIN endpoint to that which is contained within the Java code.  This is done to better simulate an NwHIN interaction, and has been done for the purposes of load testing.

When properly configured, a user of the system can leverage soapUI, a freely available tool (www.soapui.org) to simulate an end-to-end test of the system's functionality.

MOCK SERVICE:

The mock service essentially takes any patient identifier, and will simulate a query and return call for said identifier.  The content of the C32 record will contain identical mock data for all service calls.

1)  Install soapUI on the server to act as the mock, and import the adapterMock.xml file into soapUI.

2)  You will need to configure the server name as a replacement for localhost, otherwise it will not be accessible by other servers.

3)  You will need to configure the path on the machine where the two accompanying files, "VA_C32_sansId_partA.txt" and "VA_C32_sansId_partB.txt" can be found.  This is contained in the groovy script of the mock retrieve service.

JAVA APPLICATION:

The java application will need to be configured to point to the mock service.  This is done by editing the web.properties file to point to the mock endpoints configured in the previous section.

ADAPTER TEST:

The adapter test will pass service calls to the java application once deployed, and simulate an MHV front end process flow.

1)  Load soapUI on your local machine, or on any computer you wish to test from, and import the adapterTest.xml file.

2)  Point the script to wherever the Java application is deployed.

3)  Execute the test script from within soapUI.

The test script will issue getStatus calls for a configurable number of iterations and frequency until it receives a complete call for the current date, and will then issue a getDocument call.  It has several identifier match tests throughout it to ensure integrity.  Additionally, this script may be used by either soapUI or loadUI to perform concurrent load testing on any deployment.