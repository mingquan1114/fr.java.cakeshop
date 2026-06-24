$sshHost = '116.62.23.54'
$password = '12367dzcxvZCY'

function Run-Ssh {
    param([string]$command)
    
    $psi = New-Object System.Diagnostics.ProcessStartInfo
    $psi.FileName = "C:\Windows\System32\OpenSSH\ssh.exe"
    $psi.Arguments = "-o StrictHostKeyChecking=no root@$sshHost `"$command`""
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
    
    Write-Host "Command: $command"
    Write-Host "Exit: $($process.ExitCode)"
    if ($output) { Write-Host "Output:`n$output" }
    if ($error) { Write-Host "Error:`n$error" }
    Write-Host ""
    
    return $process.ExitCode
}

Write-Host "=== 1. 检查当前目录 ==="
Run-Ssh "ls -la /opt/cake-shop/"

Write-Host "=== 2. 查看 docker-compose.yml ==="
Run-Ssh "cat /opt/cake-shop/docker-compose.yml"

Write-Host "=== 3. 停止并删除旧容器 ==="
Run-Ssh "cd /opt/cake-shop && docker-compose down -v"

Write-Host "=== 4. 下载必要的 Docker 镜像 ==="
Run-Ssh "docker pull mysql:5.7"
Run-Ssh "docker pull maven:3.8.6-openjdk-8"
Run-Ssh "docker pull openjdk:8-jre-alpine"

Write-Host "=== 5. 构建并启动容器 ==="
Run-Ssh "cd /opt/cake-shop && docker-compose up -d --build"

Write-Host "=== 6. 等待容器启动 ==="
Run-Ssh "sleep 60"

Write-Host "=== 7. 检查容器状态 ==="
Run-Ssh "cd /opt/cake-shop && docker-compose ps"

Write-Host "=== 8. 查看应用日志 ==="
Run-Ssh "cd /opt/cake-shop && docker-compose logs cake-app --tail=50"

Write-Host "=== 9. 检查端口 ==="
Run-Ssh "netstat -tlnp | grep 8080"