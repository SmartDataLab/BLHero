<!-- 开服管理 -->
<template>
  <div>
    <el-card>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="serverOpenData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column v-if="false" prop="id" label="数据唯一ID" min-width="120" />
          <el-table-column prop="serverId" label="服务器ID" min-width="120" fixed />
          <el-table-column prop="name" label="服务器名" min-width="120" />
          <el-table-column label="平台信息" width="150">
            <template slot-scope="scope">
              {{ scope.row.platformName }} [{{ scope.row.platformId }}]
            </template>
          </el-table-column>
          <el-table-column prop="running" label="服务器是否运行中" width="150">
            <template slot-scope="scope">
              <el-tag :type="scope.row.running? 'success' : 'info'">
                {{ scope.row.running?'是':'否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="openTime" label="预设开服时间" min-width="160">
            <template slot-scope="scope">{{ scope.row.openTime|formatTime }}</template>
          </el-table-column>
          <el-table-column prop="realOpenTime" label="实际开服时间" min-width="160">
            <template slot-scope="scope">{{ scope.row.realOpenTime|formatTime }}</template>
          </el-table-column>
          <el-table-column prop="sendOpenStatus" label="开服时间是否发送成功" min-width="170">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.sendOpenStatus === 0 ? 'info'
                  : scope.row.sendOpenStatus === 1 ? 'success' : 'danger'"
              >
                {{ getOpen(scope.row.sendOpenStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="opening" label="是否已经开服" width="150">
            <template slot-scope="scope">
              <el-tag :type="scope.row.opening? 'success':'info'">
                {{ scope.row.opening? '是':'否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="externalIp" label="外网IP" min-width="120" />
          <el-table-column prop="internalIp" label="内网IP" min-width="120" />
          <el-table-column prop="httpPort" label="http端口" min-width="120" />
          <el-table-column prop="tcpPort" label="tcp/ws端口" min-width="120" />
          <el-table-column prop="dbIp" label="数据库IP" width="120" />
          <el-table-column prop="dbPort" label="数据库端口" width="120" />
          <el-table-column prop="dbUser" label="数据库用户" width="120" />
          <el-table-column prop="dbPwd" label="数据库密码" width="160" />
          <el-table-column prop="dbGameName" label="游戏数据库名" width="120" />
          <el-table-column prop="dbLogName" label="日志数据库名" width="120" />
          <el-table-column prop="status" label="状态" width="150">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.status === 0 ? 'danger' : scope.row.status === 1 ? 'success'
                  : scope.row.status === 2 ? 'warning' : null"
              >
                {{ getStatus(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="recommend" label="是否为推荐服务器" width="150">
            <template slot-scope="scope">
              <el-tag :type="scope.row.recommend? 'success' : 'danger'">
                {{ scope.row.recommend?'是':'否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="230">
            <template slot-scope="scope">
              <el-button icon="el-icon-time" type="primary" plain size="small" @click="setOpenBtn(scope.row)">预设开服时间</el-button>
              <el-button slot="reference" icon="el-icon-open" type="success" plain size="small" @click="nowOpenBtn(scope.row)">立即开服</el-button>
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

    <!-- 弹窗  -->
    <el-dialog
      title="设置服务器预期开服时间"
      :visible.sync="dialogVisible"
      width="35%"
      :modal-append-to-body="false"
      center
      @close="closeDialog"
    >
      <el-form :model="formData" label-position="right" label-width="150px">
        <el-form-item label="数据唯一ID">
          <el-input v-model="formData.id" disabled />
        </el-form-item>
        <el-form-item label="服务器ID">
          <el-input v-model="formData.serverId" disabled />
        </el-form-item>
        <el-form-item label="服务器名">
          <el-input v-model="formData.name" disabled />
        </el-form-item>
        <el-form-item label="预设开服时间">
          <el-date-picker
            v-model="formData.openTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="timestamp"
            style="width: 100%;"
          />
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
import { serverOpen, setOpenTime, nowOpenServer } from '@/api/serverManage'

const openEnum = { 0: '未发送', 1: '成功', 2: '失败' }
const statusEnum = { 0: '维护', 1: '流畅', 2: '爆满', 3: '新服' }

export default {
  data() {
    return {
      serverOpenData: [],
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      dialogVisible: false,
      formData: {},
      tableHeight: 0 // 表格高度
    }
  },

  created() {
    this.getServerOpen()
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
    async getServerOpen() {
      const res = await serverOpen({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.serverOpenData = res.data.data.data
    },

    // 设置开服时间
    setOpenBtn(data) {
      this.formData = Object.assign({}, data)
      this.dialogVisible = true
    },

    // 确认按钮
    async handleConfirm() {
      const res = await setOpenTime({
        serverUid: this.formData.id,
        openTime: this.formData.openTime })
      if (res.data.code === 0) {
        this.$message.success('预设开服时间成功！')
      }
      this.getServerOpen()
      this.closeDialog()
    },

    // 立即开服
    async nowOpenBtn(row) {
      this.$confirm(`你确定要立即开启服务器 ${row.name} 吗?`, '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
        lockScroll: false
      }).then(async() => {
        const res = await nowOpenServer({ serverUid: row.id })
        if (res.data.code === 0) {
          this.$message.success('开服成功！')
        }
        this.getServerOpen()
      })
    },

    // 关闭弹窗
    closeDialog() {
      this.formData = {}
      this.dialogVisible = false
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getServerOpen()
    },

    // 获取开服
    getOpen(value) {
      return openEnum[value]
    },

    // 获取服务器状态
    getStatus(value) {
      return statusEnum[value]
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

::v-deep .el-button--small, .el-button--small.is-round{
  padding: 9px;
}
</style>
