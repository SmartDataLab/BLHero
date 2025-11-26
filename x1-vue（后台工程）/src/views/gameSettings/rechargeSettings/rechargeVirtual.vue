<!-- 虚拟充值（内部充值） -->
<template>
  <div>
    <el-card>

      <div class="topInquire">
        <el-button
          type="primary"
          icon="el-icon-circle-plus-outline"
          size="small"
          @click="dialogVisible = true"
        >发放虚拟充值</el-button>

        <div>
          <span>账户ID: </span>
          <el-input v-model.number="openId" size="small" style="width:200px" placeholder="请输入" clearable />
        </div>

        <div>
          <span>玩家ID：</span>
          <el-input v-model.trim="playerId" style="width:200px" size="small" clearable placeholder="请输入" />
        </div>

        <div>
          <span>玩家名称：</span>
          <el-input v-model.trim="playerName" style="width:200px" size="small" clearable placeholder="请输入" />
        </div>

        <el-button size="small" type="primary" plain icon="el-icon-search" @click="handleSearch">搜索</el-button>
      </div>

      <el-table
        ref="table"
        border
        :data="rechargeData"
        :header-cell-style="{background:'#f5f7fa'}"
        :height="tableHeight"
      >
        <el-table-column prop="id" label="数据ID" min-width="120" />
        <el-table-column prop="channelId" label="渠道ID" min-width="120" />
        <el-table-column prop="serverUid" label="数据唯一ID" min-width="120" />
        <el-table-column prop="serverId" label="服务器ID" min-width="120" />
        <el-table-column prop="serverName" label="服务器名字" min-width="120" />
        <el-table-column prop="playerId" label="玩家ID" min-width="160" />
        <el-table-column prop="nick" label="玩家名字" min-width="160" />
        <el-table-column prop="openId" label="账号ID" min-width="130" />
        <el-table-column prop="productId" label="充值商品ID" min-width="130" />
        <el-table-column prop="productName" label="充值商品名字" min-width="160" />
        <el-table-column prop="status" label="充值状态" min-width="130">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 0 ? 'info' : scope.row.status === 1 ? 'success': 'danger'">
              {{ getStatus(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="money" label="金额 (单位：元)" min-width="130">
          <template #default="{row}">
            {{ (row.money)/100 }}
          </template>
        </el-table-column>
        <el-table-column prop="userId" label="操作员ID" min-width="150" />
        <el-table-column prop="userName" label="操作员名称" min-width="150" />
        <el-table-column prop="remark" label="备注" min-width="300" />
      </el-table>

      <el-pagination
        background
        layout="total, prev, pager, next, jumper"
        :total="total"
        :current-page="currentPage"
        :page-size="pageSize"
        @current-change="changePage"
      />

    </el-card>

    <el-dialog
      title="发放虚拟充值"
      :visible.sync="dialogVisible"
      width="35%"
      :modal-append-to-body="false"
      center
      @close="closeDialog"
    >
      <el-form label-width="100px">
        <el-form-item label="玩家ID">
          <el-input v-model="formData.playerId" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="充值商品ID">
          <el-input v-model="formData.productId" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" placeholder="请输入" clearable />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="medium" type="primary" @click="handleConfirm">确 定</el-button>
        <el-button size="medium" @click="closeDialog">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { rechargeVirtual, AddRechargeVirtual } from '@/api/relatedRecharge'
const statusEnum = { 0: '未处理', 1: '成功', 2: '失败' }

export default {
  data() {
    return {
      total: 0,
      rechargeData: [],
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      dialogVisible: false,
      openId: '',
      playerId: '',
      playerName: '',
      formData: {}
    }
  },

  created() {
    this.getRechargeVirtual()
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
    async getRechargeVirtual() {
      const res = await rechargeVirtual({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.rechargeData = res.data.data.data
      this.total = res.data.data.count
    },

    // 确定
    async handleConfirm() {
      const res = await AddRechargeVirtual(this.formData)
      if (res.data.code === 0) {
        this.$message.success(res.data.message)
      }
      this.getRechargeVirtual()
      this.closeDialog()
    },

    // 搜索
    async handleSearch() {
      try {
        const query = { page: this.currentPage, limit: this.pageSize }

        if (this.openId) {
          query.openId = this.openId
        }

        if (this.playerId) {
          query.playerId = this.playerId
        }

        if (this.playerName) {
          query.nick = this.playerName
        }
        const res = await rechargeVirtual(query)
        this.rechargeData = res.data.data.data
        this.total = res.data.data.count
      } catch (error) {
        console.log(error)
      }
    },

    // 关闭弹窗
    closeDialog() {
      this.dialogVisible = false
      this.formData = {}
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getRechargeVirtual()
    },

    // 获取充值状态
    getStatus(value) {
      return statusEnum[value]
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

::v-deep .el-form .el-input__inner{
  border-radius: 0 4px 4px 0;
}

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

