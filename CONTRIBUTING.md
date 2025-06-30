
# 🙌 Contributing Guide

感谢你有意为本项目贡献代码！以下是贡献流程和约定，请在提交前阅读并遵守。

---

## 🧭 提交流程

```shell
# ✅ Fork 本仓库，并克隆你的 Fork 到本地
git clone git@github.com:<你的用户名>/<项目名>.git
cd <项目名>

# ✅ 添加上游仓库（绑定原始仓库）这一步只需要执行一次即可, 这是使用https或ssh都可以
git remote add upstream https://github.com/<原仓库拥有者>/<项目名>.git

# ✅ 从主干拉取最新代码，确保基于最新代码开发 每次开发新功能及合并后删除分支都需要执行的命令
git fetch upstream # 从上游仓库拉代码
git checkout master # 切换到默认分支
git merge upstream/master # 合并上游代码到本地默认分支
git push # 推送默认分支

# ✅ 创建功能分支 类型/功能
git checkout -b feat/my-feature

# ✅ 编写代码并提交 commit 规则见下方的 Commit Message 规范
git add .
git commit -m "feat: 添加我的新功能"

# ✅ 推送功能分支到你自己的远程仓库 类型/功能，和创建分支的分支名相同
git push origin feat/my-feature

# ✅ 在 GitHub 提交 Pull Request（PR） 这一步需要手动操作，建议带上测试截图

# ✅（PR 被合并后）删除本地和远程分支
git fetch upstream # 从上游仓库拉代码
git checkout master # 切换到默认分支
git merge upstream/master # 合并上游代码到本地默认分支
# 删除本地及远程分支 类型/功能  和创建时的分支名相同
git branch -D feat/my-feature            # 删除本地分支
git push origin --delete feat/my-feature # 删除远程分支
```

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
2. 



感谢你的贡献！🚀


