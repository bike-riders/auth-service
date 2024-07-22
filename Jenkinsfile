pipeline {
    agent any
    triggers {
        pollSCM 'H * * * *'
    }
    stages {
        stage('Build') {
            steps {
               script{
                    echo 'Pulling...' + env.BRANCH_NAME
                    sh 'mvn clean install -DskipTests'
               }
            }
        }
        stage('Deploy') {
            steps {
                script{
                    sh 'pwd'
                    sh 'cd target/'
                    sh 'ls -lrth'
                    sh 'mv target/auth-service.war /opt/tomcat/webapps/'
                }
            }
        }
    }
}
