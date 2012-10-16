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

def doConnect():
	connect("weblogic","weblogic","localhost:7171")
#end doConnect()

# undeploy WAR that is currently deployed.
undeployApplication('das_wrap_nwhin')

exit()
