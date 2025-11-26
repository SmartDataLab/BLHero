<!-- 兑换码生成 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <el-button type="primary" icon="el-icon-circle-plus-outline" size="small" @click="handleAdd">添加礼包码</el-button>

        <div>
          <span>礼包码：</span>
          <el-input v-model.trim="code" style="width:200px" size="small" clearable placeholder="请输入" />
        </div>
        <div>
          <span>配置礼包：</span>
          <el-select v-model="configId" clearable filterable size="small" style="width:200px">
            <el-option
              v-for="(item,index) in configIdOpt"
              :key="index"
              :label="item.text"
              :value="item.value"
            />
          </el-select>
        </div>
        <div>
          <span>使用码的玩家ID：</span>
          <el-input v-model.trim="playerId" style="width:200px" size="small" clearable placeholder="请输入" />
        </div>
        <div>
          <span>使用码的玩家名称：</span>
          <el-input v-model.trim="playerName" style="width:200px" size="small" clearable placeholder="请输入" />
        </div>

        <el-button size="small" type="primary" plain icon="el-icon-search" @click="handleSearch">搜索</el-button>
      </div>

      <!-- 表格 -->
      <div>
        <el-table
          ref="table"
          border
          :data="giftCodeData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="记录ID" min-width="120" />
          <el-table-column prop="code" label="礼包码" min-width="120" />
          <el-table-column prop="type" label="礼包码类型" min-width="120">
            <template slot-scope="scope">
              {{ scope.row.type===1?'通码':"独享码" }}
            </template>
          </el-table-column>
          <el-table-column prop="channelId" label="所属渠道ID" min-width="120" />
          <el-table-column prop="configId" label="配置礼包ID" min-width="120" />
          <el-table-column prop="configName" label="配置礼包名称" min-width="120" />
          <el-table-column prop="userId" label="创建的用户ID" min-width="120" />
          <el-table-column prop="userName" label="创建的用户名称" min-width="120" />
          <el-table-column prop="playerId" label="使用码的玩家ID" min-width="120" />
          <el-table-column prop="playerName" label="使用码的玩家名称" min-width="140" />
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
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="35%"
      center
      :modal-append-to-body="false"
      @close="closeDialog"
    >
      <el-form ref="ruleForm" :model="formData" label-width="120px">
        <el-form-item label="礼包码类型">
          <el-select v-model.number="formData.type" size="small" clearable>
            <el-option :value="1" label="通码" />
            <el-option :value="2" label="独享码" />
          </el-select>
        </el-form-item>
        <el-form-item v-show="formData.type===1?true:false" label="礼包码" prop="code">
          <el-input v-model.trim="formData.code" clearable />
        </el-form-item>
        <el-form-item
          v-show="formData.type===2?true:false"
          label="数量"
          prop="num"
          :rules="[{ type: 'number', message: '数量必须为数字值', trigger: ['blur', 'change']}]"
        >
          <el-input v-model.number="formData.num" clearable />
        </el-form-item>
        <el-form-item label="配置礼包ID">
          <el-select v-model="formData.configId" clearable filterable size="small">
            <el-option
              v-for="(item,index) in configIdOpt"
              :key="index"
              :label="item.text"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="medium" type="primary" @click="handleConfirm">确 定</el-button>
        <el-button size="medium" @click="closeDialog">取 消</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { giftCode, saveGiftCode, codeCfgOpt } from '@/api/specialOperation'

export default {
  data() {
    return {
      giftCodeData: [],
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      tableHeight: 0, // 表格高度
      dialogVisible: false,
      dialogTitle: '',
      formData: {},
      code: '', // 礼包码
      configId: '', // 配置礼包ID
      playerId: '', // 使用码的玩家ID
      playerName: '', // 使用码的玩家名称
      configIdOpt: []
    }
  },

  created() {
    this.getGiftCode()
    this.getCodeCfgOpt()
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
    async getGiftCode() {
      const res = await giftCode({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.giftCodeData = res.data.data.data
    },

    // 新增
    async handleAdd() {
      this.dialogTitle = '新增数据'
      await this.getCodeCfgOpt()
      this.dialogVisible = true
    },

    // 确认
    async handleConfirm() {
      await this.$refs.ruleForm.validate(async(valid) => {
        if (valid) {
          if (this.formData.num && this.formData.type === 1) {
            delete this.formData.num
          } else if (this.formData.code && this.formData.type === 2) {
            delete this.formData.code
          }
          const res = await saveGiftCode(this.formData)
          if (res.data.code === 0) {
            this.$message.success(res.data.message)
          }
          this.getGiftCode()
          this.closeDialog()
        }
      })
    },

    // 获取配置数据
    async getCodeCfgOpt() {
      const res = await codeCfgOpt()
      this.configIdOpt = res.data.data.options
    },

    // 查询
    async handleSearch() {
      try {
        const query = {}
        if (this.code) {
          query.code = this.code
        }
        if (this.configId) {
          query.configId = this.configId
        }
        if (this.playerId) {
          query.playerId = this.playerId
        }
        if (this.playerName) {
          query.playerName = this.playerName
        }
        const res = await giftCode(query)
        this.giftCodeData = res.data.data.data
        this.total = res.data.data.count
      } catch (error) {
        console.log(error)
      }
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getGiftCode()
    },

    // 关闭弹窗
    closeDialog() {
      this.dialogVisible = false
      this.formData = {}
      this.$refs.ruleForm.clearValidate()// 清空校验
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
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
}

::v-deep .el-form .el-input__inner{
  border-radius: 0 4px 4px 0;
  height: 40px;
}

::v-deep .el-select{
 width: 100%;
}
</style>

