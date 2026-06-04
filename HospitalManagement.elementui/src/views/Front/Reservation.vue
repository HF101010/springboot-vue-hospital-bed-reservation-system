<template>
    <div class="reservation-page">
        <el-dialog title="申请床位预约" v-model="editorShow" width="720px" destroy-on-close
            :close-on-click-modal="false" @closed="handleDialogClosed">
            <el-form v-if="editorShow" ref="editModalForm" :model="formData" :rules="editModalFormRules" label-width="110px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="科室" prop="department">
                            <el-select v-model="formData.department" placeholder="请选择科室" filterable clearable>
                                <el-option v-for="item in departmentOptions" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="病区" prop="ward_id">
                            <el-select v-model="formData.ward_id" placeholder="可选，留空则系统自动分配" filterable clearable
                                :loading="wardLoading" @change="handleWardChange">
                                <el-option v-for="item in wardOptions" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="预约床位" prop="bed_id">
                            <el-select v-model="formData.bed_id" placeholder="可选，留空则系统自动分配" filterable clearable>
                                <el-option v-for="item in filteredBedOptions" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="预约时间" prop="reservation_time">
                            <el-date-picker v-model="formData.reservation_time" type="datetime"
                                value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择预约时间" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="预约原因" prop="reason">
                    <el-input v-model="formData.reason" type="textarea" :rows="3" maxlength="200"
                        placeholder="请输入预约原因或备注" show-word-limit />
                </el-form-item>
                <div class="dialog-footer">
                    <el-button @click="editorShow = false">取 消</el-button>
                    <el-button type="primary" @click="CreateOrEditForm">提 交</el-button>
                </div>
            </el-form>
        </el-dialog>

        <PaginationTable ref="PaginationTableId" url="/bed_reservation/MyList" :column="columnList" :where="where">
            <template #header>
                <el-form :inline="true" :model="searchForm" class="search-form">
                    <el-form-item label="病区">
                        <el-select v-model="searchForm.WardId" placeholder="全部病区" clearable filterable
                            :loading="wardLoading" style="width: 160px">
                            <el-option v-for="item in wardOptions" :key="item.value" :label="item.label"
                                :value="item.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="状态">
                        <el-select v-model="searchForm.Status" placeholder="全部状态" clearable style="width: 140px">
                            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label"
                                :value="item.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" :icon="Search" @click="SearchClick">查询</el-button>
                        <el-button :icon="Refresh" @click="ResetClick">重置</el-button>
                        <el-button type="success" @click="ShowCreateModal">申请预约</el-button>
                    </el-form-item>
                </el-form>
            </template>
            <template #StatusTag="{ row }">
                <el-tag :type="statusTagType(row.Status || row.status || row.OrginValue?.status || row.OrginValue?.Status)">
                    {{ getStatusText(row.Status || row.status || row.OrginValue?.status || row.OrginValue?.Status) }}
                </el-tag>
            </template>
        </PaginationTable>
    </div>
</template>

