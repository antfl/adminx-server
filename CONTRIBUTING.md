
# 🙌 Contributing Guide

感谢你有意为本项目贡献代码！以下是贡献流程和约定，请在提交前阅读并遵守。

---

## 🧭 提交流程

1. **Fork** 本仓库
2. 创建你的功能分支：`git checkout -b feat/my-feature`
3. 提交更改：`git commit -m "feat: 添加我的新功能"`
4. 推送分支：`git push origin feat/my-feature`
5. 提交 Pull Request（PR）

---

## ✍️ Commit Message 规范

请遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范。基本格式如下：

```

<type>(<scope>): <subject>

````

- `type`：提交类型（见下方表格）
- `scope`：可选，影响范围，如模块名、类名
- `subject`：一句话简要描述改动

| 类型 (type) | 说明                         |
|-------------|------------------------------|
| feat        | ✨ 新功能                     |
| fix         | 🐛 Bug 修复                  |
| docs        | 📝 文档更新                   |
| style       | 💄 代码风格调整（不影响功能） |
| refactor    | 🔨 代码重构（非功能/修复）    |
| perf        | ⚡ 性能优化                   |
| test        | ✅ 添加/修改测试代码          |
| chore       | 🔧 构建流程或工具变动         |

#### ✅ 示例

```bash
feat(user): 添加用户登录接口
fix(login): 修复 token 校验逻辑
docs: 更新 README 添加运行说明
````

---

## 💡 开发建议

* 代码请遵循项目现有风格（如使用空格、缩进、注释等）
* 尽量保持每次 PR 单一职责、描述清晰
* 如果涉及核心模块或复杂逻辑，请添加对应测试

---

## 🧪 本地运行环境

1. 请确保本地已安装 JDK 8 和 3.6.0 < Maven <= 3.9.9



感谢你的贡献！🚀


