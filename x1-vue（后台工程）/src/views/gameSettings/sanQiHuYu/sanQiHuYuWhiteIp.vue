<!-- 37互娱IP白名单 -->
<template>
  <div>
    <el-card>
      <el-button
        type="primary"
        icon="el-icon-circle-plus-outline"
        size="small"
        @click="handleAdd"
      >添加37互娱IP白名单</el-button>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="sanQiWhiteData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="唯一ID" />
          <el-table-column prop="whiteIp" label="IP白名单" />
          <el-table-column prop="room" label="机房" />
          <el-table-column fixed="right" label="操作" width="180">
            <template slot-scope="scope">
              <el-button
                type="primary"
                plain
                size="small"
                icon="el-icon-edit-outline"
                @click="handleEdit(scope.row)"
              >编辑</el-button>
              <el-button
                type="danger"
                plain
                size="small"
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
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="35%"
      :modal-append-to-body="false"
      center
      @close="closeDialog"
    >
      <el-form :model="formData" label-position="right" label-width="100px">
        <el-form-item label="唯一ID">
          <el-input v-model="formData.id" disabled />
        </el-form-item>
        <el-form-item label="IP白名单">
          <el-input v-model="formData.whiteIp" />
        </el-form-item>
        <el-form-item label="机房">
          <el-input v-model="formData.room" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button type="primary" @click="handleConfirm">确 认</el-button>
        <el-button @click="closeDialog">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
// 37互娱IP白名单
import { sanQiHuYuWhiteIp, saveSanQiHuYuWhiteIp, deleteSanQiWhiteIp } from '@/api/aboutSanQi'

export default {
  data() {
    return {
      sanQiWhiteData: [],
      total: 0,
      currentPage: 1,
      pageSize: 200,
      tableHeight: 0,
      dialogTitle: '',
      dialogVisible: false,
      formData: {}
    }
  },

  created() {
    this.getSanQiWhiteList()
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
    async getSanQiWhiteList() {
      const res = await sanQiHuYuWhiteIp()
      this.total = res.data.data.count
      this.sanQiWhiteData = res.data.data.data
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

    // 删除
    handleDelete(id) {
      this.$confirm('你确定要删除这条数据吗?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
        lockScroll: false
      }).then(async() => {
        const res = await deleteSanQiWhiteIp({ id: id })
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
        this.getSanQiWhiteList()
      })
    },

    // 确认
    async handleConfirm() {
      if (this.formData.id !== 0) {
        // 编辑
        const newData = Object.assign({}, this.formData)
        const res = await saveSanQiHuYuWhiteIp(newData)
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
      } else {
        // 新增
        const res = await saveSanQiHuYuWhiteIp(this.formData)
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
      }
      this.getSanQiWhiteList()
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
      this.getSanQiWhiteList()
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
</style>

