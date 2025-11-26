<!-- 付费LTV -->
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

        <div>
          <el-input v-model="command" style="width:200px" size="small" clearable placeholder="请输入" />
          <el-button size="small" type="success" @click="handleOrder">执行命令</el-button>
        </div>

      </div>

      <div>
        <el-table
          ref="table"
          border
          :data="playerLtvData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="serverUid" label="数据唯一ID" width="120" />
          <el-table-column prop="serverId" label="服务器ID" width="120" />
          <el-table-column prop="serverName" label="服务器名字" width="120" />
          <el-table-column prop="bornDate" label="日期" width="120" />
          <el-table-column prop="playerCount" label="账号数" width="120" />
          <el-table-column prop="day0" label="当日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day0/scope.row.playerCount)/100) }}
              ({{ scope.row.day0/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day1" label="1日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day1/scope.row.playerCount)/100) }}
              ({{ scope.row.day1/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day2" label="2日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day2/scope.row.playerCount)/100) }}
              ({{ scope.row.day2/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day3" label="3日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day3/scope.row.playerCount)/100) }}
              ({{ scope.row.day3/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day4" label="4日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day4/scope.row.playerCount)/100) }}
              ({{ scope.row.day4/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day5" label="5日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day5/scope.row.playerCount)/100) }}
              ({{ scope.row.day5/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day6" label="6日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day6/scope.row.playerCount)/100) }}
              ({{ scope.row.day6/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day7" label="7日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day7/scope.row.playerCount)/100) }}
              ({{ scope.row.day7/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day8" label="8日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day8/scope.row.playerCount)/100) }}
              ({{ scope.row.day8/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day9" label="9日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day9/scope.row.playerCount)/100) }}
              ({{ scope.row.day9/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day10" label="10日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day10/scope.row.playerCount)/100) }}
              ({{ scope.row.day10/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day11" label="11日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day11/scope.row.playerCount)/100) }}
              ({{ scope.row.day11/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day12" label="12日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day12/scope.row.playerCount)/100) }}
              ({{ scope.row.day12/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day13" label="13日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day13/scope.row.playerCount)/100) }}
              ({{ scope.row.day13/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day14" label="14日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day14/scope.row.playerCount)/100) }}
              ({{ scope.row.day14/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day30" label="30日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day30/scope.row.playerCount)/100) }}
              ({{ scope.row.day30/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day60" label="60日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day60/scope.row.playerCount)/100) }}
              ({{ scope.row.day60/100 }})
            </template>
          </el-table-column>
          <el-table-column prop="day90" label="90日LTV" min-width="150">
            <template slot-scope="scope">
              {{ formatNumber((scope.row.day90/scope.row.playerCount)/100) }}
              ({{ scope.row.day90/100 }})
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 服务器-单选弹窗 -->
    <ServerDialog
      :visible.sync="radioDialog"
      :server-options="serverOptions"
      @checkedId="handleCheckedId"
    />
  </div>
</template>

<script>
import ServerDialog from '@/components/radioDialog/index.vue'
import { playerLtv, manualTriggerLTV } from '@/api/gameStatistics'
import { multiServer } from '@/api/aboutServer'

export default {
  components: {
    ServerDialog
  },
  data() {
    return {
      playerLtvData: [],
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      date: [new Date().setDate(1), new Date()], // 日期
      serverUid: '', // 选中的服务器id
      serverOptions: [], // 服务器选项
      radioDialog: false, // 服务器弹窗
      serverText: '', // 服务器名称
      command: ''
    }
  },

  created() {
    this.getPlayerLtv()
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
    // 获取数据
    async getPlayerLtv() {
      const res = await playerLtv({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.playerLtvData = res.data.data.data
    },

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
      try {
        const query = {}
        if (this.date && this.date.length === 2) {
          // 时间戳毫秒转化为秒
          query.startTime = this.$moment(this.date[0]).unix()
          query.endTime = this.$moment(this.date[1]).unix()
        }
        if (this.serverText) {
          query.serverUid = this.serverUid
        }
        const result = await playerLtv(query)
        this.playerLtvData = result.data.data.data
      } catch (error) {
        console.error(error)
        this.$message.error('发生错误，请稍后重试')
      }
    },

    // 执行命令
    async handleOrder() {
      if (!this.command) {
        this.$message.warning('请输入执行命令')
      } else {
        const res = await manualTriggerLTV({ command: this.command })
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
          this.command = ''
        }
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
