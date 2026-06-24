$sshHost = '116.62.23.54'
$sshPath = "C:\Windows\System32\OpenSSH\ssh.exe"
$arguments = "-o StrictHostKeyChecking=no -i $env:USERPROFILE\.ssh\id_rsa root@$sshHost `"echo 'SSH key auth test'`""

$psi = New-Object System.Diagnostics.ProcessStartInfo
$psi.FileName = $sshPath
$psi.Arguments = $arguments
$psi.RedirectStandardOutput = $true
$psi.RedirectStandardError = $true
$psi.UseShellExecute = $false

$process = New-Object System.Diagnostics.Process
$process.StartInfo = $psi
$process.Start() | Out-Null

$output = $process.StandardOutput.ReadToEnd()
$errOutput = $process.StandardError.ReadToEnd()
$process.WaitForExit()

Write-Host "Exit code: $($process.ExitCode)"
Write-Host "Output: $output"
Write-Host "Error: $errOutput"