$sshHost = '116.62.23.54'
$username = 'root'
$password = '12367dzcxvZCY'

function Run-SshCommand {
    param(
        [string]$command
    )
    
    $sshPath = "C:\Windows\System32\OpenSSH\ssh.exe"
    $arguments = "-o StrictHostKeyChecking=no root@$sshHost `"$command`""
    
    $psi = New-Object System.Diagnostics.ProcessStartInfo
    $psi.FileName = $sshPath
    $psi.Arguments = $arguments
    $psi.RedirectStandardInput = $true
    $psi.RedirectStandardOutput = $true
    $psi.RedirectStandardError = $true
    $psi.UseShellExecute = $false
    
    $process = New-Object System.Diagnostics.Process
    $process.StartInfo = $psi
    $process.Start() | Out-Null
    
    Start-Sleep -Seconds 3
    $process.StandardInput.WriteLine($password)
    $process.StandardInput.Flush()
    
    $output = $process.StandardOutput.ReadToEnd()
    $errOutput = $process.StandardError.ReadToEnd()
    $process.WaitForExit()
    
    Write-Host "=== Command: $command ==="
    Write-Host "Exit code: $($process.ExitCode)"
    if ($output) { Write-Host "Output:`n$output" }
    if ($errOutput) { Write-Host "Error:`n$errOutput" }
    Write-Host ""
    
    return $process.ExitCode
}

Write-Host "=== 步骤4.2：安装 Docker 和 Docker Compose ==="
Write-Host ""

Run-SshCommand "yum update -y && yum install -y docker"
Run-SshCommand "systemctl start docker && systemctl enable docker"
Run-SshCommand "curl -L https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-`$(uname -s)-`$(uname -m) -o /usr/local/bin/docker-compose && chmod +x /usr/local/bin/docker-compose"
Run-SshCommand "docker --version && docker-compose --version"

Write-Host "=== Docker 和 Docker Compose 安装完成 ==="