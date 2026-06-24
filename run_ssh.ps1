$sshHost = '116.62.23.54'
$username = 'root'
$password = '12367dzcxvZCY'
$command = "yum update -y && yum install -y docker"

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

Start-Sleep -Seconds 2
$process.StandardInput.WriteLine($password)
$process.StandardInput.Flush()

$process.WaitForExit()

Write-Host "Exit code: $($process.ExitCode)"
Write-Host "Output:`n$($outputBuilder.ToString())"
Write-Host "Error:`n$($errorBuilder.ToString())"