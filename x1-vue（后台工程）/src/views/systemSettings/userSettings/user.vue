<!-- 用户管理 -->
<template>
  <div>
    <el-card>
      <el-button type="primary" icon="el-icon-circle-plus-outline" size="small" @click="handleAdd">添加用户</el-button>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="userData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="用户ID" min-width="100" />
          <el-table-column prop="name" label="用户名" min-width="100" />
          <el-table-column prop="level" label="用户级别" min-width="100">
            <template slot="header">
              <span>用户级别</span>&ensp;
              <el-tooltip content="数字越小，级别越高" placement="top">
                <i class="el-icon-info" style="color: #409eff" />
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="password" label="密码" min-width="100" />
          <el-table-column prop="phone" label="手机号码" min-width="150" />
          <el-table-column prop="mailAddress" label="邮箱地址" min-width="300" />
          <el-table-column prop="usable" label="是否启用" min-width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.usable?'success':'danger'">
                {{ scope.row.usable?'是':'否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="superUser" label="是否为超级管理员" min-width="150">
            <template slot-scope="scope">
              <el-tag :type="scope.row.superUser?'success':'danger'">
                {{ scope.row.superUser?'是':'否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="canReadSensitive" label="是否可查看敏感信息" min-width="150">
            <template slot-scope="scope">
              <el-tag :type="scope.row.canReadSensitive?'success':'danger'">
                {{ scope.row.canReadSensitive?'是':'否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="upUserId" label="上级用户ID" min-width="100" />
          <el-table-column label="操作" fixed="right" width="250">
            <template slot-scope="scope">
              <el-button
                icon="el-icon-unlock"
                size="small"
                type="warning"
                plain
                @click="handlePower(scope.row)"
              >编辑权限</el-button>
              <el-button
                icon="el-icon-edit-outline"
                size="small"
                type="primary"
                plain
                @click="handleEdit(scope.row)"
              >编辑</el-button>
              <el-button
                icon="el-icon-delete"
                size="small"
                type="danger"
                plain
                @click="handleDelete(scope.row)"
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

    <!--主- 弹窗 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="35%"
      center
      :modal-append-to-body="false"
      @close="closeDialog"
    >
      <el-form ref="ruleForm" :model="formData" label-width="140px">
        <el-form-item v-show="isShow" label="用户ID">
          <el-input v-model.number="formData.id" disabled />
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model.trim="formData.name" clearable :disabled="isDisabled" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="密码">
          <div class="password">
            <el-input v-model.trim="formData.password" clearable :disabled="isDisabled" placeholder="请输入" />
          </div>
        </el-form-item>
        <el-form-item
          label="手机号码"
          prop="phone"
          :rules="[{required:false,message:'请输入正确的手机号码',pattern:/^1[3456789]\d{9}$/,trigger:['blur','change']}]"
        >
          <el-input v-model.number="formData.phone" clearable placeholder="请输入" />
        </el-form-item>
        <el-form-item
          label="邮箱地址"
          prop="mailAddress"
          :rules="[{required:false,type:'email',message:'请输入正确的邮箱地址',trigger:['blur','change'] }]"
        >
          <el-input v-model.trim="formData.mailAddress" clearable placeholder="请输入" />
        </el-form-item>
        <el-form-item label="是否启用">
          <el-select v-model="formData.usable" clearable>
            <el-option :value="true" label="是" />
            <el-option :value="false" label="否" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否为超级管理员">
          <el-select v-model="formData.superUser" clearable>
            <el-option :value="true" label="是" />
            <el-option :value="false" label="否" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否可查看敏感信息">
          <el-select v-model="formData.canReadSensitive" clearable>
            <el-option :value="true" label="是" />
            <el-option :value="false" label="否" />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="medium" type="primary" @click="handleConfirm">确 定</el-button>
        <el-button size="medium" @click="closeDialog">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 权限弹窗 -->
    <el-dialog :title="powerTitle" :visible.sync="powerDialog" width="45%">
      <el-table
        ref="powerTableRef"
        border
        :data="powerData"
        style="width: 100%;"
        height="500"
        @selection-change="selectionPower"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="功能菜单ID" min-width="170" />
        <el-table-column prop="name" label="功能菜单名字" min-width="170" />
        <el-table-column prop="module" label="所属功能模块" min-width="170" />
        <el-table-column prop="writee" label="是否授权写权限" min-width="170">
          <template slot-scope="scope">
            <el-select v-model="scope.row.writee" clearable @change="changeSelect(scope.row)">
              <el-option :value="true" label="是" />
              <el-option :value="false" label="否" />
            </el-select>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center">
        <el-button size="medium" type="primary" @click="powerConfirm">确 定</el-button>
        <el-button size="medium" @click="powerDialog=false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { userManagement, saveUser, deleteUser, funcOptions, saveUserRight } from '@/api/userSetting'

export default {
  data() {
    return {
      page: 1,
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      userData: [],
      dialogTitle: '',
      dialogVisible: false,
      formData: {},
      tableHeight: 0, // 表格高度
      isDisabled: true,
      isShow: false,
      powerDialog: false,
      powerTitle: '',
      powerData: [],
      userId: '', // 用户ID
      selectPermis: [] // 选中的权限
    }
  },

  created() {
    this.getUserManagement()
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
    async getUserManagement() {
      const res = await userManagement({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.userData = res.data.data.data
      this.total = res.data.data.count
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getUserManagement()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增用户'
      this.dialogVisible = true
      this.formData.id = 0
      this.isDisabled = false
      this.isShow = false
    },

    // 编辑
    handleEdit(data) {
      this.dialogTitle = '编辑用户'
      this.formData = Object.assign({}, data)
      this.dialogVisible = true
      this.isDisabled = true
      this.isShow = true
    },

    // 删除
    handleDelete(id) {
      this.$confirm('你确定要删除这条数据吗?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
        lockScroll: false
      }).then(async() => {
        const res = await deleteUser(id)
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
        this.getUserManagement()
      })
    },

    // 确定
    async handleConfirm() {
      await this.$refs.ruleForm.validate(async(valid) => {
        if (valid) {
          if (this.formData.id !== 0) {
            // 编辑
            const newData = Object.assign({}, this.formData)
            const res = await saveUser(newData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          } else {
            // 新增
            const res = await saveUser(this.formData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          }
          this.getUserManagement()
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

    // 编辑权限
    async handlePower(row) {
      this.userId = row.id
      this.powerTitle = `正在编辑该用户权限：${row.id} - ${row.name}`
      // 获取权限列表
      const res = await funcOptions({ userId: row.id })
      this.powerData = res.data.data.data

      this.$nextTick(() => {
        this.powerData.filter(row => row.has).forEach(row => {
          this.$refs.powerTableRef.toggleRowSelection(row, true)
        })
      })
      this.powerDialog = true
    },

    // 选中的权限
    selectionPower(val) {
      this.selectPermis = val.map(item => `${item.id}#${item.writee}`)
    },

    // 权限确认
    async powerConfirm() {
      const res = await saveUserRight({
        userId: this.userId,
        functionAndWrite: this.selectPermis
      })
      this.powerData = res.data.data.data
      this.powerDialog = false
    },

    // 监听"写权限"的变化
    changeSelect(val) {
      const index = this.selectPermis.findIndex(item => item.startsWith(`${val.id}#`))
      const newSelectedValue = `${val.id}#${val.writee}`
      if (index > -1) {
        this.selectPermis.splice(index, 1, newSelectedValue)
      } else {
        this.selectPermis.push(newSelectedValue)
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

::v-deep .el-select{
 width: 100%;
}

::v-deep .password{
  display: flex;
  .el-input__inner{
    border-radius: 0;
  }
  .el-button{
    border-radius: 0 4px 4px 0;
  }
}

::v-deep .el-button--small, .el-button--small.is-round{
  padding: 9px;
}
</style>
