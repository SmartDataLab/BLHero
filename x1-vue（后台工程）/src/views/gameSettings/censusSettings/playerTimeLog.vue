<!-- 注册分时 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <div>
          <span>日期：</span>
          <el-date-picker
            v-model="date"
            type="date"
            placeholder="请选择日期"
            value-format="timestamp"
            size="small"
          />
        </div>
        <div>
          <span>服务器：</span>
          <el-input v-model="serverText" style="width:420px" size="small" disabled placeholder="请选择" />
          <el-button
            size="small"
            type="primary"
            plain
            icon="el-icon-circle-plus-outline"
            @click="dialogVisible=true"
          >选择服务器</el-button>
        </div>

        <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
      </div>

      <div>
        <el-tabs v-model="activeName">
          <!-- 表 -->
          <el-tab-pane label="表" name="table">
            <el-table
              ref="table"
              :data="registerTimeData"
              border
              :header-cell-style="{background:'#f5f7fa'}"
              :height="tableHeight"
            >
              <el-table-column prop="timePeriod" label="时间" />
              <el-table-column prop="createNum" label="注册数" />
              <el-table-column prop="loginNum" label="登录数" />
              <el-table-column prop="maxOnline" label="最高在线" />
              <el-table-column prop="minOnline" label="最低在线" />
            </el-table>
          </el-tab-pane>

          <!-- 图 -->
          <el-tab-pane label="图" name="chart">
            <div id="registerChart" style="width: 100%; height: 600px;" />
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>

    <!-- 服务器弹窗-多选 -->
    <ServerDialog
      :visible.sync="dialogVisible"
      :server-options="serverOptions"
      @checkedId="handleCheckedId"
    />
  </div>
</template>

<script>
import ServerDialog from '@/components/multipleDialog/index.vue'
import { multiServer } from '@/api/aboutServer'
import { playerTimeLog } from '@/api/gameStatistics'

export default {
  components: {
    ServerDialog
  },
  data() {
    return {
      activeName: 'chart',
      registerTimeData: [],
      tableHeight: 0, // 表格高度
      date: new Date(), // 日期
      serverUids: [], // 选中的服务器ID集合
      serverOptions: [], // 服务器选项
      dialogVisible: false,
      serverText: '' // 服务器名称
    }
  },

  created() {
    this.getServer()
  },

  mounted() {
    // 表格高度自适应
    this.$nextTick(() => {
      this.getTableHeight()
    })
    window.addEventListener('resize', this.$debounce(this.getTableHeight, 100))

    // 获取图表
    setTimeout(() => {
      this.activeName = 'table'
      this.getChart()
    }, 100)
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

    // 选中的服务器
    handleCheckedId(val) {
      this.serverUids = val

      if (this.serverUids.length > 0) {
        this.serverText = this.serverOptions
          .filter(item => this.serverUids.includes(item.value))
          .map(item => item.text)
          .join('，')
      } else {
        this.serverText = ''
      }
      this.dialogVisible = false
    },

    // 搜索按钮
    async handleSearch() {
      const query = { }

      if (this.date) {
        // 时间戳毫秒转化为秒
        const secondTimestamp = this.$moment(this.date).unix()
        query.time = secondTimestamp
      }
      if (this.serverUids) {
        query.serverUids = this.serverUids
      }
      const result = await playerTimeLog(query)
      this.registerTimeData = result.data.data.data
      await this.getChart()
    },

    // 表格自适应高度
    getTableHeight() {
      if (this.$refs.table && this.$refs.table.$el) {
        this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 270
      } else {
        this.tableHeight = 300
      }
    },

    getChart() {
      // 注册时分图表
      const registerGraph = this.$echarts.init(document.getElementById('registerChart'), null)

      // 计算注册玩家总数
      const sumCreateNum = this.registerTimeData.map(item => item.createNum)
        .reduce((sum, value) => sum + value, 0)

      // 获取每个时间段数
      const timeFrame = this.registerTimeData.map(item => item.createNum)

      // 计算占比(保留两位小数)
      const ratio = timeFrame.map(item => {
        const val = (item / sumCreateNum).toFixed(2)
        return isNaN(val) ? 0 : val
      })

      // 获取X轴
      const xAxis = this.registerTimeData.map(item => item.timePeriod.slice(-2) + '时')

      const options = {
        title: {
          text: '注册统计',
          subtext: '总注册玩家人数：' + sumCreateNum
        },
        subtextStyle: {
          fontWeight: 'bold'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['当前时段注册玩家占比']
        },
        xAxis: {
          data: xAxis
        },
        yAxis: {
          min: 0,
          max: 1
        },
        dataZoom: [{ start: 0, end: 100 }],
        series: [
          {
            name: '当前时段注册玩家占比',
            type: 'line',
            data: ratio,
            smooth: true
          }
        ],
        grid: {
          left: 50,
          right: 10,
          top: 100,
          containLabel: true
        }

      }
      registerGraph.setOption(options)

      window.addEventListener('resize', () => {
        registerGraph.resize()
      })
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

::v-deep .el-tabs__item{
  font-size: 16px;
}
</style>
