<!-- 服务器维护管理 -->
<template>
  <div class="card">
    <div class="handle">
      <div>
        <span>平台：</span>
        <el-select v-model="platform" clearable filterable size="small">
          <el-option
            v-for="item in platformList"
            :key="item.value"
            :label="item.text"
            :value="item.value"
          />
        </el-select>
      </div>
      <el-button type="primary" size="small" icon="el-icon-search" plain @click="handleInquire">查询</el-button>
      <div>
        <el-button type="success" size="small" icon="el-icon-check" @click="sendMaintain">发送维护</el-button>
        <el-button type="primary" size="small" icon="el-icon-refresh" @click="againSendMaintain">重发维护</el-button>
        <el-button type="danger" size="small" icon="el-icon-close" @click="overMaintain">结束维护</el-button>
        <el-button type="warning" size="small" icon="el-icon-refresh" @click="againOverMaintain">重发结束维护</el-button>
      </div>
    </div>

    <div v-if="isShow" class="content" :style="{height: contentHeight + 'px'}">
      <div class="statements">说明：服务器【维护状态】【服务器响应状态】 ——
        维护状态：绿【维护中】，红【非维护中】——
        服务器响应状态：绿【服务器已响应维护】，红【服务器未响应维护】</div>

      <div v-for="(chunk,index) in serverList" :key="index">
        <el-checkbox v-model="chunk.isCheck" :indeterminate="chunk.isIndeterminate" @change="handleCheckAllChange(chunk)">全选</el-checkbox>
        <el-checkbox-group v-model="chunk.checkIds" @change="getCheckedIds(chunk)">
          <el-checkbox
            v-for="item in chunk.serverGroup"
            :key="item.name"
            :label="item.serverId"
          >{{ item.name }} ({{ item.serverId }})

            <span v-if="item.maintain" class="state_green" />
            <span v-else class="state_red" />
            <span v-if="item.maintainResponse" class="state_green" />
            <span v-else class="state_red" />

          </el-checkbox>
        </el-checkbox-group>
        <el-divider v-if="serverList.length!==(index+1)" />
      </div>

    </div>

    <div v-else class="image">
      <el-empty :image-size="230" />
    </div>
  </div>
</template>

<script>
import { serverMaintain, conductMaintain, resendMaintain,
  endMaintain, resendEndMaintain, platformOptions } from '@/api/serverManage'

