#!/bin/bash

SSH_HOST="116.62.23.54"
SSH_USER="root"
SSH_PASS="12367dzcxvZCY"

export SSHPASS="$SSH_PASS"

echo "=== Step 4.2: Install Docker and Docker Compose ==="
echo ""

echo "Running: yum update -y && yum install -y docker"
sshpass -p "$SSH_PASS" ssh -o StrictHostKeyChecking=no "$SSH_USER@$SSH_HOST" "yum update -y && yum install -y docker"
echo ""

echo "Running: systemctl start docker && systemctl enable docker"
sshpass -p "$SSH_PASS" ssh -o StrictHostKeyChecking=no "$SSH_USER@$SSH_HOST" "systemctl start docker && systemctl enable docker"
echo ""

echo "Running: Install Docker Compose"
sshpass -p "$SSH_PASS" ssh -o StrictHostKeyChecking=no "$SSH_USER@$SSH_HOST" "curl -L https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-\$(uname -s)-\$(uname -m) -o /usr/local/bin/docker-compose && chmod +x /usr/local/bin/docker-compose"
echo ""

echo "Running: Verify installation"
sshpass -p "$SSH_PASS" ssh -o StrictHostKeyChecking=no "$SSH_USER@$SSH_HOST" "docker --version && docker-compose --version"
echo ""

echo "=== Docker and Docker Compose installation completed ==="