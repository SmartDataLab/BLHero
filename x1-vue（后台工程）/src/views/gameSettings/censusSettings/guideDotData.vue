<!-- 引导数据打点 -->
<template>
  <div>
    <el-card>
      <div class="topInquire">
        <div>
          <span>服务器：</span>
          <el-input v-model="serverText" style="width:200px" size="small" disabled placeholder="请选择" />
          <el-button
            size="small"
            type="primary"
            plain
            icon="el-icon-circle-plus-outline"
            @click="radioDialog=true"
          >选择服务器</el-button>
        </div>

        <el-button size="small" icon="el-icon-search" type="primary" @click="handleSearch">搜索</el-button>
      </div>

      <el-table
        ref="table"
        border
        :data="guideDotData"
        :header-cell-style="{background:'#f5f7fa'}"
        :height="tableHeight"
      >
        <el-table-column prop="guideId" label="引导ID" min-width="120" />
        <el-table-column prop="guideName" label="引导名称" min-width="120" />
        <el-table-column prop="step" label="步骤" min-width="120" />
        <el-table-column prop="num" label="数量" min-width="120" />
        <el-table-column prop="loseNum" label="流失数" min-width="120" />
        <el-table-column prop="loseRate" label="流失率" min-width="120">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.loseNum/scope.row.num*100) }}%
          </template>
        </el-table-column>
      </el-table>

      <!-- 服务器-单选弹窗 -->
      <ServerDialog
        :visible.sync="radioDialog"
        :server-options="serverOptions"
        @checkedId="handleCheckedId"
      />
    </el-card>
  </div>
</template>

<script>
import ServerDialog from '@/components/radioDialog/index.vue'
import { multiServer } from '@/api/aboutServer'
import { guideDot } from '@/api/gameStatistics'

export default {
  components: {
    ServerDialog
  },

  data() {
    return {
      guideDotData: [],
      tableHeight: 0,
      serverText: '',
      radioDialog: false,
      serverOptions: [],
      serverUid: ''
    }
  },

  created() {
    this.getServer()
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
    // 获取服务器
    async getServer() {
      const res = await multiServer()
      this.serverOptions = res.data.data.options
        .sort((a, b) => a.value - b.value)
    },

    // 选中的服务器ID
    handleCheckedId(val) {
      this.serverUid = val

      const matchedOption = this.serverOptions.find(obj => obj.value === this.serverUid)
      if (matchedOption) {
        this.serverText = matchedOption.text
      } else {
        this.serverText = ''
      }
      this.radioDialog = false
    },

    // 搜索
    async handleSearch() {
      if (!this.serverText) {
        this.$message.error('请选择服务器，服务器为必选项')
      } else {
        const result = await guideDot({ serverUid: this.serverUid })
        this.guideDotData = result.data.data.data
      }
    },

    // 表格自适应高度
    getTableHeight() {
      if (this.$refs.table && this.$refs.table.$el) {
        this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 140
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
