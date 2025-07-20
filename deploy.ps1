# 服务器配置区（需修改）
$SERVER_IP = "192.168.123.3"     # 服务器IP
$SSH_USER = "xww"       # SSH用户名
$REMOTE_DIR = "/home/xww/lucky"      # 服务器部署目录
$PROCESS_NAME = "lucky.jar"
$TIMESTAMP = Get-Date -Format "yyyyMMdd-HHmmss"


# 登录服务器删除文件
ssh "${SSH_USER}@${SERVER_IP}" @'
cd ~/lucky
# 停止旧进程（如果有）
PID=$(ps -ef | grep lucky | grep -v grep | awk '{print $2}')
if [ -n "$PID" ]; then
    kill -9 $PID >>/dev/null 2>&1
    sleep 2
fi
rm lucky.jar   >>/dev/null 2>&1
'@

cd target
# 传输文件到服务器
scp -r lucky.jar ${SSH_USER}@${SERVER_IP}:${REMOTE_DIR}/$PROCESS_NAME
if (-not $?) {
Write-Host "❌ 文件传输失败"
exit 1
}

# 执行远程部署命令
ssh "${SSH_USER}@${SERVER_IP}" @'
cd ~/lucky
nohup java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 lucky.jar  > l.log 2>&1 &
'@