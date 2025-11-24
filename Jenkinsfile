pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9.0' // Configure in Jenkins Global Tool Configuration
        jdk 'JDK-17'        // Configure in Jenkins Global Tool Configuration
    }
    
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'qa', 'prod'], description: 'Select environment to run tests')
        choice(name: 'BROWSER', choices: ['chromium', 'firefox', 'webkit', 'all'], description: 'Select browser for testing')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run tests in headless mode')
        string(name: 'TEST_CLASS', defaultValue: '', description: 'Specific test class to run (leave empty for all tests)')
    }
    
    environment {
        // Maven settings
        MAVEN_OPTS = '-Xmx1024m -XX:MaxPermSize=512m'
        
        // Playwright settings
        PLAYWRIGHT_BROWSERS_PATH = "${WORKSPACE}/.playwright"
        
        // Test environment
        TEST_ENV = "${params.ENVIRONMENT}"
        TEST_BROWSER = "${params.BROWSER}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "🔄 Checking out code from repository..."
                    // Clean workspace before checkout
                    cleanWs()
                    checkout scm
                }
            }
        }
        
        stage('Environment Info') {
            steps {
                script {
                    echo "📋 Build Information:"
                    echo "Environment: ${params.ENVIRONMENT}"
                    echo "Browser: ${params.BROWSER}"
                    echo "Headless Mode: ${params.HEADLESS}"
                    echo "Test Class: ${params.TEST_CLASS ?: 'All Tests'}"
                    
                    sh '''
                        echo "Java Version:"
                        java -version
                        echo "\nMaven Version:"
                        mvn -version
                        echo "\nNode Version:"
                        node --version || echo "Node not found"
                    '''
                }
            }
        }
        
        stage('Install Dependencies') {
            steps {
                script {
                    echo "📦 Installing Maven dependencies..."
                    sh 'mvn clean install -DskipTests'
                }
            }
        }
        
        stage('Install Playwright Browsers') {
            steps {
                script {
                    echo "🌐 Installing Playwright browsers..."
                    
                    if (params.BROWSER == 'all') {
                        sh 'mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"'
                    } else {
                        sh "mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args='install ${params.BROWSER}'"
                    }
                }
            }
        }
        
        stage('Compile Tests') {
            steps {
                script {
                    echo "🔨 Compiling test code..."
                    sh 'mvn test-compile'
                }
            }
        }
        
        stage('Run Tests') {
            steps {
                script {
                    echo "🧪 Running Playwright tests..."
                    
                    def testCommand = "mvn clean test -Denv=${params.ENVIRONMENT}"
                    
                    // Add specific test class if provided
                    if (params.TEST_CLASS) {
                        testCommand += " -Dtest=${params.TEST_CLASS}"
                    }
                    
                    // Add browser parameter if not 'all'
                    if (params.BROWSER != 'all') {
                        testCommand += " -Dbrowser=${params.BROWSER}"
                    }
                    
                    // Add headless parameter
                    testCommand += " -Dheadless=${params.HEADLESS}"
                    
                    echo "Executing: ${testCommand}"
                    
                    // Run tests and capture results (continue on failure)
                    catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                        sh testCommand
                    }
                }
            }
        }
        
        stage('Generate Reports') {
            steps {
                script {
                    echo "📊 Generating test reports..."
                    
                    // Archive TestNG reports
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/surefire-reports',
                        reportFiles: 'index.html',
                        reportName: 'TestNG HTML Report',
                        reportTitles: 'Playwright Test Results'
                    ])
                }
            }
        }
        
        stage('Archive Artifacts') {
            steps {
                script {
                    echo "📁 Archiving test artifacts..."
                    
                    // Archive test reports
                    archiveArtifacts artifacts: 'target/surefire-reports/**/*', 
                                   fingerprint: true,
                                   allowEmptyArchive: true
                    
                    // Archive logs
                    archiveArtifacts artifacts: 'target/logs/**/*.log', 
                                   fingerprint: true,
                                   allowEmptyArchive: true
                    
                    // Archive screenshots if any
                    archiveArtifacts artifacts: 'target/screenshots/**/*', 
                                   fingerprint: true,
                                   allowEmptyArchive: true
                }
            }
        }
        
        stage('Publish Test Results') {
            steps {
                script {
                    echo "📈 Publishing test results..."
                    
                    // Publish TestNG results
                    step([$class: 'Publisher', 
                          reportFilenamePattern: 'target/surefire-reports/testng-results.xml'])
                    
                    // Publish JUnit results (Surefire generates these too)
                    junit testResults: 'target/surefire-reports/*.xml',
                          allowEmptyResults: true,
                          skipPublishingChecks: false
                }
            }
        }
        
        stage('Code Quality Analysis') {
            when {
                expression { return params.ENVIRONMENT == 'dev' }
            }
            steps {
                script {
                    echo "🔍 Running code quality checks..."
                    
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                        sh 'mvn checkstyle:checkstyle || true'
                    }
                }
            }
        }
    }
    
    post {
        always {
            script {
                echo "🧹 Cleaning up..."
                
                // Clean up Playwright browsers cache (optional)
                // sh 'rm -rf ${PLAYWRIGHT_BROWSERS_PATH}'
                
                // Send notifications (configure as needed)
                emailext(
                    subject: "Jenkins Build ${currentBuild.result}: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                    body: """
                        <h2>Build ${currentBuild.result}</h2>
                        <p><b>Job:</b> ${env.JOB_NAME}</p>
                        <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                        <p><b>Environment:</b> ${params.ENVIRONMENT}</p>
                        <p><b>Browser:</b> ${params.BROWSER}</p>
                        <p><b>Duration:</b> ${currentBuild.durationString}</p>
                        <p><b>URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        <p>Check console output at <a href="${env.BUILD_URL}console">${env.BUILD_URL}console</a></p>
                    """,
                    to: 'team@example.com', // Configure your email
                    mimeType: 'text/html',
                    attachLog: true
                )
            }
        }
        
        success {
            echo "✅ Build completed successfully!"
            // Slack notification example (requires Slack plugin)
            // slackSend color: 'good', message: "Build Successful: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        }
        
        failure {
            echo "❌ Build failed!"
            // Slack notification example
            // slackSend color: 'danger', message: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        }
        
        unstable {
            echo "⚠️ Build is unstable!"
            // Slack notification example
            // slackSend color: 'warning', message: "Build Unstable: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        }
    }
}
