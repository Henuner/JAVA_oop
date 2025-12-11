// src/api/feedback.js - 简化版（符合ESLint规范）
import axios from 'axios'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8443', // 后端地址
  timeout: 10000
})

// 用户提交反馈
export function submitFeedback (data) {
  return request({
    url: '/api/feedback/submit',
    method: 'post',
    data
  })
}

// 获取我的反馈列表
export function getMyFeedbacks () {
  return request({
    url: '/api/feedback/my',
    method: 'get'
  })
}

// 管理员获取所有反馈
export function getAllFeedbacks () {
  return request({
    url: '/api/admin/feedback',
    method: 'get'
  })
}

// 管理员回复反馈
export function replyFeedback (feedbackId, replyContent) {
  return request({
    url: '/api/admin/feedback/reply',
    method: 'post',
    params: { feedbackId, replyContent }
  })
}

// 管理员删除反馈
export function deleteFeedback (feedbackId) {
  return request({
    url: '/api/admin/feedback/delete',
    method: 'post',
    params: { feedbackId }
  })
}

// 管理员更新反馈状态
export function updateFeedbackStatus (feedbackId, status) {
  return request({
    url: '/api/admin/feedback/status',
    method: 'post',
    params: { feedbackId, status }
  })
}
