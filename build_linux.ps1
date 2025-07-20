# 定义编译输出路径（根据实际项目调整）
$LINUX_OUTPUT = "./target/lucky.jar"  # 示例路径，替换为你的实际输出路径

# 执行 Maven 清理并打包
Write-Host "🔨 正在执行 Maven 编译..."
mvn  package

# 检查 Maven 编译是否成功（$? 表示上一条命令是否执行成功）
if (-not $?)
{
    Write-Host "❌ 编译失败：Maven 打包命令执行异常" -ForegroundColor Red
    exit 1
}

# 验证 Linux x64 输出文件是否存在
if (Test-Path -Path $LINUX_OUTPUT -PathType Leaf)
{
    Write-Host "✅ 编译成功！Linux x64 输出文件已生成：$LINUX_OUTPUT" -ForegroundColor Green
}
else
{
    Write-Host "❌ 编译失败：目标文件未生成（预期路径：$LINUX_OUTPUT）" -ForegroundColor Red
    exit 1
}

# 可选：后续操作（如启动测试、上传文件等）
Write-Host "🎉 构建流程完成，准备执行后续操作..."