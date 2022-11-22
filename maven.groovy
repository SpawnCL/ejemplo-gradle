def maven_compile()
	{
		sh 'mvn clean compile -e'
    	}
def mavel_test()
	{
		sh 'mvn clean test -e'
	}
def mavel_package()
	{
		sh 'mvn clean package -e'
	}

def maven_run_jar_test()
	{
		sh "mvn spring-boot:run &"
		sh "sleep 10 "
		sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
	}
def maven_update_version(versionp='')
	{
		echo "TODO: Maven Install to version ${versionp}" 
		sh "mvn versions:set -DnewVersion=${versionp}"
                sh "mvn clean package -e"
                sh "mvn clean install" 
	}

return this
