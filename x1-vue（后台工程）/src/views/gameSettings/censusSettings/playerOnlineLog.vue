<!-- 在线人数 -->
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

        <el-button size="small" icon="el-icon-search" type="primary" @click="handleSearch">搜索</el-button>
      </div>

      <div>
        <el-tabs v-model="activeName">
          <!-- 表 -->
          <el-tab-pane label="表" name="table">
            <el-table
              ref="table"
              :data="onlineNumData"
              border
              :header-cell-style="{background:'#f5f7fa'}"
              :height="tableHeight"
            >
              <el-table-column prop="period" label="序号" />
              <el-table-column prop="periodText" label="时间" />
              <el-table-column prop="onlineNum" label="在线人数" />
              <el-table-column prop="newOnlineNum" label="在线新人数" />
            </el-table>
          </el-tab-pane>

          <!-- 图 -->
          <el-tab-pane label="图" name="chart">
            <div id="onlineChart" style="width: 100%; height: 600px;" />
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
import { playerOnlineLog } from '@/api/gameStatistics'

export default {
  components: {
    ServerDialog
  },
  data() {
    return {
      activeName: 'chart',
      onlineNumData: [],
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
      const query = {}
      if (this.date) {
        // 时间戳毫秒转化为秒
        const secondTimestamp = this.$moment(this.date).unix()
        query.time = secondTimestamp
      }
      if (this.serverUids) {
        query.serverUids = this.serverUids
      }
      const result = await playerOnlineLog(query)
      this.onlineNumData = result.data.data.data
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
      // 在线人数图表
      const onlineNumber = this.$echarts.init(document.getElementById('onlineChart'), null)

      // 获取X轴
      const xAxis = this.onlineNumData.map(item => item.periodText)

      // 获取在线人数
      const onlineData = this.onlineNumData.map(item => item.onlineNum)

      // 获取新在线人数
      const newOnlineData = this.onlineNumData.map(item => item.newOnlineNum)

      // 获取最高在线人数
      const PCU = Math.max(...onlineData)

      // Y轴数据间隔
      function getOutput(maxOnline) {
        if (maxOnline >= 100000) {
          return 10000
        } else if (maxOnline >= 10000) {
          return 1000
        } else if (maxOnline >= 1000) {
          return 100
        } else if (maxOnline >= 100) {
          return 10
        }
      }
      const interval = getOutput(PCU)

      const options = {
        title: {
          text: '在线人数'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['在线人数', '在线新人数']
        },
        xAxis: {
          data: xAxis
        },
        yAxis: {
          type: 'value',
          interval: interval,
          minInterval: 1
        },
        dataZoom: [{ start: 0, end: 100 }],
        series: [
          {
            name: '在线人数',
            type: 'line',
            data: onlineData,
            smooth: true,
            showSymbol: false
          },
          {
            name: '在线新人数',
            type: 'line',
            data: newOnlineData,
            smooth: true,
            showSymbol: false
          }
        ],
        grid: {
          left: 50,
          right: 10,
          top: 100,
          containLabel: true
        }

      }
      onlineNumber.setOption(options)

      window.addEventListener('resize', () => {
        onlineNumber.resize()
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
