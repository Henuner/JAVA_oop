package com.gm.wj.dao;

import com.gm.wj.entity.JotterCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface JotterCollectionDAO extends JpaRepository<JotterCollection, Integer> {
    // 关键方法：根据 uid 和 aid 查找（用于判断某人是否收藏了某文章）
    JotterCollection findByUidAndAid(int uid, int aid);

    // 关键方法：删除收藏记录
    void deleteByUidAndAid(int uid, int aid);

    //根据用户ID查询所有收藏记录
    List<JotterCollection> findAllByUid(int uid);
}
