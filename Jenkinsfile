pipeline {
    agent any
    environment {
            RABBIT_PORT = "${env.RABBIT_PORT.tokenize('/')[1]}"
        }
    stages {
        stage('Start Container') {
            steps {
                bat 'docker compose up -d'
            }
        }
    }
}
