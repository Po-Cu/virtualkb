# VirtualKB Input

一个纯客户端 Fabric 模组，作为 [VisualKeys](https://modrinth.com/mod/visualkeys) 的扩展，提供「鼠标锁定 + 键盘代替鼠标」的无障碍操作方式。

> 本模组由 AI（DeepSeek）辅助开发。

## 功能

- **鼠标锁定**：启用后光标可见，但鼠标无法直接操作世界
- **点击虚拟键盘**：直接用鼠标点击/按住 VisualKeys 显示的虚拟键盘来操作游戏
- **方向键转视角**：视角旋转速度由原版「鼠标灵敏度」决定
- **小键盘代替鼠标**：`4/5/6` = 左/中/右键，`8/2` = 滚轮
- **ESC / F2 / F3 支持**：点击虚拟键盘上的 ESC/F2/F3 也能暂停、截图、开调试界面
- **键盘透明度可调**：配置界面里可调 0.0 ~ 1.0
- **GUI 关闭按钮**：物品栏、容器界面、聊天栏右上角加了一个 ✕ 关闭按钮

## 依赖

| 依赖 | 说明 |
|---|---|
| Minecraft | 26.2 |
| Fabric Loader | 0.19.5+ |
| Fabric API | 0.161.0+26.2 |
| **VisualKeys** | 0.0.11+26.2（**必需**，本模组是其扩展） |
| Mod Menu | 20.0.3（配置界面入口，运行时 recommends） |
| Cloth Config | 26.2.155（配置界面库，打进 jar） |

> **许可说明**：本模组以 MIT 许可开源。但 VisualKeys 是独立的第三方模组，其自身许可为 All-Rights-Reserved，本仓库**不包含、不分发** VisualKeys 的代码（构建时从 Modrinth 拉取、运行时由玩家自行安装）。

## 构建

需要 JDK 25。

```bash
./gradlew genSources   # 首次构建：下载依赖 + 反编译源码
./gradlew build        # 构建 jar（产物在 build/libs/）
./gradlew runClient    # 直接运行开发客户端
```

Windows 下用 `gradlew.bat`。

## 使用

1. 安装 VisualKeys 和本模组。
2. 在 VisualKeys 自己的配置里打开它的键盘显示（HUD 模式）。
3. 在本模组的配置（Mod Menu → VirtualKB Input → Config）里打开 `Enable`。
4. 此时光标可见，可用鼠标点击 VisualKeys 键盘操作；用方向键转视角、小键盘代替鼠标按键。

### 默认键位（可在原版「按键设置」里修改）

| 功能 | 默认键 |
|---|---|
| 视角 上/下/左/右 | 方向键 |
| 左键（攻击/挖掘） | 小键盘 4 |
| 中键（拾取方块） | 小键盘 5 |
| 右键（使用） | 小键盘 6 |
| 滚轮上 | 小键盘 8 |
| 滚轮下 | 小键盘 2 |

## 许可证

本项目以 [MIT 许可证](LICENSE) 开源。
