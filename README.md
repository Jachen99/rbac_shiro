# rbac_shiro
shiro框架整合springboot单体项目  是一款权限认证授权的网站敏捷开发的解决方案，Shiro是一个强大易用的Java安全框架,提供了认证、授权、加密和会话管理等功能，适用于首次学习权限框架的朋友，快速上手

### 1、技术选型
前后端分离的权限检验 + SpringBoot2.x + Mysql + Mybatis + Shiro + Redis + IDEA + JDK8
### 2、数据库设计
用户-角色-权限 及关联表
### 3、整合redis缓存
<!-- shiro+redis缓存插件 -->
<dependency>
<groupId>org.crazycake</groupId>
<artifactId>shiro-redis</artifactId>
<version>3.1.0</version>
</dependency>
