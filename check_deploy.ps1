$sshHost = '116.62.23.54'
$password = '12367dzcxvZCY'

$commands = @(
    "docker --version",
    "docker-compose --version",
    "docker ps",
    "ls -la /opt/cake-shop/",
    "cat /opt/cake-shop/docker-compose.yml"
)

foreach ($cmd in $commands) {
    Write-Host "=== Running: $cmd ==="
    
    $psi = New-Object System.Diagnostics.ProcessStartInfo
    $psi.FileName = "C:\Windows\System32\OpenSSH\ssh.exe"
    $psi.Arguments = "-o StrictHostKeyChecking=no root@$sshHost `"$cmd`""
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
    
    $output = $process.StandardOutput.ReadToEnd()
    $error = $process.StandardError.ReadToEnd()
    
    Write-Host "Exit code: $($process.ExitCode)"
    if ($output) { Write-Host "Output:`n$output" }
    if ($error) { Write-Host "Error:`n$error" }
    Write-Host ""
}