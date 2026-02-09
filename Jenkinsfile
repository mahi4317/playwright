pipeline {
    agent {
        docker {
            image 'mcr.microsoft.com/playwright/java:v1.48.0-noble'
            // Official Playwright image with all browsers pre-installed
            // No volume mounts needed - Maven will cache inside container
        }
    }
    
    triggers {
        // Poll GitHub every 2 minutes for changes (more frequent for testing)
        pollSCM('H/2 * * * *')
    }
    
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
                    cd java-tests

                    # Verify environment (running in Playwright container)
                    echo "Java version:"
                    java -version
                    echo "Maven version:"
                    mvn -version
                    
                    # Verify Playwright browsers are pre-installed
                    echo "Playwright browsers location:"
                    ls -la /ms-playwright/ 2>/dev/null || echo "Browsers in default location"
                    
                    # Run tests - no browser installation needed!
                    mvn clean test -Denv=${ENVIRONMENT}
                '''
            }
        }
        
        stage('Publish Results') {
            steps {
                echo "📊 Publishing test results..."
                junit testResults: 'java-tests/target/surefire-reports/*.xml', allowEmptyResults: true
                archiveArtifacts artifacts: 'java-tests/target/surefire-reports/**/*', allowEmptyArchive: true
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
