<template>
    <div class="ward-page">
        <el-dialog :title="isAdd ? '新增病区' : '编辑病区'" v-model="editorShow" width="640px" destroy-on-close
            :close-on-click-modal="false" @closed="handleDialogClosed">
            <el-form v-if="editorShow" ref="editModalForm" :model="formData" :rules="editModalFormRules" label-width="110px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="病区名称" prop="ward_name">
                            <el-input v-model="formData.ward_name" placeholder="请输入病区名称" maxlength="40" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="所属科室" prop="department">
                            <el-input v-model="formData.department" placeholder="请输入所属科室" maxlength="40" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="总床位数" prop="total_beds">
                            <el-input-number v-model="formData.total_beds" :min="0" :max="10000" :step="1"
                                controls-position="right" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="可用床位" prop="available_beds">
                            <el-input-number v-model="formData.available_beds" :min="0" :max="10000" :step="1"
                                controls-position="right" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="备注" prop="remark">
                    <el-input v-model="formData.remark" type="textarea" :rows="3" maxlength="200"
                        placeholder="可填写病区位置、特殊说明等" show-word-limit />
                </el-form-item>

                <div class="dialog-footer">
                    <el-button @click="editorShow = false">取 消</el-button>
                    <el-button type="primary" @click="CreateOrEditForm">保 存</el-button>
                </div>
            </el-form>
        </el-dialog>

        <PaginationTable ref="PaginationTableId" url="/ward/List" :column="columnList" :where="where">
            <template #header>
                <el-form :inline="true" :model="searchForm" class="search-form">
                    <el-form-item label="病区名称">
                        <el-input v-model="searchForm.WardName" placeholder="请输入病区名称" clearable />
                    </el-form-item>
                    <el-form-item label="所属科室">
                        <el-input v-model="searchForm.Department" placeholder="请输入所属科室" clearable />
                    </el-form-item>
                    <el-form-item label="备注关键词">
                        <el-input v-model="searchForm.Keyword" placeholder="请输入备注关键字" clearable />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" :icon="Search" @click="SearchClick">查询</el-button>
                        <el-button :icon="Refresh" @click="ResetClick">重置</el-button>
                        <el-button type="success" @click="ShowCreateModal">新增病区</el-button>
                        <el-button type="danger" :icon="Delete" @click="BatchDelete">批量删除</el-button>
                    </el-form-item>
                </el-form>
            </template>
            <template #DepartmentText="{ row }">
                <span>{{ getDepartmentLabel(row.department) }}</span>
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
import { Delete, Refresh, Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { computed, reactive, ref, watch } from 'vue'

const where = reactive({})

const searchForm = reactive({
    WardName: '',
    Department: '',
    Keyword: ''
})

const defaultFormState = () => ({
    Id: null,
    ward_name: '',
    department: '',
    total_beds: 0,
    available_beds: 0,
    remark: ''
})

const formData = reactive(defaultFormState())
const editorShow = ref(false)
const editModalForm = ref(null)
const PaginationTableId = ref(null)

const columnList = ref([
    { key: 'Id', hidden: true },
    { title: '病区名称', key: 'ward_name', type: ColumnType.SHORTTEXT, width: '180px' },
    { title: '所属科室', key: 'department', type: ColumnType.SHORTTEXT, width: '160px' },
    { title: '总床位数', key: 'total_beds', type: ColumnType.SHORTTEXT, width: '120px' },
    { title: '可用床位', key: 'available_beds', type: ColumnType.SHORTTEXT, width: '120px' },
    { title: '备注', key: 'remark', type: ColumnType.LONGTEXT, width: '220px' },
    { title: '操作', key: 'Operate', type: ColumnType.USERDEFINED, width: '180px' }
])

if (columnList.value[2]) {
    columnList.value[2].key = 'DepartmentText'
    columnList.value[2].type = ColumnType.USERDEFINED
}

const editModalFormRules = reactive({
    ward_name: [
        { required: true, message: '请输入病区名称', trigger: 'blur' },
        { min: 1, max: 40, message: '病区名称长度为1-40个字符', trigger: 'blur' }
    ],
    department: [
        { required: true, message: '请输入所属科室', trigger: 'blur' },
        { min: 1, max: 40, message: '所属科室长度为1-40个字符', trigger: 'blur' }
    ],
    total_beds: [
        { required: true, message: '请输入总床位数', trigger: 'change' },
        {
            validator: (_, value, callback) => {
                if (!Number.isInteger(value) || value < 0) {
                    callback(new Error('总床位数需为非负整数'))
                } else {
                    callback()
                }
            },
            trigger: 'change'
        }
    ],
    available_beds: [
        { required: true, message: '请输入可用床位数', trigger: 'change' },
        {
            validator: (_, value, callback) => {
                if (!Number.isInteger(value) || value < 0) {
                    callback(new Error('可用床位需为非负整数'))
                } else if (value > formData.total_beds) {
                    callback(new Error('可用床位不能大于总床位'))
                } else {
                    callback()
                }
            },
            trigger: 'change'
        }
    ]
})

const isAdd = computed(() => !formData.Id)

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
    const { Data } = await Post('/ward/Get', { Id })
    Object.assign(formData, Data || {})
    editorShow.value = true
}

const CreateOrEditForm = async () => {
    if (!editModalForm.value) return
    editModalForm.value.validate(async (valid) => {
        if (!valid) return
        const payload = { ...formData }
        const { Success } = await Post('/ward/CreateOrEdit', payload)
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
        await ElMessageBox.confirm('确认删除该病区吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        const { Success } = await Post('/ward/Delete', { Id })
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
        ElMessage.warning('请先选择需要删除的病区')
        return
    }
    try {
        await ElMessageBox.confirm('确认删除所选的病区吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        const { Success } = await Post('/ward/BatchDelete', { Ids: ids })
        if (Success) {
            ElMessage.success('批量删除成功')
            PaginationTableId.value?.Reload(searchForm)
        }
    } catch {
        // 用户取消
    }
}

watch(() => formData.total_beds, (val) => {
    if (val == null || val < 0) {
        formData.total_beds = 0
    }
    if (formData.available_beds > formData.total_beds) {
        formData.available_beds = formData.total_beds
    }
})

watch(() => formData.available_beds, (val) => {
    if (val == null || val < 0) {
        formData.available_beds = 0
    }
    if (val > formData.total_beds) {
        formData.available_beds = formData.total_beds
    }
})
</script>

<style scoped>
.ward-page {
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





