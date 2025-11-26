<!-- 系统菜单 -->
<template>
  <div>
    <el-card>
      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="SystemData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="唯一ID" />
          <el-table-column prop="title" label="系统名字" />
          <el-table-column prop="sort" label="排序位置" />
          <el-table-column prop="icon" label="图标" />
          <el-table-column prop="routeName" label="路由名字" />
          <el-table-column prop="routePath" label="路由路径" />
          <el-table-column prop="component" label="组件" />
          <el-table-column label="操作" fixed="right" width="120">
            <template slot-scope="scope">
              <el-button
                type="primary"
                plain
                icon="el-icon-edit-outline"
                size="small"
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
          @current-change="handleChangePage"
        />
      </div>
    </el-card>

    <!-- 弹窗 -->
    <el-dialog
      title="修改数据"
      :visible.sync="dialogVisible"
      width="30%"
      :modal-append-to-body="false"
      center
    >
      <el-form :model="formData" label-width="120px">
        <el-form-item label="唯一ID">
          <el-input v-model="formData.id" :disabled="true" />
        </el-form-item>
        <el-form-item label="系统名字">
          <el-input v-model="formData.title" :disabled="true" />
        </el-form-item>
        <el-form-item label="排序位置">
          <el-input v-model="formData.sort" />
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="formData.icon" />
        </el-form-item>
        <el-form-item label="路由名字">
          <el-input v-model="formData.routeName" :disabled="true" />
        </el-form-item>
        <el-form-item label="路由路径">
          <el-input v-model="formData.routePath" :disabled="true" />
        </el-form-item>
        <el-form-item label="组件">
          <el-input v-model="formData.component" :disabled="true" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="medium" type="primary" @click="handleConfirm">确 定</el-button>
        <el-button size="medium" @click="dialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { systemMenu, updateSystemMenu } from '@/api/menuSetting'

export default {
  data() {
    return {
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 20, // 每页显示条数
      SystemData: [],
      formData: {},
      dialogVisible: false,
      tableHeight: 0 // 表格高度
    }
  },

  created() {
    this.getSystemMenu()
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
    async getSystemMenu() {
      const res = await systemMenu({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.SystemData = res.data.data.data
      this.total = res.data.data.count
    },

    // 切换页面
    handleChangePage(page) {
      this.currentPage = page
      this.getSystemMenu()
    },

    // 编辑
    handleEdit(data) {
      this.formData = Object.assign({}, data)
      this.dialogVisible = true
    },

    // 确认
    async handleConfirm() {
      const newData = Object.assign({}, this.formData)
      const res = await updateSystemMenu(newData)
      this.getSystemMenu()
      if (res.data.code === 0) {
        this.$message.success(res.data.message)
      }
      this.dialogVisible = false
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

.el-table{
 margin-top: 0cm;
}
</style>

