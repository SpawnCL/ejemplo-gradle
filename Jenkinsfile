def mvn_init
def grdl_init

pipeline {
    agent any
    tools 
    {
   	 	gradle 'gradle_env'
        maven 'maven_jenkins'
    }
    parameters
    {
        choice(name:'Build_Tool', choices: ['Maven','Gradle'],description: 'Seleccion de Tipo Build')
        booleanParam(name:'PushToNexus' , defaultValue:True ,'Enviar hacia el Repositorio Nexus')
        booleanParam(name:'TestFromNexus' , defaultValue:False ,'Descargar el JAR desde Nexus y Testear')
    }
    stages {
        stage('Init Scripts Maven')
        {
           when
           {
                expression
                {
                    params.Build_Tool=='Maven'
                }
            }
            steps
                {
                script
                    {
                        echo "Agregando Script de Maven y Gradle"
                        mvn_init = load "maven.groovy"
                    }
                }

        }
        stage('Init Scripts Gradle')
        {
           when
           {
                expression
                {
                    params.Build_Tool=='Gradle'
                }
            }
            steps
            {
                script
                {
                    echo "Agregando Script  Gradle"
                    mvn_init = load "gradle.groovy"
                }
            }
        }        
        stage('Maven Compile')
        {
            when
            {
                expression
                {
                    params.Build_Tool=='Maven'
                }
            }
            steps
            {
                mvn_init.maven_compile()
            }

        }
        stage('Maven Test')
        {
            when
            {
                expression
                {
                    params.Build_Tool=='Maven'
                }
            }
            steps
            {
                mvn_init.mavel_test()
            }

        }
        stage ('Maven Package')
        {
            when
            {
                expression
                {
                    params.Build_Tool=='Maven'
                }
            }
            steps
            {
                mvn_init.mavel_package()
            }
        }
        stage('Gradle: Build - Test -  Jar')
        {
            when
            {
                expression
                {
                    params.Build_Tool=='Gradle'
                }
            }
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
            when
            {
                expression
                {
                    params.Build_Tool=='Gradle'
                }
            }
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
            when
            {
                expression
                {
                    params.PushToNexus
                }
            }
            steps 
            {
                nexusPublisher nexusInstanceId: 'nexus_docker', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "${WORKSPACE}/build/DevOpsUsach2020-0.0.1.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
            }
        }

        stage ('Download Nexus Jar-Run-Test')
        {
            when
            {
                expression
                {
                    params.TestFromNexus
                }
            }
	        steps
		    {
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
