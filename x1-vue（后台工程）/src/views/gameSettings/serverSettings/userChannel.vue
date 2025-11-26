<!-- 渠道权限管理 -->
<template>
  <div>
    <el-card>
      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="userCnData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="用户ID" />
          <el-table-column prop="name" label="用户名" />
          <el-table-column prop="phone" label="手机号码" />
          <el-table-column prop="mailAddress" label="邮箱地址" />
          <el-table-column fixed="right" label="操作" width="130">
            <template slot-scope="scope">
              <el-button
                size="small"
                type="primary"
                icon="el-icon-unlock"
                plain
                @click="handleMandate(scope.row)"
              >渠道授权</el-button>
            </template>
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

    <!-- 渠道授权弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="40%">
      <el-table ref="powerTableRef" border :data="powerData" style="width: 100%;" height="400" @selection-change="selectionPower">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="渠道ID" />
        <el-table-column prop="name" label="渠道名称" />
      </el-table>
      <div style="text-align: center">
        <el-button size="medium" type="primary" @click="handlePowerConfirm">确 定</el-button>
        <el-button size="medium" @click="dialogVisible =false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { userChannel, accreditOptions, editUserChannel } from '@/api/serverManage'

export default {
  data() {
    return {
      userCnData: [],
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      userName: '', // 用户名
      dialogVisible: false,
      dialogTitle: '',
      powerData: [],
      userId: '', // 用户ID
      selectPermis: [] // 选中的权限
    }
  },

  created() {
    this.getUserChannel()
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
    async getUserChannel() {
      const res = await userChannel({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.userCnData = res.data.data.data
    },

    // 渠道授权按钮
    async handleMandate(row) {
      this.userId = row.id
      this.dialogTitle = `正在编辑渠道权限：${row.id} - ${row.name}`
      // 获取授权菜单
      const res = await accreditOptions({ userId: row.id })
      this.powerData = res.data.data.data
      // 已授权渠道回显
      this.$nextTick(() => {
        this.powerData.filter(row => row.has).forEach(row => {
          this.$refs.powerTableRef.toggleRowSelection(row, true)
        })
      })
      this.dialogVisible = true
    },

    // 选中的授权
    selectionPower(select) {
      this.selectPermis = select.map(item => item.id)
    },

    // 授权确认按钮
    async handlePowerConfirm() {
      const res = await editUserChannel({ userId: this.userId, channel: this.selectPermis })
      this.powerData = res.data.data.data
      this.dialogVisible = false
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getUserChannel()
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
.el-table{
 margin-top: 0cm;
}
</style>

