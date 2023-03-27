pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'your-credential-id', url: 'git@github.com:your-repo.git']]])
            }
        }
        stage('Install Dependencies') {
            steps {
                sh 'composer install --no-dev --prefer-dist --no-interaction'
            }
        }
        stage('Lint') {
            steps {
                sh 'composer lint'
            }
        }
        stage('Test') {
            steps {
                sh 'composer test'
            }
        }
        stage('Build') {
            steps {
                sh 'composer build'
            }
        }
        stage('Deploy') {
            steps {
                sh 'composer deploy'
            }
        }
    }
}
