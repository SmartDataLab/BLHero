<template>
  <div class="wscn-http500-container">
    <div class="wscn-http500">
      <div class="pic-500">
        <img class="pic-500__parent" src="@/assets/500_images/500.svg">
      </div>
      <div class="bullshit">
        <div class="bullshit__oops">500错误！</div>
        <div class="bullshit__headline">{{ message }}</div>
        <div class="bullshit__info">
          此错误意味着您正在访问的网站出现了服务器问题，此问题阻止了该网页的显示。您可以尝试返回登录页重新登录或者询问后端。
        </div>
        <a to="" class="bullshit__return-login" @click="returnToLogin">重新登录</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Page500',
  computed: {
    message() {
      return '服务器出错了...'
    }
  },
  methods: {
    async returnToLogin() {
      await this.$store.dispatch('user/logout')
      this.$router.push('/login')
    }
  }
}
</script>

<style lang="scss" scoped>
.wscn-http500-container {
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.wscn-http500 {
  position: relative;
  width: 1200px;
  padding: 0 50px;
  overflow: hidden;

  .pic-500 {
    position: relative;
    float: left;
    width: 600px;
    overflow: hidden;

    &__parent {
      width: 80%;
    }
  }

  .bullshit {
    position: relative;
    float: left;
    width: 300px;
    padding: 30px 0;
    overflow: hidden;

    &__oops {
      margin-bottom: 20px;
      font-size: 32px;
      font-weight: bold;
      line-height: 40px;
      color: #1482f0;
      opacity: 0;
      animation-name: slideUp;
      animation-duration: 0.5s;
      animation-fill-mode: forwards;
    }

    &__headline {
      margin-bottom: 10px;
      font-size: 20px;
      font-weight: bold;
      line-height: 24px;
      color: #222;
      opacity: 0;
      animation-name: slideUp;
      animation-duration: 0.5s;
      animation-delay: 0.1s;
      animation-fill-mode: forwards;
    }

    &__info {
      margin-bottom: 30px;
      font-size: 13px;
      line-height: 21px;
      color: grey;
      opacity: 0;
      animation-name: slideUp;
      animation-duration: 0.5s;
      animation-delay: 0.2s;
      animation-fill-mode: forwards;
    }

    &__return-login {
      display: block;
      float: left;
      width: 110px;
      height: 36px;
      font-size: 14px;
      line-height: 36px;
      color: #fff;
      text-align: center;
      cursor: pointer;
      background: #409eff;
      border-radius: 100px;
      opacity: 0;
      animation-name: slideUp;
      animation-duration: 0.5s;
      animation-delay: 0.3s;
      animation-fill-mode: forwards;
    }

    @keyframes slideUp {
      0% {
        opacity: 0;
        transform: translateY(60px);
      }

      100% {
        opacity: 1;
        transform: translateY(0);
      }
    }
  }
}
</style>
