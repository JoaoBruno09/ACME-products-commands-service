pipeline {
    agent any
    environment {
            RABBIT_PORT = "${env.RABBIT_PORT}"
        }
    stages {
        stage('Start Container') {
            steps {
                bat 'docker compose up -d'
            }
        }
    }
}
