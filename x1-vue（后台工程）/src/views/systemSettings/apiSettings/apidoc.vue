<template>
  <div>
    <el-card>
      <!-- 表格 -->
      <div>
        <el-table border :data="apiData" style="width: 100%" :height="tableHeight" :header-cell-style="{background:'#f5f7fa'}">
          <el-table-column prop="id" label="唯一ID" />
          <el-table-column prop="hrefUrl" label="链接地址" />
          <el-table-column prop="comment" label="接口说明" />
          <el-table-column prop="paramForm" label="参数格式" />
          <el-table-column prop="returnForm" label="返回格式" />
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
// 接口说明
import { apidoc } from '@/api/apidoc'

export default {
  data() {
    return {
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      apiData: [],
      tableHeight: 0 // 表格高度
    }
  },

  created() {
    this.getApidoc()
    this.getTableHeight()
  },

  mounted() {
    // 表格高度自适应
    const _this = this
    window.onresize = () => {
      if (_this.resizeFlag) {
        clearTimeout(_this.resizeFlag)
      }
      _this.resizeFlag = setTimeout(() => {
        _this.getTableHeight()
        _this.resizeFlag = null
      }, 100)
    }
  },

  methods: {
    async getApidoc() {
      const res = await apidoc({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.apiData = res.data.data.data
      this.total = res.data.data.count
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getApidoc()
    },

    // 表格自适应高度
    getTableHeight() {
      const tableH = 200 // 距离页面下方的高度
      const tableHeightDetil = window.innerHeight - tableH
      if (tableHeightDetil <= 300) {
        this.tableHeight = 300
      } else {
        this.tableHeight = window.innerHeight - tableH
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.el-table{
 margin-top: 0cm;
}
</style>
