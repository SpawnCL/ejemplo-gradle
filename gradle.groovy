def gradle_build_test_jar()
        {
                sh "gradle build"
                sh "gradle clean build" 
        }
def gradle_run()
        {
                sh "gradle bootRun &"
                sh "sleep 10"
                sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
        }


return this
