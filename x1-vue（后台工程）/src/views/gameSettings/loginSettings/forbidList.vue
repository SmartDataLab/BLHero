<!-- 封禁账号 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <div>
          <el-button size="small" type="primary" icon="el-icon-circle-plus-outline" @click="dialogVisible=true">新增封禁</el-button>
        </div>
        <div>
          <span>账户ID: </span>
          <el-input v-model.number="openId" size="small" style="width:200px" placeholder="请输入" clearable />
        </div>
        <el-button size="small" type="primary" icon="el-icon-search" plain @click="handleInquire">查询</el-button>
      </div>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="bannedAccData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="唯一ID" min-width="150" />
          <el-table-column prop="channelId" label="渠道ID" min-width="150" />
          <el-table-column prop="openId" label="账户ID" min-width="170" />
          <el-table-column prop="userId" label="操作人ID" min-width="150" />
          <el-table-column prop="userName" label="操作人姓名" min-width="150" />
          <el-table-column fixed="right" label="操作" width="130">
            <template slot-scope="scope">
              <el-button
                icon="el-icon-delete"
                size="small"
                type="danger"
                plain
                @click="handleDelete(scope.row.id)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>

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

    <!-- 封禁-弹窗 -->
    <el-dialog
      title="新增封禁"
      :visible.sync="dialogVisible"
      width="32%"
      :modal-append-to-body="false"
      center
      @close="closeDialog"
    >
      <el-form label-width="100px">
        <el-form-item label="账户ID">
          <el-input v-model="accountId" placeholder="请输入" clearable />
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
import { forbidList, addForbidList, deleteForbidListr } from '@/api/playerManage'

export default {
  data() {
    return {
      bannedAccData: [],
      total: 0,
      currentPage: 1,
      pageSize: 200,
      tableHeight: 0,
      dialogVisible: false,
      accountId: '',
      openId: ''
    }
  },

  created() {
    this.getForbidList()
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
    async getForbidList() {
      const res = await forbidList({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.bannedAccData = res.data.data.data
    },

    // 确认
    async handleConfirm() {
      const res = await addForbidList({ openId: this.accountId })
      if (res.data.code === 0) {
        this.$message.success(res.data.message)
      }
      this.closeDialog()
      this.getForbidList()
    },

    // 删除
    handleDelete(id) {
      this.$confirm('你确定要删除该玩家吗?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
        lockScroll: false
      }).then(async() => {
        const res = await deleteForbidListr({ id: id })
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
        this.getForbidList()
      })
    },

    // 查询
    async handleInquire() {
      try {
        const query = {}
        if (this.openId) {
          query.openId = this.openId
        }
        const result = await forbidList(query)
        this.total = result.data.data.count
        this.bannedAccData = result.data.data.data
      } catch (error) {
        console.error(error)
      }
    },

    // 关闭弹窗
    closeDialog() {
      this.dialogVisible = false
      this.accountId = ''
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getForbidList()
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

 ::v-deep .el-form .el-input__inner{
   border-radius: 0 4px 4px 0;
 }

 ::v-deep .el-select{
   width: 100%;
 }
</style>
