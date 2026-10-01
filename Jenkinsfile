pipeline {
    agent any
    tools {
        maven 'Maven3'
        jdk 'JDK25'
    }
    environment {
        IMAGE = "shinx07/library-book-search:${BUILD_NUMBER}"
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/it24sakshamshirwadkar-lgtm/library-book-search.git', credentialsId: 'github-creds'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Docker Build') {
            steps {
                bat "docker build -t %IMAGE% ."
            }
        }
        stage('Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'U', passwordVariable: 'P')]) {
                    bat 'docker login -u %U% -p %P%'
                    bat "docker push %IMAGE%"
                }
            }
        }
    }
}
