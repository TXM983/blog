<template>
  <div class="login-container">
    <video ref="videoPlayer" class="fullscreen-video" autoplay loop muted >
      <source src="https://blog-txm.oss-cn-beijing.aliyuncs.com/aurora/video/Ori%20DE%20Silent.mp4" type="video/mp4">
    </video>
    <div class="login-card">
      <div class="login-title">管理员登录</div>
      <el-form status-icon :model="loginForm" ref="ruleForm" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            prefix-icon="el-icon-user-solid"
            placeholder="用户名"
            class="custom-input"
            @keyup.enter.native="login" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            prefix-icon="iconfont el-icon-mymima"
            show-password
            placeholder="密码"
            class="custom-input"
            @keyup.enter.native="login" />
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="login" round >登录</el-button>
    </div>
  </div>
</template>

<script>
import { generaMenu } from '@/assets/js/menu'
export default {
  data: function () {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
        password: [{ required: true, message: '密码不能为空', trigger: 'blur' }]
      },
    }
  },
  methods: {
    login() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          const that = this
          let param = new URLSearchParams()
          param.append('username', that.loginForm.username)
          param.append('password', that.loginForm.password)
          that.axios.post('/api/users/login', param).then(({ data }) => {
            if (data.flag) {
              that.$store.commit('login', data.data)
              generaMenu()
              that.$message.success('登录成功')
              that.$router.push({ path: '/' })
            } else {
              that.$message.error(data.message)
            }
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  position: relative;
  height: 100vh;
  overflow: hidden; /* 隐藏溢出的部分，防止出现滚动条 */
}

.custom-input >>> .el-input__inner{
  border: 0;
  box-shadow: 0 0 0 0;
}

.fullscreen-video {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 保持视频比例并覆盖整个容器 */
}

.login-card {
  position: absolute;
  top: 50%;
  left: 80%;
  transform: translate(-50%, -50%);
  background: rgba(255, 255, 255, 0.8); /* 使用透明背景，让视频透过 */
  padding: 2rem;
  width: 350px;
  height: 280px;
  text-align: center;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3); /* 添加一些阴影效果 */
  border-radius: 10px;
}
.login-title {
  color: #303133;
  font-weight: bold;
  font-size: 1.5rem;
  margin-bottom: 40px;
}
.login-form {
  margin-top: 1.2rem;
}
.login-card button {
  margin-top: 1rem;
  width: 100%;
}
</style>
