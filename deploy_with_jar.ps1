$sshHost = '116.62.23.54'
$password = '12367dzcxvZCY'
$jarFile = "demo-0.0.1-SNAPSHOT.jar"
$localJarPath = "$PWD\target\$jarFile"

function Run-Ssh {
    param([string]$command, [int]$timeout=60)
    
    $psi = New-Object System.Diagnostics.ProcessStartInfo
    $psi.FileName = "C:\Windows\System32\OpenSSH\ssh.exe"
    $psi.Arguments = "-o StrictHostKeyChecking=no root@${sshHost} `"$command`""
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
    $process.WaitForExit($timeout * 1000)
    
    $output = $process.StandardOutput.ReadToEnd()
    $error = $process.StandardError.ReadToEnd()
    
    Write-Host "Command: $command"
    Write-Host "Exit: $($process.ExitCode)"
    if ($output) { Write-Host "Output:`n$output" }
    if ($error) { Write-Host "Error:`n$error" }
    Write-Host ""
    
    return $process.ExitCode
}

Write-Host "=== 1. 创建部署目录 ==="
Run-Ssh "mkdir -p /opt/cake-app"

Write-Host "=== 2. 停止旧容器 ==="
Run-Ssh "docker stop cake_mysql cake_app 2>/dev/null; docker rm cake_mysql cake_app 2>/dev/null"

Write-Host "=== 3. 启动 MySQL 容器 ==="
Run-Ssh "docker run -d --name cake_mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=cakeshop -p 3306:3306 mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci"

Write-Host "=== 4. 等待 MySQL 启动 ==="
Run-Ssh "sleep 30"

Write-Host "=== 5. 导入数据库 ==="
Run-Ssh "docker cp /opt/cake-shop/cakeshop.sql cake_mysql:/tmp/cakeshop.sql 2>/dev/null || echo 'File not found'"
Run-Ssh "docker exec cake_mysql mysql -u root -proot cakeshop -e 'CREATE DATABASE IF NOT EXISTS cakeshop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;'"
Run-Ssh "docker exec cake_mysql mysql -u root -proot cakeshop < /tmp/cakeshop.sql 2>/dev/null || echo 'Import from /tmp failed'"

Write-Host "=== 6. 上传 JAR 文件 ==="
Write-Host "Local JAR: $localJarPath"
$scpArgs = "-o StrictHostKeyChecking=no `"$localJarPath`" root@${sshHost}:/opt/cake-app/"
$psi = New-Object System.Diagnostics.ProcessStartInfo
$psi.FileName = "C:\Windows\System32\OpenSSH\scp.exe"
$psi.Arguments = $scpArgs
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
Write-Host "SCP Exit: $($process.ExitCode)"
Write-Host ""

Write-Host "=== 7. 启动应用容器 ==="
Run-Ssh "docker run -d --name cake_app --link cake_mysql:cake_mysql -v /opt/cake-app:/app -w /app -p 8080:8080 openjdk:8-jre java -jar $jarFile --spring.datasource.url=jdbc:mysql://cake_mysql:3306/cakeshop?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai --spring.datasource.username=root --spring.datasource.password=root"

Write-Host "=== 8. 等待应用启动 ==="
Run-Ssh "sleep 60"

Write-Host "=== 9. 检查容器状态 ==="
Run-Ssh "docker ps"

Write-Host "=== 10. 查看应用日志 ==="
Run-Ssh "docker logs cake_app --tail=30"

Write-Host "=== 11. 检查端口 ==="
Run-Ssh "netstat -tlnp | grep 8080"

Write-Host "=== 部署完成 ==="
Write-Host "访问地址: http://${sshHost}:8080"