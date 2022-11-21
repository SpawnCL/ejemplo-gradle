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
        choice(name: 'Build_Tool', choices: ['Maven','Gradle'], description: 'Seleccion de Tipo Build')
        booleanParam(name: 'PushToNexus' , defaultValue: true , description: 'Enviar hacia el Repositorio Nexus')
        booleanParam(name: 'TestFromNexus' , defaultValue: false , description: 'Descargar el JAR desde Nexus y Testear')
        booleanParam(name: 'RVNexus', defaultValue: false, description: 'Release Version 1.0.0 a Nexus')
    }
    stages 
    {
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
                script
                {
                    mvn_init.maven_compile()
                }
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
                script
                {
                    mvn_init.mavel_test()
                }
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
                script
                {
                    mvn_init.mavel_package()
                }
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
                grdl_init.gradle_build_test_jar()
                /*sh "mvn clean install -e" */
            }
        } 

        stage('Maven: Run and Test App')
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
                    mvn_init.maven_run_jar_test()
                }
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
                script
                {
                    grdl_init.gradle_run()
                }

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
            when:
            {
                expression
                {
                    params.RVNexus
                }
            }
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
