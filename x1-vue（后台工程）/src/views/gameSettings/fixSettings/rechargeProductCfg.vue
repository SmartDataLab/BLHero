<!-- 充值商品配置 -->
<template>
  <div>
    <el-card>
      <el-button icon="el-icon-refresh" type="primary" size="small" @click="handleUpdate">更新游戏充值商品配置数据</el-button>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="productData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="商品ID" />
          <el-table-column prop="name" label="商品名称" />
          <el-table-column prop="money" label="商品价值（单位：元）">
            <template #default="{row}">
              {{ (row.money)/100 }}
            </template>
          </el-table-column>
          <el-table-column prop="describe" label="商品描述" />
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
import { rechargeProductCfg, updateRechargeProductCfg } from '@/api/specialOperation'

export default {
  data() {
    return {
      productData: [],
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0 // 表格高度
    }
  },

  created() {
    this.getProductConfig()
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
    async  getProductConfig() {
      const res = await rechargeProductCfg({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.productData = res.data.data.data
    },

    // 更新
    async handleUpdate() {
      const res = await updateRechargeProductCfg()
      if (res.data.code === 0) {
        this.$message.success('更新成功')
      } else {
        this.$message.error('更新失败')
      }
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getProductConfig()
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
