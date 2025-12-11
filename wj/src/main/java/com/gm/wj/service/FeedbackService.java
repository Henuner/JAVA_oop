package com.gm.wj.service;

import com.gm.wj.dao.FeedbackDAO;
import com.gm.wj.entity.Feedback;
import com.gm.wj.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackDAO feedbackDAO;

    @Autowired
    private UserService userService;

    /**
     * 提交意见反馈
     */
    public void submitFeedback(Feedback feedback) {
        feedback.setCreateTime(new Date());
        feedback.setStatus("pending"); // 默认待处理状态
        feedbackDAO.save(feedback);
    }

    /**
     * 获取所有反馈（管理员用）
     */
    public List<Feedback> listAllFeedbacks() {
        return feedbackDAO.findAllByOrderByCreateTimeDesc();
    }

    /**
     * 根据用户ID获取反馈
     */
    public List<Feedback> listFeedbacksByUser(int userId) {
        return feedbackDAO.findByUserIdOrderByCreateTimeDesc(userId);
    }

    /**
     * 根据状态获取反馈
     */
    public List<Feedback> listFeedbacksByStatus(String status) {
        return feedbackDAO.findByStatus(status);
    }

    /**
     * 根据ID获取反馈详情
     */
    public Feedback findById(int id) {
        Optional<Feedback> feedback = feedbackDAO.findById(id);
        return feedback.orElse(null);
    }

    /**
     * 回复反馈（管理员用）
     */
    public void replyFeedback(int feedbackId, String replyContent) {
        Optional<Feedback> feedbackOpt = feedbackDAO.findById(feedbackId);
        if (feedbackOpt.isPresent()) {
            Feedback feedback = feedbackOpt.get();
            feedback.setReplyContent(replyContent);
            feedback.setReplyTime(new Date());
            feedback.setStatus("resolved"); // 标记为已解决
            feedbackDAO.save(feedback);
        }
    }

    /**
     * 更新反馈状态
     */
    public void updateStatus(int feedbackId, String status) {
        Optional<Feedback> feedbackOpt = feedbackDAO.findById(feedbackId);
        if (feedbackOpt.isPresent()) {
            Feedback feedback = feedbackOpt.get();
            feedback.setStatus(status);
            feedbackDAO.save(feedback);
        }
    }

    /**
     * 删除反馈
     */
    public void deleteFeedback(int id) {
        feedbackDAO.deleteById(id);
    }

    /**
     * 统计反馈状态数量（用于管理员仪表盘）
     */
    public long countByStatus(String status) {
        return feedbackDAO.findByStatus(status).size();
    }

    /**
     * 搜索反馈
     */
    public List<Feedback> searchFeedbacks(String keyword) {
        return feedbackDAO.findByKeyword(keyword);
    }

    /**
     * 获取反馈统计信息
     */
    public String getFeedbackStats() {
        long pendingCount = countByStatus("pending");
        long processingCount = countByStatus("processing");
        long resolvedCount = countByStatus("resolved");
        long totalCount = feedbackDAO.count();

        return String.format("待处理: %d, 处理中: %d, 已解决: %d, 总计: %d",
                pendingCount, processingCount, resolvedCount, totalCount);
    }
}
