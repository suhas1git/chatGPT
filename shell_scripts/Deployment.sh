#!/bin/bash

# Deployment Script

# Set the project variables
PROJECT_NAME="my-web-app"
GIT_REPO="https://github.com/your-username/your-repo.git"
APP_PORT=8000

# Set the deployment directories
DEPLOY_DIR="/opt/$PROJECT_NAME"
APP_DIR="$DEPLOY_DIR/app"
NGINX_DIR="$DEPLOY_DIR/nginx"

# Ensure deployment directories exist
mkdir -p $APP_DIR $NGINX_DIR

# Clone the Git repository
echo "Cloning the Git repository..."
git clone $GIT_REPO $APP_DIR

# Build the Docker image
echo "Building the Docker image..."
docker build -t $PROJECT_NAME $APP_DIR

# Stop and remove any existing containers
echo "Stopping and removing existing containers..."
docker stop $PROJECT_NAME || true
docker rm $PROJECT_NAME || true

# Run the Docker container
echo "Starting the Docker container..."
docker run -d --name $PROJECT_NAME -p $APP_PORT:80 $PROJECT_NAME

# Update the NGINX configuration
echo "Updating NGINX configuration..."
cat <<EOF > $NGINX_DIR/nginx.conf
server {
    listen 80;
    server_name localhost;

    location / {
        proxy_pass http://localhost:$APP_PORT;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
    }
}
EOF

# Restart NGINX
echo "Restarting NGINX..."
docker restart nginx || true

echo "Deployment complete!"
