<!-- 道具配置 -->
<template>
  <div>
    <el-card>
      <el-button icon="el-icon-refresh" type="primary" size="small" @click="handleUpdate">从服务器更新道具数据</el-button>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="propsData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="道具ID" />
          <el-table-column prop="name" label="道具名" />
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
import { propsConfig, updateGameProps } from '@/api/specialOperation'

export default {
  data() {
    return {
      propsData: [],
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0 // 表格高度
    }
  },

  created() {
    this.getPropsConfig()
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
    async  getPropsConfig() {
      const res = await propsConfig({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.propsData = res.data.data.data
    },

    // 更新
    async handleUpdate() {
      const res = await updateGameProps()
      if (res.data.code === 0) {
        this.$message.success('更新成功')
      } else {
        this.$message.error('更新失败')
      }
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getPropsConfig()
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
