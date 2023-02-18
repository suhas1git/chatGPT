pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build') {
      steps {
        sh 'mvn clean package'
      }
    }

    stage('Create Pull Request') {
      steps {
        script {
          def gitUrl = sh(returnStdout: true, script: 'git config --get remote.origin.url').trim()
          def branchName = env.BRANCH_NAME
          def targetBranch = 'master'
          def prTitle = "Feature branch: ${branchName}"

          sh "git push ${gitUrl} HEAD:refs/heads/${branchName}"
          sh "curl -X POST -H 'Content-Type: application/json' \
              -d '{\"title\": \"${prTitle}\", \"head\": \"${branchName}\", \"base\": \"${targetBranch}\"}' \
              https://api.github.com/repos/<owner>/<repo>/pulls"
        }
      }
    }
  }
}
