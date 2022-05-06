pipeline {
    agent any
    tools {
        maven '3.8.5'
    }
    environment {
        AWS_ACCOUNT_ID="486278204635"
        AWS_DEFAULT_REGION="eu-west-1"
        IMAGE_REPO_NAME="kafka-twitter-stream"
        IMAGE_TAG="latest"
        REPOSITORY_URI = "486278204635.dkr.ecr.eu-west-1.amazonaws.com/kafka-twitter-stream"
    }

    stages {
         stage('AWS ECR Login') {
            steps {
                script {
                sh "aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
                }
            }
        }

        stage('Cloning Git') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '', url: 'https://github.com/parmar049/kafka-producer.git']]])
            }
        }

        stage('Build and Test') {
            steps{
                script {
                  sh "mvn clean install"
                }
            }
        }

        // Building Docker images
        stage('Building Docker image') {
            steps{
                script {
                  dockerImage = docker.build "${IMAGE_REPO_NAME}:${IMAGE_TAG}"
                }
            }
        }

        // Uploading Docker images into AWS ECR
        stage('Pushing to ECR') {
             steps{
                 script {
                        sh "docker tag ${IMAGE_REPO_NAME}:${IMAGE_TAG} ${REPOSITORY_URI}:$IMAGE_TAG"
                        sh "docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${IMAGE_TAG}"
                 }
             }
        }
    }

}