pipeline {
    agent any

    tools {
        maven "maven_3.8.6"
    }

    stages {
        stage('Git Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'GitHubPasswordLocalSystem', url: 'https://github.com/JasonShuyinta/YouTorialSpringBackend.git']]])
            }
        }
        stage('Junit Tests') {
            steps {
                bat 'mvn -Dactive.profile=test test'
            }
            post {
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }
        stage('Build Artifacts') {
            steps {
                bat 'mvn clean install -Dmaven.test.skip=true  -Dactive.profile=prod'
            }
            post {
                success {
                    bat """
                    cd target
                    rename \"youtorial-backend.jar\" \"youtorial-backend-%BUILD_NUMBER%.jar\"
                    """
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
        stage('Deploy to S3') {
            steps {
                s3Upload(workingDir:'target',includePathPattern:'*.jar', bucket:'youtorial-artifact-repository', path:'builds')
            }
        }
        stage('Build Docker Image') {
            steps {
                bat 'docker build -t  jason9722/youtorial-backend-image .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([string(credentialsId: 'DockerId', variable: 'DockerPassword')]) {
                    bat 'docker login -u jason9722 -p %DockerPassword%'
                    bat 'docker push jason9722/youtorial-backend-image'
                }
            }
        }

        stage('Run shell script on EC2'){
            steps {
                   bat 'ssh -T ec2-user@%EC2-IP% "docker stop youtorialbackend"'
                   bat 'ssh -T ec2-user@%EC2-IP% "docker rm youtorialbackend"'
                   bat 'ssh -T ec2-user@%EC2-IP% "docker rmi jason9722/youtorial-backend-image"'
                   bat 'ssh -T ec2-user@%EC2-IP% "docker pull jason9722/youtorial-backend-image:latest"'
                   bat 'ssh -T ec2-user@%EC2-IP% "docker run -d -p 8000:8000 --name youtorialbackend jason9722/youtorial-backend-image:latest"'
            }
        }
    }
}