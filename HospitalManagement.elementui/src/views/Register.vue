<template>
  <div class="register-container">
    <div class="register-box">
      <div class="register-form-container">
        <h2 class="register-title">医院床位预约管理系统</h2>
        <el-form ref="registerForm" :model="formData" :rules="rules" class="register-form">
          <el-form-item prop="UserName">
            <el-input v-model="formData.UserName" placeholder="请输入账号" />
          </el-form-item>

          <el-form-item prop="Password">
            <el-input v-model="formData.Password" type="password" show-password placeholder="请输入密码" />
          </el-form-item>

          <el-form-item prop="Email">
            <el-input v-model="formData.Email" placeholder="请输入邮箱" />
          </el-form-item>

          <el-form-item prop="PhoneNumber">
            <el-input v-model="formData.PhoneNumber" placeholder="请输入手机号" />
          </el-form-item>

          <el-form-item prop="Name">
            <el-input v-model="formData.Name" placeholder="请输入姓名" />
          </el-form-item>

          <el-form-item prop="Code">
            <div class="code-container">
              <el-input v-model="formData.Code" placeholder="请输入验证码" />
              <div class="code-image">
                <ValidCode ref="validCodeRef" />
              </div>
            </div>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" class="register-button" @click="registerBtn">
              注册
            </el-button>
          </el-form-item>
        </el-form>

        <div class="register-options">
          <div class="login-link">
            <span>如果已有账号可以</span>
            <router-link :to="{ path: '/Login' }">
              <span class="link-text">去登录</span>
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
import { Post } from '@/api/http'
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import ValidCode from '../components/Identifyingcode/ImageCode.vue'

const router = useRouter()
const registerForm = ref(null)
const validCodeRef = ref(null)

const formData = reactive({
  UserName: '',
  Password: '',
  PhoneNumber: '',
  RoleType: '2',
  Email: '',
  Name: '',
  Code: ''
})

const rules = reactive({
  UserName: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度应在3到20个字符之间', trigger: 'blur' }
  ],
  Password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const reg = /^.{8,16}$/
        if (!value || !reg.test(value)) {
          callback(new Error('请输入8-16位密码'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  Email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    {
      type: 'email',
      message: '请输入正确的邮箱格式',
      trigger: 'blur'
    },
    {
      validator: (rule, value, callback) => {
        const emailRegex = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
        if (!emailRegex.test(value)) {
          callback(new Error('邮箱格式不正确，请输入有效的邮箱地址'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  Name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度应在2到20个字符之间', trigger: 'blur' }
  ],
  PhoneNumber: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { min: 11, max: 11, message: '手机号长度应为11位', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const phoneRegex = /^1[0-9]\d{9}$/
        if (!phoneRegex.test(value)) {
          callback(new Error('请输入正确的手机号'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
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

const registerBtn = () => {
  registerForm.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.error('注册验证不通过')
      validCodeRef.value.refreshCode()
      return false
    }

    try {
      const { Success } = await Post('/User/Register', formData)
      if (Success) {
        ElMessage.success('注册成功')
        router.push({
          path: '/Login'
        })
      }
    } catch (error) {
      console.error('注册失败', error)
      validCodeRef.value.refreshCode()
    }
  })
}
</script>

<style scoped lang="scss">
.register-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #e6f4ff 0%, #b3d9ff 50%, #91caff 100%);
}

.register-box {
  width: 450px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  padding: 50px 40px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.register-form-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.register-title {
  font-size: 28px;
  color: #1890ff;
  margin-bottom: 40px;
  text-align: center;
  font-weight: 600;
  letter-spacing: 2px;
}

.register-form {
  width: 100%;
  margin-top: 20px;
}

.register-form :deep(.el-form-item) {
  margin-bottom: 24px;
}

.register-form :deep(.el-form-item__label) {
  display: none;
}

.register-form :deep(.el-form-item__content) {
  margin-left: 0 !important;
  display: flex;
  justify-content: center;
}

.register-form :deep(.el-input) {
  width: 100%;
}

.register-form :deep(.el-select) {
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

.register-button {
  width: 100%;
  height: 40px;
  font-size: 16px;
  margin-top: 10px;
}

.register-options {
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
  .register-box {
    width: 90%;
    padding: 30px 20px;
  }

  .register-title {
    font-size: 24px;
    margin-bottom: 30px;
  }
}
</style>
