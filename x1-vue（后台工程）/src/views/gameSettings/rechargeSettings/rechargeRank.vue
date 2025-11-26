<!-- 充值排名 -->
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
          <el-input v-model="serverText" style="width:300px" size="small" disabled placeholder="请选择" />
          <el-button
            size="small"
            type="primary"
            plain
            icon="el-icon-circle-plus-outline"
            @click="dialogVisible=true"
          >选择服务器</el-button>
        </div>
        <div>
          <span>排名：</span>
          <el-input v-model.number="rank" size="small" style="width:200px" clearable placeholder="请输入" />
        </div>

        <el-button size="small" icon="el-icon-search" type="primary" @click="searchBtn">搜索</el-button>
      </div>

      <div>
        <el-table
          ref="table"
          border
          :data="rankData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="channelId" label="渠道ID" min-width="120" />
          <el-table-column prop="channelName" label="渠道名字" min-width="150" />
          <el-table-column prop="playerId" label="玩家ID" min-width="150" />
          <el-table-column prop="nick" label="玩家昵称" min-width="150" />
          <el-table-column prop="serverUid" label="数据唯一ID" min-width="150" />
          <el-table-column prop="serverId" label="服务器ID" min-width="120" />
          <el-table-column prop="serverName" label="服务器名字" min-width="120" />
          <el-table-column prop="bornTime" label="创号时间" min-width="180">
            <template slot-scope="scope">{{ scope.row.bornTime|formatTime }}</template>
          </el-table-column>
          <el-table-column prop="level" label="玩家等级" min-width="120" />
          <el-table-column prop="gold" label="金币" min-width="120" />
          <el-table-column prop="diamond" label="钻石" min-width="120" />
          <el-table-column prop="fighting" label="总战力" min-width="120" />
          <el-table-column prop="money" label="充值金额 (单位：元)" min-width="150">
            <template #default="{row}">
              {{ (row.money)/100 }}
            </template>
          </el-table-column>
          <el-table-column prop="lastRechargeTime" label="最后充值时间" min-width="180">
            <template slot-scope="scope">{{ scope.row.lastRechargeTime|formatTime }}</template>
          </el-table-column>
          <el-table-column prop="lastLoginTime" label="最后登录时间" min-width="180">
            <template slot-scope="scope">{{ scope.row.lastLoginTime|formatTime }}</template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="120">
            <template slot-scope="scope">
              <el-button
                icon="el-icon-view"
                size="small"
                type="primary"
                plain
                @click="handleViewDetail(scope.row.playerId)"
              >查看详情</el-button>
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
import { multiServer } from '@/api/aboutServer'
import { rechargeRank } from '@/api/relatedRecharge'

export default {
  components: {
    ServerDialog
  },
  data() {
    return {
      rankData: [],
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      serverUids: [], // 选中的服务器ID集合
      serverOptions: [], // 服务器选项
      dialogVisible: false,
      serverText: '', // 服务器名称
      date: [new Date().setDate(1), new Date()],
      rank: 200// 排名
    }
  },

  created() {
    this.getRank()
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
    async getRank() {
      const res = await rechargeRank({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.rankData = res.data.data.data
    },

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
    async searchBtn() {
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
        if (this.rank) {
          query.limit = this.rank
        }
        const result = await rechargeRank(query)
        this.rankData = result.data.data.data
      } catch (error) {
        console.error(error)
        this.$message.error('发生错误，请稍后重试')
      }
    },

    // 查看详情-跳转
    handleViewDetail(id) {
      this.$router.push({ path: '/player', query: { detailId: id }})
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
  flex-wrap: wrap;
  gap: 30px;
}

</style>
