<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-form-container">
        <h2 class="login-title">医院床位预约管理系统</h2>
        <el-form ref="loginForm" :model="formData" :rules="rules" class="login-form">
          <el-form-item prop="UserName">
            <el-input v-model="formData.UserName" placeholder="请输入账号" />
          </el-form-item>

          <el-form-item prop="Password">
            <el-input v-model="formData.Password" type="password" show-password placeholder="请输入密码" />
          </el-form-item>

          <el-form-item prop="Code">
            <div class="code-container">
              <el-input v-model="formData.Code" placeholder="请输入验证码" />
              <div class="code-image">
                <ImageCode ref="validCodeRef" />
              </div>
            </div>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" class="login-button" @click="loginBtn">
              登录
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-options">
          <div class="register-link">
            <span>如果没有账号可以</span>
            <router-link :to="{ path: '/Register' }">
              <span class="link-text">立即注册</span>
            </router-link>
          </div>
          <div class="forget-link">
            <router-link :to="{ path: '/ForgetPassword' }">
              <span class="link-text">忘记密码</span>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import ImageCode from '@/components/Identifyingcode/ImageCode.vue'
import { useCommonStore } from '@/store'
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const commonStore = useCommonStore()
const loginForm = ref(null)
const validCodeRef = ref(null)

const formData = reactive({
  UserName: '',
  Password: '',
  Code: ''
})

const rules = reactive({
  UserName: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度应在3到20个字符之间', trigger: 'blur' }
  ],
  Password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  Code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const identifyCode = validCodeRef.value.getCode()
        if (value !== identifyCode) {
          callback(new Error('请输入正确的验证码'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
})

const loginBtn = () => {
  loginForm.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.error('登录验证不通过')
      return false
    }

    try {
      const { Success } = await commonStore.Login(formData)

      if (Success) {
        await commonStore.GetInfo()
        ElMessage.success('登录成功')

        if (commonStore.UserInfo && commonStore.UserInfo.RoleType === 1) {
          router.push({ path: '/Admin' })
        } else {
          router.push({ path: '/Front/Reservation' })
        }
      }
    } catch (error) {
      console.error('登录失败', error)
    }
  })
}
</script>

<style scoped lang="scss">
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #e6f4ff 0%, #b3d9ff 50%, #91caff 100%);
}

.login-box {
  width: 450px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  padding: 50px 40px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-form-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.login-title {
  font-size: 28px;
  color: #1890ff;
  margin-bottom: 40px;
  text-align: center;
  font-weight: 600;
  letter-spacing: 2px;
}

.login-form {
  width: 100%;
  margin-top: 20px;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 24px;
}

.login-form :deep(.el-form-item__label) {
  display: none;
}

.login-form :deep(.el-form-item__content) {
  margin-left: 0 !important;
  display: flex;
  justify-content: center;
}

.login-form :deep(.el-input) {
  width: 100%;
}

.login-form :deep(.el-select) {
  width: 100%;
}

.code-container {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
}

.code-image {
  flex-shrink: 0;
}

.login-button {
  width: 100%;
  height: 40px;
  font-size: 16px;
  margin-top: 10px;
}

.login-options {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  font-size: 14px;
  color: #606266;
  width: 100%;
}

.link-text {
  color: #1890ff;
  margin-left: 5px;
  cursor: pointer;
  transition: color 0.3s;

  &:hover {
    color: #40a9ff;
  }
}

@media (max-width: 768px) {
  .login-box {
    width: 90%;
    padding: 30px 20px;
  }

  .login-title {
    font-size: 24px;
    margin-bottom: 30px;
  }
}
</style>
