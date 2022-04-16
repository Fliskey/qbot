# Qbot
一款能求签的QQ机器人

- [Qbot](#qbot)
  - [项目说明](#项目说明)
  - [功能使用指南](#功能使用指南)
    - [求签模块](#求签模块)
      - [求签](#求签)
      - [求签统计](#求签统计)
      - [求签排名](#求签排名)
    - [活动报名模块](#活动报名模块)
      - [报名 活动名](#报名-活动名)
      - [取消报名 活动名](#取消报名-活动名)
      - [报名统计 活动名](#报名统计-活动名)
    - [成就模块](#成就模块)
      - [成就](#成就)

## 项目说明

群机器人实现：go-cqhttp  指路：[Mrs4s/go-cqhttp](https://github.com/Mrs4s/go-cqhttp)

后端框架：Spring + MybatisPlus + MySQL

本项目严重依赖于数据库运行，若需要自行编译运行此项目，请参照 README/补全文件.md 指示补全项目文件。

---

## 功能使用指南

  **模块下的每个条目就是关键词。**

  **所有功能均不需要@群bot，直接在群内正常发送消息即可。**

### 求签模块

#### 求签

- 功能：返回一条签文
- 签文示例：
  > 【今日运势】小吉
  >
  > 【宜】打游戏: 芜湖起飞！
  >
  > 【宜】量子力学: 遇事不决，量子力学
  >
  > 【忌】装弱: 会被戳穿
  >
  > 【忌】买小米: 谁TM买小米
- 签文说明：
  - 签文等级
    - 签文共有四个等级：大凶，小凶，中平，小吉，大吉
    - 一般签文都有两条【宜】和两条【忌】
    - 大凶无【宜】大吉无【忌】
  - 签文库
    - 求签模块受洛谷的签到系统启发，故有使用部分洛谷签文
    - 大部分签文为作者与使用者添加，签文来自于生活中各方面的梗，如B站热梗
    - 签文库持续更新中，若需贡献签文，直接在群内模仿bot发送的签文发送消息即可
  - 签文贡献示例：`【宜】贡献签文：群众的智慧`

#### 求签统计

- 功能：返回该群当日的求签情况。
- 平均运势：
  - 按从大凶到大吉赋值1~5，计算当日运势平均值，向下取整后匹配运势等级
  - 如中平+0.65表示平均值为3.65，靠近小吉

#### 求签排名

- at出该群当日求签顺序排名前五的求签者

### 活动报名模块

限定在部分群内测。参数用空格隔开。

#### 报名 活动名

- 功能：报名活动
- 不需要提前创建活动名，如果无此活动会自动创建该活动

#### 取消报名 活动名

- 功能：取消对该活动的报名

#### 报名统计 活动名

- 功能：显示所有报名了该活动的报名者

### 成就模块

#### 成就

- 关键词：“成就”，将返回查询者拥有的全部成就，按获得时间排序