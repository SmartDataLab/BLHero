<!-- 热更配置 -->
<template>
  <div>
    <el-card>
      <el-button icon="el-icon-circle-plus-outline" type="primary" size="small" @click="dialogVisible = true">添加热更配置</el-button>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="hotConfigData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="唯一ID" width="130" />
          <el-table-column label="服务器发送状态" width="130">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="handleDetails(scope.row)">【查看详情】</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="fixTime" label="热更时间批号" width="200" />
          <el-table-column prop="fileNames" label="配置文件相对路径" min-width="300" show-overflow-tooltip>
            <template slot-scope="scope">
              <span v-for="(item,index) in scope.row.fileNames" :key="index">
                {{ item }}{{ index !== scope.row.fileNames.length - 1 ? '，' : '' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="userId" label="操作员ID" width="130" />
          <el-table-column prop="userName" label="操作员名称" width="130" />
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
      title="添加数据"
      :visible.sync="dialogVisible"
      width="40%"
      center
      @close="closeDialog"
    >
      <el-form ref="ruleForm" :model="ruleForm" label-width="100px">
        <el-form-item
          label="服务器选择："
          prop="serverText"
          :rules="[{ required: true, message: '请选择服务器', trigger: 'input' }]"
        >
          <div class="add">
            <el-input v-model="ruleForm.serverText" disabled />
            <el-button icon="el-icon-plus" type="primary" plain @click="serverDialog=true" />
          </div>
        </el-form-item>
        <el-form-item label="选择上传文件：">
          <el-upload
            multiple
            action="#"
            accept=".csv"
            :file-list="fileList"
            :auto-upload="false"
            :on-change="handleChange"
            :on-remove="handleRemove"
          >
            <el-button size="medium" type="primary" icon="el-icon-upload" plain>选取文件</el-button>
            <span slot="tip" class="el-upload__tip" style="color:#91949a">（当前仅支持上传csv文件，且单个文件不超过2MB）</span>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="medium" type="primary" @click="handleConfirm">确 定</el-button>
        <el-button size="medium" @click="closeDialog">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 服务器弹窗-多选 -->
    <ServerDialog
      :visible.sync="serverDialog"
      :server-options="serverOptions"
      @checkedId="handleCheckedId"
    />

    <!-- 详情-弹窗 -->
    <el-dialog title="热更配置详情" :visible.sync="detailsDialog" width="55%">
      <el-tabs type="card" style="height: 470px;">
        <el-tab-pane label="详情信息">
          <el-table style="width: 100%;" :data="configDetail" height="400">
            <el-table-column prop="id" label="ID" min-width="100" />
            <el-table-column prop="fixId" label="热更ID" min-width="100" />
            <el-table-column prop="serverUid" label="数据唯一ID" min-width="100" />
            <el-table-column prop="serverId" label="服务器ID" min-width="100" />
            <el-table-column prop="name" label="服务器名字" min-width="100" />
            <el-table-column prop="status" label="处理状态" min-width="100">
              <template slot-scope="scope">
                <el-tag
                  :type="scope.row.status === 0 ? 'info'
                    : scope.row.status === 1 ? 'success' : 'danger'"
                  size="medium"
                >
                  {{ scope.row.status === 0 ? '未处理' : scope.row.status === 1 ? '成功' : '失败' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="message" label="处理信息" min-width="200" show-overflow-tooltip />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="配置文件相对路径">
          <div class="file_path">
            <ul>
              <li v-for="(item,index) in filePath" :key="index">
                {{ item }}
              </li>
            </ul>
          </div>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer">
        <el-button type="warning" size="small" icon="el-icon-refresh-right" @click="handleResend">失败重发</el-button>
        <el-button type="success" size="small" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
        <el-button type="danger" size="small" icon="el-icon-close" @click="detailsDialog = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
import ServerDialog from '@/components/multipleDialog/index.vue'
import { fixDesign, fixDesignResult, resendFixDesign } from '@/api/specialOperation'
import { baseApiUrl } from '@/utils/request'
import { multiServer } from '@/api/aboutServer'

export default {
  components: {
    ServerDialog
  },
  data() {
    return {
      baseApiUrl,
      hotConfigData: [],
      configDetail: [],
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      dialogVisible: false,
      detailsDialog: false, // 详情弹窗
      serverDialog: false, // 服务器弹窗
      serverOptions: [],
      serverUids: [], // 选中的服务器id集合
      ruleForm: {
        serverText: '' // 服务器名称
      },
      files: [],
      fileList: [],
      fixId: '', // 热更Id
      filePath: []// 文件路径
    }
  },

  created() {
    this.getFixDesign()
    this.getServer()
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
    async getFixDesign() {
      const res = await fixDesign({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.hotConfigData = res.data.data.data
    },

    // 确定
    async handleConfirm() {
      await this.$refs.ruleForm.validate(async(valid) => {
        if (valid) {
          const formData = new FormData()
          formData.append('serverUids', this.serverUids)
          this.files.forEach(file => {
            formData.append('files', file.raw)
          })

          await axios.post(`${baseApiUrl}/fixDesign/upload.auth`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          this.getFixDesign()
          this.closeDialog()
        }
      })
    },

    // 添加文件
    handleChange(file) {
      // 判断文件类型以及文件大小
      const validTypes = 'csv'
      const validSize = 2 * 1024 * 1024

      const lastDotIndex = file.name.lastIndexOf('.')
      const fileType = file.name.substring(lastDotIndex + 1).toLowerCase()
      const fileSize = file.size

      if (validTypes !== fileType) {
        this.$message.closeAll()
        this.$message({
          type: 'error',
          message: '当前限制只能上传csv文件',
          duration: 5000
        })
      } else if (fileSize > validSize) {
        this.$message.closeAll()
        this.$message({
          type: 'error',
          message: '请上传小于2MB的文件',
          duration: 5000
        })
      } else {
        this.fileList.push(file)
        this.files = this.fileList
      }
    },

    // 移除文件
    handleRemove(file) {
      const index = this.fileList.indexOf(file)
      if (index !== -1) {
        this.fileList.splice(index, 1)
      }
    },

    // 获取服务器
    async getServer() {
      const res = await multiServer()
      this.serverOptions = res.data.data.options.sort((a, b) => a.value - b.value)
    },

    // 选中的服务器ID
    handleCheckedId(val) {
      this.serverUids = val

      if (this.serverUids.length > 0) {
        this.ruleForm.serverText = this.serverOptions
          .filter(item => this.serverUids.includes(item.value))
          .map(item => item.text)
          .join('，')
      } else {
        this.ruleForm.serverText = ''
      }
      this.serverDialog = false
    },

    // 查看详情
    async handleDetails(row) {
      this.filePath = row.fileNames
      this.fixId = row.id
      const res = await fixDesignResult({ id: row.id })
      this.configDetail = res.data.data.data
      this.detailsDialog = true
    },

    // 关闭弹窗
    closeDialog() {
      this.dialogVisible = false
      this.ruleForm.serverText = ''
      this.fileList = []
      this.$refs.ruleForm.clearValidate()
    },

    // 刷新
    async handleRefresh() {
      const res = await fixDesignResult({ id: this.fixId })
      this.configDetail = res.data.data.data
      if (res.data.code === 0) {
        this.$message.success('刷新成功！')
      } else {
        this.$message.error('刷新失败！')
      }
    },

    // 重发
    async handleResend() {
      const res = await resendFixDesign({ id: this.fixId })
      if (res.data.code === 0) {
        this.$message.success('重发成功！')
      } else {
        this.$message.error('重发失败！')
      }
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getFixDesign()
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

::v-deep .add{
  display: flex;
 .el-button--primary.is-plain{
  border-radius: 0px 4px 4px 0px;
  height: 40px;
  width: 40%;
 }
 .el-input__inner{
  border-radius: 4px 0px 0px 4px;
  border-right: 0;
 }
}

::v-deep  .el-form-item__label{
  background-color: #fff;
  border: none;
  height: none;
}

.file_path{
  height: 415px;
  overflow-y: auto;
  border-top: 1px solid #ebeef5;
  border-bottom: 1px solid #ebeef5;
}

::v-deep .el-dialog__body{
  padding: 30px 30px 10px 30px;
}
</style>
