<!-- 玩家信息管理 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <div>
          <span>玩家ID：</span>
          <el-input v-model.trim="playerId" clearable placeholder="请输入玩家ID" style="width:200px;" size="small" />
        </div>
        <div>
          <span>玩家昵称：</span>
          <el-input v-model.trim="name" clearable placeholder="请输入昵称" style="width:200px;" size="small" />
        </div>
        <div>
          <span>玩家账户：</span>
          <el-input v-model.trim="openId" clearable placeholder="请输入账号ID" style="width:200px;" size="small" />
        </div>
        <el-button icon="el-icon-search" type="primary" size="small" @click="handleInquire">查询</el-button>

        <el-button icon="el-icon-upload2" type="success" size="small" @click="handleExport">导出Excel</el-button>

      </div>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          :data="playerInfoData"
          border
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
          :cell-style="{'text-align':'center'}"
        >
          <el-table-column prop="id" label="序号" min-width="120" />
          <el-table-column prop="channelInfo" label="渠道信息" min-width="120" />
          <el-table-column prop="playerId" label="玩家ID" min-width="160" />
          <el-table-column prop="openId" label="账户ID" width="300" />
          <el-table-column prop="serverInfo" label="服务器信息" min-width="120" />
          <el-table-column prop="nick" label="昵称" min-width="120" />
          <el-table-column v-if="false" prop="head" label="头像" min-width="120">
            <template slot-scope="scope">
              <el-avatar v-if="!scope.row.head" shape="square" :src="defaultUrl" size="small" />
              <el-avatar v-else :src="scope.row.head" shape="square" size="small" />
            </template>
          </el-table-column>
          <el-table-column v-if="false" prop="sex" label="性别" min-width="120">
            <template slot-scope="scope">{{ getSex(scope.row.sex) }}</template>
          </el-table-column>
          <el-table-column prop="level" label="等级" min-width="120" />
          <el-table-column prop="gold" label="金币" min-width="120" />
          <el-table-column prop="diamond" label="钻石" min-width="120" />
          <el-table-column prop="fighting" label="战斗力" min-width="120" />
          <el-table-column prop="recharge" label="累计充值（单位:元）" min-width="160">
            <template #default="{row}">
              {{ (row.recharge)/100 }}
            </template>
          </el-table-column>
          <el-table-column prop="dailyOnline" label="最后登录当天在线时长" min-width="180">
            <template slot-scope="scope">{{ scope.row.dailyOnline|formatDuration }}</template>
          </el-table-column>
          <el-table-column prop="lastLoginTime" label="最后登录时间" min-width="180">
            <template slot-scope="scope">{{ scope.row.lastLoginTime|formatTime }}</template>
          </el-table-column>
          <el-table-column prop="online" label="是否在线" min-width="120">
            <template slot-scope="scope">
              <el-tag :type="scope.row.online? 'success' : 'info'">
                {{ scope.row.online?'在线':'离线' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="bornTime" label="创号时间" min-width="180">
            <template slot-scope="scope">{{ scope.row.bornTime|formatTime }}</template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="210">
            <template slot-scope="scope">
              <el-button type="text" size="medium" @click="handleParticular(scope.row.playerId)">详情</el-button>
              <el-button type="text" size="medium" @click="handleKickout(scope.row)">踢出</el-button>
              <el-button type="text" size="medium" @click="handleBanned(scope.row)">封禁</el-button>&emsp;
              <el-button type="text" size="medium" >
                <el-link type="success" :href="scope.row.quickUrl" target="_blank">快速通道</el-link>
              </el-button>
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
      title="详情信息"
      :visible.sync="dialogVisible"
      width="70%"
      :modal-append-to-body="false"
      @close="closeDialog"
    >
      <el-tabs type="card">

        <el-tab-pane label="角色信息">

          <div class="role-content">
            <!-- 角色信息表格 -->
            <div class="topTable" style="margin-top: 10px;">玩家账号信息</div>
            <el-table
              border
              :data="playerAccountData"
              :header-cell-style="{background:'#f5f7fa'}"

              :cell-style="{'text-align':'center'}"
            >
              <el-table-column prop="id" label="玩家ID" min-width="160" />
              <el-table-column prop="openId" label="账户ID" min-width="300" />
              <el-table-column prop="serverId" label="服务器ID" min-width="100" />
              <el-table-column prop="nick" label="昵称" min-width="100" />
              <el-table-column prop="head" label="头像" min-width="100">
                <template slot-scope="scope">
                  <el-avatar v-if="!scope.row.head" shape="square" :src="defaultUrl" size="medium" />
                  <el-avatar v-else :src="scope.row.head" shape="square" size="medium" />
                </template>
              </el-table-column>
              <el-table-column prop="sex" label="性别" min-width="100">
                <template slot-scope="scope">{{ getSex(scope.row.sex) }}</template>
              </el-table-column>
              <el-table-column prop="oldPlayer" label="新/老用户" min-width="100">
                <template #default="{row}">
                  {{ row.oldPlayer?"老":'新' }}
                </template>
              </el-table-column>
              <el-table-column prop="level" label="等级" min-width="100" />
              <el-table-column prop="exp" label="经验" min-width="120" />
              <el-table-column prop="gold" label="金币" min-width="120" />
              <el-table-column prop="diamond" label="钻石" min-width="120" />
              <el-table-column prop="loginIp" label="登录IP" min-width="120" />
              <el-table-column prop="loginDeviceId" label="登录设备ID" width="120" />
              <el-table-column prop="loginDeviceType" label="登录客户端类型" min-width="120" />
              <el-table-column prop="createIp" label="创号IP" min-width="120" />
              <el-table-column prop="createDeviceType" label="创号客户端类型" min-width="120" />
              <el-table-column prop="createDeviceId" label="创号设备ID" min-width="120" />
              <el-table-column prop="loginVersion" label="登录游戏版本号" min-width="120" />
              <el-table-column prop="changeNameNum" label="修改名字的次数" min-width="120" />
              <el-table-column prop="hisMaxFighting" label="历史最高战力" min-width="120" />
              <el-table-column prop="nowFighting" label="当前战力" min-width="120" />
              <el-table-column prop="realTotalPay" label="真实付费金额（单位:元）" min-width="200">
                <template #default="{row}">
                  {{ (row.realTotalPay)/100 }}
                </template>
              </el-table-column>
            </el-table>

            <!-- 在线情况表格 -->
            <div class="topTable">在线情况</div>
            <el-table
              border
              :data="onlineData"
              :header-cell-style="{background:'#f5f7fa'}"

              :cell-style="{'text-align':'center'}"
            >
              <el-table-column prop="online" label="在线状态">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.online? 'success' : 'info'">
                    {{ scope.row.online?'在线':'离线' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="dailyOnline" label="今日在线时长" width="200">
                <template slot-scope="scope">{{ scope.row.dailyOnline|formatDuration }}</template>
              </el-table-column>
              <el-table-column prop="" label="解除禁言时间" width="200" />
              <el-table-column prop="forbidEndTime" label="解除封号时间" width="200">
                <template slot-scope="scope">{{ scope.row.forbidEndTime|formatTime }}</template>
              </el-table-column>
              <el-table-column prop="lastLoginTime" label="上次登录时间" width="200">
                <template slot-scope="scope">{{ scope.row.lastLoginTime|formatTime }}</template>
              </el-table-column>
              <el-table-column prop="lastLogoutTime" label="上次下线时间" width="200">
                <template slot-scope="scope">{{ scope.row.lastLogoutTime|formatTime }}</template>
              </el-table-column>
              <el-table-column prop="createChannel" label="注册渠道" />
              <el-table-column prop="createDeviceType" label="平台" />
            </el-table>
          </div>
        </el-tab-pane>

        <el-tab-pane label="玩家背包">
          <!----------- 玩家背包--------->
          <div class="topTable" style="margin-top: 10px;">背包</div>
          <el-table
            border
            :data="bagData"
            :header-cell-style="{background:'#f5f7fa'}"
            height="400"
            :cell-style="{'text-align':'center'}"
            :default-sort="{prop: 'itemId', order: 'ascending'}"
          >
            <el-table-column prop="itemId" label="道具ID" sortable />
            <el-table-column prop="itemName" label="道具名称" />
            <el-table-column prop="haveNum" label="道具数量" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="伙伴信息">
          <!-- 伙伴信息表格 -->
          <div class="topTable" style="margin-top: 10px;">伙伴信息</div>
          <el-table
            border
            :data="friendData"
            :header-cell-style="{background:'#f5f7fa'}"
            height="400"
            :cell-style="{'text-align':'center'}"
            :default-sort="{prop: 'quality', order: 'ascending'}"
          >
            <el-table-column prop="id" label="伙伴ID" />
            <el-table-column prop="name" label="伙伴名称" />
            <el-table-column prop="quality" label="伙伴品质" sortable>
              <template slot-scope="scope">
                <div
                  :style="{color: (scope.row.quality === 1) ? 'red' : (scope.row.quality === 2) ? 'green'
                    : (scope.row.quality === 3) ? 'blue' : (scope.row.quality === 4) ? 'purple' : 'orange'}"
                >
                  {{ scope.row.quality }}
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="level" label="伙伴等级" />
            <el-table-column prop="num" label="碎片数量" />
            <el-table-column prop="fighting" label="伙伴战力" />
          </el-table>
        </el-tab-pane>
      </el-tabs>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" plain @click="dialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 封禁弹窗 -->
    <el-dialog
      title="封禁玩家"
      :visible.sync="bannedDialog"
      width="35%"
      :modal-append-to-body="false"
      center
    >
      <el-form :model="formData" label-width="120px">
        <el-form-item label="玩家ID">
          <el-input v-model.trim="formData.playerId" disabled />
        </el-form-item>
        <el-form-item label="玩家昵称">
          <el-input v-model.trim="formData.nick" disabled />
        </el-form-item>
        <el-form-item label="玩家账号">
          <el-input v-model.trim="formData.openId" disabled />
        </el-form-item>
        <el-form-item label="封禁结束时间">
          <el-date-picker
            v-model="data"
            type="datetime"
            placeholder="选择日期时间"
            value-format="timestamp"
            style="width: 100%;"
          />
        </el-form-item>
        <div style="color:red;margin-left: 120px;">注意：选择当前时间即为解封</div>
      </el-form>
      <span slot="footer">
        <el-button type="primary" @click="handleConfirm">确 认</el-button>
        <el-button @click="closeDialog">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { playerInfo, playerDetail, playerHero, onlineSituation,
  playerBag, KickoutPlayer, blockPlayer } from '@/api/playerManage'
import { export_json_to_excel } from '@/widget/Export2Excel'

const sexEnum = { 0: '无性别', 1: '男', 2: '女' }

export default {
  data() {
    return {
      playerId: '', // 玩家ID
      name: '', // 玩家名称
      openId: '', // 玩家账号
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      dialogVisible: false,
      bannedDialog: false,
      playerInfoData: [],
      playerAccountData: [],
      friendData: [],
      onlineData: [],
      bagData: [],
      data: '', // 日期
      formData: {},
      defaultUrl: 'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png'
    }
  },

  created() {
    this.getPlayerInfo()

    // 充值排名-跳转
    if (this.$route.query.detailId) {
      const id = this.$route.query.detailId
      this.handleParticular(id)
    }
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
    async getPlayerInfo() {
      const res = await playerInfo({
        page: this.currentPage,
        limit: this.pageSize,
        playerId: 0,
        name: this.name,
        openId: this.openId
      })
      this.total = res.data.data.count
      this.playerInfoData = res.data.data.data
    },

    // 查询
    async handleInquire() {
      try {
        const query = { page: this.currentPage, limit: this.pageSize }
        if (this.playerId) {
          query.playerId = this.playerId
        }
        if (this.name) {
          query.name = this.name
        }
        if (this.openId) {
          query.openId = this.openId
        }
        const res = await playerInfo(query)
        this.total = res.data.data.count
        this.playerInfoData = res.data.data.data

        // 给出提示
        if (res.data.data.count === 0) {
          if (query.playerId) {
            this.message.warning('此ID不存在')
          } else if (query.name) {
            this.message.warning('此昵称不存在')
          } else if (query.openId) {
            this.$message.warning('此账号不存在')
          }
        }
      } catch (error) {
        console.error(error)
        this.$message.error('发生错误，请稍后重试')
      }
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getPlayerInfo()
    },

    // 获取性别
    getSex(value) {
      return sexEnum[value]
    },

    // 详情
    async handleParticular(playerId) {
      // 玩家账号信息
      const resInfo = await playerDetail({ playerId: playerId })
      this.playerAccountData = resInfo.data.data.data

      // 伙伴信息
      const resFriend = await playerHero({ playerId: playerId })
      this.friendData = resFriend.data.data.data

      // 在线情况
      const resOnline = await onlineSituation({ playerId: playerId })
      this.onlineData = resOnline.data.data.data

      // 玩家背包
      const resBag = await playerBag({ playerId: playerId })
      this.bagData = resBag.data.data.data

      this.dialogVisible = true
    },

    // 踢出
    async handleKickout(row) {
      this.$confirm(`确定踢出昵称为 ${row.nick} 的玩家吗？`, '踢出提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
        lockScroll: false
      }).then(async() => {
        const res = await KickoutPlayer({ playerId: row.playerId })
        if (res.data.code === 0) {
          this.$message.success('成功踢出该玩家')
        } else {
          this.$message.error('踢出该玩家失败')
        }
      })
    },

    // 封禁
    async handleBanned(row) {
      this.formData.playerId = row.playerId
      this.formData.openId = row.openId
      this.formData.nick = row.nick
      this.bannedDialog = true
    },

    // 封禁-确认
    async handleConfirm() {
      const res = await blockPlayer({
        playerId: this.formData.playerId,
        forbidEndTime: this.data
      })
      if (res.data.code === 0) {
        this.$message.success('成功封禁该玩家账号')
      } else {
        this.$message.error('封禁该玩家账号失败')
      }
      this.closeDialog()
    },

    // 关闭弹窗
    closeDialog() {
      this.dialogVisible = false
      this.bannedDialog = false
      this.formData = {}
      this.data = ''
    },

    // 导出
    async handleExport() {
      try {
        const res = await playerInfo({
          limit: this.total
        })

        // 处理导出数据
        const exportData = res.data.data.data.map(i => {
          // 累计充值
          const recharge = this.formatNumber(i.recharge / 100)
          // 最后登录当天在线时长
          const dailyOnline = this.$options.filters.formatDuration(i.dailyOnline)
          // 最后登录时间
          const lastLoginTime = this.$options.filters.formatTime(i.lastLoginTime)
          // 创号时间
          const bornTime = this.$options.filters.formatTime(i.bornTime)
          // 是否在线
          const online = i.online ? '在线' : '离线'

          return { ...i, recharge, dailyOnline, lastLoginTime, bornTime, online }
        })

        const newData = exportData.map(item => {
          const arr = []
          const obj = {
            id: '序号',
            channelInfo: '渠道信息',
            playerId: '玩家ID',
            openId: '账户ID',
            serverInfo: '服务器信息',
            nick: '昵称',
            level: '等级',
            gold: '金币',
            diamond: '钻石',
            fighting: '战斗力',
            recharge: '累计充值（单位:元）',
            dailyOnline: '最后登录当天在线时长',
            lastLoginTime: '最后登录时间',
            online: '是否在线',
            bornTime: '创号时间'
          }
          for (const key in obj) {
            arr.push(item[key])
          }
          return arr
        })

        const tHeader = [
          '序号', '渠道信息', '玩家ID', '账户ID', '服务器信息', '昵称', '等级', '金币', '钻石',
          '战斗力', '累计充值（单位:元）', '最后登录当天在线时长', '最后登录时间', '是否在线', '创号时间']

        export_json_to_excel({
          header: tHeader,
          data: newData,
          filename: `玩家信息管理表${this.$moment(new Date()).format('YYYYMMDDHHmm')}`,
          autoWidth: true,
          bookType: 'xlsx'
        })
      } catch (error) {
        this.$message.error(error)
      }
    },

    // 表格自适应高度
    getTableHeight() {
      if (this.$refs.table && this.$refs.table.$el) {
        this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 175
      } else {
        this.tableHeight = 300
      }
    },

    // 保留两位小数
    formatNumber(value) {
      return value.toFixed(2)
    }

  }

}
</script>

<style lang="scss" scoped>
.el-table{
  margin: 0;
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

.topTable{
  background-color:#f5f7fa;
  color:  #409eff;
  height: 45px;
  line-height: 45px;
  font-weight: bold;
  font-size: 15px;
  padding-left: 10px;
  border: 1px solid #ebeef5;
  border-bottom: 0;
  margin-top: 80px;
}

.el-pagination{
 margin-top: 20px;
}

.role-content{
  max-height: 60vh;
 overflow-y: auto;
 overflow-x: hidden;

 &::-webkit-scrollbar {
  width: 6px;
  background-color: transparent;
 }

 &::-webkit-scrollbar-thumb {
  background-color: #c4c1c1;
  border-radius: 4px;
 }

 &::-webkit-scrollbar-thumb:hover {
  background-color: #999;
 }
}

::v-deep .role-content .el-input__inner{
  border-radius: 0px 4px 4px 0px;
}

.dialog-footer{
  padding-top: 10px;
}

::v-deep .el-form .el-input__inner{
  border-radius: 0 4px 4px 0;
}

::v-deep .el-table th {
  text-align: center;
}
</style>
