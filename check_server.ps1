$sshHost = '116.62.23.54'
$password = '12367dzcxvZCY'

$psi = New-Object System.Diagnostics.ProcessStartInfo
$psi.FileName = "C:\Windows\System32\OpenSSH\ssh.exe"
$psi.Arguments = "-o StrictHostKeyChecking=no root@${sshHost} `"docker ps; echo '===LOGS==='; docker logs cake_app 2>&1 | tail -50`""
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
$process.WaitForExit(120000)

$output = $process.StandardOutput.ReadToEnd()
$error = $process.StandardError.ReadToEnd()

Write-Host "=== Server Output ==="
Write-Host $output
if ($error) { Write-Host "Error:`n$error" }