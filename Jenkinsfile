pipeline {
    agent any
    tools 
    {
   	 	gradle 'gradle_env'
        maven 'maven_jenkins'
    }

    stages {
        stage('Build - Test -  Jar')
        {
            steps
            {
                sh "gradle build"
                sh "mvn clean install -e"
            }
        } 
       
        stage('Sonarque')
 	    {
            steps
            {
               withSonarQubeEnv(credentialsId: 'rnpijenkins', installationName: 'rnpisonarqube')
               {
                sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar -Dsonar.projectKey=ejemplo-gradle-maven -Dsonar.java.binaries=build'
               }
            }
	    }

        stage('Run Gradle') 
        {
            steps 
            {
                echo 'TODO: Gradle Run '
                sh "gradle bootRun &"
                sh "sleep 10"
                sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
            }
        }

        stage('PushToNexus')
        {
            steps 
            {
                nexusPublisher nexusInstanceId: 'nexus_docker', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "${WORKSPACE}/build/DevOpsUsach2020-0.0.1.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
            }
        }

        stage ('Download Jar from Nexus')
        { 
	        steps
		    {
                cleanWs()
   		        sh 'curl http://nexus:8081/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/001/DevOpsUsach2020-001.jar --output /tmp/DevOpsUsach2020-001.jar'
                sh 'java -jar /tmp/DevOpsUsach2020-001.jar &'
		        sh 'curl -X GET  http://localhost:8081/rest/mscovid/test?msg=testing'
                echo "Stopping App"
                sh 'pkill -f "java -jar /tmp/DevOpsUsach2020-001.jar"'
            }
	      
       }
       stage ('Send to Nexus 1.0.0')
       {
       	   steps 
		    {
		        echo 'TODO: Maven Install to version 1.0.0'
		        sh "mvn versions:set -DnewVersion=1.0.0"
                sh "mvn clean package -e"
                sh "mvn clean install" 
                nexusPublisher nexusInstanceId: 'nexus_docker', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "${WORKSPACE}/build/DevOpsUsach2020-1.0.0.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
		}
           
       }
   }
}