export default {
  data() {
    return {
      contentHeight: 0,
      platform: '',
      platformList: [], // 平台列表
      checkServerIds: [], // 选中的服务器id集合
      maintainList: [], // 服务器维护列表
      isShow: false
    }
  },

  computed: {

    // 分组
    serverList() {
      const serverNum = 50
      const serverGroup = []
      for (let i = 0; i < this.maintainList.length; i += serverNum) {
        const obj = {
          isCheck: false,
          isIndeterminate: false,
          checkIds: [],
          serverGroup: this.maintainList.slice(i, i + serverNum)
        }
        serverGroup.push(obj)
      }
      return serverGroup
    }
  },

  mounted() {
    this.$nextTick(() => {
      this.getContentHeight()
    })
    window.addEventListener('resize', this.$debounce(this.getContentHeight, 100))
  },

  beforeDestroy() {
    window.removeEventListener('resize', this.getContentHeight)
  },

  created() {
    this.getPlatformOpt()
  },

  methods: {

    // 选中的服务器ID
    getCheckedIds(data) {
      const checkedCount = data.checkIds.length
      data.isIndeterminate = checkedCount > 0 && checkedCount < data.serverGroup.length
      this.$forceUpdate()
      this.checkServerIds = this.getCheckAllIds()
    },

    // 全选
    handleCheckAllChange(data) {
      data.checkIds = data.isCheck ? data.serverGroup.map((item) => item.serverId) : []
      data.isIndeterminate = false
      this.$forceUpdate()
      this.checkServerIds = this.getCheckAllIds()
    },

    // 获取全部选中
    getCheckAllIds() {
      let ids = []
      this.serverList.forEach(i => {
        ids = [...ids, ...i.checkIds]
      })
      return ids
    },

    // 获取平台选项
    async getPlatformOpt() {
      const res = await platformOptions()
      this.platformList = res.data.data.options
    },

    // 查询
    async handleInquire() {
      if (!this.platform) {
        this.$message.error('请选择平台')
      } else {
        const res = await serverMaintain({ platformId: this.platform })
        this.maintainList = res.data.data.data.sort((a, b) => a.value - b.value)
        this.isShow = true
      }
    },

    // 发送维护
    async sendMaintain() {
      if (this.checkServerIds.length === 0) {
        this.$message.error('请选择需要维护的服务器')
      } else {
        const res = await conductMaintain({
          platformId: this.platform,
          serverIds: this.checkServerIds
        })
        if (res.data.code === 0) {
          this.$message.success('发送维护成功')
          this.checkServerIds = []
        }
        this.handleInquire()
      }
    },

    // 重发维护
    async againSendMaintain() {
      if (this.checkServerIds.length === 0) {
        this.$message.error('请选择需要重发维护的服务器')
      } else {
        const res = await resendMaintain({
          platformId: this.platform,
          serverIds: this.checkServerIds
        })
        if (res.data.code === 0) {
          this.$message.success('重发维护成功')
          this.checkServerIds = []
        }
        this.handleInquire()
      }
    },

    // 结束维护
    async overMaintain() {
      if (this.checkServerIds.length === 0) {
        this.$message.error('请选择需要结束维护的服务器')
      } else {
        const res = await endMaintain({
          platformId: this.platform,
          serverIds: this.checkServerIds
        })
        if (res.data.code === 0) {
          this.$message.success('结束维护成功')
          this.checkServerIds = []
        }
        this.handleInquire()
      }
    },

    // 重发结束维护
    async againOverMaintain() {
      if (this.checkServerIds.length === 0) {
        this.$message.error('请选择需要重发结束维护的服务器')
      } else {
        const res = await resendEndMaintain({
          platformId: this.platform,
          serverIds: this.checkServerIds
        })
        if (res.data.code === 0) {
          this.$message.success('重发结束维护成功')
          this.checkServerIds = []
        }
        this.handleInquire()
      }
    },

    // 内容自适应高度
    getContentHeight() {
      this.contentHeight = window.innerHeight - 220
    }

  }
}
</script>

<style lang="scss">
.card{
  padding: 30px;

  .handle{
    margin-bottom: 30px;
    display: flex;
    flex-wrap: wrap;
    font-weight: bold;
    font-size: 14px;
    color: #6a7488;
    gap: 30px;
  }

  .content{
    overflow: auto;
    border: 1px solid #dcdfe6;
    padding: 30px;
  }

  .el-checkbox{
    margin: 12px;
  }

  .statements{
    color: #ee3c3c;
    font-weight: bold;
    font-size: 14px;
    margin-bottom: 20px;
    margin-left: 10px;
  }

  .state_red{
    display: inline-block;
    width: 6px;
    height: 6px;
    border-radius:100%;
    background: #fe5250;
    box-shadow: 0 0 0 2px white,0 0 0 3px #fe5250;
    margin-left: 10px;
  }

  .state_green{
    display: inline-block;
    width: 6px;
    height: 6px;
    border-radius:100%;
    background: #26bf3a;
    box-shadow: 0 0 0 2px white,0 0 0 3px #26bf3a;
    margin-left: 10px;
  }

  // .state_grey{
  //   display: inline-block;
  //   width: 6px;
  //   height: 6px;
  //   border-radius:100%;
  //   background: #c1c1c1;
  //   box-shadow: 0 0 0 2px white,0 0 0 3px #c1c1c1;
  //   margin-left: 10px;
  // }

  .image{
    padding-top: 100px;
  }

}

</style>
