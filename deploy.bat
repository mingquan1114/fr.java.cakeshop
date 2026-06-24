@echo off
setlocal

set SSH_HOST=116.62.23.54
set SSH_USER=root
set SSH_PASS=12367dzcxvZCY

echo === Step 4.2: Install Docker and Docker Compose ===
echo.

echo Running: yum update -y && yum install -y docker
echo %SSH_PASS% | C:\Windows\System32\OpenSSH\ssh.exe -o StrictHostKeyChecking=no root@%SSH_HOST% "yum update -y && yum install -y docker"
echo.

echo Running: systemctl start docker && systemctl enable docker
echo %SSH_PASS% | C:\Windows\System32\OpenSSH\ssh.exe -o StrictHostKeyChecking=no root@%SSH_HOST% "systemctl start docker && systemctl enable docker"
echo.

echo Running: Install Docker Compose
echo %SSH_PASS% | C:\Windows\System32\OpenSSH\ssh.exe -o StrictHostKeyChecking=no root@%SSH_HOST% "curl -L https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose && chmod +x /usr/local/bin/docker-compose"
echo.

echo Running: Verify installation
echo %SSH_PASS% | C:\Windows\System32\OpenSSH\ssh.exe -o StrictHostKeyChecking=no root@%SSH_HOST% "docker --version && docker-compose --version"
echo.

echo === Docker and Docker Compose installation completed ===

endlocal