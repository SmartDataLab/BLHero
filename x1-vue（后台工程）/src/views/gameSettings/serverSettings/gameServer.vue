<!-- 服务器管理 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <el-button type="primary" icon="el-icon-circle-plus-outline" size="small" @click="handleAdd">添加服务器</el-button>
        <div>
          <span>平台名称：</span>
          <el-select v-model="platformId" clearable filterable size="small" style="width:200px;">
            <el-option
              v-for="(item,index) in platformOpt"
              :key="index"
              :label="item.text"
              :value="item.value"
            />
          </el-select>
        </div>
        <div>
          <span>服务器：</span>
          <el-input v-model.number="serverUid" style="width:200px" size="small" placeholder="请输入服务器ID" />
        </div>
        <el-button icon="el-icon-search" type="primary" plain size="small" @click="handleInquire">查询</el-button>
      </div>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="serverData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column v-if="false" prop="id" label="数据唯一ID" min-width="120" />
          <el-table-column prop="serverId" label="服务器ID" min-width="120" fixed />
          <el-table-column prop="name" label="服务器名字" min-width="120" />
          <el-table-column label="平台信息" min-width="150">
            <template slot-scope="scope">
              {{ scope.row.platformName }} [{{ scope.row.platformId }}]
            </template>
          </el-table-column>
          <el-table-column prop="serverType" label="服务器类型" min-width="120">
            <template #default="{row}">
              <el-tag :type="row.serverType === 1?'warning':row.serverType === 2?'danger':'success'">
                {{ row.serverType === 1?'测试':row.serverType === 2?'审核':'正式' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="socketType" label="socket连接方式" min-width="130" />
          <el-table-column prop="externalIp" label="外网IP" min-width="120" />
          <el-table-column prop="internalIp" label="内网IP" min-width="120" />
          <el-table-column prop="tcpPort" label="tcp/ws端口" min-width="120" />
          <el-table-column prop="httpPort" label="http端口" min-width="120" />
          <el-table-column prop="dbGameName" label="游戏数据库名" min-width="120" />
          <el-table-column prop="dbLogName" label="日志数据库名" min-width="120" />
          <el-table-column prop="openTime" label="预设开服时间" min-width="180">
            <template slot-scope="scope">{{ scope.row.openTime|formatTime }}</template>
          </el-table-column>
          <el-table-column prop="realOpenTime" label="实际开服时间" min-width="180">
            <template slot-scope="scope">{{ scope.row.realOpenTime|formatTime }}</template>
          </el-table-column>
          <el-table-column prop="opening" label="是否已经开服" min-width="180">
            <template slot-scope="scope">
              <el-tag :type="scope.row.opening? 'success' : 'info'">
                {{ scope.row.opening?'是':'否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="sendOpenStatus" label="开服时间是否发送成功" min-width="180">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.sendOpenStatus === 0 ? 'info'
                  : scope.row.sendOpenStatus === 1 ? 'success' : 'danger'"
              >
                {{ getOpen(scope.row.sendOpenStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" min-width="150">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.status === 0 ? 'danger' : scope.row.status === 1 ? 'success'
                  : scope.row.status === 2 ? 'warning' : null"
              >
                {{ getStatus(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="recommend" label="是否为推荐服务器" min-width="150">
            <template slot-scope="scope">
              <el-tag :type="scope.row.recommend? 'success' : 'danger'">
                {{ scope.row.recommend?'是':'否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="heartTime" label="最后心跳时间" min-width="180">
            <template slot-scope="scope">{{ scope.row.heartTime|formatTime }}</template>
          </el-table-column>
          <el-table-column prop="running" label="服务器是否运行中" min-width="150">
            <template slot-scope="scope">
              <el-tag :type="scope.row.running? 'success' : 'info'">
                {{ scope.row.running?'是':'否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="registerNum" label="注册数" min-width="150" />
          <el-table-column prop="createNum" label="创角数" min-width="150" />
          <el-table-column prop="onlineNum" label="在线数" min-width="150" />
          <el-table-column prop="battleNum" label="战斗数" min-width="150" />
          <el-table-column prop="currBattleNum" label="当前战斗数" min-width="150" />
          <el-table-column prop="maxMemory" label="最大可用内存" min-width="150" />
          <el-table-column prop="freeMemory" label="空闲内存" min-width="150" />
          <el-table-column prop="totalMemory" label="占用内存" min-width="150">
            <template slot="header">
              <span>占用内存</span>&ensp;
              <el-tooltip content="占用内存 = 空闲内存 + 实际使用内存" placement="top">
                <i class="el-icon-info" style="color: #409eff" />
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="usedMemory" label="实际使用内存" min-width="150" />
          <el-table-column prop="leftMemory" label="剩余可用内存" min-width="150">
            <template slot="header">
              <span>剩余可用内存</span>&ensp;
              <el-tooltip content="剩余可用内存 = 最大可用内存 - 实际使用内存" placement="top">
                <i class="el-icon-info" style="color: #409eff" />
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" min-width="120">
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
      width="45%"
      center
      :modal-append-to-body="false"
      @close="closeDialog"
    >
      <el-form ref="ruleForm" :model="formData" label-width="130px" :rules="rules">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="服务器ID" prop="serverId">
              <el-input v-model.trim="formData.serverId" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务器名字" prop="name">
              <el-input v-model.trim="formData.name" clearable />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="外网IP" prop="externalIp">
              <el-input v-model.trim="formData.externalIp" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内网IP" prop="internalIp">
              <el-input v-model.trim="formData.internalIp" clearable />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="tcp/ws端口" prop="tcpPort">
              <el-input v-model.trim="formData.tcpPort" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="http端口" prop="httpPort">
              <el-input v-model.trim="formData.httpPort" clearable />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="游戏数据库名" prop="dbGameName">
              <el-input v-model.trim="formData.dbGameName" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="日志数据库名" prop="dbLogName">
              <el-input v-model.trim="formData.dbLogName" clearable />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否为推荐服务器">
              <el-select v-model="formData.recommend" clearable>
                <el-option :value="true" label="是" />
                <el-option :value="false" label="否" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="formData.status" clearable>
                <el-option :value="0" label="维护" />
                <el-option :value="1" label="流畅" />
                <el-option :value="2" label="爆满" />
                <el-option :value="3" label="新服" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              label="平台名称"
              prop="platformId"
              :rules="[{ required: true, message: '请选择平台', trigger: 'change' }]"
            >
              <el-select v-model="formData.platformId" clearable filterable>
                <el-option
                  v-for="item in platformOpt"
                  :key="item.value"
                  :label="item.text"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
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
          </el-col>
        </el-row>
        <el-form-item label="socket连接方式" prop="socketType">
          <el-input v-model.trim="formData.socketType" clearable />
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
import { gameServer, saveGameServer, platformOptions } from '@/api/serverManage'

const openEnum = { 0: '未发送', 1: '成功', 2: '失败' }
const statusEnum = { 0: '维护', 1: '流畅', 2: '爆满', 3: '新服' }

export default {
  data() {
    return {
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      serverData: [],
      dialogTitle: '',
      dialogVisible: false,
      formData: {},
      tableHeight: 0, // 表格高度
      platformOpt: [], // 平台选项
      platformId: '', // 平台id
      serverUid: '', // 选中的服务器id
      rules: {
        serverId: [{ required: true, message: '请输入服务器ID', trigger: 'blur' }],
        name: [{ required: true, message: '请输入服务器名字', trigger: 'blur' }],
        externalIp: [{ required: true, message: '请输入外网IP', trigger: 'blur' }],
        internalIp: [{ required: true, message: '请输入内网IP', trigger: 'blur' }],
        tcpPort: [{ required: true, message: '请输入tcp/ws端口', trigger: 'blur' }],
        httpPort: [{ required: true, message: '请输入http端口', trigger: 'blur' }],
        dbIp: [{ required: true, message: '请输入数据库IP', trigger: 'blur' }],
        dbPort: [{ required: true, message: '请输入数据库端口', trigger: 'blur' }],
        dbUser: [{ required: true, message: '请输入数据库用户', trigger: 'blur' }],
        dbPwd: [{ required: true, message: '请输入数据库密码', trigger: 'blur' }],
        dbGameName: [{ required: true, message: '请输入游戏数据库名', trigger: 'blur' }],
        dbLogName: [{ required: true, message: '请输入日志数据库名', trigger: 'blur' }],
        socketType: [{ required: true, message: '请输入socket连接方式', trigger: 'blur' }]
      }
    }
  },

  created() {
    this.getGameServer()
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

  methods: {
    // 获取数据
    async getGameServer() {
      const res = await gameServer({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.serverData = res.data.data.data
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增服务器'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.ruleForm.clearValidate()
      })
      this.formData.id = 0
    },

    // 编辑
    handleEdit(data) {
      this.dialogTitle = '编辑服务器数据'
      this.formData = Object.assign({}, data)
      this.dialogVisible = true
    },

    // 确定
    async handleConfirm() {
      await this.$refs.ruleForm.validate(async(valid) => {
        if (valid) {
          if (this.formData.id !== 0) {
          // 编辑
            const newData = Object.assign({}, this.formData)
            const res = await saveGameServer(newData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          } else {
          // 新增
            const res = await saveGameServer(this.formData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          }
          this.getGameServer()
          this.closeDialog()
        }
      })
    },

    // 关闭弹窗
    closeDialog() {
      this.dialogVisible = false
      this.formData = { }
      this.$refs.ruleForm.clearValidate()
    },

    // 获取平台列表
    async getPlatformList() {
      const res = await platformOptions()
      this.platformOpt = res.data.data.options
    },

    // 查询
    async handleInquire() {
      try {
        const query = { page: this.currentPage, limit: this.pageSize }
        if (this.platformId) {
          query.platformId = this.platformId
        }
        if (this.serverUid) {
          query.serverUid = this.serverUid
        }
        const result = await gameServer(query)
        this.total = result.data.data.count
        this.serverData = result.data.data.data
      } catch (error) {
        console.error(error)
      }
    },

    // 获取开服
    getOpen(value) {
      return openEnum[value]
    },

    // 获取服务器状态
    getStatus(value) {
      return statusEnum[value]
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getGameServer()
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

.topInquire{
  font-weight: bold;
  font-size: 14px;
  color: #6a7488;
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
}
</style>
