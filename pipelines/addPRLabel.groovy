pipeline {
  agent any

  stages {
    stage("Add GitHub PR label") {
      steps {
        script {
          def token = "YOUR_GITHUB_ACCESS_TOKEN"
          def repo = "OWNER/REPO"
          def prNumber = "${env.CHANGE_ID}"
          def label = "label-name"

          httpRequest url: "https://api.github.com/repos/${repo}/issues/${prNumber}/labels",
            customHeaders: [
              "Authorization": "Token ${token}",
              "Accept": "application/vnd.github+json"
            ],
            httpMode: "POST",
            requestBody: "[\"${label}\"]"
        }
      }
    }
  }
}

