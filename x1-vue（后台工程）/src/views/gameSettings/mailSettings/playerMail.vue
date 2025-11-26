<!-- 玩家邮件 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <div>
          <span>渠道：</span>
          <el-select v-model="channelId" filterable clearable size="small">
            <el-option v-for="item in channelOptions" :key="item.value" :label="item.text" :value="item.value" />
          </el-select>
        </div>
        <div>
          <span>玩家ID：</span>
          <el-input v-model.number="playerId" size="small" style="width:200px" clearable placeholder="请输入" />
        </div>
        <el-button size="small" icon="el-icon-search" type="primary" @click="handleSearch">搜索</el-button>
      </div>

      <div>
        <el-table
          ref="table"
          border
          :data="playerMailsData"
          :header-cell-style="rowClass"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="邮件自增ID" min-width="100" />
          <el-table-column prop="receiver" label="玩家ID" min-width="100" />
          <el-table-column prop="template" label="邮件模板ID" min-width="100" />
          <el-table-column prop="titleArgs" label="标题参数" min-width="100" />
          <el-table-column prop="contentArgs" label="内容参数" min-width="100" />
          <el-table-column prop="attachment" label="附件" min-width="100" />
          <el-table-column prop="read" label="是否已读" min-width="100" />
          <el-table-column prop="receive" label="是否已领" min-width="100" />
          <el-table-column prop="expireTime" label="到期时间" min-width="200">
            <template slot-scope="scope">{{ scope.row.expireTime|formatTime }}</template>
          </el-table-column>
          <el-table-column label="游戏事件" header-align="center">
            <el-table-column prop="gameCause" min-width="150" />
            <el-table-column prop="gameCauseText" min-width="150" />
          </el-table-column>
          <el-table-column prop="fromPoolId" label="来自哪个系统邮件" min-width="140" />
          <el-table-column v-if="false" prop="channelId" label="渠道ID" />
          <el-table-column label="操作" fixed="right" width="100">
            <template slot-scope="scope">
              <el-button size="small" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

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

  </div>
</template>

<script>
import { playerMails, deletePlayerMail } from '@/api/mailSettings'
import { channelOpt } from '@/api/aboutChannel'

export default {
  data() {
    return {
      total: 0,
      playerMailsData: [],
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      channelId: '', // 渠道ID
      channelOptions: [], // 渠道选项
      playerId: '' // 玩家ID
    }
  },

  created() {
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

  methods: {
    // 获取渠道
    async getChannel() {
      const res = await channelOpt()
      this.channelOptions = res.data.data.options
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.handleSearch()
    },

    // 删除
    handleDelete(row) {
      this.$confirm('你确定要删除这条数据吗?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
        lockScroll: false
      }).then(async() => {
        const res = await deletePlayerMail({
          channelId: row.channelId,
          playerId: row.receiver,
          mailId: row.fromPoolId
        })

        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
        this.handleSearch()
      })
    },

    // 搜索
    async handleSearch() {
      try {
        const query = { page: this.currentPage,
          limit: this.pageSize }

        if (!this.playerId || isNaN(this.playerId)) {
          this.$message.error('请输入玩家ID，且玩家ID为数字类型')
        } else if (!this.channelId) {
          this.$message.error('请选择渠道，渠道为必选项')
        } else {
          query.channelId = this.channelId
          query.playerId = this.playerId

          const result = await playerMails(query)
          this.total = result.data.data.count
          this.playerMailsData = result.data.data.data
        }
      } catch (error) {
        console.error(error)
      }
    },

    // 合并游戏事件表头
    rowClass({ rowIndex }) {
      if (rowIndex === 1) {
        return { display: 'none', background: '#f5f7fa' }
      }
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
