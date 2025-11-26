<!-- 白名单管理 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <el-button type="primary" icon="el-icon-circle-plus-outline" size="small" @click="dialogVisible = true">添加白名单</el-button>
        <div>
          <span>账户：</span>
          <el-input v-model="account" clearable placeholder="请输入" style="width:200px;" size="small" />
        </div>
        <el-button icon="el-icon-search" plain type="primary" size="small" @click="handleInquire">查询</el-button>
      </div>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="whitelistData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="唯一ID" />
          <el-table-column prop="channelId" label="渠道ID" />
          <el-table-column prop="channelName" label="渠道名称" />
          <el-table-column prop="openId" label="账户ID" />
          <el-table-column prop="userId" label="操作人ID" />
          <el-table-column prop="userName" label="操作人名字" />
          <el-table-column prop="remark" label="备注" />
          <el-table-column label="操作" fixed="right" width="120">
            <template slot-scope="scope">
              <el-button
                size="small"
                type="danger"
                plain
                icon="el-icon-delete"
                @click="handleDelete(scope.row.id)"
              >删除</el-button>
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

    <!-- 弹窗 -->
    <el-dialog
      title="新增白名单"
      :visible.sync="dialogVisible"
      width="35%"
      :modal-append-to-body="false"
      center
      @close="closeDialog"
    >
      <el-form ref="formData" :model="formData" label-width="80px">
        <el-form-item
          label="账户ID"
          prop="openId"
          :rules="[{ required: true, message: '请输入账号', trigger: 'blur'}]"
        >
          <el-input v-model.trim="formData.openId" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model.trim="formData.remark" placeholder="请输入" clearable />
        </el-form-item>
      </el-form>
      <template slot="footer">
        <el-button size="medium" type="primary" @click="confirmBtn">确 定</el-button>
        <el-button size="medium" @click="closeDialog">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { whiteList, AddwhiteList, deletewhiteList } from '@/api/playerManage'

export default {
  data() {
    return {
      total: 0,
      whitelistData: [],
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      account: '', // 账户
      dialogTitle: '',
      dialogVisible: false,
      formData: {}
    }
  },

  created() {
    this.getWhitelist()
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
    async getWhitelist() {
      const res = await whiteList({
        page: this.currentPage,
        limit: this.pageSize,
        openId: this.account
      })
      this.total = res.data.data.count
      this.whitelistData = res.data.data.data
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getWhitelist()
    },

    // 查询
    async handleInquire() {
      try {
        const query = { page: this.currentPage, limit: this.pageSize }
        if (this.account) {
          query.openId = this.account
        }
        const result = await whiteList(query)
        this.total = result.data.data.count
        this.whitelistData = result.data.data.data
      } catch (error) {
        console.error(error)
        this.$message.error('发生错误，请稍后重试')
      }
    },

    // 确定
    async confirmBtn() {
      await this.$refs.formData.validate(async(valid) => {
        if (valid) {
          await AddwhiteList(this.formData)
          this.getWhitelist()
          this.closeDialog()
        }
      })
    },

    // 删除
    handleDelete(id) {
      this.$confirm('你确定要删除这条数据吗?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
        lockScroll: false
      }).then(async() => {
        const res = await deletewhiteList({ id: id })
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
        this.getWhitelist()
      })
    },

    // 关闭弹窗
    closeDialog() {
      this.$refs.formData.clearValidate()
      this.dialogVisible = false
      this.formData = {}
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
  gap: 30px;
}

::v-deep .el-form .el-input__inner{
  border-radius: 0 4px 4px 0;
}
</style>
