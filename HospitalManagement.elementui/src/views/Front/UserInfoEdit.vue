<template>
  <div>
    <el-card>
      <div class="clearfix">
        <h2>修改账号基础信息</h2>
      </div>
      <div class="margin-top-sm">
        <el-form ref="editModalForm" v-if="editShow" :model="formData" label-width="80px" size="default" :rules="rules">
          <el-form-item label="账号" prop="UserName">
            <el-input v-model="formData.UserName" clearable :disabled="true"></el-input>
          </el-form-item>

          <el-form-item label="邮箱" prop="Email">
            <el-input v-model="formData.Email" clearable></el-input>
          </el-form-item>

          <el-form-item label="姓名" prop="Name">
            <el-input v-model="formData.Name" clearable></el-input>
          </el-form-item>

          <el-form-item label="手机号" prop="PhoneNumber">
            <el-input v-model="formData.PhoneNumber" clearable></el-input>
          </el-form-item>

          <el-form-item label="头像" prop="ImageUrls">
            <UploadImages v-model="formData.ImageUrls"></UploadImages>
          </el-form-item>

          <el-form-item label="出生年月" prop="Birth">
            <el-date-picker
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择日期"
              v-model="formData.Birth"
              clearable
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" size="default" @click="CreateOrEdit">确定</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { Post } from '@/api/http'
import { useCommonStore } from '@/store'
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

const commonStore = useCommonStore()
const editShow = ref(false)
const formData = reactive({})
const editModalForm = ref(null)

const rules = {
  UserName: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度应在3到20个字符之间', trigger: 'blur' }
  ],
  Email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
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
  Name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  PhoneNumber: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
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
  Birth: [
    { required: true, message: '请选择出生年月', trigger: 'blur' }
  ]
}

const ShowEditModal = async () => {
  const { Data } = await Post('/User/Get', { Id: commonStore.UserId })
  Object.assign(formData, Data)
  editShow.value = true
}

const CreateOrEdit = async () => {
  if (!editModalForm.value) return
  await editModalForm.value.validate(async valid => {
    if (valid) {
      const { Success } = await Post('/User/CreateOrEdit', formData)
      if (Success) {
        ElMessage.success('修改成功')
        commonStore.GetInfo()
      }
    }
  })
}

onMounted(() => {
  ShowEditModal()
})
</script>

<style scoped lang="scss"></style>
