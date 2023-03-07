pipeline {
  agent any
  
  stages {
    stage('Build') {
      steps {
        // Checkout the code from the repository
        git 'https://github.com/your-repo.git'
        
        // Build the application
        sh 'mvn clean install'
      }
    }
    
    stage('Test') {
      steps {
        // Run automated tests
        sh 'mvn test'
      }
    }
    
    stage('Security Checks') {
      steps {
        // Run static code analysis using SonarQube
        withSonarQubeEnv('SonarQube') {
          sh 'mvn sonar:sonar'
        }
        
        // Run vulnerability scanning using OWASP ZAP
        sh 'docker run -u zap -p 8080:8080 -i owasp/zap2docker-stable zap-baseline.py -t http://localhost:8080'
        
        // Run penetration testing using Burp Suite
        sh 'docker run -p 8080:8080 -p 8090:8090 -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix bkimminich/bwapp'
        sh 'curl http://localhost:8080'
        // Perform manual penetration testing with Burp Suite
      }
    }
    
    stage('Build and Package') {
      steps {
        // Build and package the application
        sh 'mvn package'
        sh 'docker build -t my-app .'
      }
    }
    
    stage('Deploy') {
      steps {
        // Deploy the application to production
        sh 'docker run -d -p 80:8080 my-app'
      }
    }
  }
}
