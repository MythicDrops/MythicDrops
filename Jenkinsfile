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
                checkout(
                        poll: false,
                        scm: [$class                           : 'GitSCM',
                              branches                         : [[name: "*/${env.BRANCH_NAME}"]],
                              doGenerateSubmoduleConfigurations: false,
                              extensions                       : [[$class: 'CloneOption', depth: 0, noTags: false, reference: '', shallow: false]],
                              submoduleCfg                     : [],
                              userRemoteConfigs                : [[credentialsId: 'ToppleTheNun-GitHub', url: 'https://github.com/PixelOutlaw/MythicDrops.git']]]
                )
            }
        }

        stage('Gradle Build') {
            steps {
                sh label: '', script: 'chmod +x gradlew && ./gradlew clean build'
            }
        }

        stage('Deploy to Discord') {
            when {
                branch 'master'
            }

            steps {
                withCredentials([string(credentialsId: 'MythicDrops-Discord-Webhook', variable: 'DISCORD_WEBHOOK')]) {
                    sh label: '', script: './upload-to-discord.sh mythicdrops/build/distributions/MythicDrops*.zip'
                }
            }
        }
    }
}
