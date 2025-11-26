<!-- 用户权限管理 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <div>
          <span>用户名：</span>
          <el-input v-model.trim="userName" style="width:200px;" size="small" clearable placeholder="请输入" />
        </div>
        <el-button icon="el-icon-search" type="primary" size="small" @click="handleSearch">搜索</el-button>
      </div>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="userFnData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="唯一ID" min-width="120" />
          <el-table-column prop="userId" label="用户ID" min-width="120" />
          <el-table-column prop="userName" label="用户名" min-width="120" />
          <el-table-column prop="functionId" label="功能ID" min-width="120" />
          <el-table-column prop="functionName" label="功能名称" min-width="120" />
          <el-table-column prop="writee" label="是否具有写权限" min-width="120">
            <template slot-scope="scope">
              <el-tag :type="scope.row.writee ? 'success' : 'info'">
                {{ scope.row.writee?'是':'否' }}
              </el-tag>
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
  </div>
</template>

<script>
import { userRight } from '@/api/userSetting'

export default {
  data() {
    return {
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      userFnData: [],
      tableHeight: 0, // 表格高度
      userName: ''// 用户名
    }
  },

  created() {
    this.getUserRight()
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
    async getUserRight() {
      const res = await userRight({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.userFnData = res.data.data.data
    },

    // 搜索
    async handleSearch() {
      const query = { page: this.currentPage, limit: this.pageSize }
      if (this.userName) {
        query.userName = this.userName
      }
      const result = await userRight(query)
      this.total = result.data.data.count
      this.userFnData = result.data.data.data
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getUserRight()
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
.topInquire{
  font-weight: bold;
  font-size: 14px;
  color: #6a7488;
  margin-left: 3px;
  display: flex;
  gap: 30px;
  flex-wrap: wrap;
}
</style>
