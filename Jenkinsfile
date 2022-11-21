pipeline {
    agent any
    tools 
    {
   	maven 'maven_jenkins'
  	gradle 'gradle_env'
    }

    stages {
        stage('Build - Test -  Jar')
        {
            steps
            {
                sh "gradle build"
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
                sh "sleep 20"
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
   		        sh 'curl http://nexus:8081/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/001/DevOpsUsach2020-001.jar --output /tmp/DevOpsUsach2020-001.jar'
                sh 'java -jar /tmp/DevOpsUsach2020-001.jar'
		        sh 'curl -X GET  http://localhost:8081/rest/mscovid/test?msg=testing'
            }
	      
       }
       stage ('Send to Nexus 1.0.0')
       {
       	   steps 
		    {
		        echo 'TODO: Maven Install to version 1.0.0'
		        sh "./mvnw versions:set -DnewVersion=1.0.0"
                sh "./mvnw clean package -e"
                sh "./mvnw clean install" 
                nexusPublisher nexusInstanceId: 'nexus_docker', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "${WORKSPACE}/build/DevOpsUsach2020-1.0.0.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
		}
           
       }
   }
}
