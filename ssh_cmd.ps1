$sshHost = '116.62.23.54'
$username = 'root'
$password = '12367dzcxvZCY'
$command = "mkdir -p ~/.ssh && cat /tmp/id_rsa.pub >> ~/.ssh/authorized_keys && chmod 700 ~/.ssh && chmod 600 ~/.ssh/authorized_keys"

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

Start-Sleep -Seconds 2
$process.StandardInput.WriteLine($password)
$process.StandardInput.Flush()

$output = $process.StandardOutput.ReadToEnd()
$errOutput = $process.StandardError.ReadToEnd()
$process.WaitForExit()

Write-Host "Output: $output"
Write-Host "Error: $errOutput"
Write-Host "Exit code: $($process.ExitCode)"