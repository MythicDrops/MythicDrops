node {
    checkout scm
    def mvnHome = tool 'Maven 3.3.9'
    sh "${mvnHome}/bin/mvn -B clean deploy"
}