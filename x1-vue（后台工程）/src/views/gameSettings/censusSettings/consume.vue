<!-- 货币消费统计 -->
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
          <span>资源类型：</span>
          <el-select v-model.number="resourceType" size="small" clearable>
            <el-option :value="2" label="钻石" />
            <el-option :value="1" label="金币" />
            <el-option :value="5" label="肉" />
            <el-option :value="4" label="木材" />
            <el-option :value="6" label="矿石" />
          </el-select>
        </div>
        <div>
          <span>消费类型：</span>
          <el-select v-model.number="consumeType" size="small" clearable>
            <el-option :value="1" label="消耗" />
            <el-option :value="2" label="产出" />
          </el-select>
        </div>
        <div>
          <span>服务器：</span>
          <el-input v-model="serverText" style="width:200px" size="small" disabled placeholder="请选择" />
          <el-button
            size="small"
            type="primary"
            plain
            icon="el-icon-circle-plus-outline"
            @click="radioDialog = true"
          >选择服务器</el-button>
        </div>

        <el-button size="small" type="primary" icon="el-icon-search" @click="handleInquire">查询</el-button>
      </div>

      <div>
        <el-table
          ref="table"
          border
          :data="consumeSumData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="index" label="序号" min-width="100" />
          <el-table-column prop="gameCause" label="流水类型编号" min-width="120" />
          <el-table-column prop="gameCauseTxt" label="流水类型名称" min-width="160" />
          <el-table-column prop="resourceType" label="资源类型" min-width="120">
            <template slot-scope="scope">
              {{ getType(scope.row.resourceType) }}
            </template>
          </el-table-column>
          <el-table-column prop="consumeType" label="消费类型" min-width="120">
            <template slot-scope="scope">
              {{ scope.row.consumeType==1?'消耗':'产出' }}
            </template>
          </el-table-column>
          <el-table-column prop="playerNum" label="消费人数" min-width="120" />
          <el-table-column prop="countNum" label="消费次数" min-width="120" />
          <el-table-column prop="total" label="消费总数" min-width="120" />
          <el-table-column prop="avg" label="平均消费数" min-width="120">
            <template slot-scope="scope">
              {{ formatNumber(scope.row.avg) }}
            </template>
          </el-table-column>
          <el-table-column prop="weight" label="占比" min-width="120">
            <template slot-scope="scope">
              {{ formatNumber(scope.row.weight*100) }}%
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
      <el-pagination
        background
        layout="total, prev, pager, next, jumper"
        :total="total"
        :current-page="currentPage"
        :page-size="pageSize"
        @current-change="changePage"
      />
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
import { consumeSum } from '@/api/gameStatistics'
import { multiServer } from '@/api/aboutServer'
const typeEnum = { 1: '金币', 2: '钻石', 4: '木材', 5: '肉', 6: '矿石' }

export default {
  components: {
    ServerDialog
  },
  data() {
    return {
      consumeSumData: [],
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      date: [new Date().setDate(1), new Date()], // 日期
      serverUid: '', // 选中的服务器id
      serverOptions: [], // 服务器选项
      radioDialog: false, // 服务器弹窗
      serverText: '', // 服务器名称
      resourceType: 2, // 资源类型
      consumeType: 1// 消费类型
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

    // 搜索按钮
    async handleInquire() {
      try {
        const query = { page: this.currentPage, limit: this.pageSize }
        if (this.date && this.date.length === 2) {
          // 时间戳毫秒转化为秒
          query.startTime = this.$moment(this.date[0]).unix()
          query.endTime = this.$moment(this.date[1]).unix()
        }
        if (this.resourceType) {
          query.resourceType = this.resourceType
        }
        if (this.consumeType) {
          query.consumeType = this.consumeType
        }
        if (!this.serverText) {
          this.$message.error('请选择服务器，服务器为必选项')
        } else {
          query.serverUid = this.serverUid

          const result = await consumeSum(query)
          this.consumeSumData = result.data.data.data
          this.total = result.data.data.count
        }
      } catch (error) {
        console.error(error)
      }
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.handleInquire()
    },

    // 获取资源类型
    getType(value) {
      return typeEnum[value]
    },

    // 保留两位小数(不四舍五入)
    formatNumber(value) {
      return (parseInt(value * 100) / 100).toFixed(2)
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

