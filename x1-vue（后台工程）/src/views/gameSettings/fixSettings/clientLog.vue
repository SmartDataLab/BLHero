<!-- 客户端日志 -->
<template>
  <div>
    <el-card>

      <!-- 表格 -->
      <el-table
        ref="table"
        border
        :data="clientData"
        :header-cell-style="{background:'#f5f7fa'}"
        :height="tableHeight"
      >
        <el-table-column prop="id" label="日志ID" min-width="100" />
        <el-table-column prop="level" label="日志级别" min-width="100" />
        <el-table-column prop="title" label="日志标题" min-width="100" />
        <el-table-column prop="content" label="日志信息" min-width="600" />
        <el-table-column prop="insertTime" label="报错时间" min-width="150">
          <template slot-scope="scope">{{ scope.row.insertTime|formatTime }}</template>
        </el-table-column>
        <el-table-column prop="send" label="是否已发送给管理员" min-width="150">
          <template slot-scope="{row}">
            <el-tag :type="row.send? 'success' : 'danger'">
              {{ row.send?'是':'否' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        background
        layout="total, prev, pager, next, jumper"
        :total="total"
        :current-page="currentPage"
        :page-size="pageSize"
        @current-change="changePage"
      />

    </el-card>
  </div>
</template>

<script>
import { clientLog } from '@/api/specialOperation'

export default {
  data() {
    return {
      clientData: [],
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0 // 表格高度
    }
  },

  created() {
    this.getClientLog()
  },

  mounted() {
    this.$nextTick(() => {
      this.getTableHeight()
    })
    window.addEventListener('resize', this.$debounce(this.getTableHeight, 100))
  },

  beforeDestroy() {
    window.removeEventListener('resize', this.getTableHeight)
  },

  methods: {
    // 获取数据
    async getClientLog() {
      const res = await clientLog({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.clientData = res.data.data.data
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getClientLog()
    },

    // 表格自适应高度
    getTableHeight() {
      if (this.$refs.table && this.$refs.table.$el) {
        this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 170
      } else {
        this.tableHeight = 300
      }
    }
  }
}
</script>

<style lang='scss' scoped>

</style>
