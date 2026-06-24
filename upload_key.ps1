$host = '116.62.23.54'
$username = 'root'
$password = '12367dzcxvZCY'
$pubKey = Get-Content "$env:USERPROFILE\.ssh\id_rsa.pub" -Raw

Add-Type @"
using System;
using System.Net.Sockets;
using System.Text;

public class SSHClient {
    public static string SendCommand(string host, int port, string username, string password, string command) {
        TcpClient tcp = new TcpClient(host, port);
        NetworkStream stream = tcp.GetStream();
        
        byte[] buffer = new byte[1024];
        int bytesRead = stream.Read(buffer, 0, buffer.Length);
        string response = Encoding.ASCII.GetString(buffer, 0, bytesRead);
        
        SendData(stream, "SSH-2.0-OpenSSH_7.4\r\n");
        bytesRead = stream.Read(buffer, 0, buffer.Length);
        response += Encoding.ASCII.GetString(buffer, 0, bytesRead);
        
        SendData(stream, "auth password\r\n");
        bytesRead = stream.Read(buffer, 0, buffer.Length);
        
        SendData(stream, username + "\r\n");
        bytesRead = stream.Read(buffer, 0, buffer.Length);
        
        SendData(stream, password + "\r\n");
        bytesRead = stream.Read(buffer, 0, buffer.Length);
        
        SendData(stream, command + "\r\n");
        bytesRead = stream.Read(buffer, 0, buffer.Length);
        response += Encoding.ASCII.GetString(buffer, 0, bytesRead);
        
        tcp.Close();
        return response;
    }
    
    private static void SendData(NetworkStream stream, string data) {
        byte[] buffer = Encoding.ASCII.GetBytes(data);
        stream.Write(buffer, 0, buffer.Length);
        stream.Flush();
    }
}
"@

$command = "mkdir -p ~/.ssh && echo '$pubKey' >> ~/.ssh/authorized_keys && chmod 700 ~/.ssh && chmod 600 ~/.ssh/authorized_keys"
$result = [SSHClient]::SendCommand($host, 22, $username, $password, $command)
Write-Host "Result: $result"