$sshHost = '116.62.23.54'
$password = '12367dzcxvZCY'

$scriptContent = @'
#!/bin/bash
set -e

echo "=== 1. 停止旧容器 ==="
docker stop cake_mysql cake_app 2>/dev/null || true
docker rm cake_mysql cake_app 2>/dev/null || true

echo "=== 2. 清理旧镜像 ==="
docker rmi cake-shop_cake-app 2>/dev/null || true

echo "=== 3. 创建目录 ==="
mkdir -p /opt/cake-app

echo "=== 4. 启动 MySQL ==="
docker run -d --name cake_mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=cakeshop \
  -p 3306:3306 \
  mysql:5.7 \
  --character-set-server=utf8mb4 \
  --collation-server=utf8mb4_unicode_ci

echo "=== 5. 等待 MySQL ==="
sleep 30

echo "=== 6. 导入数据库 ==="
docker exec cake_mysql mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS cakeshop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

if [ -f /opt/cake-shop/cakeshop.sql ]; then
  docker cp /opt/cake-shop/cakeshop.sql cake_mysql:/tmp/cakeshop.sql
  docker exec cake_mysql mysql -u root -proot cakeshop < /tmp/cakeshop.sql
  echo "Database imported from /opt/cake-shop"
else
  echo "ERROR: cakeshop.sql not found!"
  exit 1
fi

echo "=== 7. 检查数据库 ==="
docker exec cake_mysql mysql -u root -proot cakeshop -e "SHOW TABLES;"

echo "=== 8. 启动应用 ==="
docker run -d --name cake_app \
  --link cake_mysql:cake_mysql \
  -v /opt/cake-app:/app \
  -w /app \
  -p 8080:8080 \
  openjdk:8-jre \
  java -jar demo-0.0.1-SNAPSHOT.jar \
    --spring.datasource.url=jdbc:mysql://cake_mysql:3306/cakeshop?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai \
    --spring.datasource.username=root \
    --spring.datasource.password=root

echo "=== 9. 等待应用 ==="
sleep 60

echo "=== 10. 检查状态 ==="
docker ps

echo "=== 11. 检查端口 ==="
netstat -tlnp | grep 8080 || ss -tlnp | grep 8080

echo "=== 12. 应用日志 ==="
docker logs cake_app --tail=20

echo "=== DONE ==="
'@

$scriptPath = "$env:TEMP\deploy.sh"
$scriptContent | Out-File -FilePath $scriptPath -Encoding UTF8

Write-Host "=== 上传部署脚本 ==="
$psi = New-Object System.Diagnostics.ProcessStartInfo
$psi.FileName = "C:\Windows\System32\OpenSSH\scp.exe"
$psi.Arguments = "-o StrictHostKeyChecking=no `"$scriptPath`" root@${sshHost}:/tmp/deploy.sh"
$psi.RedirectStandardInput = $true
$psi.RedirectStandardOutput = $true
$psi.RedirectStandardError = $true
$psi.UseShellExecute = $false
$psi.CreateNoWindow = $true
$process = New-Object System.Diagnostics.Process
$process.StartInfo = $psi
$process.Start() | Out-Null
Start-Sleep -Seconds 3
$process.StandardInput.WriteLine($password)
$process.StandardInput.Flush()
$process.WaitForExit(60000)
Write-Host "SCP Exit: $($process.ExitCode)"

Write-Host "=== 上传 JAR 文件 ==="
$jarPath = "$PWD\target\demo-0.0.1-SNAPSHOT.jar"
$psi = New-Object System.Diagnostics.ProcessStartInfo
$psi.FileName = "C:\Windows\System32\OpenSSH\scp.exe"
$psi.Arguments = "-o StrictHostKeyChecking=no `"$jarPath`" root@${sshHost}:/opt/cake-app/demo-0.0.1-SNAPSHOT.jar"
$psi.RedirectStandardInput = $true
$psi.RedirectStandardOutput = $true
$psi.RedirectStandardError = $true
$psi.UseShellExecute = $false
$psi.CreateNoWindow = $true
$process = New-Object System.Diagnostics.Process
$process.StartInfo = $psi
$process.Start() | Out-Null
Start-Sleep -Seconds 5
$process.StandardInput.WriteLine($password)
$process.StandardInput.Flush()
$process.WaitForExit(120000)
Write-Host "SCP JAR Exit: $($process.ExitCode)"

Write-Host "=== 执行部署脚本 ==="
$psi = New-Object System.Diagnostics.ProcessStartInfo
$psi.FileName = "C:\Windows\System32\OpenSSH\ssh.exe"
$psi.Arguments = "-o StrictHostKeyChecking=no root@${sshHost} `"bash /tmp/deploy.sh`""
$psi.RedirectStandardInput = $true
$psi.RedirectStandardOutput = $true
$psi.RedirectStandardError = $true
$psi.UseShellExecute = $false
$psi.CreateNoWindow = $true
$process = New-Object System.Diagnostics.Process
$process.StartInfo = $psi
$process.Start() | Out-Null
Start-Sleep -Seconds 3
$process.StandardInput.WriteLine($password)
$process.StandardInput.Flush()
$process.WaitForExit(600000)

$output = $process.StandardOutput.ReadToEnd()
$error = $process.StandardError.ReadToEnd()
Write-Host "=== Server Output ==="
Write-Host $output
if ($error) { Write-Host "Error:`n$error" }
Write-Host "Exit: $($process.ExitCode)"