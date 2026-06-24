import paramiko
import time

def run_ssh_command(host, username, password, command):
    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh.connect(host, username=username, password=password, timeout=30)
    
    stdin, stdout, stderr = ssh.exec_command(command)
    
    stdout.channel.set_combine_stderr(True)
    output = ""
    while not stdout.channel.closed:
        if stdout.channel.recv_ready():
            output += stdout.channel.recv(1024).decode('utf-8', errors='ignore')
            print(output[-2000:], end='')
        time.sleep(0.5)
    
    ssh.close()
    return output

host = '116.62.23.54'
username = 'root'
password = '12367dzcxvZCY'

print("=== 步骤1：更新系统并安装 Docker ===")
output = run_ssh_command(host, username, password, 'yum update -y && yum install -y docker')
print("\n" + "="*60 + "\n")

print("=== 步骤2：启动 Docker 服务 ===")
output = run_ssh_command(host, username, password, 'systemctl start docker && systemctl enable docker')
print("\n" + "="*60 + "\n")

print("=== 步骤3：安装 Docker Compose ===")
output = run_ssh_command(host, username, password, 'curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && chmod +x /usr/local/bin/docker-compose')
print("\n" + "="*60 + "\n")

print("=== 步骤4：验证安装 ===")
output = run_ssh_command(host, username, password, 'docker --version && docker-compose --version')
print("\n" + "="*60 + "\n")

print("=== Docker 和 Docker Compose 安装完成 ===")