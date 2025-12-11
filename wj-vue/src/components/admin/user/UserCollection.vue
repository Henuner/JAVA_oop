<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">管理中心</el-breadcrumb-item>
      <el-breadcrumb-item>我的收藏</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card style="margin-top: 20px; min-height: 600px;">
      <div v-if="articles.length === 0" style="text-align: center; color: #909399; margin-top: 100px;">
        <i class="el-icon-folder-opened" style="font-size: 60px;"></i>
        <p>暂无收藏，快去笔记本看看吧</p>
      </div>

      <div v-else>
        <el-table
          :data="articles"
          style="width: 100%"
          :show-header="false">
          <el-table-column>
            <template slot-scope="scope">
              <div style="cursor: pointer;" @click="viewArticle(scope.row.id)">
                <span style="font-size: 16px; font-weight: bold">{{ scope.row.articleTitle }}</span>
                <span style="float: right; color: #909399; font-size: 13px">{{ scope.row.articleDate }}</span>
                <p style="color: #606266; margin-top: 10px; font-size: 14px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                  {{ scope.row.articleAbstract }}
                </p>
              </div>
            </template>
          </el-table-column>

          <el-table-column width="100">
            <template slot-scope="scope">
              <el-button type="text" style="color: #F56C6C" @click="cancelCollection(scope.row.id)">取消收藏</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'UserCollection',
  data () {
    return {
      articles: []
    }
  },
  mounted () {
    this.loadCollections()
  },
  methods: {
    loadCollections () {
      // 1. 获取用户名 (这里兼容你现在的 store 结构)
      // 尝试获取 state.user.username (新版结构) 或 state.username (旧版结构)
      // 只要拿到 username 字符串就行
      let username = ''
      if (this.$store.state.user && this.$store.state.user.username) {
          username = this.$store.state.user.username
      } else {
          username = this.$store.state.username
      }

      // 如果没登录，就不查了
      if (!username) return

      // 2. 调用我们在第一步写好的“智能接口”
      this.$axios.get('/collection/list?username=' + username).then(resp => {
        if (resp && resp.data.code === 200) {
          this.articles = resp.data.result
        }
      })
    },
    // 跳转去公共笔记本看详情
    viewArticle (id) {
      // 路由跳转到 ArticleDetails (使用 query 参数传 id)
      // 注意：这里路径要和你前端路由里配的一致，通常是 /jotter/article
      this.$router.push('/jotter/article?id=' + id)
    },
    // 在列表页直接取消收藏
    cancelCollection (aid) {
      // 这里我们需要 uid 来取消收藏。
      // 因为前端 store 里可能没有 id，我们用 username 再去查一次不太方便。
      // 为了简单稳健，我们这里只做前端移除，或者提示去详情页取消。
      // ---------------------------------------------------------
      // 更好的方案：既然要取消，最好去详情页操作，或者后端再加一个 removeByUsername 接口。
      // 但为了不让你现在改后端太复杂，我们直接跳转到文章详情页让用户去取消。

      this.$confirm('是否跳转到文章详情页进行取消操作?', '提示', {
        confirmButtonText: '去看看',
        cancelButtonText: '算了',
        type: 'warning'
      }).then(() => {
        this.viewArticle(aid)
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
  /* 可以加一点微调 */
</style>
