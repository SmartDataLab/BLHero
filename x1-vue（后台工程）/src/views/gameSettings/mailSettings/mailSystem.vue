<!-- 系统邮件 -->
<template>
  <div>
    <el-card>
      <!-- 头顶按钮 -->
      <div>
        <el-button
          type="primary"
          icon="el-icon-circle-plus-outline"
          size="small"
          @click="handleAdd"
        >添加邮件</el-button>
        <el-button
          type="success"
          icon="el-icon-folder-checked"
          size="small"
          :disabled="isDisabled"
          @click="handlePass"
        >审核通过</el-button>
        <el-button
          type="danger"
          icon="el-icon-folder-delete"
          size="small"
          :disabled="isDisabled"
          @click="handleNotPass"
        >审核不通过</el-button>
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="small"
          :disabled="isDisabled"
          @click="handleResend"
        >重新发送</el-button>
      </div>

      <!-- 表格 -->
      <div>
        <el-table
          ref="table"
          border
          :data="mailData"
          :header-cell-style="rowClass"
          :height="tableHeight"
          :cell-style="{'text-align':'center'}"
          @selection-change="selectionMailChange"
        >
          <el-table-column type="selection" width="50" fixed />
          <el-table-column prop="id" label="数据ID" min-width="150" />
          <el-table-column prop="status" label="邮件审核状态" min-width="150">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 0 ? 'info' : scope.row.status === 1 ? 'success' : 'danger'">
                {{ getStatus(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
          <el-table-column prop="content" label="内容" min-width="300" show-overflow-tooltip />
          <el-table-column prop="rewards" label="奖励" min-width="300" show-overflow-tooltip>
            <template slot-scope="scope">
              <span v-for="(item,index) in scope.row.rewards" :key="index">
                {{ item.name }}*{{ item.num }}{{ index !== scope.row.rewards.length - 1 ? '，' : '' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="邮件类型" min-width="150">
            <template slot-scope="scope">{{ scope.row.type==1?'全服邮件':scope.row.type==2?'玩家邮件':null }}</template>
          </el-table-column>
          <el-table-column label="服务器发送状态" min-width="150">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="handleDetails(scope.row.id)">【查看详情】</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="pids" label="玩家ID集合" min-width="200" show-overflow-tooltip />
          <el-table-column prop="playerLevel" label="玩家等级限制" min-width="150" />
          <el-table-column label="创建邮件的用户">
            <el-table-column prop="userId" min-width="150" />
            <el-table-column prop="userName" min-width="150" />
          </el-table-column>
          <el-table-column label="审核用户">
            <el-table-column prop="checkUserId" min-width="150" />
            <el-table-column prop="checkUserName" min-width="150" />
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="155">
            <template slot-scope="scope">
              <el-button
                v-show="scope.row.status === 0"
                size="small"
                type="primary"
                plain
                icon="el-icon-edit-outline"
                @click="handleEdit(scope.row)"
              >编辑</el-button>
              <el-button
                v-show="scope.row.status === 1"
                size="small"
                type="danger"
                plain
                icon="el-icon-delete"
                @click="handleDelete(scope.row.id)"
              >通知游戏服删除</el-button>
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

    <!-- 主-弹窗 -->
    <div class="main-dialog">
      <el-dialog
        :title="dialogTitle"
        :visible.sync="dialogVisible"
        :modal-append-to-body="false"
        width="40%"
        center
        @close="closeDialog"
      >
        <div class="dialog-content">
          <el-form ref="ruleForm" :model="formData" label-width="120px" :rules="rules">
            <el-form-item label="标题" prop="title">
              <el-input v-model="formData.title" clearable placeholder="请输入" />
            </el-form-item>
            <el-form-item label="内容" prop="content">
              <el-input v-model="formData.content" type="textarea" :rows="18" placeholder="请输入..." clearable />
            </el-form-item>
            <el-form-item label="奖励" prop="rewards">
              <el-button
                style="border-radius:0px 4px 4px 0px;width:100%"
                type="primary"
                plain
                @click="addRewards"
              >添加</el-button>
              <div v-for="(item,index) in formData.rewards" :key="index" class="props">
                <div style="width:55%">
                  <el-form-item
                    label="道具："
                    :prop="`rewards[${index}].item`"
                    :rules="[{ required: true, message: '请选择道具', trigger: 'change' }]"
                    label-width="60px"
                  >
                    <el-select v-model="item.item" size="small" clearable filterable>
                      <el-option
                        v-for="group in prop0ption"
                        :key="group.value"
                        :label="group.text"
                        :value="group.value"
                      />
                    </el-select>
                  </el-form-item>
                </div>
                <div style="width:40%">
                  <el-form-item
                    label="数量："
                    :prop="`rewards[${index}].num`"
                    :rules="[ { required: true, message: '数量不能为空'},{ type: 'number', message: '数量必须为数字值'}]"
                    label-width="60px"
                  >
                    <el-input v-model.number="item.num" size="small" clearable placeholder="请输入" />
                  </el-form-item>
                </div>
                <div style="width:5%">
                  <el-button
                    type="danger"
                    plain
                    icon="el-icon-minus"
                    circle
                    style="padding:1px;margin-left:10px"
                    @click="delRewards(index)"
                  />
                </div>
              </div>
            </el-form-item>
            <el-form-item label="邮件类型" prop="type">
              <el-select v-model.number="formData.type" clearable>
                <el-option :value="1" label="全服邮件" />
                <el-option :value="2" label="玩家邮件" />
              </el-select>
            </el-form-item>
            <el-form-item label="服务器选择" prop="serverText">
              <div class="idGather">
                <el-input v-model="formData.serverText" disabled placeholder="请选择" />
                <el-button icon="el-icon-plus" type="primary" plain @click="serverDialog=true" />
              </div>
            </el-form-item>
            <el-form-item v-if="formData.type==2" label="玩家ID集合" prop="pids">
              <el-input v-model.trim="formData.pids" placeholder="注：请输入玩家ID，并用英文分号(;)分隔" clearable />
            </el-form-item>
            <el-form-item label="玩家等级限制" prop="playerLevel">
              <el-input v-model.number="formData.playerLevel" clearable placeholder="请输入" />
            </el-form-item>
          </el-form>
        </div>
        <span slot="footer">
          <el-button size="medium" type="primary" @click="handleConfirm">确 定</el-button>
          <el-button size="medium" @click="closeDialog">取 消</el-button>
        </span>
      </el-dialog>
    </div>

    <!-- 服务器弹窗-多选 -->
    <el-dialog
      title="选择服务器"
      :visible.sync="serverDialog"
      :modal-append-to-body="false"
      width="48%"
      center
    >
      <el-button-group>
        <el-button size="mini" plain type="primary" @click="handleSelectAll">全选</el-button>
        <el-button size="mini" plain type="primary" @click="handleDeselectAll">全不选</el-button>
      </el-button-group>
      <div style="margin:30px 0" />
      <el-checkbox-group v-model="serverUids" @change="getCheckedId">
        <el-checkbox
          v-for="item in serverOptions"
          :key="item.value"
          :label="item.value"
          :vlaue="item.value"
        >{{ item.text }}</el-checkbox>
      </el-checkbox-group>
      <span slot="footer">
        <el-button size="medium" type="primary" @click="serverConfirm">确 定</el-button>
        <el-button size="medium" @click="serverDialog=false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 详情-弹窗 -->
    <el-dialog title="详情" :visible.sync="detailsDialog" width="60%" :modal-append-to-body="false">
      <el-table
        :header-cell-style="rowClass"
        :data="mailResultData"
        height="300"
        :cell-style="{'text-align':'center'}"
      >
        <el-table-column prop="id" label="日志ID" min-width="100" />
        <el-table-column prop="mailId" label="系统邮件ID" min-width="100" />
        <el-table-column prop="serverUid" label="服务器ID" min-width="100" />
        <el-table-column label="服务器名字">
          <el-table-column prop="serverId" min-width="100" />
          <el-table-column prop="serverName" min-width="100" />
        </el-table-column>
        <el-table-column prop="typeText" label="结果类型" min-width="100" />
        <el-table-column prop="status" label="处理状态" min-width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 0 ? 'info' : scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 0 ? '未处理' : scope.row.status === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="message" label="处理信息" min-width="200" show-overflow-tooltip />
      </el-table>
      <span slot="footer">
        <el-button type="primary" plain @click="detailsDialog = false">关闭</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { mailDatas, deleteMail, saveMail, mailPass,
  mailNotPass, mailResend, mailResult } from '@/api/mailSettings'
import { multiServer } from '@/api/aboutServer'
import { propsOpt } from '@/api/specialOperation'

const statusEnum = { 0: '未审核', 1: '审核通过', 2: '审核不通过', 3: '通知删除' }

export default {
  data() {
    return {
      total: 0,
      mailData: [],
      mailResultData: [],
      currentPage: 1,
      pageSize: 200,
      tableHeight: 0,
      dialogTitle: '',
      isDisabled: true, // 审核与重发按钮是否禁用
      dialogVisible: false, // 主弹窗
      detailsDialog: false, // 详情弹窗
      serverDialog: false,
      mailId: [], // 选中的邮件数据ID
      prop0ption: [],
      serverUids: [], // 选中的服务器id集合
      serverOptions: [],
      formData: {
        rewards: [], // 奖励
        serverText: '', // 服务器名称
        pids: ''
      },
      // 校验条件
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
        type: [{ required: true, message: '请选择邮件类型', trigger: 'change' }],
        pids: [{ validator: this.checkDataFormat, trigger: 'blur' }],
        serverText: [{ required: true, message: '请选择服务器', trigger: 'input' }],
        playerLevel: [{ type: 'number', message: '玩家等级必须为数字值' }]
      }
    }
  },

  watch: {
    'formData.type'(newType) {
      if (newType === 1) {
        delete this.formData.pids
      }
    }
  },

  created() {
    this.getMailDatas()
    this.getServer()
    this.getProps()
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
    async getMailDatas() {
      const res = await mailDatas({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.mailData = res.data.data.data
    },

    // 多选框选中邮件
    selectionMailChange(select) {
      this.isDisabled = select.length === 0
      this.mailId = select.map((item) => item.id)
    },

    // 审核通过
    async handlePass() {
      const res = await mailPass({ ids: this.mailId })
      this.getMailDatas()
      if (res.data.code === 0) {
        this.$message.success(res.data.message)
      }
    },

    // 审核不通过
    async handleNotPass() {
      const res = await mailNotPass({ ids: this.mailId })
      this.getMailDatas()
      if (res.data.code === 0) {
        this.$message.success(res.data.message)
      }
    },

    // 重新发送
    async handleResend() {
      if (this.mailId.length === 1) {
        const res = await mailResend({ id: this.mailId })
        this.getMailDatas()
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
      } else {
        this.$message.warning('邮件重发只能选择一项，请重新选择')
      }
    },

    // 添加邮件
    async handleAdd() {
      this.dialogTitle = '新增数据'
      this.formData.id = 0// 给后端判断当前是新增
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.ruleForm.clearValidate()
      })
    },

    // 编辑
    async handleEdit(data) {
      this.serverUids = data.serverUids
      this.formData = Object.assign({}, data)

      if (data.serverUids.length > 0) {
        this.formData.serverText = this.serverOptions
          .filter(item => data.serverUids.includes(item.value))
          .map(item => item.text).join(',')
      } else {
        this.formData.serverText = ''
      }
      this.dialogTitle = '编辑数据'
      this.dialogVisible = true
    },

    // 删除
    handleDelete(id) {
      this.$confirm('你确定要通知游戏服删除这条数据吗?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
        lockScroll: false
      }).then(async() => {
        const res = await deleteMail({ id: id })
        if (res.data.code === 0) {
          this.$message.success('成功通知游戏服删除该条数据')
        }
        this.getMailDatas()
      })
    },

    // 主弹窗-确定
    async handleConfirm() {
      await this.$refs.ruleForm.validate(async(valid) => {
        if (valid) {
          if (this.formData.id !== 0) {
            // 编辑
            const newData = Object.assign({}, this.formData)
            newData.rewards = JSON.stringify(this.formData.rewards)
            newData.serverUids = this.serverUids
            const res = await saveMail(newData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          } else {
            // 新增
            this.formData.serverUids = this.serverUids
            const rewards = JSON.stringify(this.formData.rewards)
            const addData = { ...this.formData, rewards }
            const res = await saveMail(addData)
            if (res.data.code === 0) {
              this.$message.success(res.data.message)
            }
          }
          this.getMailDatas()
          this.closeDialog()
        }
      })
    },

    // 添加奖励
    async addRewards() {
      this.formData.rewards.push({ item: '', num: '' })
    },

    // 删除奖励
    delRewards(index) {
      this.formData.rewards.splice(index, 1)
    },

    // 获取服务器
    async getServer() {
      const res = await multiServer()
      this.serverOptions = res.data.data.options
        .sort((a, b) => a.value - b.value)
    },

    // 选中的服务器ID
    getCheckedId(val) {
      this.serverUids = val
    },

    // 全选
    handleSelectAll() {
      this.serverUids = this.serverOptions.map((item) => item.value)
    },

    // 全不选
    handleDeselectAll() {
      this.serverUids = []
    },

    // 获取道具数据
    async getProps() {
      const res = await propsOpt()
      this.prop0ption = res.data.data.options
    },

    // 服务器弹窗-确认
    serverConfirm() {
      if (this.serverUids.length > 0) {
        this.formData.serverText = this.serverOptions
          .filter(item => this.serverUids.includes(item.value))
          .map(item => item.text)
          .join(',')
      } else {
        this.formData.serverText = ''
      }
      this.serverDialog = false
    },

    // 查看详情
    async handleDetails(id) {
      const res = await mailResult({ id: id })
      this.mailResultData = res.data.data.data
      this.detailsDialog = true
    },

    // 关闭弹窗
    closeDialog() {
      this.dialogVisible = false
      this.formData = { rewards: [] }
      this.serverUids = []
      this.$refs.ruleForm.clearValidate()
    },

    // 获取邮件审核状态
    getStatus(value) {
      return statusEnum[value]
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getMailDatas()
    },

    // 校验玩家id集合
    checkDataFormat(rule, value, callback) {
      if (!value || /^[\d;]+$/.test(value)) {
        callback()
      } else {
        callback(new Error('请用英文符号(;)分割玩家ID，且玩家ID为数字值'))
      }
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

::v-deep .el-select{
  width: 100%;
}

::v-deep .el-input__inner{
  border-radius: 0 4px 4px 0;
}

//奖励

::v-deep .dialog-content .props .el-input__inner{
  border-radius: 4px;
}
.props{
  display: flex;
  width: 100%;
}
.props:not(:last-child) {
  margin-bottom: 15px;
}
::v-deep .props .el-form-item__label{
 background-color: #fff;
 border: none;
 height: none;
}

// 服务器id集合
::v-deep .dialog-content .idGather{
  display: flex;
 .el-button--primary.is-plain{
  border-radius: 0px 4px 4px 0px;
  height: 40px;
  width: 40%;
 }
 .el-input__inner{
  border-radius: 0;
  border-right: 0;
 }
}

//弹窗高度
.dialog-content {
 max-height: 60vh;
 overflow-y: auto;
 overflow-x: hidden;
}
.dialog-content::-webkit-scrollbar {
  width: 6px;
  background-color: transparent;
}
.dialog-content::-webkit-scrollbar-thumb {
  background-color: #dddee0;
  border-radius: 4px;
}
.dialog-content::-webkit-scrollbar-thumb:hover {
  background-color: #c7c9cc;
}
::v-deep .dialog-content .el-form{
  padding:30px 20px 0px 20px
}
::v-deep .dialog-content .el-input__inner{
  border-radius: 0px 4px 4px 0px;
}

::v-deep .main-dialog .el-dialog__body {
 padding: 4px
}

::v-deep .el-textarea__inner{
  border-top-left-radius: 0;
}

::v-deep .el-table th {
  text-align: center;
}

::v-deep .el-button--small, .el-button--small.is-round{
  padding: 9px;
}
</style>
