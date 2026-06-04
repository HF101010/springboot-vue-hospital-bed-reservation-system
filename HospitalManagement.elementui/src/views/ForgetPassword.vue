<template>
    <div>
        <div class="forget-container">
            <div class="forget-box">
                <div class="image-section">
                    <img src="@/assets/loginbg.png" alt="登录图片">
                </div>
                <div class="form-section">
                    <div class="form-content">
                        <h2>医院床位预约管理系统 - 找回密码</h2>
                        <el-form ref="loginFormRef" :model="formData" label-width="90px" label-position="top" :rules="rules"
                            class="forget-form">
                            <el-form-item label="账号" prop="UserName">
                                <el-input v-model="formData.UserName" placeholder="请输入账号" />
                            </el-form-item>

                            <el-form-item label="邮箱" prop="Email">
                                <el-input v-model="formData.Email" placeholder="请输入邮箱" />
                            </el-form-item>

                            <el-form-item label="联系方式" prop="PhoneNumber">
                                <el-input v-model="formData.PhoneNumber" placeholder="请输入联系方式" />
                            </el-form-item>

                            <el-form-item label="新密码" prop="Password">
                                <el-input type="password" v-model="formData.Password" show-password placeholder="请输入新密码" />
                            </el-form-item>

                            <el-form-item label="验证码" prop="Code">
                                <div class="code-container">
                                    <el-input v-model="formData.Code" placeholder="请输入验证码" />
                                    <div class="valid-code">
                                        <ValidCode ref="validCodeRef" />
                                    </div>
                                </div>
                            </el-form-item>

                            <el-form-item>
                                <el-button type="primary" class="submit-btn" @click="handleForgetPassword">
                                    确定
                                </el-button>
                            </el-form-item>
                        </el-form>

                        <div class="login-link">
                            <span>如果有账号</span>
                            <RouterLink to="/Login">
                                <span class="link-text">去登录</span>
                            </RouterLink>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { Post } from '@/api/http';
import ValidCode from '@/components/Identifyingcode/ImageCode.vue';
import { ElMessage } from 'element-plus';
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

// 路由实例
const router = useRouter()

// 表单引用
const loginFormRef = ref(null)
const validCodeRef = ref(null)

// 表单数据
const formData = reactive({
    UserName: '',
    Password: '',
    Email: '',
    PhoneNumber: '',
    Code: ''
})

// 表单验证规则
const rules = {
    UserName: [
        { required: true, message: '请输入账号', trigger: 'blur' },
        { min: 3, max: 20, message: '账号长度应在3到20个字符之间', trigger: 'blur' }
    ],
    Password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {

                var reg = /^.{8,16}$/;
                if (!value || !reg.test(value)) {
                    callback(new Error('请输入8-16位密码'));
                } else {
                    callback();
                }
            }, trigger: 'blur'
        },
    ],
    Email: [
        { required: true, message: '该项为必填项', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                const reg = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/
                if (!value || !reg.test(value)) {
                    callback(new Error('请输入正确邮箱'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }
    ],
    PhoneNumber: [
        { required: true, message: '该项为必填项', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                const reg = /^1[123456789]\d{9}$/
                if (!value || !reg.test(value)) {
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
}

// 处理找回密码
const handleForgetPassword = async () => {
    if (!loginFormRef.value) return

    await loginFormRef.value.validate(async (valid) => {
        if (valid) {
            try {
                const res = await Post('/User/ForgetPassword', formData)
                if (res.Success) {
                    ElMessage.success('修改密码成功!')
                    router.push('/Login')
                } else {
                    validCodeRef.value.refreshCode()
                }
            } catch (error) {
                console.error(error)
                validCodeRef.value.refreshCode()
            }
        } else {
            ElMessage.error('验证不通过')
            validCodeRef.value.refreshCode()
        }
    })
}
</script>

<style scoped lang="scss">
/* 整体容器样式 */
.forget-container {
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #e6f4ff 0%, #b3d9ff 50%, #91caff 100%);
}

/* 找回密码框样式 */
.forget-box {
    display: flex;
    width: 1200px;
    background-color: white;
    border-radius: 12px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
    overflow: hidden;
}

/* 图片区域样式 */
.image-section {
    flex: 1.8;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #f5f7fa;
    overflow: hidden;
    position: relative;
}

.image-section img {
    width: 100%;
    height: 100%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}

/* 表单区域样式 */
.form-section {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 40px;
}

.form-content {
    width: 100%;
}

/* 表单标题 */
.form-content h2 {
    font-size: 26px;
    color: #1890ff;
    margin-bottom: 30px;
    text-align: center;
    font-weight: 600;
    letter-spacing: 1px;
}

/* 表单样式 */
.forget-form {
    margin-top: 20px;
}

/* 验证码容器样式 */
.code-container {
    display: flex;
    align-items: center;
    gap: 10px;
}

.valid-code {
    flex-shrink: 0;
}

/* 提交按钮样式 */
.submit-btn {
    width: 100%;
    height: 40px;
    font-size: 16px;
    margin-top: 10px;
}

/* 登录链接样式 */
.login-link {
    display: flex;
    justify-content: center;
    margin-top: 20px;
    font-size: 14px;
    color: #606266;
}

.link-text {
    color: #1890ff;
    margin-left: 5px;
    cursor: pointer;
    transition: color 0.3s;
}

.link-text:hover {
    color: #40a9ff;
}

/* 响应式调整 */
@media (max-width: 992px) {
    .forget-box {
        width: 90%;
        height: auto;
        flex-direction: column;
    }

    .image-section {
        display: none;
    }

    .form-section {
        padding: 20px;
    }
}
</style>

