# 地图编辑器脚本使用说明  

> Tiled Map Editor For Git User

## 配置

复制`template.config.ini`并改名成`config.ini`, 修改其中的内容:

```Python
[map-editor-settings]  

# 是否同步到工作目录
SYNC_TO_WORKBENCH=true

# 地图所在的目录(相对路径和绝对路径都OK)
MAP_PATH=..\design\X-X1美术资源\02场景\map_new

# 前端工程项目 地图TMX和美术资源 的路径(相对路径和绝对路径都OK)
TARGET_FOLDER=..\..\x1\assets\bundles\map\

# 前端工程项目 相关配置表 的路径(相对路径和绝对路径都OK)
GAME_CFG=..\..\x1\assets\bundles\gamecfg\
```

## 使用方法

1.用SVN更新地图资源文件(美术仓库)

2.依次点击脚本

```cmd
z_1更新Git.bat
z_2一键操作.bat
z_3上传Git.bat (本地测试不需要上传)
```

3.在前端项目中提交差异文件 (本地测试不需要上传)
