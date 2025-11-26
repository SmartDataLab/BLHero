<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">

      <div class="title-container">
        <h3 class="title">X1中央后台管理系统</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="账户"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="密码"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <!-- 记住密码 -->
      <el-checkbox label="记住密码" :checked="isChecked" @change="rememberpsd" />

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin" @keyup.enter.native="handleLogin">登录</el-button>

    </el-form>
  </div>
</template>

<script>
import { Base64 } from 'js-base64'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, message: '请输入账户', trigger: ['blur', 'change'] }],
        password: [
          { required: true, message: '请输入密码', trigger: ['blur', 'change'] }
          // { min: 6, max: 12, message: '密码长度必须为6至12位', trigger: 'blur' }
        ]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
      isChecked: false
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },

  created() {
    this.getCookie()
  },

  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },

    rememberpsd(e) {
      this.isChecked = e
    },

    // 保存账号密码加密至cookie
    setCookie(cname, cpwd, cdays) {
      // 获取当前时间
      const curDate = new Date()
      // 设置7天过期时间
      curDate.setTime(curDate.getTime() + 24 * 60 * 60 * 1000 * cdays)
      // 账号密码加密
      const codeName = Base64.encode(cname)
      const codePwd = Base64.encode(cpwd)

      // 字符串拼接cookie
      window.document.cookie = 'username' + '=' + codeName + ';Expires=' + curDate.toGMTString()
      window.document.cookie = 'password' + '=' + codePwd + ';Expires=' + curDate.toGMTString()
      window.document.cookie = 'isChecked' + '=' + this.isChecked + ';Expires=' + curDate.toGMTString()
    },

    // 获取cookie解密后的数据
    getCookie() {
      if (document.cookie.length > 0) {
        const arr = document.cookie.split('; ')

        for (let i = 0; i < arr.length; i++) {
          const arr2 = arr[i].split('=')

          if (arr2[0] === 'username') {
            // 账号解密赋值
            this.loginForm.username = Base64.decode(arr2[1])
          } else if (arr2[0] === 'password') {
            // 密码解密赋值
            this.loginForm.password = Base64.decode(arr2[1])
          } else if (arr2[0] === 'isChecked') {
          // 获取复选框状态
            this.isChecked = JSON.parse(arr2[1])
          }
        }
      }
    },

    // 清除cookie
    clearCookie() {
      this.setCookie('', '', -1)
    },

    // 登录-按钮
    async handleLogin() {
      await this.$refs.loginForm.validate(async(valid) => {
        if (valid) {
          // 存储用户名用于显示在页面右上方
          localStorage.setItem('username', this.loginForm.username)
          localStorage.setItem('password', Base64.encode(this.loginForm.password))

          // 记住密码是否为选中状态
          if (this.isChecked) {
            this.setCookie(this.loginForm.username, this.loginForm.password, 7)
          } else {
            this.clearCookie()
          }

          // 调用actions中的函数，实现登录
          await this.$store.dispatch('user/Newlogin', this.loginForm)

          // 获取路由
          await this.$store.dispatch('router/GenerateRoutes', this.loginForm)

          // 跳转到首页
          this.$router.push('/')
        } else {
          console.log('账户或者密码校验未通过')
          return false
        }
      })
    }
  }

}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {

  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      // -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}

.el-checkbox{
  margin: 0px 0px 20px 4px
}
</style>
