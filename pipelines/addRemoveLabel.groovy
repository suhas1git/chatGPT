if (currentBuild.result == 'SUCCESS') {
    github {
        issue("pull_request_number").labels("label_success")
    }
} else {
    github {
        issue("pull_request_number").labels("label_failure")
    }
}
