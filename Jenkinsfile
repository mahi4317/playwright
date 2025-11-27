pipeline {
    agent any
    
    triggers {
        // Poll GitHub every 5 minutes for changes
        pollSCM('H/5 * * * *')
        
        // Or use GitHub webhook for instant triggers (recommended)
        // Configure webhook in GitHub repo settings pointing to: http://your-jenkins-url/github-webhook/
    }
    
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'qa', 'prod'], description: 'Select environment to run tests')
        choice(name: 'BROWSER', choices: ['chromium', 'firefox', 'webkit'], description: 'Select browser for testing')
        string(name: 'TEST_CLASS', defaultValue: '', description: 'Specific test class to run (leave empty for all tests)')
    }
    
    environment {
        CI = 'true'
        MAVEN_OPTS = '-Xmx1024m'
        PATH = "/opt/homebrew/bin:/opt/homebrew/opt/openjdk@17/bin:${env.PATH}"
        JAVA_HOME = '/opt/homebrew/opt/openjdk@17'
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
                    # Set paths
                    export PATH="/opt/homebrew/bin:/opt/homebrew/opt/openjdk@17/bin:$PATH"
                    export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
                    
                    # Verify tools
                    echo "Java version:"
                    /opt/homebrew/opt/openjdk@17/bin/java -version
                    echo "Maven version:"
                    /opt/homebrew/bin/mvn -version
                    
                    # Install Playwright browsers
                    /opt/homebrew/bin/mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps" || true
                    
                    # Run tests
                    /opt/homebrew/bin/mvn clean test -Denv=${ENVIRONMENT}
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
