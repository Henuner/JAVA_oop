//package com.gm.wj.service;
//
//import com.gm.wj.dao.JotterCollectionDAO;
//import com.gm.wj.entity.JotterCollection;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//
//@Service
//public class JotterCollectionService {
//    @Autowired
//    JotterCollectionDAO jotterCollectionDAO;
//
//    // 查询是否已收藏
//    public boolean isCollected(int uid, int aid) {
//        JotterCollection collection = jotterCollectionDAO.findByUidAndAid(uid, aid);
//        return null != collection;
//    }
//
//    // 添加收藏
//    public void addCollection(int uid, int aid) {
//        JotterCollection collection = new JotterCollection();
//        collection.setUid(uid);
//        collection.setAid(aid);
//        collection.setCreateTime(new Date());
//        jotterCollectionDAO.save(collection);
//    }
//
//    // 取消收藏 (涉及删除，必须加事务注解)
//    @Transactional
//    public void removeCollection(int uid, int aid) {
//        jotterCollectionDAO.deleteByUidAndAid(uid, aid);
//    }
//}
package com.gm.wj.service;

import com.gm.wj.dao.JotterArticleDAO; // 【新增】
import com.gm.wj.dao.JotterCollectionDAO;
import com.gm.wj.entity.JotterArticle; // 【新增】
import com.gm.wj.entity.JotterCollection;
import com.gm.wj.entity.User;          // 【新增】
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JotterCollectionService {
    @Autowired
    JotterCollectionDAO jotterCollectionDAO;

    // 【新增 1】注入用户服务，用于根据用户名查ID
    @Autowired
    UserService userService;

    // 【新增 2】注入文章DAO，用于查文章详情
    @Autowired
    JotterArticleDAO jotterArticleDAO;

    // --- 原有方法保持不变 ---

    public boolean isCollected(int uid, int aid) {
        JotterCollection collection = jotterCollectionDAO.findByUidAndAid(uid, aid);
        return null != collection;
    }

    public void addCollection(int uid, int aid) {
        JotterCollection collection = new JotterCollection();
        collection.setUid(uid);
        collection.setAid(aid);
        collection.setCreateTime(new Date());
        jotterCollectionDAO.save(collection);
    }

    @Transactional
    public void removeCollection(int uid, int aid) {
        jotterCollectionDAO.deleteByUidAndAid(uid, aid);
    }

    // --- 【核心新增方法】根据用户名查询收藏列表 ---
    public List<JotterArticle> findCollectionsByUsername(String username) {
        // 1. 根据用户名查用户对象
        User user = userService.findByUsername(username);
        // 如果查不到人，直接返回空列表
        if (user == null) {
            return new ArrayList<>();
        }
        int uid = user.getId();

        // 2. 根据 UID 查出收藏记录
        List<JotterCollection> collections = jotterCollectionDAO.findAllByUid(uid);

        // 3. 提取所有的文章 ID
        List<Integer> aids = new ArrayList<>();
        for (JotterCollection collection : collections) {
            aids.add(collection.getAid());
        }

        // 4. 如果没收藏任何东西，直接返回空
        if (aids.isEmpty()) {
            return new ArrayList<>();
        }

        // 5. 根据文章 ID 列表查出文章详情
        return jotterArticleDAO.findAllById(aids);
    }
}
