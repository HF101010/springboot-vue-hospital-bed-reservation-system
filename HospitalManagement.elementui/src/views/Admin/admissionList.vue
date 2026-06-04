<template>
  <div class="admission-page">
    <el-dialog
      :title="isAdd ? '新增出入院登记' : '编辑出入院登记'"
      v-model="editorShow"
      width="680px"
      destroy-on-close
      :close-on-click-modal="false"
      @closed="handleDialogClosed"
    >
      <el-form
        v-if="editorShow"
        ref="editModalForm"
        :rules="editModalFormRules"
        :model="formData"
        label-width="110px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="患者" prop="patient_id">
              <el-select v-model="formData.patient_id" placeholder="请选择患者" filterable clearable :loading="patientLoading">
                <el-option v-for="item in patientOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="床位" prop="bed_id">
              <el-select v-model="formData.bed_id" placeholder="请选择床位" filterable clearable :loading="bedLoading">
                <el-option v-for="item in bedOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="入院日期" prop="admission_date">
              <el-date-picker
                v-model="formData.admission_date"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择入院时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出院日期" prop="discharge_date">
              <el-date-picker
                v-model="formData.discharge_date"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="未出院可留空"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择状态" filterable>
                <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="病情简介" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            rows="4"
            maxlength="200"
            placeholder="请输入病情简介或备注"
            show-word-limit
          />
        </el-form-item>
        <div class="dialog-footer">
          <el-button @click="editorShow = false">取消</el-button>
          <el-button type="primary" @click="CreateOrEditForm">保存</el-button>
        </div>
      </el-form>
    </el-dialog>

    <PaginationTable ref="PaginationTableId" url="/admission/List" :column="columnList" :where="where">
      <template #header>
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="患者">
            <el-select v-model="searchForm.PatientId" placeholder="全部患者" clearable filterable :loading="patientLoading" style="width: 180px">
              <el-option v-for="item in patientOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="床位">
            <el-select v-model="searchForm.BedId" placeholder="全部床位" clearable filterable :loading="bedLoading" style="width: 150px">
              <el-option v-for="item in bedOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.Status" placeholder="全部状态" clearable style="width: 140px">
              <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="病情简介">
            <el-input v-model="searchForm.Keyword" placeholder="备注/病情关键字" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="SearchClick">查询</el-button>
            <el-button :icon="Refresh" @click="ResetClick">重置</el-button>
            <el-button type="success" @click="ShowCreateModal">新增登记</el-button>
            <el-button type="danger" :icon="Delete" @click="BatchDelete">批量删除</el-button>
          </el-form-item>
        </el-form>
      </template>
      <template #StatusText="{ row }">
        <el-tag :type="isDischarged(row) ? 'success' : 'warning'">
          {{ getAdmissionStatusLabel(row.status) }}
        </el-tag>
      </template>
      <template #Discharged="{ row }">
        <el-tag :type="isDischarged(row) ? 'success' : 'warning'">
          {{ isDischarged(row) ? '已出院' : '住院中' }}
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
import { Post } from '@/api/http';
import { ColumnType } from '@/components/Tables/columnTypes';
import { Delete, Refresh, Search } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { computed, onMounted, reactive, ref } from 'vue';

const where = reactive({});

const searchForm = reactive({
  PatientId: '',
  BedId: '',
  Status: '',
  Keyword: ''
});

const defaultFormState = () => ({
  Id: null,
  patient_id: '',
  bed_id: '',
  admission_date: '',
  discharge_date: '',
  status: 'in_hospital',
  remark: ''
});

const formData = reactive(defaultFormState());
const editorShow = ref(false);
const editModalForm = ref(null);
const PaginationTableId = ref(null);
const patientOptions = ref([]);
const bedOptions = ref([]);
const patientLoading = ref(false);
const bedLoading = ref(false);

const admissionStatusMap = {
  in_hospital: '住院中',
  discharged: '已出院',
  '住院中': '住院中',
  '已出院': '已出院',
  '浣忛櫌涓?': '住院中',
  '宸插嚭闄?': '已出院'
};

const statusOptions = [
  { label: '住院中', value: 'in_hospital' },
  { label: '已出院', value: 'discharged' }
];

const isAdd = computed(() => !formData.Id);

const columnList = ref([
  { key: 'Id', hidden: true },
  { title: '患者姓名', key: 'patient_idDto.real_name', type: ColumnType.SHORTTEXT, width: '140px' },
  { title: '账号', key: 'patient_idDto.username', type: ColumnType.SHORTTEXT, width: '120px' },
  { title: '联系电话', key: 'patient_idDto.phone', type: ColumnType.SHORTTEXT, width: '140px' },
  { title: '床位号', key: 'bed_idDto.bed_number', type: ColumnType.SHORTTEXT, width: '100px' },
  { title: '入院时间', key: 'admission_date', type: ColumnType.DATE, width: '170px' },
  { title: '出院时间', key: 'discharge_date', type: ColumnType.DATE, width: '170px' },
  { title: '状态', key: 'StatusText', type: ColumnType.USERDEFINED, width: '100px' },
  { title: '是否出院', key: 'Discharged', type: ColumnType.USERDEFINED, width: '120px' },
  { title: '病情简介', key: 'remark', type: ColumnType.LONGTEXT, width: '200px' },
  { title: '操作', key: 'Operate', type: ColumnType.USERDEFINED, width: '180px' }
]);

