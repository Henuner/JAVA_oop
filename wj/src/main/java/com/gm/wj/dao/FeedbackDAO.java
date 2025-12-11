package com.gm.wj.dao;

import com.gm.wj.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FeedbackDAO extends JpaRepository<Feedback, Integer> {

    // 根据用户ID查找反馈
    List<Feedback> findByUserId(int userId);

    // 根据状态查找反馈
    List<Feedback> findByStatus(String status);

    // 根据类型查找反馈
    List<Feedback> findByType(String type);

    // 根据用户ID和状态查找反馈
    List<Feedback> findByUserIdAndStatus(int userId, String status);

    // 查找最近的意见反馈（按时间倒序）
    List<Feedback> findAllByOrderByCreateTimeDesc();

    // 统计各种状态的反馈数量
    @Query("SELECT f.status, COUNT(f) FROM Feedback f GROUP BY f.status")
    List<Object[]> countByStatusGroup();

    // 根据关键词搜索反馈（标题或内容）
    @Query("SELECT f FROM Feedback f WHERE f.title LIKE %:keyword% OR f.content LIKE %:keyword%")
    List<Feedback> findByKeyword(@Param("keyword") String keyword);

    // 根据用户ID查找反馈（按时间倒序）
    List<Feedback> findByUserIdOrderByCreateTimeDesc(int userId);
}
