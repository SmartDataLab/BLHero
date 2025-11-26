<!-- 平台管理 -->
<template>
  <div>
    <el-card>
      <div>
        <el-button
          icon="el-icon-circle-plus-outline"
          type="primary"
          size="small"
          @click="handleAdd"
        >新增平台信息</el-button>
      </div>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="gamePlatformData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="平台ID" />
          <el-table-column prop="name" label="平台名称" />
          <el-table-column prop="userId" label="操作人ID" />
          <el-table-column prop="userName" label="操作人名字" />
          <el-table-column label="操作" fixed="right" width="130">
            <template slot-scope="scope">
              <el-button
                size="small"
                type="primary"
                plain
                icon="el-icon-edit-outline"
                @click="handleEdit(scope.row)"
              >编辑</el-button>
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
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="35%"
      center
      :modal-append-to-body="false"
      @close="closeDialog"
    >
      <el-form :model="formData" label-width="100px">
        <el-form-item label="平台名称">
          <el-input v-model.trim="formData.name" placeholder="请输入" clearable />
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
import { gamePlatform, savaGamePlatform } from '@/api/serverManage'

export default {
  data() {
    return {
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      gamePlatformData: [],
      dialogTitle: '',
      dialogVisible: false,
      formData: {},
      tableHeight: 0
    }
  },

  created() {
    this.getGamePlatform()
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
    // 解决标签切换，表格移位
    if (this.$refs.table && this.$refs.table.doLayout) {
      this.$refs.table.doLayout()
    }
  },

  methods: {
    // 获取数据
    async getGamePlatform() {
      const res = await gamePlatform({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.gamePlatformData = res.data.data.data
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增数据'
      this.dialogVisible = true
      this.formData.id = 0
    },

    // 编辑
    handleEdit(data) {
      this.dialogTitle = '编辑数据'
      this.formData = Object.assign({}, data)
      this.dialogVisible = true
    },

    // 确定
    async handleConfirm() {
      if (this.formData.id !== 0) {
      // 编辑
        const newData = Object.assign({}, this.formData)
        const res = await savaGamePlatform(newData)
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
      } else {
      // 新增
        const res = await savaGamePlatform(this.formData)
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
      }
      this.getGamePlatform()
      this.closeDialog()
    },

    // 关闭弹窗
    closeDialog() {
      this.dialogVisible = false
      this.formData = {}
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getGamePlatform()
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

::v-deep .el-select{
  width: 100%;
}

</style>
