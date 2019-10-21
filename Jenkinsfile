pipeline {
  agent {
    docker {
      alwaysPull true
      image 'openjdk:8-jdk-slim'
    }
  }
  
  post {
    always {
      cleanWs()
    }
  }
  
  stages {
    stage('Checkout Code') {
      steps {
        checkout scm
      }
    }
    
    stage('Gradle Build') {
      steps {
        sh label: '', script: 'chmod +x gradlew && ./gradlew clean build'
      }
    }
  }
}
