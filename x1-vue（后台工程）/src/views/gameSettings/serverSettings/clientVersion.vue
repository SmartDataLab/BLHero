<!-- 客户端版本管理 -->
<template>
  <div>
    <el-card>
      <div class="inquire">
        <el-button type="primary" icon="el-icon-circle-plus-outline" size="small" @click="handleAdd">添加客户端版本</el-button>

        <div>
          <span>渠道：</span>
          <el-select v-model="channelId" style="width:200px" size="small" clearable>
            <el-option v-for="item in channelOptions" :key="item.value" :label="item.text" :value="item.value" />
          </el-select>
        </div>

        <el-button icon="el-icon-search" type="primary" size="small" plain @click="handleInquire">查询</el-button>
      </div>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="clientVersionData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="数据ID" min-width="120" />
          <el-table-column label="渠道信息" min-width="140">
            <template slot-scope="scope">
              {{ scope.row.channelName }} [{{ scope.row.channelId }}]
            </template>
          </el-table-column>
          <el-table-column prop="serverType" label="服务器类型" min-width="140">
            <template #default="{row}">
              <el-tag :type="row.serverType === 1?'warning':row.serverType === 2?'danger':'success'">
                {{ row.serverType === 1?'测试':row.serverType === 2?'审核':'正式' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="versionCode" label="版本编号" min-width="140" />
          <el-table-column prop="resourceVersion" label="客户端资源版本号" min-width="150" />
          <el-table-column prop="pcResourceVersion" label="PC客户端资源版本号" min-width="170" />
          <el-table-column prop="remoteUrl" label="远程地址" min-width="300" />
          <el-table-column prop="quickUrl" label="快速通道地址" min-width="300" />
          <el-table-column prop="remark" label="备注" min-width="200" />
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
                @click="handleDelete(scope.row)"
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
      width="40%"
      :modal-append-to-body="false"
      center
      @close="closeDialog"
    >
      <el-form ref="ruleForm" :model="formData" label-position="right" label-width="150px">
        <el-form-item v-show="isShow" label="数据ID">
          <el-input v-model="formData.id" disabled />
        </el-form-item>
        <el-form-item
          v-show="isHide"
          label="渠道"
          prop="channelId"
          :rules="[{ required: true, message: '请选择渠道', trigger: 'change' }]"
        >
          <el-select v-model="formData.channelId" placeholder="请选择渠道" filterable clearable>
            <el-option v-for="item in channelOptions" :key="item.value" :label="item.text" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-row v-show="isShow" :gutter="20">
          <el-col :span="12">
            <el-form-item label="渠道ID">
              <el-input v-model="formData.channelId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="渠道名字" label-width="120px">
              <el-input v-model="formData.channelName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item
          label="版本编号"
          prop="versionCode"
          :rules="[ { required: true, message: '版本编号不能为空'},{ type: 'number', message: '版本编号必须为数字值'}]"
        >
          <el-input v-model.number="formData.versionCode" clearable />
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
        <el-form-item label="客户端资源版本号">
          <el-input v-model="formData.resourceVersion" clearable />
        </el-form-item>
        <el-form-item label="PC客户端资源版本号">
          <el-input v-model="formData.pcResourceVersion" clearable />
        </el-form-item>
        <el-form-item label="远程地址">
          <el-input v-model="formData.remoteUrl" clearable />
        </el-form-item>
        <el-form-item label="快速通道地址">
          <el-input v-model="formData.quickUrl" clearable />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" clearable />
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
import { clientVersion, savaClientVersion, delClientVersion } from '@/api/serverManage'
import { channelOpt } from '@/api/aboutChannel'

export default {
  data() {
    return {
      clientVersionData: [],
      total: 0,
      currentPage: 1,
      pageSize: 200,
      tableHeight: 0,
      channelId: '',
      channelOptions: [],
      formData: {},
      dialogTitle: '',
      dialogVisible: false,
      isShow: false,
      isHide: false
    }
  },

  created() {
    this.getChannel()
    this.getClientVersion()
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
    async getClientVersion() {
      const res = await clientVersion({
        page: this.currentPage,
        limit: this.pageSize,
        channelId: 0
      })
      this.total = res.data.data.count
      this.clientVersionData = res.data.data.data
    },

    // 获取渠道
    async getChannel() {
      const res = await channelOpt()
      this.channelOptions = res.data.data.options
    },

    // 查询
    async handleInquire() {
      const query = { page: this.currentPage, limit: this.pageSize }

      if (!this.channelId) {
        this.$message.error('请选择渠道，渠道为必选项')
      } else {
        query.channelId = this.channelId

        const result = await clientVersion(query)
        this.total = result.data.data.count
        this.clientVersionData = result.data.data.data
      }
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '添加数据'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.ruleForm.clearValidate()
      })
      this.formData.id = 0
      this.isShow = false
      this.isHide = true
    },

    // 编辑
    handleEdit(data) {
      this.dialogTitle = '编辑数据'
      this.formData = Object.assign({}, data)
      this.dialogVisible = true
      this.isShow = true
      this.isHide = false
    },

    // 确定
    async handleConfirm() {
      await this.$refs.ruleForm.validate(async(valid) => {
        if (valid) {
          if (this.formData.id !== 0) {
            // 编辑
            const newData = Object.assign({}, this.formData)
            const res = await savaClientVersion(newData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          } else {
            // 新增
            const res = await savaClientVersion(this.formData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          }
          this.channelId = this.formData.channelId
          this.handleInquire()
          this.closeDialog()
        }
      })
    },

    // 删除
    handleDelete(row) {
      this.$confirm('你确定要删除这条数据吗?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
        lockScroll: false
      }).then(async() => {
        const res = await delClientVersion({ id: row.id })
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
        this.channelId = row.channelId
        this.handleInquire()
      })
    },

    // 关闭弹窗
    closeDialog() {
      this.formData = {}
      this.dialogVisible = false
      this.$refs.ruleForm.clearValidate()
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.handleInquire()
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

.inquire{
   font-weight: bold;
   font-size: 14px;
   color: #6a7488;
   margin-bottom: 20px;
   display: flex;
   flex-wrap: wrap;
   gap: 30px;
}

.el-table{
 margin-top: 0cm;
}
</style>

