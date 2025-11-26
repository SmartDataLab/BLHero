<template>
  <div>
    <div class="navbar">
      <!-- 切换渠道 -->
      <el-dropdown trigger="click">
        <el-button type="text" class="el-dropdown-link">
          切换渠道<i class="el-icon-arrow-down el-icon--right" />
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item v-for="(item,index) in channelOption" :key="index" @click.native="channelSelect(item)">{{ item.text }}</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

      <div class="selected-channel">{{ channelName }}</div>

      <!-- 用户名及退出 -->
      <el-dropdown trigger="click">
        <el-button type="text" class="user-action">
          <i class="el-icon-user-solid" />
          <span>{{ getUsername() }}</span>
          <i class="el-icon-arrow-down el-icon--right" />
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="dialogVisible = true">修改密码</el-dropdown-item>
          <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <!-- 弹窗 -->
    <div class="dialog">
      <el-dialog title="修改密码" :visible.sync="dialogVisible" width="30%" center @close="closeDialog">
        <el-form ref="ruleForm" :model="ruleForm" :rules="rules" status-icon label-width="120px" class="demo-ruleForm">
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input v-model="ruleForm.oldPassword" type="password" />
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="ruleForm.newPassword" type="password" />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="ruleForm.confirmPassword" type="password" placeholder="确认密码与新密码保持一致" />
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button size="medium" type="primary" @click="confirmBtn">确 定</el-button>
          <el-button size="medium" @click="closeDialog">取 消</el-button>
        </span>
      </el-dialog>
    </div>

  </div>

</template>

<script>
import { mapGetters } from 'vuex'
import { channelOpt, changeChannel } from '@/api/aboutChannel'
import { Base64 } from 'js-base64'
import { changePasssword } from '@/api/user'

export default {
  data() {
    return {
      channel: '', // 渠道
      channelName: '',
      channelOption: [], // 渠道选项
      dialogVisible: false,
      ruleForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      // 校验条件
      rules: {
        oldPassword: [{ validator: this.validateOldPass, trigger: ['blur', 'change'] }],
        newPassword: [{ validator: this.validateNewPass, trigger: ['blur', 'change'] }],
        confirmPassword: [{ validator: this.validatePass, trigger: ['blur', 'change'] }]
      }
    }
  },

  computed: {
    ...mapGetters([
      'sidebar',
      'avatar'
    ])
  },

  created() {
    this.getChannel().then(() => {
      if (this.channelOption.length > 0) {
        this.channel = localStorage.getItem('selectedChannel') || this.channelOption[0].value
        this.channelName = this.channelOption.find(item => item.value == this.channel)?.text
        changeChannel({ channelId: this.channel })
      }
    })
  },

  methods: {
    // 获取用户名
    getUsername() {
      return localStorage.getItem('username')
    },

    // 退出-按钮
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push('/login')

      // 清除用户名及密码
      localStorage.removeItem('username')
      localStorage.removeItem('password')
    },

    // 校验旧密码
    validateOldPass(rule, value, callback) {
      if (value === '') {
        callback(new Error('请输入旧密码'))
      } else {
        const oldPsw = Base64.decode(localStorage.getItem('password'))
        if (this.ruleForm.oldPassword !== oldPsw) {
          callback(new Error('当前输入的密码与旧密码不一致'))
        }
        callback()
      }
    },

    // 校验新密码
    validateNewPass(rule, value, callback) {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else {
        if (this.ruleForm.confirmPassword !== '') {
          this.$refs.ruleForm.validateField('confirmPassword')
        }
        callback()
      }
    },

    // 校验确认密码
    validatePass(rule, value, callback) {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.ruleForm.newPassword) {
        callback(new Error('两次输入的密码不一致!'))
      } else {
        callback()
      }
    },

    // 确认按钮
    async confirmBtn() {
      await this.$refs.ruleForm.validate(async(valid) => {
        if (valid) {
          const res = await changePasssword(this.ruleForm)
          if (res.data.code === 0) {
            this.logout()
            this.$message({
              type: 'success',
              message: '密码修改成功，请使用新密码重新登录。',
              duration: 4000
            })
          }
          this.closeDialog()
        }
      })
    },

    // 关闭主弹窗
    closeDialog() {
      this.ruleForm = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
      this.$refs.ruleForm.clearValidate()
      this.dialogVisible = false
    },

    // 渠道选择项
    async getChannel() {
      const res = await channelOpt()
      this.channelOption = res.data.data.options
    },

    // 渠道选中
    async channelSelect(item) {
      this.channel = item.value
      this.channelName = item.text
      await changeChannel({ channelId: this.channel })

      // 更新渠道名称并保存到 localStorage
      this.channelName = this.channelOption.find(channel => channel.value == this.channel).text
      localStorage.setItem('selectedChannel', this.channel)

      location.reload() // 刷新整个页面
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  display: flex;
  height: 65px;
  line-height:65px;
}

.dialog{
  line-height: 0;
}

.user-action{
 font-size: 15px;
 color: #fff;
 padding: 0 10px;
 .el-icon--right{
  font-size: 12px;
  color: #d3dce6;
 }
 .el-icon-user-solid{
  padding:5px;
  color: #fff;
  background-color:#d3dce67a;
  border-radius: 50%;
 }
}

.el-dropdown-link {
   color: #409EFF;
   font-size: 15px;
   padding: 0 5px;
   .el-icon--right{
  font-size: 12px;
}
}

.selected-channel{
  color: #fff;
  font-size: 15px;
  padding: 0 15px ;
}

.el-dropdown-menu{
  z-index: 3000 !important;
  background:#23262f;
  top: 54px !important;
  border-radius: 3px;

}

.el-dropdown-menu__item{
  color: #fff;
  min-width: 140px;
  &:hover{
    background:#545c64;
    border-radius: 0;
  }
}

::v-deep .el-input__inner{
  border-radius: 0 4px 4px 0;
}
</style>

<style>
/* 消除小三角 */
.el-popper[x-placement^=bottom] .popper__arrow{
  border: none;
}
.el-popper[x-placement^=bottom] .popper__arrow::after{
  border: none;
}
</style>
