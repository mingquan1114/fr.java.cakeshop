$sshHost = '116.62.23.54'
$username = 'root'
$password = '12367dzcxvZCY'
$localPath = Get-Location

function Invoke-SshCommand {
    param([string]$command, [int]$timeout=300)
    
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
    
    $outputBuilder = New-Object System.Text.StringBuilder
    $errorBuilder = New-Object System.Text.StringBuilder
    
    $outputEvent = Register-ObjectEvent -InputObject $process -EventName OutputDataReceived -Action {
        $event.MessageData.AppendLine($event.SourceEventArgs.Data)
    } -MessageData $outputBuilder
    
    $errorEvent = Register-ObjectEvent -InputObject $process -EventName ErrorDataReceived -Action {
        $event.MessageData.AppendLine($event.SourceEventArgs.Data)
    } -MessageData $errorBuilder
    
    $process.Start() | Out-Null
    $process.BeginOutputReadLine()
    $process.BeginErrorReadLine()
    
    Start-Sleep -Seconds 3
    $process.StandardInput.WriteLine($password)
    $process.StandardInput.Flush()
    
    $process.WaitForExit($timeout * 1000)
    
    Write-Host "=== Command: $command ==="
    Write-Host "Exit code: $($process.ExitCode)"
    $output = $outputBuilder.ToString()
    $error = $errorBuilder.ToString()
    if ($output) { Write-Host "Output:`n$output" }
    if ($error) { Write-Host "Error:`n$error" }
    Write-Host ""
    
    Unregister-Event -SourceIdentifier $outputEvent.Name
    Unregister-Event -SourceIdentifier $errorEvent.Name
    
    return $process.ExitCode
}

Write-Host "======================================"
Write-Host "=== 阿里云 ECS 部署 - 蛋糕管理系统 ==="
Write-Host "======================================"
Write-Host ""

Write-Host "===== 步骤4.2：安装 Docker 和 Docker Compose ====="
Write-Host ""

Invoke-SshCommand "yum update -y && yum install -y docker" 300
Invoke-SshCommand "systemctl start docker && systemctl enable docker" 60
Invoke-SshCommand "curl -L https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-`$(uname -s)-`$(uname -m) -o /usr/local/bin/docker-compose && chmod +x /usr/local/bin/docker-compose" 120
Invoke-SshCommand "docker --version && docker-compose --version" 60

Write-Host "===== 步骤4.3：上传项目代码 ====="
Write-Host ""

Invoke-SshCommand "mkdir -p /opt/cake-shop" 60

Write-Host "Uploading files via scp..."
$scpArgs = "-o StrictHostKeyChecking=no -r `"$localPath`" root@${sshHost}:/opt/cake-shop/"
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
$process.WaitForExit(600000)

Write-Host "SCP Exit code: $($process.ExitCode)"
Write-Host ""

Write-Host "===== 步骤4.4：构建并运行容器 ====="
Write-Host ""

Invoke-SshCommand "cd /opt/cake-shop && docker-compose up -d --build" 600

Write-Host "===== 验证部署 ====="
Write-Host ""

Invoke-SshCommand "docker-compose ps" 60
Invoke-SshCommand "docker-compose logs cake-app --tail=20" 60

Write-Host "======================================"
Write-Host "=== 部署完成！ ==="
Write-Host "访问地址: http://${sshHost}:8080"
Write-Host "======================================"