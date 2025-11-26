<!-- 在线时长占比 -->
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
        <el-table
          ref="table"
          border
          :data="durationData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
          :span-method="objectSpanMethod"
        >
          <el-table-column prop="date" label="日期" />
          <el-table-column prop="timePeriodText" label="时间时区" />
          <el-table-column prop="onlineNum" label="在线人数" />
          <el-table-column v-if="false" prop="onlineBase" />
          <el-table-column label="占比">
            <template slot-scope="scope">
              {{ formatNumber(scope.row.onlineNum/scope.row.onlineBase*100) }}%
            </template>
          </el-table-column>
        </el-table>
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
import { onlineTimeRatio } from '@/api/gameStatistics'
import { multiServer } from '@/api/aboutServer'

export default {
  components: {
    ServerDialog
  },
  data() {
    return {
      durationData: [],
      tableHeight: 0, // 表格高度
      date: [new Date().setDate(1), new Date()], // 日期
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
  },

  beforeDestroy() {
    window.removeEventListener('resize', this.getTableHeight)
  },

  methods: {
    // 获取服务器
    async getServer() {
      const res = await multiServer()
      this.serverOptions = res.data.data.options.sort((a, b) => a.value - b.value)
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
      try {
        const query = {}
        if (this.date && this.date.length === 2) {
          // 时间戳毫秒转化为秒
          query.startTime = this.$moment(this.date[0]).unix()
          query.endTime = this.$moment(this.date[1]).unix()
        }
        if (this.serverUids) {
          query.serverUids = this.serverUids
        }
        const result = await onlineTimeRatio(query)
        this.durationData = result.data.data.data
      } catch (error) {
        console.error(error)
        this.$message.error('发生错误，请稍后重试')
      }
    },

    // 表格自适应高度
    getTableHeight() {
      if (this.$refs.table && this.$refs.table.$el) {
        this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 140
      } else {
        this.tableHeight = 300
      }
    },

    // 保留两位小数
    formatNumber(value) {
      return value.toFixed(2)
    },

    // 合并单元格
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0) {
        const _row = this.filterData(this.durationData, columnIndex).one[rowIndex]
        const _col = _row > 0 ? 1 : 0
        return {
          rowspan: _row,
          colspan: _col
        }
      }
    },
    filterData(arr, colIndex) {
      const spanOneArr = []
      let concatOne = 0
      arr.forEach((item, index) => {
        if (index === 0) {
          spanOneArr.push(1)
        } else {
          if (colIndex === 0) {
            if (item.date === arr[index - 1].date) {
              spanOneArr[concatOne] += 1
              spanOneArr.push(0)
            } else {
              spanOneArr.push(1)
              concatOne = index
            }
          }
        }
      })
      return {
        one: spanOneArr
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

::v-deep .el-tabs__item{
  font-size: 18px;
}
</style>
