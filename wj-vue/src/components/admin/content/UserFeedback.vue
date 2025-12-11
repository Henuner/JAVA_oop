<template>
  <div class="app-container">
    <el-card class="feedback-card">
      <div slot="header">
        <span>提交意见反馈</span>
      </div>
      <el-form :model="feedbackForm" :rules="rules" ref="feedbackForm" label-width="100px">
        <el-form-item label="反馈类型" prop="type">
          <el-select v-model="feedbackForm.type" placeholder="请选择反馈类型">
            <el-option label="功能建议" value="suggestion"></el-option>
            <el-option label="问题反馈" value="bug"></el-option>
            <el-option label="其他" value="other"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="反馈标题" prop="title">
          <el-input v-model="feedbackForm.title" placeholder="请输入反馈标题"></el-input>
        </el-form-item>
        <el-form-item label="详细内容" prop="content">
          <el-input
            type="textarea"
            v-model="feedbackForm.content"
            :rows="6"
            placeholder="请详细描述您的反馈内容..."
            maxlength="1000"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitFeedback" :loading="submitting">
            {{ submitting ? '提交中...' : '提交反馈' }}
          </el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 我的反馈历史 -->
    <el-card class="history-card" style="margin-top: 20px;">
      <div slot="header">
        <span>我的反馈历史</span>
      </div>
      <el-table :data="myFeedbacks" v-loading="listLoading" style="width: 100%">
        <el-table-column prop="title" label="标题" min-width="200"></el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTypeTag(scope.row.type)">{{ getTypeText(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="180">
          <template slot-scope="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="viewFeedbackDetail(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 反馈详情对话框 -->
    <el-dialog :title="'反馈详情 - ' + currentFeedback.title" :visible.sync="detailDialogVisible" width="60%">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="标题">{{ currentFeedback.title }}</el-descriptions-item>
        <el-descriptions-item label="类型">
          <el-tag :type="getTypeTag(currentFeedback.type)">{{ getTypeText(currentFeedback.type) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(currentFeedback.status)">{{ getStatusText(currentFeedback.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ formatTime(currentFeedback.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="反馈内容">
          <div style="padding: 10px; background: #f5f7fa; border-radius: 4px; white-space: pre-wrap;">
            {{ currentFeedback.content }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentFeedback.replyContent" label="管理员回复">
          <div style="padding: 10px; background: #e1f3d8; border-radius: 4px; white-space: pre-wrap;">
            {{ currentFeedback.replyContent }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentFeedback.replyTime" label="回复时间">
          {{ formatTime(currentFeedback.replyTime) }}
        </el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { submitFeedback, getMyFeedbacks } from '@/api/feedback'

export default {
  name: 'UserFeedback',
  data () {
    return {
      submitting: false,
      listLoading: false,
      myFeedbacks: [],
      detailDialogVisible: false,
      currentFeedback: {},
      feedbackForm: {
        type: 'suggestion',
        title: '',
        content: ''
      },
      rules: {
        type: [{ required: true, message: '请选择反馈类型', trigger: 'change' }],
        title: [
          { required: true, message: '请输入反馈标题', trigger: 'blur' },
          { min: 5, max: 50, message: '标题长度在 5 到 50 个字符', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入反馈内容', trigger: 'blur' },
          { min: 10, max: 1000, message: '内容长度在 10 到 1000 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    this.loadMyFeedbacks()
  },
  methods: {
    // 提交反馈
    async submitFeedback () {
      this.$refs.feedbackForm.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            await submitFeedback(this.feedbackForm)
            this.$message.success('反馈提交成功！')
            this.resetForm()
            this.loadMyFeedbacks() // 重新加载反馈列表
          } catch (error) {
            this.$message.error('提交失败：' + (error.message || '未知错误'))
          } finally {
            this.submitting = false
          }
        }
      })
    },
    // 重置表单
    resetForm () {
      this.$refs.feedbackForm.resetFields()
      this.feedbackForm.type = 'suggestion'
    },
    // 加载我的反馈列表
    async loadMyFeedbacks () {
      this.listLoading = true
      try {
        const response = await getMyFeedbacks()
        console.log('API响应:', response)
        if (response && response.data) {
          // 方式1：如果后端直接返回数组
          if (Array.isArray(response.data)) {
            this.myFeedbacks = response.data
          } else if (response.data.data && Array.isArray(response.data.data)) {
            this.myFeedbacks = response.data.data
          } else if (response.data.result && Array.isArray(response.data.result)) {
            this.myFeedbacks = response.data.result
          } else {
            console.warn('未知的数据结构:', response.data)
            this.myFeedbacks = []
          }
        } else {
          this.myFeedbacks = []
        }
        console.log('最终数据:', this.myFeedbacks)
      } catch (error) {
        console.error('加载反馈列表失败:', error)
        this.$message.error('加载反馈列表失败')
        this.myFeedbacks = []
      } finally {
        this.listLoading = false
      }
    },
    // 查看反馈详情
    viewFeedbackDetail (feedback) {
      this.currentFeedback = feedback
      this.detailDialogVisible = true
    },
    // 类型标签样式
    getTypeTag (type) {
      const typeMap = {
        suggestion: 'primary',
        bug: 'danger',
        other: 'info'
      }
      return typeMap[type] || 'info'
    },
    getTypeText (type) {
      const typeMap = {
        suggestion: '建议',
        bug: '问题',
        other: '其他'
      }
      return typeMap[type] || '其他'
    },
    // 状态标签样式
    getStatusTag (status) {
      const statusMap = {
        pending: 'warning',
        processing: 'primary',
        resolved: 'success'
      }
      return statusMap[status] || 'info'
    },
    getStatusText (status) {
      const statusMap = {
        pending: '待处理',
        processing: '处理中',
        resolved: '已解决'
      }
      return statusMap[status] || '未知'
    },
    // 格式化时间
    formatTime (time) {
      if (!time) return ''
      return new Date(time).toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.feedback-card {
  margin-bottom: 20px;
}
</style>
