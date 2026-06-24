$sshHost = '116.62.23.54'
$password = '12367dzcxvZCY'

$scriptContent = @'
#!/bin/bash

echo "=== 1. 检查 firewalld ==="
systemctl status firewalld

echo "=== 2. 关闭防火墙 ==="
systemctl stop firewalld
systemctl disable firewalld

echo "=== 3. 检查 iptables ==="
iptables -L

echo "=== 4. 清空 iptables ==="
iptables -F

echo "=== 5. 检查端口 ==="
netstat -tlnp | grep 8080 || ss -tlnp | grep 8080

echo "=== 6. 检查 Docker 容器 ==="
docker ps

echo "=== 7. 如果容器未运行，重启 ==="
if ! docker ps | grep cake_app; then
  echo "Restarting containers..."
  docker stop cake_mysql cake_app 2>/dev/null || true
  docker rm cake_mysql cake_app 2>/dev/null || true
  
  docker run -d --name cake_mysql \
    -e MYSQL_ROOT_PASSWORD=root \
    -e MYSQL_DATABASE=cakeshop \
    -p 3306:3306 \
    mysql:5.7 \
    --character-set-server=utf8mb4 \
    --collation-server=utf8mb4_unicode_ci
  
  sleep 30
  
  docker exec cake_mysql mysql -u root -proot cakeshop -e "SHOW TABLES;" || echo "Database not ready"
  
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
  
  sleep 60
fi

echo "=== 8. 最终状态 ==="
docker ps
netstat -tlnp | grep 8080 || ss -tlnp | grep 8080

echo "=== DONE ==="
'@

$scriptPath = "$env:TEMP\fix_fw.sh"
$scriptContent | Out-File -FilePath $scriptPath -Encoding UTF8

Write-Host "=== 上传脚本 ==="
$psi = New-Object System.Diagnostics.ProcessStartInfo
$psi.FileName = "C:\Windows\System32\OpenSSH\scp.exe"
$psi.Arguments = "-o StrictHostKeyChecking=no `"$scriptPath`" root@${sshHost}:/tmp/fix_fw.sh"
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

Write-Host "=== 执行脚本 ==="
$psi = New-Object System.Diagnostics.ProcessStartInfo
$psi.FileName = "C:\Windows\System32\OpenSSH\ssh.exe"
$psi.Arguments = "-o StrictHostKeyChecking=no root@${sshHost} `"bash /tmp/fix_fw.sh`""
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