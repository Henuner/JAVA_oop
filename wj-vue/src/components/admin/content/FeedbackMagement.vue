<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">管理中心</el-breadcrumb-item>
        <el-breadcrumb-item>用户管理</el-breadcrumb-item>
        <el-breadcrumb-item>用户反馈</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <el-card style="margin: 18px 2%;width: 95%">
      <div class="filter-container" style="margin-bottom: 20px;">
        <el-input v-model="listQuery.keyword" placeholder="搜索反馈内容" style="width: 200px;" @keyup.enter.native="handleFilter" />
        <el-select v-model="listQuery.status" placeholder="状态" clearable style="width: 120px; margin-left: 10px;">
          <el-option label="待处理" value="pending" />
          <el-option label="处理中" value="processing" />
          <el-option label="已解决" value="resolved" />
        </el-select>
        <el-select v-model="listQuery.type" placeholder="类型" clearable style="width: 120px; margin-left: 10px;">
          <el-option label="建议" value="suggestion" />
          <el-option label="问题" value="bug" />
          <el-option label="其他" value="other" />
        </el-select>
        <el-button type="primary" icon="el-icon-search" style="margin-left: 10px;" @click="handleFilter">
          搜索
        </el-button>
      </div>

      <el-table
        ref="feedbackTable"
        :data="filteredList"
        stripe
        style="width: 100%"
        :max-height="tableHeight"
        v-loading="listLoading"
        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <!-- <el-table-column
          prop="id"
          label="ID"
          width="80">
        </el-table-column> -->
        <el-table-column
          prop="user.username"
          label="用户"
          width="120">
        </el-table-column>
        <el-table-column
          prop="title"
          label="标题"
          min-width="200">
        </el-table-column>
        <el-table-column
          label="类型"
          width="100">
          <template slot-scope="scope">
            <el-tag :type="getTypeTag(scope.row.type)">{{ getTypeText(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="状态"
          width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="提交时间"
          width="160">
          <template slot-scope="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="200">
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="handleView(scope.row)"
              type="text"
              size="small">
              查看
            </el-button>
            <el-button
              v-if="scope.row.status !== 'resolved'"
              @click.native.prevent="handleReply(scope.row)"
              type="text"
              size="small">
              回复
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin: 20px 0 20px 0;float: left">
        <el-button @click="clearSelection">取消选择</el-button>
        <el-button
          type="danger"
          @click="handleBatchDelete"
          :disabled="selectedFeedbacks.length === 0">
          批量删除（{{ selectedFeedbacks.length }}）
        </el-button>
      </div>
    </el-card>

    <!-- 查看反馈详情对话框 -->
    <el-dialog :title="'反馈详情 - ' + currentFeedback.title" :visible.sync="detailDialogVisible" width="60%">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户">{{ currentFeedback.user ? currentFeedback.user.username : '未知用户' }}</el-descriptions-item>
        <el-descriptions-item label="类型">
          <el-tag :type="getTypeTag(currentFeedback.type)">{{ getTypeText(currentFeedback.type) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(currentFeedback.status)">{{ getStatusText(currentFeedback.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ formatTime(currentFeedback.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="反馈内容" :span="2">
          <div style="padding: 10px; background: #f5f7fa; border-radius: 4px;">
            {{ currentFeedback.content }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentFeedback.replyContent" label="回复内容" :span="2">
          <div style="padding: 10px; background: #e1f3d8; border-radius: 4px;">
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

    <!-- 回复反馈对话框 -->
    <el-dialog title="回复反馈" :visible.sync="replyDialogVisible" width="50%">
      <el-form :model="replyForm" :rules="replyRules" ref="replyForm">
        <el-form-item label="回复内容" prop="replyContent">
          <el-input
            type="textarea"
            v-model="replyForm.replyContent"
            :rows="6"
            placeholder="请输入回复内容..."
            maxlength="500"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="replyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleReplySubmit" :loading="replying">确定回复</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getAllFeedbacks, replyFeedback } from '@/api/feedback'

export default {
  name: 'FeedbackManagement',
  data () {
    return {
      list: [],
      filteredList: [],
      selectedFeedbacks: [],
      listLoading: true,
      listQuery: {
        keyword: undefined,
        status: undefined,
        type: undefined
      },
      detailDialogVisible: false,
      replyDialogVisible: false,
      replying: false,
      currentFeedback: {},
      replyForm: {
        replyContent: ''
      },
      replyRules: {
        replyContent: [
          { required: true, message: '请输入回复内容', trigger: 'blur' },
          { min: 5, message: '回复内容至少5个字符', trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    this.loadFeedbacks()
  },
  computed: {
    tableHeight () {
      return window.innerHeight - 320
    }
  },
  methods: {
    handleSelectionChange (selectedRows) {
      this.selectedFeedbacks = selectedRows
    },
    clearSelection () {
      if (this.$refs.feedbackTable) {
        this.$refs.feedbackTable.clearSelection()
      }
      this.selectedFeedbacks = []
    },
    handleBatchDelete () {
      if (this.selectedFeedbacks.length === 0) {
        this.$message.warning('请先选择要删除的反馈')
        return
      }
      const ids = this.selectedFeedbacks.map(item => item.id)
      const count = ids.length
      this.$confirm(`确定要删除这 ${count} 条反馈吗？删除后无法恢复。`, '确认删除', {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.batchDelete(ids)
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    async batchDelete (ids) {
      this.listLoading = true
      try {
        // 逐个删除
        for (const id of ids) {
          // 修改这里：使用查询参数方式
          await this.$axios.post('/admin/feedback/delete', null, {
            params: { feedbackId: id }
          })
        }
        this.$message.success(`成功删除 ${ids.length} 条反馈`)
        this.loadFeedbacks() // 重新加载列表
        this.clearSelection() // 清空选择
      } catch (error) {
        console.error('删除失败:', error)
        this.$message.error('删除失败：' + (error.message || '未知错误'))
      } finally {
        this.listLoading = false
      }
    },
    loadFeedbacks () {
      this.listLoading = true
      getAllFeedbacks().then(resp => {
        if (resp && resp.data.code === 200) {
          this.list = resp.data.result
          this.filteredList = this.list
        }
        this.listLoading = false
      }).catch(error => {
        console.error('加载反馈列表失败:', error)
        this.listLoading = false
      })
    },
    handleFilter () {
      let data = this.list
      // 前端过滤
      if (this.listQuery.keyword) {
        data = data.filter(item =>
          item.title.includes(this.listQuery.keyword) ||
          item.content.includes(this.listQuery.keyword)
        )
      }
      if (this.listQuery.status) {
        data = data.filter(item => item.status === this.listQuery.status)
      }
      if (this.listQuery.type) {
        data = data.filter(item => item.type === this.listQuery.type)
      }
      this.filteredList = data
    },
    handleView (row) {
      this.currentFeedback = row
      this.detailDialogVisible = true
    },
    handleReply (row) {
      this.currentFeedback = row
      this.replyForm.replyContent = row.replyContent || ''
      this.replyDialogVisible = true
    },
    handleReplySubmit () {
      this.$refs.replyForm.validate((valid) => {
        if (valid) {
          this.replying = true
          replyFeedback(this.currentFeedback.id, this.replyForm.replyContent)
            .then(resp => {
            if (resp && resp.data.code === 200) {
              this.$message.success('回复成功')
              this.replyDialogVisible = false
              this.loadFeedbacks()
            }
            this.replying = false
          }).catch(error => {
            this.$message.error('回复失败：' + (error.message || '未知错误'))
            this.replying = false
          })
        }
      })
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
</style>
