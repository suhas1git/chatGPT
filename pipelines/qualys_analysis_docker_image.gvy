
pipeline {
    agent any
    environment {
        ARTIFACTORY_URL = 'https://your.artifactory.url'
        ARTIFACTORY_USER = 'your-artifactory-username'
        ARTIFACTORY_API_KEY = credentials('your-artifactory-api-key')
        QUALYS_API_URL = 'https://qualys-api-url'
        QUALYS_USER = 'your-qualys-username'
        QUALYS_PASSWORD = credentials('your-qualys-password')
    }
    stages {
        stage('Get Docker Image IDs from Artifactory') {
            steps {
                script {
                    def response = sh(
                        script: """
                            curl -s -H "X-JFrog-Art-Api: ${ARTIFACTORY_API_KEY}" \
                            "${ARTIFACTORY_URL}/api/search/aql" \
                            -H 'Content-Type: text/plain' \
                            -d 'items.find({"@docker.manifest": {"$exists": true}}).include("name", "@docker.manifest.digest").limit(1000)' \
                            | jq -r '.results[].properties."docker.manifest.digest"'
                        """,
                        returnStdout: true
                    )
                    env.DOCKER_IMAGE_IDS = response.trim().split('\n')
                }
            }
        }
        stage('Qualys Scan') {
            when {
                expression { env.DOCKER_IMAGE_IDS }
            }
            steps {
                script {
                    for (def imageId : env.DOCKER_IMAGE_IDS) {
                        sh(
                            script: """
                                curl -X POST \
                                "${QUALYS_API_URL}/api/2.0/fo/container/scheduleScan/?action=launch&user_login=${QUALYS_USER}&user_password=${QUALYS_PASSWORD}&container_image_ref=${imageId}"
                            """
                        )
                    }
                }
            }
        }
    }
}
