<!-- 37互娱发放礼包配置 -->
<template>
  <div>
    <el-card>
      <div>
        <el-button
          type="primary"
          icon="el-icon-circle-plus-outline"
          size="small"
          @click="dialogVisible = true"
        >添加礼包</el-button>

      </div>

      <div>
        <!-- 表格 -->
        <el-table
          ref="table"
          border
          :data="sanQiGifData"
          :header-cell-style="{background:'#f5f7fa'}"
          :height="tableHeight"
        >
          <el-table-column prop="id" label="礼包ID" />
          <el-table-column prop="rewards" label="奖励">
            <template #default="{row}">
              <span v-for="(item,index) in row.rewards" :key="index">
                {{ item.name }}*{{ item.num }}{{ index !== row.rewards.length - 1 ? '，' : '' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" />
          <el-table-column fixed="right" label="操作" width="130">
            <template #default="{row}">
              <el-button
                icon="el-icon-delete"
                size="small"
                type="danger"
                plain
                @click="handleDelete(row.id)"
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

    <el-dialog
      title="新增奖励"
      :visible.sync="dialogVisible"
      :modal-append-to-body="false"
      width="40%"
      center
      @close="closeDialog"
    >
      <div class="dialog-content">
        <el-form ref="ruleForm" :model="formData" label-width="100px" :rules="rules">
          <el-form-item label="备注">
            <el-input v-model="formData.remark" placeholder="请输入" clearable class="remarkInput" />
          </el-form-item>

          <el-form-item
            label="奖励"
            prop="rewards"
          >
            <el-button
              style="border-radius:0px 4px 4px 0px;width:100%"
              type="primary"
              plain
              @click="addRewards"
            >添加</el-button>

            <div v-for="(item,index) in formData.rewards" :key="index" class="props">
              <el-form-item
                class="item"
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

              <el-form-item
                class="num"
                label="数量："
                :prop="`rewards[${index}].num`"
                :rules="[ { required: true, message: '数量不能为空'},{ type: 'number', message: '数量必须为数字值'}]"
                label-width="60px"
              >
                <el-input v-model.number="item.num" size="small" clearable placeholder="请输入" />
              </el-form-item>

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
        </el-form>
      </div>
      <span slot="footer">
        <el-button size="medium" type="primary" @click="handleConfirm">确 定</el-button>
        <el-button size="medium" @click="closeDialog">取 消</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { sanQiHuYuGiftCfg, saveSanQiHuYuGiftCfg, deleteSanQiHuYuGiftCfg } from '@/api/aboutSanQi'
import { propsOpt } from '@/api/specialOperation'

export default {
  data() {
    return {
      sanQiGifData: [],
      total: 0,
      currentPage: 1,
      pageSize: 200,
      tableHeight: 0,
      dialogVisible: false,
      formData: {
        rewards: []
      },
      prop0ption: [],
      rules: {
        rewards: [{ validator: this.validateAdd }]
      }
    }
  },

  created() {
    this.getSanQiGifCfg()
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
    async getSanQiGifCfg() {
      const res = await sanQiHuYuGiftCfg({
        page: this.currentPage,
        limit: this.pageSize
      })
      this.total = res.data.data.count
      this.sanQiGifData = res.data.data.data
    },

    // 获取道具数据
    async getProps() {
      const res = await propsOpt()
      this.prop0ption = res.data.data.options
    },

    // 添加奖励
    async addRewards() {
      this.formData.rewards.push({ item: '', num: '' })
      this.$refs.ruleForm.clearValidate()
    },

    // 删除奖励
    delRewards(index) {
      this.formData.rewards.splice(index, 1)
    },

    // 确认
    async handleConfirm() {
      await this.$refs.ruleForm.validate(async(valid) => {
        if (valid) {
          const rewards = JSON.stringify(this.formData.rewards)
          const addData = { ...this.formData, rewards }
          const res = await saveSanQiHuYuGiftCfg(addData)
          if (res.data.code === 0) {
            this.$message.success(res.data.message)
          }
          this.getSanQiGifCfg()
          this.closeDialog()
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
        const res = await deleteSanQiHuYuGiftCfg(id)
        if (res.data.code === 0) {
          this.$message.success(res.data.message)
        }
        this.getSanQiGifCfg()
      })
    },

    // 校验是否添加奖励
    validateAdd(rule, value, callback) {
      if (this.formData.rewards.length !== 0) {
        callback()
      } else {
        callback(new Error('请添加奖励'))
      }
    },

    // 关闭弹窗
    closeDialog() {
      this.formData = { rewards: [] }
      this.dialogVisible = false
      this.$refs.ruleForm.clearValidate()
    },

    // 切换页面
    changePage(page) {
      this.currentPage = page
      this.getSanQiGifCfg()
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

::v-deep{
  .el-dialog--center .el-dialog__body{
    padding:  0 10px;
  }
}

.dialog-content{
 max-height: 60vh;
 overflow-y: auto;
 overflow-x: hidden;
 padding: 20px;

 .props{
    display: flex;
    width: 100%;
  .item{
    width: 50%;
  }
  .num{
    margin-left: 30px;
    width: 50%;
  }
}

.props:not(:last-child) {
  margin-bottom: 15px;
}

}

::v-deep .dialog-content .props .el-input__inner{
  border-radius: 4px;
}

::v-deep .props .el-form-item__label{
 background-color: #fff;
 border: none;
 height: none;
}

::v-deep .el-form .el-input__inner{
  border-radius: 0 4px 4px 0;
}
</style>

