<template>
  <div class="bed-page">
    <el-dialog
      :title="isAdd ? '新增床位' : '编辑床位'"
      v-model="editorShow"
      width="600px"
      destroy-on-close
      :close-on-click-modal="false"
      @closed="handleDialogClosed"
    >
      <el-form v-if="editorShow" ref="editModalForm" :model="formData" :rules="editModalFormRules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="床位编号" prop="bed_number">
              <el-input v-model="formData.bed_number" placeholder="请输入床位编号" maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属病区" prop="ward_id">
              <el-select v-model="formData.ward_id" placeholder="请选择病区" filterable clearable>
                <el-option v-for="item in wardOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="床位状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择床位状态" clearable>
                <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="3"
            maxlength="200"
            placeholder="可填写床位位置、注意事项等"
            show-word-limit
          />
        </el-form-item>
        <div class="dialog-footer">
          <el-button @click="editorShow = false">取消</el-button>
          <el-button type="primary" @click="CreateOrEditForm">保存</el-button>
        </div>
      </el-form>
    </el-dialog>

    <PaginationTable ref="PaginationTableId" url="/bed/List" :column="columnList" :where="where">
      <template #header>
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="床位编号">
            <el-input v-model="searchForm.BedNumber" placeholder="请输入床位编号" clearable />
          </el-form-item>
          <el-form-item label="病区">
            <el-select v-model="searchForm.WardId" placeholder="全部病区" clearable filterable style="width: 160px">
              <el-option v-for="item in wardOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.Status" placeholder="全部状态" clearable style="width: 150px">
              <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="备注关键字">
            <el-input v-model="searchForm.Keyword" placeholder="请输入备注关键字" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="SearchClick">查询</el-button>
            <el-button :icon="Refresh" @click="ResetClick">重置</el-button>
            <el-button type="success" @click="ShowCreateModal">新增床位</el-button>
            <el-button type="danger" :icon="Delete" @click="BatchDelete">批量删除</el-button>
          </el-form-item>
        </el-form>
      </template>
      <template #DepartmentText="{ row }">
        <span>{{ getDepartmentLabel(row.ward_idDto?.department) }}</span>
      </template>
      <template #StatusTag="{ row }">
        <el-tag :type="getBedStatusTagType(getStatusValue(row))">
          {{ getBedStatusLabel(getStatusValue(row)) }}
        </el-tag>
      </template>
      <template #Operate="{ row }">
        <el-space>
          <el-button type="primary" text size="small" @click="ShowEditModal(row.Id)">编辑</el-button>
          <el-button type="danger" text size="small" @click="ShowDeleteModal(row.Id)">删除</el-button>
        </el-space>
      </template>
    </PaginationTable>
  </div>
</template>

