$sshHost = '116.62.23.54'
$password = '12367dzcxvZCY'

$commands = @(
    "docker ps",
    "docker ps -a",
    "cd /opt/cake-shop && docker-compose ps",
    "cd /opt/cake-shop && docker-compose logs cake-app --tail=30",
    "cd /opt/cake-shop && docker-compose logs mysql --tail=10",
    "netstat -tlnp | grep 8080",
    "netstat -tlnp | grep 3306"
)

foreach ($cmd in $commands) {
    Write-Host "="*60
    Write-Host "Running: $cmd"
    Write-Host "="*60
    
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
    
    if ($output) { Write-Host "Output:`n$output" }
    if ($error) { Write-Host "Error:`n$error" }
}