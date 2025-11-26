<!-- 渠道大区管理 -->
<template>
  <div>
    <el-card>
      <!-- 头部 -->
      <div class="header">
        <el-button type="primary" icon="el-icon-circle-plus-outline" size="small" @click="handleAdd">添加渠道大区</el-button>

        <div class="ditch">
          <span>渠道：</span>
          <el-select v-model="headerChannel" style="width:200px" size="small" clearable>
            <el-option v-for="item in channelOptions" :key="item.value" :label="item.text" :value="item.value" />
          </el-select>
        </div>

        <el-button size="small" icon="el-icon-search" plain type="primary" @click="handleSearch">搜索</el-button>
      </div>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="gameRegionData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column v-if="false" prop="id" label="数据ID" />
          <el-table-column label="渠道信息">
            <template slot-scope="scope">
              {{ scope.row.channelName }} [{{ scope.row.channelId }}]
            </template>
          </el-table-column>
          <el-table-column prop="regionId" label="大区ID" />
          <el-table-column prop="name" label="大区名称" />
          <el-table-column prop="serverType" label="服务器类型">
            <template #default="{row}">
              <el-tag :type="row.serverType === 1?'warning':row.serverType === 2?'danger':'success'">
                {{ row.serverType === 1?'测试':row.serverType === 2?'审核':'正式' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="200">
            <template slot-scope="scope">
              <el-button size="small" type="primary" plain icon="el-icon-edit-outline" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
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
      <el-form ref="ruleForm" :model="formData" label-width="120px">
        <el-form-item label="渠道选择">
          <el-select v-model="formData.channelId" placeholder="请选择渠道" filterable clearable :disabled="isdisabled" @change="channelData">
            <el-option v-for="item in channelOptions" :key="item.value" :label="item.text" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="大区ID">
          <el-input v-model.trim="formData.regionId" :disabled="isdisabled" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="大区名称">
          <el-input v-model.trim="formData.name" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item
          label="服务器类型"
          prop="serverType"
          :rules="[{ required: true, message: '请选择服务器类型', trigger: 'change' }]"
        >
          <el-select v-model="formData.serverType" clearable>
            <el-option :value="1" label="测试" />
            <el-option :value="2" label="审核" />
            <el-option :value="3" label="正式" />
          </el-select>
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
import { gameRegion, saveGameRegion, delGameRegion } from '@/api/serverManage'
import { channelOpt } from '@/api/aboutChannel'

export default {
  data() {
    return {
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      gameRegionData: [],
      dialogTitle: '',
      dialogVisible: false,
      formData: {},
      tableHeight: 0, // 表格高度
      channelOptions: [],
      headerChannel: '', // 头部渠道
      isdisabled: false
    }
  },

  created() {
    this.getGameRegion()
    this.getChannel()
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
    async getGameRegion() {
      const res = await gameRegion({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.gameRegionData = res.data.data.data
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增渠道大区'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.ruleForm.clearValidate()
      })
      this.formData.id = 0
      this.isdisabled = false
    },

    // 编辑
    handleEdit(data) {
      this.dialogTitle = '编辑数据'
      this.formData = Object.assign({}, data)
      this.dialogVisible = true
      this.isdisabled = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('你确定要删除这条数据吗?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
        lockScroll: false
      }).then(async() => {
        const res = await delGameRegion({
          channelId: row.channelId,
          regionId: row.regionId
        })
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
        this.getGameRegion()
      })
    },

    // 获取渠道
    async getChannel() {
      const res = await channelOpt()
      this.channelOptions = res.data.data.options
    },

    // 切换渠道
    channelData(val) {
      this.formData.channelId = val
    },

    // 确定按钮
    async handleConfirm() {
      await this.$refs.ruleForm.validate(async(valid) => {
        if (valid) {
          if (this.formData.id !== 0) {
            // 编辑
            const newData = Object.assign({}, this.formData)
            const res = await saveGameRegion(newData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          } else {
            // 新增
            const res = await saveGameRegion(this.formData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          }
          this.getGameRegion()
          this.closeDialog()
        }
      })
    },

    // 搜索按钮
    async handleSearch() {
      const query = { page: this.currentPage, limit: this.pageSize }
      if (this.headerChannel) {
        query.channelId = this.headerChannel
      }
      const result = await gameRegion(query)
      this.total = result.data.data.count
      this.gameRegionData = result.data.data.data
    },

    // 关闭弹窗
    closeDialog() {
      this.dialogVisible = false
      this.formData = {}
      this.$refs.ruleForm.clearValidate()
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getGameRegion()
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

.header{
  display: flex;
  font-size: 15px;
  font-weight: bold;
  color: #6a7488;
.ditch{
  margin: 0 20px;
}
}
</style>
