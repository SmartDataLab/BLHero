<!-- 登录数据打点 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <div>
          <span>日期：</span>
          <el-date-picker
            v-model="date"
            type="daterange"
            range-separator="至"
            start-placeholder="选择开始日期"
            end-placeholder="选择结束日期"
            value-format="timestamp"
            size="small"
          />
        </div>

        <el-button size="small" icon="el-icon-search" type="primary" @click="handleSearch">搜索</el-button>
      </div>

      <el-table
        ref="table"
        border
        :data="loginDotData"
        :header-cell-style="{background:'#f5f7fa'}"
        :height="tableHeight"
      >
        <el-table-column prop="id" label="数据ID" min-width="120" />
        <el-table-column prop="channelId" label="渠道ID" min-width="120" />
        <el-table-column prop="dateStr" label="日期" min-width="120" />
        <el-table-column prop="reqResUrlBegin" label="根据版本号请求资源路径" min-width="180" />
        <el-table-column prop="reqResUrlSucess" label="根据版本号请求资源路径成功" min-width="210" />
        <el-table-column prop="reqResUrlFail" label="根据版本号请求资源路径成功" min-width="210" />
        <el-table-column prop="sdkLoginBegin" label="SDK开始登陆" min-width="180" />
        <el-table-column prop="sdkLoginSucess" label="SDK的登录成功" min-width="180" />
        <el-table-column prop="sdkLoginFail" label="SDK的登录失败" min-width="180" />
        <el-table-column prop="reqServerVerifyBegin" label="请求服务器账号验证" min-width="180" />
        <el-table-column prop="reqServerVerifySucess" label="请求服务器账号验证成功" min-width="180" />
        <el-table-column prop="reqServerVerifyFail" label="请求服务器账号验证失败" min-width="180" />
        <el-table-column prop="reqServerListBegin" label="请求服务器列表" min-width="180" />
        <el-table-column prop="reqServerListSucess" label="请求服务器列表成功" min-width="180" />
        <el-table-column prop="reqServerListFail" label="请求服务器列表失败" min-width="180" />
        <el-table-column prop="startGame" label="点击开始游戏" min-width="180" />
        <el-table-column prop="reqSocketConnectSucess" label="向服务器socket连接成功" min-width="180" />
        <el-table-column prop="reqSocketLoginBegin" label="向服务器发送登陆请求协议" min-width="200" />
        <el-table-column prop="reqSocketLoginSucess" label="向服务器发送登陆请求协议成功" min-width="230" />
        <el-table-column prop="reqSocketLoginFail" label="向服务器发送登陆请求协议失败" min-width="230" />
        <el-table-column prop="createSucc" label="创号成功" min-width="180" />
        <el-table-column prop="reqServerPushDataEnd" label="服务器推送数据结束" min-width="180" />
        <el-table-column prop="inGame" label="进入游戏" min-width="180" />
      </el-table>

      <el-pagination
        background
        layout="total, prev, pager, next, jumper"
        :total="total"
        :current-page="currentPage"
        :page-size="pageSize"
        @current-change="changePage"
      />
    </el-card>
  </div>
</template>

<script>
import { loginDot } from '@/api/gameStatistics'

export default {
  data() {
    return {
      loginDotData: [],
      total: 0,
      currentPage: 1,
      pageSize: 200,
      tableHeight: 0,
      date: [new Date().setDate(1), new Date()]
    }
  },

  created() {
    this.handleSearch()
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
    // 搜索
    async handleSearch() {
      try {
        const query = { page: this.currentPage, limit: this.pageSize }

        if (this.date && this.date.length === 2) {
          query.startTime = this.$moment(this.date[0]).unix()
          query.endTime = this.$moment(this.date[1]).unix()
        }

        const result = await loginDot(query)
        this.loginDotData = result.data.data.data
        this.total = result.data.data.count
      } catch (error) {
        console.error(error)
      }
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.handleSearch()
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

<style lang='scss' scoped>
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
