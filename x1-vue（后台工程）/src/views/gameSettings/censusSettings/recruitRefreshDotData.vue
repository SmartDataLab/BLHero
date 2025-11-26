<!-- 招募刷新数据打点 -->
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

        <div>
          <span>服务器：</span>
          <el-input v-model="serverText" style="width:200px" size="small" disabled placeholder="请选择" />
          <el-button
            size="small"
            type="primary"
            plain
            icon="el-icon-circle-plus-outline"
            @click="radioDialog=true"
          >选择服务器</el-button>
        </div>

        <el-button size="small" icon="el-icon-search" type="primary" @click="handleSearch">搜索</el-button>
      </div>

      <el-table
        ref="table"
        border
        :data="recruitRefreshDotData"
        :header-cell-style="{background:'#f5f7fa'}"
        :height="tableHeight"
      >
        <el-table-column prop="dateStr" label="日期" min-width="120" />
        <el-table-column prop="num0to10" label="0 - 10" min-width="120" />
        <el-table-column prop="num11to20" label="11 - 20" min-width="120" />
        <el-table-column prop="num21toMax" label="21 - 以上" min-width="120" />
        <el-table-column prop="joinNum" label="参与人数" min-width="120" />
        <el-table-column prop="createNum" label="创号人数" min-width="120" />
      </el-table>

      <!-- 服务器-单选弹窗 -->
      <ServerDialog
        :visible.sync="radioDialog"
        :server-options="serverOptions"
        @checkedId="handleCheckedId"
      />

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
import ServerDialog from '@/components/radioDialog/index.vue'
import { multiServer } from '@/api/aboutServer'
import { recruitRefreshDot } from '@/api/gameStatistics'

export default {
  components: {
    ServerDialog
  },

  data() {
    return {
      recruitRefreshDotData: [],
      total: 0,
      currentPage: 1,
      pageSize: 200,
      tableHeight: 0,
      serverText: '',
      radioDialog: false,
      serverOptions: [],
      serverUid: '',
      date: [new Date().setDate(1), new Date()]
    }
  },

  created() {
    this.getServer()
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
    // 获取服务器
    async getServer() {
      const res = await multiServer()
      this.serverOptions = res.data.data.options
        .sort((a, b) => a.value - b.value)
    },

    // 选中的服务器ID
    handleCheckedId(val) {
      this.serverUid = val

      const matchedOption = this.serverOptions.find(obj => obj.value === this.serverUid)
      if (matchedOption) {
        this.serverText = matchedOption.text
      } else {
        this.serverText = ''
      }
      this.radioDialog = false
    },

    // 搜索
    async handleSearch() {
      const query = { page: this.currentPage, limit: this.pageSize }

      if (this.date && this.date.length === 2) {
        // 时间戳毫秒转化为秒
        query.startTime = this.$moment(this.date[0]).unix()
        query.endTime = this.$moment(this.date[1]).unix()
      }

      if (!this.serverText) {
        this.$message.error('请选择服务器，服务器为必选项')
      } else {
        const result = await recruitRefreshDot(query)
        this.recruitRefreshDotData = result.data.data.data
        this.total = result.data.data.count
      }
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.handleSearch()
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