const editModalFormRules = reactive({
  patient_id: [{ required: true, message: '请选择患者', trigger: 'change' }],
  bed_id: [{ required: true, message: '请选择床位', trigger: 'change' }],
  admission_date: [{ required: true, message: '请选择入院时间', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
});

const resetFormData = () => {
  Object.assign(formData, defaultFormState());
};

const getAdmissionStatusLabel = (status) => {
  const statusStr = String(status || '').trim();
  if (!statusStr || statusStr === 'null' || statusStr === 'undefined') {
    return '未知';
  }
  return admissionStatusMap[statusStr] || statusStr;
};

const normalizeAdmissionStatus = (status) => {
  const statusStr = String(status || '').trim();
  if (statusStr === 'discharged' || statusStr === '已出院' || statusStr === '宸插嚭闄?') {
    return 'discharged';
  }
  return 'in_hospital';
};

const fetchPatients = async () => {
  patientLoading.value = true;
  try {
    const { Data: { Items } } = await Post('/user/List', { Page: 1, Limit: 1000 });
    patientOptions.value = (Items || []).map(item => ({
      label: item.real_name || item.username || `用户${item.Id}`,
      value: item.Id ? item.Id.toString() : ''
    })).filter(item => item.value);
  } catch (error) {
    ElMessage.error('获取患者列表失败');
  } finally {
    patientLoading.value = false;
  }
};

const fetchBeds = async () => {
  bedLoading.value = true;
  try {
    const { Data: { Items } } = await Post('/bed/List', { Page: 1, Limit: 1000 });
    bedOptions.value = (Items || []).map(item => ({
      label: item.bed_number ? `床位 ${item.bed_number}` : `床位ID ${item.Id}`,
      value: item.Id ? item.Id.toString() : ''
    })).filter(item => item.value);
  } catch (error) {
    ElMessage.error('获取床位列表失败');
  } finally {
    bedLoading.value = false;
  }
};

const handleDialogClosed = () => {
  resetFormData();
  editModalForm.value?.clearValidate();
};

const ShowCreateModal = () => {
  resetFormData();
  editorShow.value = true;
};

const ShowEditModal = async (Id) => {
  resetFormData();
  const { Data } = await Post('/admission/Get', { Id });
  Object.assign(formData, Data || {});
  if (formData.patient_id && typeof formData.patient_id !== 'string') {
    formData.patient_id = String(formData.patient_id);
  }
  if (formData.bed_id && typeof formData.bed_id !== 'string') {
    formData.bed_id = String(formData.bed_id);
  }
  formData.status = normalizeAdmissionStatus(formData.status);
  editorShow.value = true;
};

const CreateOrEditForm = async () => {
  if (!editModalForm.value) return;
  editModalForm.value.validate(async (valid) => {
    if (!valid) return;
    const payload = { ...formData };
    const { Success } = await Post('/admission/CreateOrEdit', payload);
    if (Success) {
      ElMessage.success('保存成功');
      editorShow.value = false;
      PaginationTableId.value?.Reload(searchForm);
    }
  });
};

const SearchClick = () => {
  PaginationTableId.value?.Reload(searchForm);
};

const ResetClick = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = '';
  });
  PaginationTableId.value?.Reload(searchForm);
};

const ShowDeleteModal = async (Id) => {
  try {
    await ElMessageBox.confirm('确认删除该出入院登记记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });
    const { Success } = await Post('/admission/Delete', { Id });
    if (Success) {
      ElMessage.success('删除成功');
      PaginationTableId.value?.Reload(searchForm);
    }
  } catch {
    // 用户取消
  }
};

const BatchDelete = async () => {
  const ids = PaginationTableId.value?.GetSelectionRow().map(x => x.Id) || [];
  if (ids.length === 0) {
    ElMessage.warning('请先选择需要删除的记录');
    return;
  }
  try {
    await ElMessageBox.confirm('确认删除所选的出入院登记记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });
    const { Success } = await Post('/admission/BatchDelete', { Ids: ids });
    if (Success) {
      ElMessage.success('批量删除成功');
      PaginationTableId.value?.Reload(searchForm);
    }
  } catch {
    // 用户取消
  }
};

const isDischarged = (row) => {
  if (!row) return false;
  if (row.discharge_date) return true;
  const statusStr = String(row.status || '').trim();
  return statusStr === 'discharged' || statusStr === '已出院' || statusStr === '宸插嚭闄?';
};

onMounted(() => {
  fetchPatients();
  fetchBeds();
});
</script>

<style scoped>
.admission-page {
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
