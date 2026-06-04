<template>
    <div class="reservation-page">
        <el-dialog :title="isAdd ? '新增床位预约' : '编辑床位预约'" v-model="editorShow" width="720px" destroy-on-close
            :close-on-click-modal="false" @closed="handleDialogClosed">
            <el-form v-if="editorShow" ref="editModalForm" :model="formData" :rules="editModalFormRules" label-width="110px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="患者" prop="patient_id">
                            <el-select v-model="formData.patient_id" placeholder="请选择患者" filterable clearable
                                :loading="patientLoading">
                                <el-option v-for="item in patientOptions" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="病区" prop="ward_id">
                            <el-select v-model="formData.ward_id" placeholder="请选择病区" filterable clearable
                                :loading="wardLoading">
                                <el-option v-for="item in wardOptions" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="预约床位" prop="bed_id">
                            <el-select v-model="formData.bed_id" placeholder="请选择床位" filterable clearable>
                                <el-option v-for="item in filteredBedOptions" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="预约时间" prop="reservation_time">
                            <el-date-picker v-model="formData.reservation_time" type="datetime"
                                value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择预约时间" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="状态" prop="status">
                            <el-select v-model="formData.status" placeholder="请选择预约状态">
                                <el-option v-for="item in statusOptions" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select>
                    </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="预约原因" prop="reason">
                    <el-input v-model="formData.reason" type="textarea" :rows="3" maxlength="200"
                        placeholder="请输入预约原因或备注" show-word-limit />
                </el-form-item>
                <div class="dialog-footer">
                    <el-button @click="editorShow = false">取 消</el-button>
                    <el-button type="primary" @click="CreateOrEditForm">保 存</el-button>
                </div>
            </el-form>
        </el-dialog>

        <PaginationTable ref="PaginationTableId" url="/bed_reservation/List" :column="columnList" :where="where">
            <template #header>
                <el-form :inline="true" :model="searchForm" class="search-form">
                    <el-form-item label="患者">
                        <el-select v-model="searchForm.PatientId" placeholder="全部患者" clearable filterable
                            :loading="patientLoading" style="width: 180px">
                            <el-option v-for="item in patientOptions" :key="item.value" :label="item.label"
                                :value="item.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="病区">
                        <el-select v-model="searchForm.WardId" placeholder="全部病区" clearable filterable
                            :loading="wardLoading" style="width: 160px">
                            <el-option v-for="item in wardOptions" :key="item.value" :label="item.label"
                                :value="item.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="床位">
                        <el-select v-model="searchForm.BedId" placeholder="全部床位" clearable filterable style="width: 150px">
                            <el-option v-for="item in bedOptions" :key="item.value" :label="item.label"
                                :value="item.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="状态">
                        <el-select v-model="searchForm.Status" placeholder="全部状态" clearable style="width: 140px">
                            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label"
                                :value="item.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="预约时间">
                        <el-date-picker v-model="searchForm.DateRange" type="datetimerange" range-separator="至"
                            start-placeholder="开始时间" end-placeholder="结束时间" value-format="YYYY-MM-DD HH:mm:ss" />
                    </el-form-item>
                    <el-form-item label="原因关键词">
                        <el-input v-model="searchForm.Keyword" placeholder="请输入预约原因关键字" clearable />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" :icon="Search" @click="SearchClick">查询</el-button>
                        <el-button :icon="Refresh" @click="ResetClick">重置</el-button>
                        <el-button type="success" @click="ShowCreateModal">新增预约</el-button>
                        <el-button type="danger" :icon="Delete" @click="BatchDelete">批量删除</el-button>
                    </el-form-item>
                </el-form>
            </template>
            <template #StatusTag="{ row }">
                <el-tag :type="statusTagType(row.Status || row.status || row.OrginValue?.status || row.OrginValue?.Status)">
                    {{ getStatusText(row.Status || row.status || row.OrginValue?.status || row.OrginValue?.Status) }}
                </el-tag>
            </template>
            <template #Operate="{ row }">
                <el-space>
                    <el-button v-if="((row.Status || row.status || row.OrginValue?.status || row.OrginValue?.Status) === '1' || (row.Status || row.status || row.OrginValue?.status || row.OrginValue?.Status) === '待审核')" type="success" text size="small" @click="AuditReservation(row.Id, '2')">通过</el-button>
                    <el-button v-if="((row.Status || row.status || row.OrginValue?.status || row.OrginValue?.Status) === '1' || (row.Status || row.status || row.OrginValue?.status || row.OrginValue?.Status) === '待审核')" type="danger" text size="small" @click="AuditReservation(row.Id, '3')">拒绝</el-button>
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
import { Delete, Refresh, Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { computed, onMounted, reactive, ref, watch } from 'vue'

const where = reactive({})

const searchForm = reactive({
    PatientId: '',
    WardId: '',
    BedId: '',
    Status: '',
    DateRange: [],
    StartTime: '',
    EndTime: '',
    Keyword: ''
})

const defaultFormState = () => ({
    Id: null,
    patient_id: '',
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

const patientOptions = ref([])
const wardOptions = ref([])
const bedOptions = ref([])
const patientLoading = ref(false)
const wardLoading = ref(false)
const bedLoading = ref(false)

const statusOptions = [
    { label: '待审核', value: '1' },
    { label: '已通过', value: '2' },
    { label: '已拒绝', value: '3' },
    { label: '已取消', value: '4' }
]

const statusMap = {
    '1': '待审核',
    '2': '已通过',
    '3': '已拒绝',
    '4': '已取消'
}

const columnList = ref([
    { key: 'Id', hidden: true },
    { title: '患者', key: 'patient_idDto.real_name', type: ColumnType.SHORTTEXT, width: '140px' },
    { title: '联系方式', key: 'patient_idDto.phone', type: ColumnType.SHORTTEXT, width: '140px' },
    { title: '预约病区', key: 'ward_idDto.ward_name', type: ColumnType.SHORTTEXT, width: '150px' },
    { title: '预约床位', key: 'bed_idDto.bed_number', type: ColumnType.SHORTTEXT, width: '120px' },
    { title: '预约时间', key: 'reservation_time', type: ColumnType.DATE, width: '180px' },
    { title: '状态', key: 'StatusTag', type: ColumnType.USERDEFINED, width: '110px' },
    { title: '预约原因', key: 'reason', type: ColumnType.LONGTEXT, width: '220px' },
    { title: '操作', key: 'Operate', type: ColumnType.USERDEFINED, width: '160px' }
])

const editModalFormRules = reactive({
    patient_id: [{ required: true, message: '请选择患者', trigger: 'change' }],
    ward_id: [{ required: true, message: '请选择病区', trigger: 'change' }],
    bed_id: [{ required: true, message: '请选择床位', trigger: 'change' }],
    reservation_time: [{ required: true, message: '请选择预约时间', trigger: 'change' }],
    status: [{ required: true, message: '请选择预约状态', trigger: 'change' }],
    reason: [{ required: true, message: '请输入预约原因', trigger: 'blur' }]
})

const isAdd = computed(() => !formData.Id)

const fetchPatients = async () => {
    patientLoading.value = true
    try {
        const { Data: { Items } } = await Post('/user/List', { Page: 1, Limit: 1000 })
        patientOptions.value = (Items || []).map(item => ({
            label: item.real_name || item.username || `用户${item.Id}`,
            value: item.Id ? item.Id.toString() : ''
        })).filter(o => o.value)
    } catch (error) {
        ElMessage.error('获取患者列表失败')
    } finally {
        patientLoading.value = false
    }
}

const fetchWards = async () => {
    wardLoading.value = true
    try {
        const { Data: { Items } } = await Post('/ward/List', { Page: 1, Limit: 1000 })
        wardOptions.value = (Items || []).map(item => ({
            label: item.ward_name || `病区${item.Id}`,
            value: item.Id ? item.Id.toString() : ''
        })).filter(o => o.value)
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

const filteredBedOptions = computed(() => {
    if (!formData.ward_id) return bedOptions.value
    return bedOptions.value.filter(item => item.wardId === formData.ward_id)
})

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
    const { Data } = await Post('/bed_reservation/Get', { Id })
    Object.assign(formData, Data || {})
    ;['patient_id', 'ward_id', 'bed_id'].forEach(key => {
        if (formData[key] && typeof formData[key] !== 'string') {
            formData[key] = String(formData[key])
        }
    })
    // 确保状态字段是字符串格式的数字
    if (formData.status && typeof formData.status !== 'string') {
        formData.status = String(formData.status)
    }
    // 兼容旧的中文状态值，转换为数字
    if (formData.status === '待审核') formData.status = '1'
    if (formData.status === '已通过') formData.status = '2'
    if (formData.status === '已拒绝') formData.status = '3'
    if (formData.status === '已取消') formData.status = '4'
    editorShow.value = true
}

const CreateOrEditForm = async () => {
    if (!editModalForm.value) return
    editModalForm.value.validate(async (valid) => {
        if (!valid) return
        const payload = { ...formData }
        const { Success } = await Post('/bed_reservation/CreateOrEdit', payload)
            if (Success) {
            ElMessage.success('保存成功')
            editorShow.value = false
            PaginationTableId.value?.Reload(searchForm)
        }
    })
}

const SearchClick = () => {
    if (searchForm.DateRange?.length === 2) {
        searchForm.StartTime = searchForm.DateRange[0]
        searchForm.EndTime = searchForm.DateRange[1]
    } else {
        searchForm.StartTime = ''
        searchForm.EndTime = ''
    }
    PaginationTableId.value?.Reload(searchForm)
}

const ResetClick = () => {
    Object.keys(searchForm).forEach(key => {
        if (key === 'DateRange') {
            searchForm.DateRange = []
        } else {
            searchForm[key] = ''
        }
    })
    PaginationTableId.value?.Reload(searchForm)
}

const ShowDeleteModal = async (Id) => {
    try {
        await ElMessageBox.confirm('确认删除该预约记录吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        const { Success } = await Post('/bed_reservation/Delete', { Id })
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
        ElMessage.warning('请先选择需要删除的预约记录')
        return
    }
        try {
        await ElMessageBox.confirm('确认删除所选的预约记录吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
        const { Success } = await Post('/bed_reservation/BatchDelete', { Ids: ids })
            if (Success) {
            ElMessage.success('批量删除成功')
            PaginationTableId.value?.Reload(searchForm)
        }
    } catch {
        // 用户取消
    }
}

const AuditReservation = async (Id, status) => {
    const statusText = status === '2' ? '通过' : '拒绝'
    try {
        await ElMessageBox.confirm(`确认${statusText}该预约申请吗？`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        const { Success } = await Post('/bed_reservation/Audit', { Id, status })
        if (Success) {
            ElMessage.success(`审核${statusText}成功`)
            PaginationTableId.value?.Reload(searchForm)
        }
    } catch (error) {
        if (error !== 'cancel') {
            ElMessage.error('审核失败')
        }
    }
}

const getStatusText = (status) => {
    // 转换为字符串进行比较
    const statusStr = String(status || '').trim()
    if (!statusStr || statusStr === '' || statusStr === 'null' || statusStr === 'undefined' || statusStr === 'NaN') {
        return '未知'
    }
    // 如果是数字，转换为中文
    if (statusMap[statusStr]) {
        return statusMap[statusStr]
    }
    // 兼容旧的中文值（如果存在）
    if (statusStr === '待审核' || statusStr === '已通过' || statusStr === '已拒绝' || statusStr === '已取消') {
        return statusStr
    }
    return '未知'
}

const statusTagType = (status) => {
    const statusText = getStatusText(status)
    switch (statusText) {
        case '待审核':
            return 'warning'
        case '已通过':
            return 'success'
        case '已拒绝':
            return 'danger'
        case '已取消':
            return 'info'
        default:
            return 'info'
    }
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
    fetchPatients()
    fetchWards()
    fetchBeds()
})
</script>

<style scoped>
.reservation-page {
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