<script setup>
import { Post } from '@/api/http'
import { ColumnType } from '@/components/Tables/columnTypes'
import { useCommonStore } from '@/store'
import { toDepartmentOption } from '@/utils/department'
import { getReservationStatusLabel, getReservationStatusTagType } from '@/utils/status'
import { Delete, Refresh, Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { computed, onMounted, reactive, ref, watch } from 'vue'

const commonStore = useCommonStore()
const where = reactive({})

const searchForm = reactive({
    WardId: '',
    Status: '',
})

const defaultFormState = () => ({
    Id: null,
    patient_id: commonStore.UserId ? commonStore.UserId.toString() : '',
    department: '',
    ward_id: '',
    bed_id: '',
    reservation_time: '',
    status: '1', // 1 = 待审核
    reason: ''
})

const formData = reactive(defaultFormState())
const editorShow = ref(false)
const editModalForm = ref(null)
const PaginationTableId = ref(null)

const wardOptions = ref([])
const departmentOptions = ref([])
const bedOptions = ref([])
const wardLoading = ref(false)
const bedLoading = ref(false)

const statusOptions = [
    { label: '待审核', value: '1' },
    { label: '已通过', value: '2' },
    { label: '已拒绝', value: '3' },
    { label: '已取消', value: '4' }
]

const columnList = ref([
    { key: 'Id', hidden: true },
    { title: '预约病区', key: 'ward_idDto.ward_name', type: ColumnType.SHORTTEXT, width: '150px' },
    { title: '预约床位', key: 'bed_idDto.bed_number', type: ColumnType.SHORTTEXT, width: '120px' },
    { title: '预约时间', key: 'reservation_time', type: ColumnType.DATE, width: '180px' },
    { title: '状态', key: 'StatusTag', type: ColumnType.USERDEFINED, width: '110px' },
    { title: '预约原因', key: 'reason', type: ColumnType.LONGTEXT, width: '220px' },
])

const editModalFormRules = reactive({
    department: [{ required: true, message: '请选择科室', trigger: 'change' }],
    // 病区与床位改为可选：如果不选则走自动分配
    ward_id: [],
    bed_id: [],
    reservation_time: [{ required: true, message: '请选择预约时间', trigger: 'change' }],
    reason: [{ required: true, message: '请输入预约原因', trigger: 'blur' }]
})

const filteredBedOptions = computed(() => {
    if (!formData.ward_id) return []
    return bedOptions.value.filter(item => item.wardId === formData.ward_id)
})

const fetchWards = async () => {
    wardLoading.value = true
    try {
        const { Data: { Items } } = await Post('/ward/List', { Page: 1, Limit: 1000 })
        const wards = Items || []
        wardOptions.value = wards.map(item => ({
            label: item.ward_name || `病区${item.Id}`,
            value: item.Id ? item.Id.toString() : ''
        })).filter(o => o.value)

        // 从病区列表中提取科室列表
        const deptSet = new Set()
        wards.forEach(item => {
            if (item.department) {
                deptSet.add(item.department)
            }
        })
        departmentOptions.value = Array.from(deptSet).map(toDepartmentOption)
    } catch (error) {
        ElMessage.error('获取病区列表失败')
    } finally {
        wardLoading.value = false
    }
}

const fetchBeds = async () => {
    bedLoading.value = true
    try {
        const { Data: { Items } } = await Post('/bed/List', { Page: 1, Limit: 1000 })
        bedOptions.value = (Items || []).map(item => ({
            label: item.bed_number ? `床位 ${item.bed_number}` : `床位ID ${item.Id}`,
            value: item.Id ? item.Id.toString() : '',
            wardId: item.ward_id ? item.ward_id.toString() : ''
        })).filter(o => o.value)
    } catch (error) {
        ElMessage.error('获取床位列表失败')
    } finally {
        bedLoading.value = false
    }
}

const handleWardChange = () => {
    formData.bed_id = ''
}

const resetFormData = () => {
    Object.assign(formData, defaultFormState())
}

const handleDialogClosed = () => {
    resetFormData()
    editModalForm.value?.clearValidate()
}

const ShowCreateModal = () => {
    if (!commonStore.UserId) {
        ElMessage.warning('请先登录')
        return
    }
    resetFormData()
    editorShow.value = true
}

const CreateOrEditForm = async () => {
    if (!editModalForm.value) return
    editModalForm.value.validate(async (valid) => {
        if (!valid) return
        const payload = { ...formData }
        const { Success } = await Post('/bed_reservation/CreateOrEdit', payload)
        if (Success) {
            ElMessage.success('申请提交成功，等待管理员审核')
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

const getStatusText = (status) => {
    return getReservationStatusLabel(status)
}

const statusTagType = (status) => {
    return getReservationStatusTagType(status)
}

watch(() => formData.ward_id, (newVal) => {
    if (!newVal) {
        formData.bed_id = ''
        return
    }
    if (formData.bed_id) {
        const bed = bedOptions.value.find(item => item.value === formData.bed_id)
        if (bed && bed.wardId !== newVal) {
            formData.bed_id = ''
        }
    }
})

onMounted(() => {
    fetchWards()
    fetchBeds()
})
</script>

<style scoped>
.reservation-page {
    padding: 20px;
    background: white;
    border-radius: 8px;
    min-height: calc(100vh - 200px);
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
