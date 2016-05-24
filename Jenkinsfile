node {
    checkout scm
    def mvnHome = tool 'Maven 3.3.9'
    sh "${mvnHome}/bin/mvn -B clean deploy"
    slackSend "Build Finished - ${env.JOB_NAME} - ${env.BUILD_STATUS} - ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
}