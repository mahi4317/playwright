pipeline {
    agent any
    
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'qa', 'prod'], description: 'Select environment to run tests')
        choice(name: 'BROWSER', choices: ['chromium', 'firefox', 'webkit'], description: 'Select browser for testing')
        string(name: 'TEST_CLASS', defaultValue: '', description: 'Specific test class to run (leave empty for all tests)')
    }
    
    environment {
        CI = 'true'
        MAVEN_OPTS = '-Xmx1024m'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo "🔄 Checking out code..."
                checkout scm
            }
        }
        
        stage('Build & Test') {
            steps {
                echo "🔨 Building and running tests..."
                sh '''
                    # Install Playwright browsers
                    mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps" || true
                    
                    # Run tests
                    mvn clean test -Denv=${ENVIRONMENT}
                '''
            }
        }
        
        stage('Publish Results') {
            steps {
                echo "📊 Publishing test results..."
                junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                archiveArtifacts artifacts: 'target/surefire-reports/**/*', allowEmptyArchive: true
            }
        }
    }
    
    post {
        always {
            echo "🧹 Build finished!"
        }
        success {
            echo "✅ Tests passed successfully!"
        }
        failure {
            echo "❌ Tests failed!"
        }
    }
}
