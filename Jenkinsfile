node {
    step([$class: 'GitHubSetCommitStatusBuilder'])
    checkout scm
    def mvnHome = tool 'Maven 3.3.9'
    sh "${mvnHome}/bin/mvn -B clean deploy"
    archive excludes: 'target/original-*.jar', includes: 'target/*.jar, target/*.zip'
    slackSend "Build Finished - ${env.JOB_NAME} - ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
}