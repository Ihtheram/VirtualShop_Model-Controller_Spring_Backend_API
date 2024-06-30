pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "mavenauto"
    }

    stages {
        stage('Build Maven') {
            steps {
                // Get some code from a GitHub repositoryS
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'Ihtheram', url: 'https://github.com/Ihtheram/VirtualShop_Model-Controller_Spring_Backend_API']])

                // To run Maven on a Windows agent, use
                //bat "mvn -Dmaven.test.failure.ignore=true clean package"S
                bat 'mvn clean install'

            }

            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    bat 'docker build -t ihtheram/virtualshop .'
                }
            }
        }
        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'virtualshop_password', variable: 'virtualshop_password')]) {
                            bat "docker login -u ihtheram -p ${virtualshop_password} docker.io"
                            bat 'docker push ihtheram/virtualshop'
                    }
                }
            }
        }


    }
}