<script setup>
import { Post } from '@/api/http'
import { ColumnType } from '@/components/Tables/columnTypes'
import { getDepartmentLabel } from '@/utils/department'
import { getBedStatusLabel, getBedStatusTagType } from '@/utils/status'
import { Delete, Refresh, Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { computed, onMounted, reactive, ref } from 'vue'

const where = reactive({})

const searchForm = reactive({
  BedNumber: '',
  WardId: '',
  Status: '',
  Keyword: ''
})

const defaultFormState = () => ({
  Id: null,
  bed_number: '',
  ward_id: '',
  status: '1',
  remark: ''
})

const formData = reactive(defaultFormState())
const editorShow = ref(false)
const editModalForm = ref(null)
const PaginationTableId = ref(null)
const wardOptions = ref([])
const statusOptions = [
  { label: '可用', value: '1' },
  { label: '已占用', value: '2' },
  { label: '维修中', value: '3' }
]

const columnList = ref([
  { key: 'Id', hidden: true },
  { title: '床位编号', key: 'bed_number', type: ColumnType.SHORTTEXT, width: '140px' },
  { title: '所属病区', key: 'ward_idDto.ward_name', type: ColumnType.SHORTTEXT, width: '160px' },
  { title: '科室', key: 'DepartmentText', type: ColumnType.USERDEFINED, width: '140px' },
  { title: '床位状态', key: 'StatusTag', type: ColumnType.USERDEFINED, width: '120px' },
  { title: '备注', key: 'remark', type: ColumnType.LONGTEXT, width: '200px' },
  { title: '操作', key: 'Operate', type: ColumnType.USERDEFINED, width: '160px' }
])

const editModalFormRules = reactive({
  bed_number: [{ required: true, message: '请输入床位编号', trigger: 'blur' }],
  ward_id: [{ required: true, message: '请选择病区', trigger: 'change' }],
  status: [{ required: true, message: '请选择床位状态', trigger: 'change' }]
})

const isAdd = computed(() => !formData.Id)

const fetchWardOptions = async () => {
  try {
    const { Data: { Items } } = await Post('/ward/List', { Page: 1, Limit: 1000 })
    wardOptions.value = (Items || []).map(item => ({
      label: item.ward_name || `病区${item.Id}`,
      value: item.Id ? item.Id.toString() : ''
    })).filter(o => o.value)
  } catch (error) {
    ElMessage.error('获取病区列表失败')
  }
}

const resetFormData = () => {
  Object.assign(formData, defaultFormState())
}

const handleDialogClosed = () => {
  resetFormData()
  editModalForm.value?.clearValidate()
}

const ShowCreateModal = () => {
  resetFormData()
  editorShow.value = true
}

const ShowEditModal = async (Id) => {
  resetFormData()
  const { Data } = await Post('/bed/Get', { Id })
  Object.assign(formData, Data || {})
  if (formData.ward_id && typeof formData.ward_id !== 'string') {
    formData.ward_id = String(formData.ward_id)
  }
  if (formData.status != null && typeof formData.status !== 'string') {
    formData.status = String(formData.status)
  }
  editorShow.value = true
}

const CreateOrEditForm = async () => {
  if (!editModalForm.value) return
  editModalForm.value.validate(async (valid) => {
    if (!valid) return
    const payload = { ...formData }
    const { Success } = await Post('/bed/CreateOrEdit', payload)
    if (Success) {
      ElMessage.success('保存成功')
      editorShow.value = false
      PaginationTableId.value?.Reload(searchForm)
    }
  })
}

const SearchClick = () => {
  PaginationTableId.value?.Reload(searchForm)
}

const ResetClick = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  PaginationTableId.value?.Reload(searchForm)
}

const ShowDeleteModal = async (Id) => {
  try {
    await ElMessageBox.confirm('确认删除该床位吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const { Success } = await Post('/bed/Delete', { Id })
    if (Success) {
      ElMessage.success('删除成功')
      PaginationTableId.value?.Reload(searchForm)
    }
  } catch {
    // 用户取消
  }
}

const BatchDelete = async () => {
  const ids = PaginationTableId.value?.GetSelectionRow().map(x => x.Id) || []
  if (ids.length === 0) {
    ElMessage.warning('请先选择需要删除的床位')
    return
  }
  try {
    await ElMessageBox.confirm('确认删除所选床位吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const { Success } = await Post('/bed/BatchDelete', { Ids: ids })
    if (Success) {
      ElMessage.success('批量删除成功')
      PaginationTableId.value?.Reload(searchForm)
    }
  } catch {
    // 用户取消
  }
}

const getStatusValue = (row) => {
  if (!row || typeof row !== 'object') {
    return null
  }
  if (row.Status !== undefined && row.Status !== null && row.Status !== '') {
    return String(row.Status).trim()
  }
  if (row.status !== undefined && row.status !== null && row.status !== '') {
    return String(row.status).trim()
  }
  if (row.OrginValue && typeof row.OrginValue === 'object') {
    if (row.OrginValue.Status !== undefined && row.OrginValue.Status !== null && row.OrginValue.Status !== '') {
      return String(row.OrginValue.Status).trim()
    }
    if (row.OrginValue.status !== undefined && row.OrginValue.status !== null && row.OrginValue.status !== '') {
      return String(row.OrginValue.status).trim()
    }
  }
  return null
}

onMounted(() => {
  fetchWardOptions()
})
</script>

<style scoped>
.bed-page {
  padding: 10px 20px;
}

.search-form {
  margin-bottom: 10px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
}
</style>
