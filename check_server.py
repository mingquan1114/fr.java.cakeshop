import paramiko
import time

def run_ssh_command(host, username, password, command):
    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    try:
        ssh.connect(host, username=username, password=password, timeout=30)
        stdin, stdout, stderr = ssh.exec_command(command)
        output = stdout.read().decode('utf-8')
        error = stderr.read().decode('utf-8')
        return output, error
    finally:
        ssh.close()

host = '116.62.23.54'
username = 'root'
password = '12367dzcxvZCY'

commands = [
    'docker ps',
    'netstat -tlnp',
    'systemctl status firewalld',
    'iptables -L | head -20',
    'cat /tmp/deploy_result.txt 2>/dev/null || echo "No result file"',
    'docker logs cake_app 2>/dev/null | tail -30 || echo "No logs"',
    'docker logs cake_mysql 2>/dev/null | tail -20 || echo "No logs"'
]

for cmd in commands:
    print(f"\n=== Executing: {cmd} ===")
    try:
        output, error = run_ssh_command(host, username, password, cmd)
        print("Output:")
        print(output)
        if error:
            print("Error:")
            print(error)
    except Exception as e:
        print(f"Exception: {e}")
        time.sleep(5)