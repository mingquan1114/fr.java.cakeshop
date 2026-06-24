$host = '116.62.23.54'
$username = 'root'
$password = '12367dzcxvZCY'

Add-Type -Path "C:\Program Files\dotnet\shared\Microsoft.NETCore.App\3.1.0\System.Net.Sockets.dll"

try {
    $ssh = New-Object System.Net.Sockets.TCPClient($host, 22)
    Write-Host "SSH 连接成功"
    $ssh.Close()
} catch {
    Write-Host "SSH 连接失败: $_"
    exit 1
}

Write-Host "=== 使用 OpenSSH 命令 ==="
$env:SSH_PASS = $password
& "C:\Windows\System32\OpenSSH\ssh.exe" -o StrictHostKeyChecking=no root@$host "yum update -y && yum install -y docker"