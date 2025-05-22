pipeline {
    agent any
    environment {
        EC2_IP = 'your-ec2-public-ip'
        SSH_KEY = credentials('ansible-key')
    }
    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/your-repo.git', branch: 'main'
            }
        }
        stage('Build & Test') {
            steps {
                script {
                    // Node.js
                    sh 'cd node-app && npm install && npm test'
                    // Python/Flask
                    sh 'cd python-app && python3 -m venv venv && source venv/bin/activate && pip install -r requirements.txt && pytest'
                    // PHP
                    sh 'cd php-app && composer install && phpunit'
                }
            }
        }
        stage('Deploy to EC2') {
            steps {
                script {
                    // Node.js
                    sh "scp -i ${SSH_KEY} -r node-app/* ubuntu@${EC2_IP}:/var/www/node-app/"
                    // Python/Flask
                    sh "scp -i ${SSH_KEY} -r python-app/* ubuntu@${EC2_IP}:/var/www/python-app/"
                    // PHP
                    sh "scp -i ${SSH_KEY} -r php-app/* ubuntu@${EC2_IP}:/var/www/php-app/"
                }
            }
        }
    }
}