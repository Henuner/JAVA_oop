/*
 Navicat MySQL Data Transfer
 Source Server         : wj
 Source Database       : wj
 Date: 2025-12-11
 Description: White Jotter 收藏功能完整数据库脚本 (含建表与菜单配置)
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ==========================================================
-- 1. 创建收藏表 (jotter_collection)
-- ==========================================================
-- 如果表不存在则创建，存储用户ID(uid)和文章ID(aid)的关系
CREATE TABLE IF NOT EXISTS `jotter_collection` (
                                                   `id` int(11) NOT NULL AUTO_INCREMENT,
    `uid` int(11) DEFAULT NULL COMMENT '用户ID',
    `aid` int(11) DEFAULT NULL COMMENT '文章ID',
    `create_time` datetime DEFAULT NULL COMMENT '收藏时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ==========================================================
-- 2. 清理旧菜单数据 (防止重复报错)
-- ==========================================================
-- 先删除 admin_role_menu 中关联的权限记录，防止有残留
DELETE FROM `admin_role_menu`
WHERE mid IN (SELECT id FROM `admin_menu` WHERE name IN ('UserCollection', 'CollectionList'));

-- 再删除 admin_menu 中的菜单记录
DELETE FROM `admin_menu`
WHERE name IN ('UserCollection', 'CollectionList');

-- ==========================================================
-- 3. 插入一级菜单 "我的收藏" (父文件夹)
-- ==========================================================
-- path: /admin/collection
-- component: user/UserCollection (复用组件)
-- parent_id: 0 (代表一级菜单)
INSERT INTO `admin_menu` VALUES (NULL, '/admin/collection', 'UserCollection', '我的收藏', 'el-icon-star-on', 'user/UserCollection', 0);

-- 获取刚才插入的父菜单 ID
SET @parentId = (SELECT id FROM `admin_menu` WHERE name = 'UserCollection');

-- ==========================================================
-- 4. 插入二级菜单 "收藏列表" (实际点击跳转的页面)
-- ==========================================================
-- path: /admin/collection/list
-- component: user/UserCollection (真正显示列表的组件)
-- parent_id: @parentId (挂载到"我的收藏"下面)
INSERT INTO `admin_menu` VALUES (NULL, '/admin/collection/list', 'CollectionList', '收藏列表', 'el-icon-document', 'user/UserCollection', @parentId);

-- 获取刚才插入的子菜单 ID
SET @childId = (SELECT id FROM `admin_menu` WHERE name = 'CollectionList');

-- ==========================================================
-- 5. 给角色分配权限
-- ==========================================================
-- 给 Admin (rid=1) 分配子菜单权限
INSERT INTO `admin_role_menu` VALUES (NULL, 1, @childId);

-- 给 Editor (rid=2) 分配子菜单权限
INSERT INTO `admin_role_menu` VALUES (NULL, 2, @childId);

-- 给 id=3 的角色分配 (如果有的话，保留此行以防万一)
INSERT INTO `admin_role_menu` VALUES (NULL, 3, @childId);

SET FOREIGN_KEY_CHECKS = 1;

-- 脚本结束
