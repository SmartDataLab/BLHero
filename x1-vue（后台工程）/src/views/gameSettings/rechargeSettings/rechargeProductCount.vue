<!-- 充值项目统计 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <div>
          <span>日期：</span>
          <el-date-picker
            v-model="date"
            type="daterange"
            range-separator="至"
            start-placeholder="选择开始日期"
            end-placeholder="选择结束日期"
            value-format="timestamp"
            size="small"
          />
        </div>

        <el-button size="small" icon="el-icon-search" type="primary" @click="handleSearch">搜索</el-button>
      </div>

      <div>
        <el-table
          ref="table"
          border
          :data="statisticsData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="productId" label="充值商品ID" />
          <el-table-column prop="productName" label="充值商品名字" />
          <el-table-column prop="playerCount" label="购买人数" />
          <el-table-column prop="buyCount" label="购买次数" />
          <el-table-column prop="totalPay" label="总额（单位：元）">
            <template #default="{row}">
              {{ (row.totalPay)/100 }}
            </template>
          </el-table-column>
          <el-table-column prop="rate" label="充值占比">
            <template slot-scope="scope">
              {{ formatNumber(scope.row.rate/100) }}%
            </template>
          </el-table-column>
        </el-table>
      </div>

    </el-card>
  </div>
</template>

<script>
import { rechargeStats } from '@/api/relatedRecharge'

export default {
  data() {
    return {
      statisticsData: [],
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      date: [new Date().setDate(1), new Date()] // 日期
    }
  },

  created() {
    this.getStatistics()
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
    async getStatistics() {
      const res = await rechargeStats({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.statisticsData = res.data.data.data
    },

    // 搜索
    async handleSearch() {
      try {
        const query = {}
        if (this.date && this.date.length === 2) {
          // 时间戳毫秒转化为秒
          query.startTime = this.$moment(this.date[0]).unix()
          query.endTime = this.$moment(this.date[1]).unix()
        }
        const result = await rechargeStats(query)
        this.statisticsData = result.data.data.data
      } catch (error) {
        console.error(error)
        this.$message.error('发生错误，请稍后重试')
      }
    },

    // 保留两位小数
    formatNumber(value) {
      return value.toFixed(2)
    },

    // 表格自适应高度
    getTableHeight() {
      if (this.$refs.table && this.$refs.table.$el) {
        this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 140
      } else {
        this.tableHeight = 300
      }
    }

  }
}
</script>
  <style lang="scss" scoped>

  .topInquire{
    font-weight: bold;
    font-size: 14px;
    color: #6a7488;
    margin-bottom: 20px;
    display: flex;
    gap: 30px;
    flex-wrap: wrap;
  }

  </style>

