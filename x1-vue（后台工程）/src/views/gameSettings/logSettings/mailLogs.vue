<!-- 已删邮件日志 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <div>
          <span>日期查询：</span>
          <el-date-picker
            v-model="date"
            type="daterange"
            range-separator="至"
            start-placeholder="选择开始日期"
            end-placeholder="选择结束日期"
            :picker-options="pickerOptions"
            value-format="timestamp"
            size="small"
          />
        </div>
        <div>
          <span>服务器：</span>
          <el-input v-model="serverText" style="width:200px;" size="small" disabled placeholder="请选择" />
          <el-button
            size="small"
            type="primary"
            plain
            icon="el-icon-circle-plus-outline"
            @click="dialogVisible = true"
          >选择服务器</el-button>
        </div>
        <div>
          <span>玩家ID：</span>
          <el-input v-model.number="playerId" clearable placeholder="请输入" style="width:200px;" size="small" />
        </div>
        <div>
          <span>流水事件类型：</span>
          <el-select v-model="waterEvent" clearable filterable size="small" style="width:200px;">
            <el-option
              v-for="(item,index) in waterOptions"
              :key="index"
              :label="item.text"
              :value="item.value"
            />
          </el-select>
        </div>

        <el-button icon="el-icon-search" type="primary" size="small" @click="handleInquire">查询</el-button>
      </div>

      <!-- 表格 -->
      <div>
        <el-table
          ref="table"
          border
          :data="mailData"
          :header-cell-style="rowClass"
          :cell-style="{'text-align':'center'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="日志ID" min-width="150" />
          <el-table-column prop="oriMailId" label="原邮件ID" min-width="150" />
          <el-table-column prop="ownerId" label="玩家ID" min-width="150" />
          <el-table-column prop="ownerName" label="玩家名字" min-width="150" />

          <el-table-column prop="template" label="邮件模板ID" min-width="150" />
          <el-table-column prop="titleArgs" label="标题参数" min-width="150" />
          <el-table-column prop="contentArgs" label="内容参数" min-width="150" />
          <el-table-column prop="attachment" label="附件" min-width="250" />

          <el-table-column prop="read" label="是否已读" min-width="150">
            <template slot-scope="scope">
              {{ scope.row.read?'是':'否' }}
            </template>
          </el-table-column>
          <el-table-column prop="receive" label="是否已领" min-width="150">
            <template slot-scope="scope">
              {{ scope.row.receive?'是':'否' }}
            </template>
          </el-table-column>

          <el-table-column prop="expireTime" label="到期时间" min-width="200">
            <template slot-scope="scope">{{ scope.row.expireTime|formatTime }}</template>
          </el-table-column>
          <el-table-column label="游戏事件">
            <el-table-column prop="gameCause" min-width="150" />
            <el-table-column prop="gameCauseText" min-width="250" />
          </el-table-column>
          <el-table-column prop="fromPoolId" label="来自哪个系统邮件" min-width="200" />
          <el-table-column prop="time" label="发生时间" min-width="200">
            <template slot-scope="scope">{{ scope.row.time|formatTime }}</template>
          </el-table-column>
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

    <!-- 服务器-单选弹窗 -->
    <ServerDialog
      :visible.sync="dialogVisible"
      :server-options="serverOptions"
      @checkedId="getCheckedServer"
    />

  </div>
</template>

<script>
import ServerDialog from '@/components/radioDialog/index.vue'
import { mailLogs } from '@/api/playerResourceLog'
import { multiServer } from '@/api/aboutServer'
import { waterEventOpt } from '@/api/specialOperation'

export default {
  components: {
    ServerDialog
  },
  data() {
    return {
      total: 0,
      mailData: [],
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      playerId: '', // 玩家Id
      date: [new Date().setDate(1), new Date()], // 日期
      pickerOptions: { // 限制只能选择当前月
        disabledDate(time) {
          const month = new Date().getMonth()
          const year = new Date().getFullYear()
          return time.getMonth() !== month || time.getFullYear() !== year
        }
      },
      serverUid: '', // 选中的服务器ID
      serverOptions: [], // 服务器选项
      dialogVisible: false, // 服务器弹窗
      serverText: '', // 服务器名称
      waterEvent: '', // 流水事件类型
      waterOptions: []
    }
  },

  created() {
    this.getWater()
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

    // 获取选中的服务器
    getCheckedServer(val) {
      this.serverUid = val
      const matchedOption = this.serverOptions.find(obj => obj.value === this.serverUid)
      if (matchedOption) {
        this.serverText = matchedOption.text
      } else {
        this.serverText = ''
      }
    },

    // 获取流水事件
    async getWater() {
      const res = await waterEventOpt()
      this.waterOptions = res.data.data.options
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.handleInquire()
    },

    // 查询
    async handleInquire() {
      try {
        const query = { page: this.currentPage, limit: this.pageSize }
        if (this.date && this.date.length === 2) {
          // 时间戳毫秒转化为秒
          query.startTime = this.$moment(this.date[0]).unix()
          query.endTime = this.$moment(this.date[1]).unix()
        }
        if (this.playerId) {
          query.playerId = this.playerId
        }
        if (this.waterEvent) {
          query.cause = this.waterEvent
        }
        if (!this.serverText) {
          this.$message.error('请选择服务器，服务器为必选项')
        } else {
          query.serverUid = this.serverUid

          const result = await mailLogs(query)
          this.total = result.data.data.count
          this.mailData = result.data.data.data
        }
      } catch (error) {
        console.error(error)
        this.$message.error('发生错误，请稍后重试')
      }
    },

    // 表格自适应高度
    getTableHeight() {
      if (this.$refs.table && this.$refs.table.$el) {
        this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 170
      } else {
        this.tableHeight = 300
      }
    },

    // 合并游戏事件表头
    rowClass({ rowIndex }) {
      if (rowIndex === 1) {
        return { display: 'none' }
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

::v-deep .el-table th {
  text-align: center;
}
</style>
