package com.gm.wj.controller;

import com.gm.wj.entity.Feedback;
import com.gm.wj.entity.User;
import com.gm.wj.result.Result;
import com.gm.wj.result.ResultFactory;
import com.gm.wj.service.FeedbackService;
import com.gm.wj.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    /**
     * 用户提交意见反馈
     */
    @PostMapping("/api/feedback/submit")
    public Result submitFeedback(@Valid @RequestBody Feedback feedbackRequest) {
        // 获取当前登录用户
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResultFactory.buildFailResult("用户未登录");
        }

        // 设置反馈用户
        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setTitle(feedbackRequest.getTitle());
        feedback.setContent(feedbackRequest.getContent());
        feedback.setType(feedbackRequest.getType());

        feedbackService.submitFeedback(feedback);
        return ResultFactory.buildSuccessResult("反馈提交成功");
    }

    /**
     * 用户查看自己的反馈列表
     */
    @GetMapping("/api/feedback/my")
    public Result getMyFeedbacks() {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResultFactory.buildFailResult("用户未登录");
        }

        List<Feedback> feedbacks = feedbackService.listFeedbacksByUser(user.getId());
        return ResultFactory.buildSuccessResult(feedbacks);
    }

    /**
     * 管理员获取所有反馈列表
     */
    @GetMapping("/api/admin/feedback")
    public Result getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.listAllFeedbacks();
        return ResultFactory.buildSuccessResult(feedbacks);
    }

    /**
     * 管理员根据状态获取反馈
     */
    @GetMapping("/api/admin/feedback/status/{status}")
    public Result getFeedbacksByStatus(@PathVariable("status") String status) {
        List<Feedback> feedbacks = feedbackService.listFeedbacksByStatus(status);
        return ResultFactory.buildSuccessResult(feedbacks);
    }

    /**
     * 管理员回复反馈
     */
    @PostMapping("/api/admin/feedback/reply")
    public Result replyFeedback(@RequestParam int feedbackId,
                                @RequestParam String replyContent) {
        if (replyContent == null || replyContent.trim().isEmpty()) {
            return ResultFactory.buildFailResult("回复内容不能为空");
        }

        feedbackService.replyFeedback(feedbackId, replyContent);
        return ResultFactory.buildSuccessResult("回复成功");
    }

    /**
     * 管理员更新反馈状态
     */
    @PostMapping("/api/admin/feedback/status")
    public Result updateFeedbackStatus(@RequestParam int feedbackId,
                                       @RequestParam String status) {
        feedbackService.updateStatus(feedbackId, status);
        return ResultFactory.buildSuccessResult("状态更新成功");
    }

    /**
     * 管理员删除反馈
     */
    @PostMapping("/api/admin/feedback/delete")
    public Result deleteFeedback(@RequestParam int feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return ResultFactory.buildSuccessResult("删除成功");
    }

    /**
     * 搜索反馈
     */
    @GetMapping("/api/admin/feedback/search")
    public Result searchFeedbacks(@RequestParam String keyword) {
        List<Feedback> feedbacks = feedbackService.searchFeedbacks(keyword);
        return ResultFactory.buildSuccessResult(feedbacks);
    }

    /**
     * 获取反馈统计信息
     */
    @GetMapping("/api/admin/feedback/stats")
    public Result getFeedbackStats() {
        String stats = feedbackService.getFeedbackStats();
        return ResultFactory.buildSuccessResult(stats);
    }
}
