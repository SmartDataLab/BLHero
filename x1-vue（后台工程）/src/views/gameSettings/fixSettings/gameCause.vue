<!-- 流水事件 -->
<template>
  <div>
    <el-card>
      <el-button icon="el-icon-refresh" type="primary" size="small" @click="handleUpdate">从服务器更新流水事件</el-button>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="waterData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="事件ID" />
          <el-table-column prop="name" label="事件名" />
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
import { waterEvent, updateWaterEvent } from '@/api/specialOperation'

export default {
  data() {
    return {
      waterData: [],
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0 // 表格高度
    }
  },

  created() {
    this.getWaterEvent()
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
    async getWaterEvent() {
      const res = await waterEvent({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.waterData = res.data.data.data
    },

    // 更新
    async handleUpdate() {
      const res = await updateWaterEvent()
      if (res.data.code === 0) {
        this.$message.success('更新成功')
      } else {
        this.$message.error('更新失败')
      }
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getWaterEvent()
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

</style>
