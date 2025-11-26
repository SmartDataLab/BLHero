<!-- 通用兑换码日志 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <div>
          <span>礼包码：</span>
          <el-input v-model.trim="code" style="width:200px" size="small" clearable placeholder="请输入" />
        </div>
        <div>
          <span>玩家ID：</span>
          <el-input v-model.trim="playerId" style="width:200px" size="small" clearable placeholder="请输入" />
        </div>
        <div>
          <span>玩家名称：</span>
          <el-input v-model.trim="playerName" style="width:200px" size="small" clearable placeholder="请输入" />
        </div>

        <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
      </div>

      <!-- 表格 -->
      <div>
        <el-table
          ref="table"
          border
          :data="giftCodeLogData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="记录ID" />
          <el-table-column prop="code" label="礼包码" />
          <el-table-column prop="playerId" label="玩家ID" />
          <el-table-column prop="playerName" label="玩家名称" />
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
import { giftCodeShareLog } from '@/api/specialOperation'

export default {
  data() {
    return {
      giftCodeLogData: [],
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      code: '', // 礼包码
      playerId: '', // 玩家ID
      playerName: ''// 玩家名称
    }
  },

  created() {
    this.getGiftCodeLog()
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
    async getGiftCodeLog() {
      const res = await giftCodeShareLog({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.giftCodeLogData = res.data.data.data
    },

    // 查询
    async handleSearch() {
      try {
        const query = {}
        if (this.code) {
          query.code = this.code
        }
        if (this.playerId) {
          query.playerId = this.playerId
        }
        if (this.playerName) {
          query.playerName = this.playerName
        }
        const res = await giftCodeShareLog(query)
        this.giftCodeLogData = res.data.data.data
        this.total = res.data.data.count
      } catch (error) {
        console.log(error)
      }
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getGiftCodeLog()
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
.topInquire{
  font-weight: bold;
  font-size: 14px;
  color: #6a7488;
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
}

</style>

