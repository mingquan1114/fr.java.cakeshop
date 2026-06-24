$sshHost = '116.62.23.54'
$username = 'root'
$password = '12367dzcxvZCY'

$code = @"
using System;
using System.Net.Sockets;
using System.Text;

public class SimpleSSH {
    public static string Execute(string host, int port, string user, string pass, string cmd) {
        TcpClient tcp = new TcpClient(host, port);
        NetworkStream stream = tcp.GetStream();
        byte[] buf = new byte[4096];
        int len;
        
        len = stream.Read(buf, 0, buf.Length);
        string result = Encoding.ASCII.GetString(buf, 0, len);
        
        Send(stream, "SSH-2.0-TestClient_1.0\r\n");
        len = stream.Read(buf, 0, buf.Length);
        result += Encoding.ASCII.GetString(buf, 0, len);
        
        Send(stream, "AUTH_PASSWORD\r\n");
        len = stream.Read(buf, 0, buf.Length);
        
        Send(stream, user + "\r\n");
        len = stream.Read(buf, 0, buf.Length);
        
        Send(stream, pass + "\r\n");
        len = stream.Read(buf, 0, buf.Length);
        
        Send(stream, cmd + "\r\n");
        System.Threading.Thread.Sleep(3000);
        
        while (stream.DataAvailable) {
            len = stream.Read(buf, 0, buf.Length);
            result += Encoding.ASCII.GetString(buf, 0, len);
        }
        
        tcp.Close();
        return result;
    }
    
    static void Send(NetworkStream s, string data) {
        byte[] buf = Encoding.ASCII.GetBytes(data);
        s.Write(buf, 0, buf.Length);
    }
}
"@

Add-Type -TypeDefinition $code -Language CSharp

$output = [SimpleSSH]::Execute($sshHost, 22, $username, $password, "yum update -y && yum install -y docker")
Write-Host $output