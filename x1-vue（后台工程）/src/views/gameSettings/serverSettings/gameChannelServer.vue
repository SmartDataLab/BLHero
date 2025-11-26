<!-- 渠道与服务器关系 -->
<template>
  <div>
    <el-card>
      <div class="header">
        <el-button type="primary" icon="el-icon-circle-plus-outline" size="small" @click="addDialogVisible=true">添加服务器到指定渠道</el-button>

        <div class="ditch">
          <span>渠道：</span>
          <el-select v-model="headerChannel" style="width:200px" size="small" clearable>
            <el-option v-for="item in channelOptions" :key="item.value" :label="item.text" :value="item.value" />
          </el-select>
        </div>

        <el-button size="small" plain type="primary" icon="el-icon-search" @click="searchBtn">搜索</el-button>
      </div>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="channelServerData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="关系ID" />
          <el-table-column label="渠道信息">
            <template slot-scope="scope">
              {{ scope.row.channelName }} [{{ scope.row.channelId }}]
            </template>
          </el-table-column>
          <el-table-column prop="serverUid" label="服务器数据ID" />
          <el-table-column prop="serverId" label="服务器ID" />
          <el-table-column prop="serverName" label="服务器名字" />
          <el-table-column prop="regionId" label="所属大区ID" />
          <el-table-column prop="regionName" label="所属大区名称" />
          <el-table-column prop="recommend" label="是否为推荐服务器">
            <template slot-scope="scope">
              <el-tag :type="scope.row.recommend? 'success' : 'danger'">
                {{ scope.row.recommend?'是':'否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="120">
            <template slot-scope="scope">
              <el-button
                size="small"
                type="primary"
                plain
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

    <!-- 新增弹窗 -->
    <el-dialog
      title="设定渠道关联服务器"
      :visible.sync="addDialogVisible"
      width="40%"
      :modal-append-to-body="false"
      center
      @close="closeDialog"
    >
      <div style="margin-bottom: 30px;font-size: 15px;">
        <span> 渠道选择：</span>
        <el-select v-model="channelId" placeholder="请选择渠道" filterable style="width:80%" clearable @change="channelData">
          <el-option v-for="item in channelOptions" :key="item.value" :label="item.text" :value="item.value" />
        </el-select>
      </div>
      <div v-show="channelId" style="font-size:15px">
        <span>选择服务器：</span>
        <div>
          <el-checkbox-group v-model="checkedServer" @change="getCheckedServer">
            <el-checkbox v-for="child in serverOptions" :key="child.id" :label="child.id" :vlaue="child.id">{{ child.text }}</el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
      <span slot="footer">
        <el-button size="medium" type="primary" @click="handleAddConfirm">确 定</el-button>
        <el-button size="medium" @click="closeDialog">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑弹窗 -->
    <el-dialog
      title="修改数据"
      :visible.sync="editDialogVisible"
      width="35%"
      :modal-append-to-body="false"
      center
      @close="closeDialog"
    >
      <el-form :model="formData" label-width="120px">
        <el-form-item label="关系ID">
          <el-input v-model.trim="formData.id" disabled />
        </el-form-item>
        <el-form-item label="渠道ID">
          <el-input v-model.trim="formData.channelId" disabled />
        </el-form-item>
        <el-form-item label="渠道名字">
          <el-input v-model.trim="formData.channelName" disabled />
        </el-form-item>
        <el-form-item label="服务器数据ID">
          <el-input v-model.trim="formData.serverUid" disabled />
        </el-form-item>
        <el-form-item label="服务器ID">
          <el-input v-model.trim="formData.serverId" disabled />
        </el-form-item>
        <el-form-item label="服务器名字">
          <el-input v-model.trim="formData.serverName" disabled />
        </el-form-item>
        <el-form-item label="选择归属大区">
          <el-select v-model="formData.regionId" placeholder="选择归属大区" filterable clearable>
            <el-option v-for="item in regionOptions" :key="item.value" :label="item.text" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否为推荐服务器">
          <el-select v-model="formData.recommend" disabled>
            <el-option :value="true" label="是" />
            <el-option :value="false" label="否" />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="medium" type="primary" @click="handleEditConfirm">确 定</el-button>
        <el-button size="medium" @click="closeDialog">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { gameChannelServer, addGameChannelServer, editGameChannelServer } from '@/api/serverManage'
import { channelOpt, channelServerOpt, gameRegionOpt } from '@/api/aboutChannel'

export default {
  data() {
    return {
      total: 0,
      currentPage: 1, // 当前页
      pageSize: 200, // 每页显示条数
      formData: {},
      channelServerData: [],
      tableHeight: 0, // 表格高度
      addDialogVisible: false, // 新增弹窗
      editDialogVisible: false, // 编辑弹窗
      channelId: '', // 添加数据里的渠道
      channelOptions: [], // 渠道选项
      serverOptions: [], // 服务器选项
      checkedServer: [], // 选中的服务器
      headerChannel: '', // 头部渠道
      currentEdit: '', // 当前编辑的渠道ID
      regionOptions: []// 归属大区的选项
    }
  },

  created() {
    this.getGameChannelServer()
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

  activated() {
    if (this.$refs.table && this.$refs.table.doLayout) {
      this.$refs.table.doLayout()
    }
  },

  methods: {
    // 获取数据
    async getGameChannelServer() {
      const res = await gameChannelServer({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.channelServerData = res.data.data.data
    },

    // 新增-确定
    async handleAddConfirm() {
      const res = await addGameChannelServer(
        { channelId: this.channelId,
          serverUids: this.checkedServer })
      if (res.data.code === 0) {
        this.$message.success(res.data.message)
      }
      this.getGameChannelServer()
      this.closeDialog()
    },

    // 编辑
    async handleEdit(data) {
      this.currentEdit = data.channelId
      this.formData = Object.assign({}, data)
      await this.getGameRegion()// 首次获取渠道大区选项
      this.editDialogVisible = true
    },

    // 编辑-确认
    async handleEditConfirm() {
      const newData = Object.assign({}, this.formData)
      const res = await editGameChannelServer({
        channelId: newData.channelId,
        serverUid: newData.serverUid,
        regionId: newData.regionId
      })
      if (res.data.code === 0) {
        this.$message.success(res.data.message)
      }

      this.headerChannel = newData.channelId
      this.searchBtn()

      this.closeDialog()
    },

    // 关闭弹窗
    closeDialog() {
      this.addDialogVisible = false
      this.channelId = ''

      this.editDialogVisible = false
      this.formData = {}
    },

    // 获取渠道
    async getChannel() {
      const res = await channelOpt()
      this.channelOptions = res.data.data.options
    },

    // 选择渠道
    async channelData(val) {
      this.channelId = val

      // 选中渠道后获取服务器
      if (this.channelId) {
        const res = await channelServerOpt({ channelId: val })
        this.serverOptions = res.data.data.datas
        this.checkedServer = this.serverOptions.filter(item => item.select).map(item => item.id)
      }
    },

    // 选中的服务器（复选框）
    getCheckedServer(val) {
      this.checkedServer = val
    },

    // 获取渠道大区选项
    async getGameRegion() {
      const res = await gameRegionOpt({ channelId: this.currentEdit })
      this.regionOptions = res.data.data.options
    },

    // 搜索按钮
    async searchBtn() {
      const query = { page: this.currentPage, limit: this.pageSize }
      if (this.headerChannel) {
        query.channelId = this.headerChannel
      }
      const result = await gameChannelServer(query)
      this.total = result.data.data.count
      this.channelServerData = result.data.data.data
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getGameChannelServer()
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
//弹窗
::v-deep .el-select{
  width: 100%;
}
::v-deep .el-form .el-input__inner{
  border-radius: 0 4px 4px 0;
}

.el-checkbox-group{
  margin-top: 20px;
}

.header{
  display: flex;
  font-size: 15px;
  font-weight: bold;
  color: #6a7488;
.ditch{
  margin: 0 20px;
}
}

</style>
