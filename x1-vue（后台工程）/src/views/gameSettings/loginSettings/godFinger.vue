<!-- 金手指名单 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <el-button
          type="primary"
          icon="el-icon-circle-plus-outline"
          size="small"
          @click="handleAdd"
        >添加GM</el-button>
        <div>
          <span>玩家账号：</span>
          <el-input v-model.trim="account" clearable placeholder="请输入" size="small" style="width:200px" />
        </div>

        <el-button size="small" icon="el-icon-search" plain type="primary" @click="handleSearch">搜索</el-button>
      </div>

      <div>
        <el-table
          ref="table"
          border
          :data="godFingerData"
          :height="tableHeight"
          :header-cell-style="rowClass"
        >
          <el-table-column prop="id" label="数据ID" min-width="120" />
          <el-table-column prop="channelId" label="渠道ID" min-width="120" />
          <el-table-column prop="channelName" label="渠道名字" min-width="120" />
          <el-table-column prop="serverUid" label="数据唯一ID" min-width="120" />
          <el-table-column prop="serverId" label="服务器ID" min-width="120" />
          <el-table-column prop="serverName" label="服务器名字" min-width="120" />
          <el-table-column prop="playerId" label="玩家ID" min-width="150" />
          <el-table-column prop="openId" label="账号ID" min-width="300" />
          <el-table-column prop="nick" label="玩家昵称" min-width="120" />
          <el-table-column prop="money" label="每日金额 (单位：分)" min-width="150" />
          <el-table-column prop="userId" label="操作人ID" min-width="120" />
          <el-table-column prop="userName" label="操作人名字" min-width="120" />
          <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
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
                @click="handleDelete(scope.row.id)"
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="35%" :modal-append-to-body="false" center @close="closeDialog">
      <el-form ref="formData" :model="formData" label-position="right" label-width="150px">
        <el-form-item v-show="isShow" label="数据ID">
          <el-input v-model="formData.id" disabled />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item v-show="isShow" label="渠道ID">
              <el-input v-model="formData.channelId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-show="isShow" label="渠道名字">
              <el-input v-model="formData.channelName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item v-show="isShow" label="服务器ID">
              <el-input v-model="formData.serverId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-show="isShow" label="服务器名字">
              <el-input v-model="formData.serverName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item
          label="玩家ID"
          prop="playerId"
          :rules="[{ type: 'number', message: '玩家账号必须为数字值', trigger: ['blur', 'change']}]"
        >
          <el-input v-model.number="formData.playerId" clearable :disabled="isDisabled" placeholder="请输入" />
        </el-form-item>
        <el-form-item v-show="isShow" label="账号ID">
          <el-input v-model="formData.openId" disabled />
        </el-form-item>
        <el-form-item v-show="isShow" label="玩家昵称">
          <el-input v-model="formData.nick" disabled />
        </el-form-item>
        <el-form-item
          label="每日金额 (单位：分)"
          prop="money"
          :rules="[{ type: 'number', message: '金额必须为数字值', trigger: ['blur', 'change']}]"
        >
          <el-input v-model.number="formData.money" clearable placeholder="请输入" />
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input v-model.trim="formData.remark" clearable placeholder="请输入" />
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
import { godFinger, saveGodFinger, deleteGodFinger } from '@/api/playerManage'

export default {
  data() {
    return {
      godFingerData: [],
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      formData: {},
      account: '', // 玩家账号
      dialogVisible: false,
      dialogTitle: '',
      isShow: false,
      isDisabled: false
    }
  },

  created() {
    this.getGodFinger()
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
    async getGodFinger() {
      const res = await godFinger({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.godFingerData = res.data.data.data
    },

    // 搜索
    async handleSearch() {
      try {
        const query = { page: this.currentPage, limit: this.pageSize }

        if (this.account) {
          query.openId = this.account
        }

        const result = await godFinger(query)
        this.total = result.data.data.count
        this.godFingerData = result.data.data.data
      } catch (error) {
        console.error(error)
        this.$message.error('发生错误，请稍后重试')
      }
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '添加GM'
      this.isShow = false
      this.isDisabled = false
      this.dialogVisible = true
      this.formData.id = 0
    },

    // 编辑
    handleEdit(data) {
      this.dialogTitle = '编辑账号'
      this.isShow = true
      this.isDisabled = true
      this.formData = Object.assign({}, data)
      this.dialogVisible = true
    },

    // 确定
    async handleConfirm() {
      await this.$refs.formData.validate(async(valid) => {
        if (valid) {
          if (this.formData.id !== 0) {
            // 编辑
            const newData = Object.assign({}, this.formData)
            const res = await saveGodFinger(newData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          } else {
            // 新增
            const res = await saveGodFinger(this.formData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          }
          this.getGodFinger()
          this.closeDialog()
        } else {
          console.log('校验未通过')
          return false
        }
      })
    },

    // 删除
    handleDelete(id) {
      this.$confirm('你确定要删除这条数据吗?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
        lockScroll: false
      }).then(async() => {
        const res = await deleteGodFinger({ id: id })
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
        this.getGodFinger()
      })
    },

    // 关闭弹窗
    closeDialog() {
      this.dialogVisible = false
      this.formData = {}
      this.$refs.formData.clearValidate()
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getGodFinger()
    },

    // 表格自适应高度
    getTableHeight() {
      if (this.$refs.table && this.$refs.table.$el) {
        this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 170
      } else {
        this.tableHeight = 300
      }
    },

    // 合并表头
    rowClass({ rowIndex }) {
      if (rowIndex === 1) {
        return { display: 'none', background: '#f5f7fa' }
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
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
}

::v-deep .el-form .el-input__inner{
  border-radius: 0 4px 4px 0;
}

</style>
