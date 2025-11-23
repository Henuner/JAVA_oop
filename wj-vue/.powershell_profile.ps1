# 项目专用的 npm 配置
$env:npm_config_cache = "D:\npm-cache"
$env:npm_config_legacy_peer_deps = "true"

Write-Host "=== White-Jotter 项目环境已配置 ===" -ForegroundColor Green
Write-Host "npm 缓存路径: $env:npm_config_cache" -ForegroundColor Yellow
Write-Host "legacy-peer-deps: $env:npm_config_legacy_peer_deps" -ForegroundColor Yellow