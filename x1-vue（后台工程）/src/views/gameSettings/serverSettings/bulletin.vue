<!-- 游戏公告管理 -->
<template>
  <div>
    <el-card>
      <el-button type="primary" icon="el-icon-circle-plus-outline" size="small" @click="handleAdd">添加公告</el-button>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="bulletinData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="公告ID" width="120" />
          <el-table-column prop="title" label="公告标题" width="250" />
          <el-table-column prop="content" label="公告内容" />
          <el-table-column label="操作" fixed="right" width="200">
            <template slot-scope="scope">
              <el-button size="small" type="primary" plain icon="el-icon-edit-outline" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)">删除</el-button>
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
    <div class="main-dialog">
      <el-dialog
        :title="dialogTitle"
        :visible.sync="dialogVisible"
        width="40%"
        :modal-append-to-body="false"
        center
        @close="closeDialog"
      >
        <div class="dialog-content">
          <el-form :model="formData" label-width="100px">
            <el-form-item v-show="isShow" label="公告ID">
              <el-input v-model.trim="formData.id" />
            </el-form-item>
            <el-form-item label="公告标题">
              <el-input v-model="formData.title" placeholder="请输入" clearable />
            </el-form-item>
            <el-form-item label="公告内容">
              <el-input v-model="formData.content" type="textarea" :rows="19" placeholder="请输入..." clearable />
            </el-form-item>
          </el-form>
        </div>
        <span slot="footer">
          <el-button size="medium" type="primary" @click="handleConfirm">确 定</el-button>
          <el-button size="medium" @click="closeDialog">取 消</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { gameBulletin, saveGameBulletin, delGameBulletin } from '@/api/serverManage'

export default {
  data() {
    return {
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      bulletinData: [],
      dialogTitle: '',
      dialogVisible: false,
      formData: {},
      tableHeight: 0, // 表格高度
      isShow: false
    }
  },

  created() {
    this.getGameBulletin()
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
    async getGameBulletin() {
      const res = await gameBulletin({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.bulletinData = res.data.data.data
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增公告'
      this.formData.id = 0
      this.dialogVisible = true
      this.isShow = false
    },

    // 编辑
    handleEdit(data) {
      this.dialogTitle = '编辑公告'
      this.formData = Object.assign({}, data)
      this.dialogVisible = true
      this.isShow = true
    },

    // 删除
    handleDelete(id) {
      this.$confirm('你确定要删除这条数据吗?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
        lockScroll: false
      }).then(async() => {
        const res = await delGameBulletin({ id: id })
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
        this.getGameBulletin()
      })
    },

    // 确定
    async handleConfirm() {
      if (this.formData.id !== 0) {
      // 编辑
        const newData = Object.assign({}, this.formData)
        const res = await saveGameBulletin(newData)
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
      } else {
      // 新增
        const res = await saveGameBulletin(this.formData)
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
      }
      this.getGameBulletin()
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
      this.getGameBulletin()
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
::v-deep .el-select{
  width: 100%;
}

.dialog-content {
 max-height: 65vh;
 overflow-y: auto;
 overflow-x: hidden;
}
.dialog-content::-webkit-scrollbar {
  width: 6px;
  background-color: transparent;
}
.dialog-content::-webkit-scrollbar-thumb {
  background-color: #dddee0;
  border-radius: 4px;
}
.dialog-content::-webkit-scrollbar-thumb:hover {
  background-color: #c7c9cc;
}

::v-deep .dialog-content .el-form{
  padding:30px 20px 0px 20px
}

::v-deep .dialog-content .el-input__inner{
  border-radius: 0 4px 4px 0;
}

::v-deep .main-dialog .el-dialog__body {
 padding: 4px
}

::v-deep .el-textarea__inner{
  border-top-left-radius: 0;
}

</style>
