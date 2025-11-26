<!-- 渠道管理 -->
<template>
  <div>
    <el-card>
      <el-button type="primary" icon="el-icon-circle-plus-outline" size="small" @click="handleAdd">添加渠道</el-button>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="channeData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="渠道ID" />
          <el-table-column prop="name" label="渠道名字" />
          <el-table-column prop="bulletinId" label="公告ID" />
          <el-table-column prop="platformInfo" label="平台信息">
            <template slot-scope="scope">
              {{ scope.row.platformName }} [{{ scope.row.platformId }}]
            </template>
          </el-table-column>
          <el-table-column prop="programVersion" label="程序版本" />
          <el-table-column prop="resourceVersion" label="资源版本" />
          <el-table-column prop="userId" label="操作人ID" />
          <el-table-column prop="userName" label="操作人名字" />
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
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="35%"
      :modal-append-to-body="false"
      center
      @close="closeDialog"
    >
      <div class="dialog-content">
        <el-form ref="ruleForm" :model="formData" label-width="100px">
          <el-form-item v-show="isShow" label="渠道ID">
            <el-input v-model.trim="formData.id" disabled />
          </el-form-item>
          <el-form-item
            label="渠道名字"
            prop="name"
            :rules="[{ required: true, message: '请输入渠道名字', trigger: 'blur' }]"
          >
            <el-input v-model.trim="formData.name" placeholder="请输入" clearable />
          </el-form-item>
          <el-form-item label="公告标题">
            <el-select v-model="formData.bulletinId" clearable filterable>
              <el-option v-for="item in bulletinOpt" :key="item.value" :label="item.text" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="isHide"
            label="平台名称"
            prop="platformId"
            :rules="[{ required: true, message: '请选择平台', trigger: 'change' }]"
          >
            <el-select v-model="formData.platformId" clearable filterable :disabled="isDisabled">
              <el-option v-for="item in platformOpt" :key="item.value" :label="item.text" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item v-show="isShow" label="平台信息">
            <el-input v-model="formData.platformInfo" placeholder="请输入" clearable disabled />
          </el-form-item>
          <el-form-item label="程序版本">
            <el-input v-model="formData.programVersion" placeholder="请输入" clearable />
          </el-form-item>
          <el-form-item label="资源版本">
            <el-input v-model="formData.resourceVersion" placeholder="请输入" clearable />
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer">
        <el-button size="medium" type="primary" @click="handleConfirm">确 定</el-button>
        <el-button size="medium" @click="closeDialog">取 消</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { gameChannel, saveGameChannel, delGameChannel,
  bulletinOptions, platformOptions } from '@/api/serverManage'

export default {
  data() {
    return {
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      channeData: [],
      dialogTitle: '',
      dialogVisible: false,
      formData: {},
      tableHeight: 0, // 表格高度
      isShow: false,
      bulletinOpt: [], // 公告选项
      platformOpt: [], // 平台选项
      isDisabled: false,
      isHide: false
    }
  },

  created() {
    this.getGameChannel()
    this.getBulletin()
    this.getPlatformList()
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
    async getGameChannel() {
      const res = await gameChannel({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.channeData = res.data.data.data
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增渠道'
      this.formData.id = 0
      this.isShow = false
      this.isHide = true
      this.dialogVisible = true
      this.isDisabled = false
    },

    // 编辑
    handleEdit(data) {
      this.dialogTitle = '编辑渠道'
      this.formData = Object.assign({}, data)
      this.formData.platformInfo = `${data.platformName}[${data.platformId}]`
      this.isShow = true
      this.isHide = false
      this.dialogVisible = true
      this.isDisabled = true
    },

    // 删除
    handleDelete(id) {
      this.$confirm('你确定要删除这条数据吗?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
        lockScroll: false
      }).then(async() => {
        const res = await delGameChannel({ channelId: id })
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
        this.getGameChannel()
      })
    },

    // 确定
    async handleConfirm() {
      await this.$refs.ruleForm.validate(async(valid) => {
        if (valid) {
          if (this.formData.id !== 0) {
          // 编辑
            const newData = Object.assign({}, this.formData)
            const res = await saveGameChannel(newData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          } else {
          // 新增
            const res = await saveGameChannel(this.formData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          }
          this.getGameChannel()
          this.closeDialog()
        }
      })
    },

    // 关闭弹窗
    closeDialog() {
      this.dialogVisible = false
      this.formData = {}
      this.$refs.ruleForm.clearValidate()
    },

    // 获取公告数据
    async getBulletin() {
      const res = await bulletinOptions()
      this.bulletinOpt = res.data.data.options
    },

    // 获取平台列表
    async getPlatformList() {
      const res = await platformOptions()
      this.platformOpt = res.data.data.options
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getGameChannel()
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

::v-deep .el-input__inner{
  border-radius: 0 4px 4px 0;
}

</style>
