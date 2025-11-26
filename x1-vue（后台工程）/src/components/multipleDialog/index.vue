<!-- 多选对话框 -->
<template>
  <div>
    <el-dialog
      title="选择服务器"
      :visible="visible"
      width="48%"
      :modal-append-to-body="false"
      center
      @close="handleCloseDialog"
    >
      <el-button-group>
        <el-button
          size="mini"
          plain
          type="primary"
          @click="handleSelectAll"
        >全选</el-button>
        <el-button
          size="mini"
          plain
          type="primary"
          @click="handleDeselectAll"
        >全不选</el-button>
      </el-button-group>

      <div style="margin: 30px 0" />

      <el-checkbox-group v-model="checkedServer" @change="handleCheckedId">
        <el-checkbox
          v-for="item in serverOptions"
          :key="item.value"
          :label="item.value"
          :vlaue="item.value"
        >{{ item.text }}</el-checkbox>
      </el-checkbox-group>
      <template #footer>
        <el-button type="primary" size="small" @click="handleConfirm">确 认</el-button>
        <el-button size="small" @click="handleCloseDialog">取 消</el-button>
      </template>
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
      checkedServer: [] // 选中的服务器
    }
  },
  methods: {
    // 全选
    handleSelectAll() {
      this.checkedServer = this.serverOptions.map((item) => item.value)
    },

    // 全不选
    handleDeselectAll() {
      this.checkedServer = []
    },

    // 选中的服务器ID
    handleCheckedId(val) {
      this.checkedServer = val
    },

    // 确认按钮
    handleConfirm() {
      this.$emit('checkedId', this.checkedServer)
    },

    // 关闭弹窗
    handleCloseDialog() {
      this.$emit('update:visible', false)
    }
  }

}
</script>
