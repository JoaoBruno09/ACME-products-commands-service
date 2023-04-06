pipeline {
    agent any
    environment {
            DB_URL = "${env.DB_URL}"
            DB_USER = "${env.DB_USER}"
            DB_PW = "${env.DB_PW}"
            RABBIT_PORT = "${env.RABBIT_PORT}"
        }
    stages {
        stage('Start Container') {
            steps {
                echo "DB_URL is ${DB_URL}"
                echo "DB_USER is ${DB_USER}"
                echo "DB_PW is ${DB_PW}"
                echo "RABBIT_PORT is ${RABBIT_PORT}"
                bat 'docker compose logs products_c'
            }
        }
    }
}
