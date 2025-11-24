# Playwright Jenkins Agent with Java 17 and Playwright Support
FROM jenkins/agent:latest-jdk17

# Switch to root user for installations
USER root

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive \
    PLAYWRIGHT_BROWSERS_PATH=/ms-playwright

# Install system dependencies for Playwright
RUN apt-get update && apt-get install -y \
    # Core utilities
    wget \
    curl \
    git \
    unzip \
    # Node.js dependencies
    ca-certificates \
    gnupg \
    # Playwright browser dependencies
    libnss3 \
    libnspr4 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libcups2 \
    libdrm2 \
    libdbus-1-3 \
    libxkbcommon0 \
    libxcomposite1 \
    libxdamage1 \
    libxfixes3 \
    libxrandr2 \
    libgbm1 \
    libpango-1.0-0 \
    libcairo2 \
    libasound2 \
    libatspi2.0-0 \
    libwayland-client0 \
    # Fonts
    fonts-liberation \
    fonts-noto-color-emoji \
    # Additional dependencies
    xvfb \
    && rm -rf /var/lib/apt/lists/*

# Install Node.js 18.x (required for Playwright CLI)
RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash - \
    && apt-get install -y nodejs \
    && rm -rf /var/lib/apt/lists/*

# Verify installations
RUN echo "Java version:" && java -version && \
    echo "\nNode.js version:" && node --version && \
    echo "\nNPM version:" && npm --version

# Install Playwright CLI globally
RUN npm install -g playwright@latest

# Pre-install Playwright browsers (optional - saves time in pipeline)
# Uncomment the next line to pre-install browsers in the image
# RUN npx playwright install chromium firefox webkit

# Create directory for Playwright browsers with proper permissions
RUN mkdir -p /ms-playwright && \
    chown -R jenkins:jenkins /ms-playwright

# Create workspace directory
RUN mkdir -p /home/jenkins/workspace && \
    chown -R jenkins:jenkins /home/jenkins

# Switch back to jenkins user
USER jenkins

# Set working directory
WORKDIR /home/jenkins

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD java -version || exit 1

# Default command (inherited from base image)
