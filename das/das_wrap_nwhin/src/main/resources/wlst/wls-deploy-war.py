from java import util
from java import io
from java.lang import String

def undeployApplication(appName):
    doConnect()
    cd("AppDeployments")
    for i in ls().split() :
        if( i.count(appName) > 0 ) :
            undeploy(i)
    disconnect()
# end undeployApplication()

def deployAndStartApplication():
    doConnect()
    cd("AppDeployments")
    deploy(applicationName, applicationFilePath, upload='true')
    startApplication(applicationName)
    disconnect()
#end deployCdsEnterpriseApp()

def doConnect():
	connect("weblogic","weblogic","localhost:7171")
#end doConnect()

applicationName = sys.argv[1]
applicationFilePath = sys.argv[2]

# undeploy any VAP EAR that is currently deployed.
undeployApplication('das_wrap_nwhin')

# deploy the CDS EAR to the target server.
deployAndStartApplication()

exit()
