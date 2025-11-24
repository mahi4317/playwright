#!/bin/bash

# Jenkins Docker Setup Script
# This script helps you set up Jenkins with Docker for Playwright tests

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}🚀 Jenkins Docker Setup for Playwright Tests${NC}"
echo "=================================================="

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker is not installed. Please install Docker first.${NC}"
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}❌ Docker Compose is not installed. Please install Docker Compose first.${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Docker and Docker Compose are installed${NC}"

# Create .env file if it doesn't exist
if [ ! -f .env ]; then
    echo -e "${YELLOW}📝 Creating .env file from .env.example...${NC}"
    cp .env.example .env
    echo -e "${YELLOW}⚠️  Please edit .env file with your configuration before proceeding${NC}"
    echo -e "${YELLOW}   Press Enter to continue or Ctrl+C to exit${NC}"
    read
fi

# Build the Playwright Jenkins agent image
echo -e "${GREEN}🔨 Building Playwright Jenkins agent Docker image...${NC}"
docker-compose build jenkins-agent-playwright

# Start Jenkins containers
echo -e "${GREEN}🚀 Starting Jenkins containers...${NC}"
docker-compose up -d

# Wait for Jenkins to be ready
echo -e "${YELLOW}⏳ Waiting for Jenkins to start (this may take a minute)...${NC}"
sleep 30

# Get Jenkins initial admin password
if docker exec jenkins-master test -f /var/jenkins_home/secrets/initialAdminPassword; then
    INITIAL_PASSWORD=$(docker exec jenkins-master cat /var/jenkins_home/secrets/initialAdminPassword)
    echo ""
    echo -e "${GREEN}=================================================="
    echo "Jenkins is starting up!"
    echo "=================================================="
    echo ""
    echo -e "Access Jenkins at: ${YELLOW}http://localhost:8080${NC}"
    echo ""
    echo -e "Initial Admin Password: ${YELLOW}${INITIAL_PASSWORD}${NC}"
    echo ""
    echo "Next Steps:"
    echo "1. Open http://localhost:8080 in your browser"
    echo "2. Use the password above to unlock Jenkins"
    echo "3. Install suggested plugins"
    echo "4. Create your first admin user"
    echo "5. Configure the Playwright Jenkins agent"
    echo ""
    echo -e "${GREEN}=================================================="${NC}
else
    echo -e "${YELLOW}⚠️  Jenkins is already configured. Access it at http://localhost:8080${NC}"
fi

# Show container status
echo ""
echo -e "${GREEN}📊 Container Status:${NC}"
docker-compose ps

echo ""
echo -e "${GREEN}✅ Setup complete!${NC}"
echo ""
echo "Useful commands:"
echo "  View logs:        docker-compose logs -f"
echo "  Stop Jenkins:     docker-compose down"
echo "  Restart:          docker-compose restart"
echo "  Remove all:       docker-compose down -v"
