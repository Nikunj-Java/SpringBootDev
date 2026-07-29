pipeline {

    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        GIT_URL = 'https://github.com/Nikunj-Java/SpringBootDev.git'
        BRANCH = 'main'
    }

    stages {

        stage('Checkout Source') {
            steps {
                git branch: "${BRANCH}",
                    url: "${GIT_URL}"
            }
        }

        stage('Build Spring Boot') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Stop Existing Containers') {
            steps {
                sh '''
                    docker compose down || true
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker compose build --no-cache
                '''
            }
        }

        stage('Deploy Containers') {
            steps {
                sh '''
                    docker compose up -d
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                sh '''
                    docker ps
                '''
            }
        }

    }

    post {

        success {
            echo 'Application deployed successfully.'
        }

        failure {
            echo 'Deployment failed.'
        }

        always {
            sh 'docker image prune -f'
        }

    }

}