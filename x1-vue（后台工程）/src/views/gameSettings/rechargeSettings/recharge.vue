<!-- 充值回调查询 -->
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
          <span>玩家ID：</span>
          <el-input v-model.number="playerId" size="small" style="width:200px" clearable placeholder="请输入玩家ID" />
        </div>
        <div>
          <span>玩家名字：</span>
          <el-input v-model.trim="nick" size="small" style="width:200px" clearable placeholder="请输入玩家名字" />
        </div>
        <div>
          <span>账号ID：</span>
          <el-input v-model.number="openId" size="small" style="width:200px" clearable placeholder="请输入账号ID" />
        </div>
        <div>
          <span>渠道订单号：</span>
          <el-input v-model.number="orderNumber" size="small" style="width:200px" clearable placeholder="请输入订单号" />
        </div>
        <div>
          <span>订单状态：</span>
          <el-select v-model.number="orderStatus" size="small" style="width:200px" clearable>
            <el-option :value="-1" label="全部" />
            <el-option :value="0" label="未处理" />
            <el-option :value="1" label="发货成功" />
            <el-option :value="2" label="发货失败" />
          </el-select>
        </div>

        <el-button size="small" icon="el-icon-search" type="primary" @click="handleSearch">搜索</el-button>
      </div>

      <div>
        <el-table
          ref="table"
          border
          :data="rechargeData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="数据ID" width="120" />
          <el-table-column prop="platformId" label="平台ID" width="120" />
          <el-table-column prop="channelId" label="渠道ID" width="100" />
          <el-table-column prop="serverId" label="服务器ID" width="100" />
          <el-table-column prop="playerId" label="玩家ID" width="160" />
          <el-table-column prop="nick" label="玩家名字" width="130" />
          <el-table-column prop="openId" label="账号ID" width="130" />
          <el-table-column prop="level" label="下单等级" width="100" />
          <el-table-column prop="sdkOrderId" label="渠道订单号" width="160" />
          <el-table-column prop="gameOrderId" label="游戏服订单号" width="130" />
          <el-table-column prop="money" label="支付金额 (单位：元)" width="150">
            <template #default="{row}">
              {{ (row.money)/100 }}
            </template>
          </el-table-column>
          <el-table-column prop="productId" label="充值商品ID" width="130" />
          <el-table-column prop="productName" label="充值商品名字" width="160" />
          <el-table-column prop="insertTime" label="充值时间" min-width="160">
            <template slot-scope="scope">{{ scope.row.insertTime|formatTime }}</template>
          </el-table-column>
          <el-table-column prop="give" label="发货状态" width="130">
            <template slot-scope="scope">
              <el-tag :type="scope.row.give === 0 ? 'info' : scope.row.give === 1 ? 'success' : 'danger'">
                {{ getStatus(scope.row.give) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" width="400" />
          <el-table-column prop="test" label="是否测试订单" width="130">
            <template #default="{row}">
              <el-tag :type="row.test === 0? 'success' : 'danger'">
                {{ row.test === 0?'是':'否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="allCheck" label="是否所有验证都通过" width="150">
            <template #default="{row}">
              <el-tag :type="row.allCheck? 'success' : 'danger'">
                {{ row.allCheck?'是':'否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="gameResponse" label="游戏服的响应" width="400" />
          <el-table-column prop="callbackData" label="回调原数据" width="1000" />
          <el-table-column prop="sign" label="订单签名" width="300" />
          <el-table-column prop="localSign" label="本地订单签名" width="300" />
          <el-table-column prop="extraSign" label="扩展参数签名" width="300" />
          <el-table-column prop="localExtraSign" label="本地扩展参数签名" width="300" />
          <el-table-column prop="remoteIp" label="请求的IP" width="150" />
          <el-table-column fixed="right" label="操作" width="120">
            <template slot-scope="scope">
              <el-button
                v-show="scope.row.give!==1"
                size="small"
                type="primary"
                plain
                icon="el-icon-edit"
                @click="handleReplenishment(scope.row.id)"
              >人工补单</el-button>
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
  </div>

</template>

<script>
import { recharge, manualRecharge } from '@/api/relatedRecharge'

const shippingStatus = { 0: '未处理', 1: '发货成功', 2: '发货失败' }

export default {
  data() {
    return {
      total: 0,
      rechargeData: [],
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      date: [new Date().setDate(1), new Date()],
      playerId: '',
      nick: '',
      openId: '',
      orderNumber: '',
      orderStatus: -1
    }
  },

  created() {
    this.getRecharge()
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

  activated() {
    if (this.$refs.table && this.$refs.table.doLayout) {
      this.$refs.table.doLayout()
    }
  },

  methods: {
    // 获取数据
    async getRecharge() {
      const res = await recharge({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.rechargeData = res.data.data.data
    },

    // 搜索
    async handleSearch() {
      try {
        const query = {
          page: this.currentPage,
          limit: this.pageSize
        }
        if (this.date && this.date.length === 2) {
        // 时间戳毫秒转化为秒
          query.startTime = this.$moment(this.date[0]).unix()
          query.endTime = this.$moment(this.date[1]).unix()
        }
        if (this.openId) {
          query.openId = this.openId
        }
        if (this.nick) {
          query.nick = this.nick
        }
        if (this.playerId) {
          query.playerId = this.playerId
        }
        if (this.orderNumber) {
          query.sdkOrderId = this.orderNumber
        }
        if (this.orderStatus) {
          query.status = this.orderStatus
        }
        const result = await recharge(query)
        this.rechargeData = result.data.data.data
        this.total = result.data.data.count
      } catch (error) {
        console.error(error)
        this.$message.error('发生错误，请稍后重试')
      }
    },

    // 人工补单
    async handleReplenishment(id) {
      const res = await manualRecharge({ rechargeId: id })
      if (res.data.code === 0) {
        this.$message.success('补单成功！')
      } else {
        this.$message.error('补单失败！')
      }
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getRecharge()
    },

    // 发货状态
    getStatus(val) {
      return shippingStatus[val]
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
