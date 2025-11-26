<!-- 单选对话框 -->
<template>
  <div>
    <el-dialog
      :visible="visible"
      title="选择服务器"
      width="48%"
      :modal-append-to-body="false"
      center
      @close="closeDialog"
    >
      <el-radio-group v-model="checkedServer">
        <el-radio
          v-for="item in serverOptions"
          :key="item.value"
          :label="item.value"
          :vlaue="item.value"
        >{{ item.text }}</el-radio>
      </el-radio-group>
      <span slot="footer">
        <el-button type="primary" size="small" @click="serverConfirm">确 认</el-button>
        <el-button size="small" @click="closeDialog">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    visible: Boolean,
    serverOptions: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      checkedServer: '' // 选中的服务器
    }
  },
  methods: {
    // 确认按钮
    serverConfirm() {
      this.$emit('checkedId', this.checkedServer)
      this.closeDialog()
    },
    // 关闭弹窗
    closeDialog() {
      this.$emit('update:visible', false)
    }
  }
}
</script>
