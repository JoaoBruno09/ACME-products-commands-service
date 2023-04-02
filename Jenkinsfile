pipeline {
    agent any
    stages {
        stage('Checkout'){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'git_access', url: 'https://Tiagorpl@bitbucket.org/joaobruno1220256/acme_products_queries.git']]])
            }
        }
        stage('Start Container') {
            steps {
                sh 'docker compose up'
            }
        }
    }
}
