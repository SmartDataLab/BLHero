<!-- 37互娱渠道参数设置 -->
<template>
  <div>
    <el-card>
      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="sanQiSetData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="数据ID" min-width="130" />
          <el-table-column prop="platformId" label="37平台数据ID" min-width="130" />
          <el-table-column prop="gameKey" label="37提供的游戏key" min-width="130" />
          <el-table-column prop="loginKey" label="登录秘钥" min-width="270" />
          <el-table-column prop="rechargeKey" label="充值秘钥" min-width="270" />
          <el-table-column prop="chatKey" label="聊天秘钥" min-width="270" />
          <el-table-column prop="wordUrl" label="请求敏感词库地址" min-width="270" />
          <el-table-column prop="wordUpdateTime" label="词库更新时间" min-width="160">
            <template slot-scope="scope">{{ scope.row.wordUpdateTime|formatTime }}</template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="120">
            <template slot-scope="scope">
              <el-button
                type="primary"
                plain
                size="small"
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
      title="编辑数据"
      :visible.sync="dialogVisible"
      width="35%"
      :modal-append-to-body="false"
      center
      @close="closeDialog"
    >
      <el-form :model="formData" label-position="right" label-width="130px">
        <el-form-item label="数据ID">
          <el-input v-model="formData.id" disabled />
        </el-form-item>
        <el-form-item label="37平台数据ID">
          <el-input v-model.number="formData.platformId" />
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
import { sanQiHuYuSetting, savaSanQiHuYuSetting } from '@/api/aboutSanQi'

export default {
  data() {
    return {
      sanQiSetData: [],
      total: 0,
      currentPage: 1,
      pageSize: 200,
      tableHeight: 0,
      dialogVisible: false,
      formData: {}
    }
  },
  created() {
    this.getSanQiSetList()
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
    async getSanQiSetList() {
      const res = await sanQiHuYuSetting()
      this.total = res.data.data.count
      this.sanQiSetData = res.data.data.data
    },

    // 编辑
    handleEdit(data) {
      this.formData = Object.assign({}, data)
      this.dialogVisible = true
    },

    // 确认
    async handleConfirm() {
      await savaSanQiHuYuSetting(this.formData.platformId)
      this.getSanQiSetList()
      this.closeDialog()
    },

    // 关闭弹窗
    closeDialog() {
      this.dialogVisible = false
      this.formData = {}
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getSanQiSetList()
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
::v-deep .el-form .el-input__inner{
  border-radius: 0 4px 4px 0;
}
</style>

