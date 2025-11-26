<!-- 用户操作日记 -->
<template>
  <div>
    <el-card>
      <!-- 表格 -->
      <div>
        <el-table
          ref="table"
          border
          :data="userLogData"
          :height="tableHeight"
          :header-cell-style="{background:'#f5f7fa'}"
        >
          <el-table-column prop="id" label="唯一ID" />
          <el-table-column prop="userId" label="用户ID" />
          <el-table-column prop="userName" label="用户名称" />
          <el-table-column prop="requestUrl" label="请求地址" />
          <el-table-column prop="param" label="操作参数" />
          <el-table-column prop="time" label="操作时间">
            <template slot-scope="scope">{{ scope.row.time|formatTime }}</template>
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

      </div>
    </el-card>

  </div>
</template>

<script>
import { userLog } from '@/api/userSetting'

export default {
  data() {
    return {
      currentPage: 1,
      pageSize: 200,
      total: 0,
      userLogData: [],
      tableHeight: 0 // 表格高度
    }
  },

  created() {
    this.getUserLog()
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
    async getUserLog() {
      const res = await userLog({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.userLogData = res.data.data.data
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getUserLog()
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

<style lang="scss" scoped>
.el-table{
 margin-top: 0cm;
}
</style>
