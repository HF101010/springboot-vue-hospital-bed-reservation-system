<template>
    <div class="user-page">
        <el-dialog :title="isAdd ? '添加账号' : '编辑账号'" v-model="editorShow" width="600px" destroy-on-close
            :close-on-click-modal="false" @closed="handleDialogClosed">
            <el-form v-if="editorShow" ref="editModalForm" :rules="editModalFormRules" :model="formData"
                label-width="110px" size="default">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="账号" prop="UserName">
                            <el-input v-model="formData.UserName" placeholder="请输入账号" maxlength="20" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="isAdd ? '密码' : '重置密码'" prop="Password">
                            <el-input v-model="formData.Password" :placeholder="isAdd ? '请输入密码' : '留空则不修改密码'"
                                type="password" show-password maxlength="20" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="姓名" prop="Name">
                            <el-input v-model="formData.Name" placeholder="请输入姓名" maxlength="30" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="邮箱" prop="Email">
                            <el-input v-model="formData.Email" placeholder="请输入邮箱" maxlength="50" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="手机号" prop="PhoneNumber">
                            <el-input v-model="formData.PhoneNumber" placeholder="请输入手机号" maxlength="20" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="账号权限" prop="RoleType">
                            <el-select v-model="formData.RoleType" placeholder="请选择账号角色" style="width: 100%">
                                <el-option v-for="item in roleOptions" :key="item.Code" :label="item.Label"
                                    :value="item.Code" />
                            </el-select>
                    </el-form-item>
                    </el-col>
                </el-row>
                <div class="dialog-footer">
                    <el-button @click="editorShow = false">取 消</el-button>
                    <el-button type="primary" @click="CreateOrEditForm">保 存</el-button>
                </div>
            </el-form>
        </el-dialog>

        <PaginationTable ref="PaginationTableId" url="/User/List" :column="columnList" :where="where">
            <template #header>
                <el-form :inline="true" :model="searchForm" class="search-form">
                    <el-form-item label="账号">
                        <el-input v-model="searchForm.UserName" placeholder="请输入账号" clearable />
                    </el-form-item>
                    <el-form-item label="姓名">
                        <el-input v-model="searchForm.Name" placeholder="请输入姓名" clearable />
                    </el-form-item>
                    <el-form-item label="邮箱">
                        <el-input v-model="searchForm.Email" placeholder="请输入邮箱" clearable />
                    </el-form-item>
                    <el-form-item label="手机号">
                        <el-input v-model="searchForm.PhoneNumber" placeholder="请输入手机号" clearable />
                    </el-form-item>
                    <el-form-item label="角色">
                        <el-select v-model="searchForm.RoleType" placeholder="请选择角色" clearable style="width: 160px">
                            <el-option v-for="item in roleOptions" :key="item.Code" :label="item.Label"
                                :value="item.Code" />
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" :icon="Search" @click="SearchClick">查询</el-button>
                        <el-button :icon="Refresh" @click="ResetClick">重置</el-button>
                        <el-button type="success" @click="ShowCreateModal">新增账号</el-button>
                        <el-button type="danger" :icon="Delete" @click="BatchDelete">批量删除</el-button>
                    </el-form-item>
                </el-form>
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
    UserName: '',
    Name: '',
    Email: '',
    PhoneNumber: '',
    RoleType: ''
});

const defaultFormState = () => ({
    Id: null,
    UserName: '',
    Password: '',
    Name: '',
    Email: '',
    PhoneNumber: '',
    RoleType: ''
});

const formData = reactive(defaultFormState());

const editorShow = ref(false);
const editModalForm = ref(null);
const PaginationTableId = ref(null);
const roleOptions = ref([]);

const isAdd = computed(() => !formData.Id);

const columnList = ref([
    { key: 'Id', hidden: true },
    { title: '账号', key: 'UserName', type: ColumnType.SHORTTEXT },
    { title: '姓名', key: 'Name', type: ColumnType.SHORTTEXT },
    { title: '邮箱', key: 'Email', type: ColumnType.SHORTTEXT },
    { title: '手机号', key: 'PhoneNumber', type: ColumnType.SHORTTEXT, width: '140px' },
    { title: '角色', key: 'RoleTypeFormat', type: ColumnType.SHORTTEXT },
    { title: '操作', key: 'Operate', type: ColumnType.USERDEFINED, width: '200px' }
            ]);

const editModalFormRules = reactive({
    UserName: [
        { required: true, message: '请输入账号', trigger: 'blur' },
        { min: 3, max: 20, message: '账号长度应在3到20个字符之间', trigger: 'blur' }
    ],
    Password: [
        {
            validator: (rule, value, callback) => {
                if (isAdd.value && !value) {
                    callback(new Error('新增账号必须填写密码'));
                } else {
                    callback();
                }
            },
            trigger: 'blur'
        }
    ],
    RoleType: [
        { required: true, message: '请选择账号角色', trigger: 'change' }
    ],
    Email: [
        {
            validator: (rule, value, callback) => {
                if (!value) {
                    callback();
                    return;
                }
                const emailReg = /^[\w-.]+@[\w-]+\.[\w-]{2,4}$/;
                if (!emailReg.test(value)) {
                    callback(new Error('请输入正确的邮箱地址'));
                } else {
                    callback();
                }
            },
            trigger: 'blur'
        }
    ]
});

const resetFormData = () => {
    Object.assign(formData, defaultFormState());
};

const fetchRoleOptions = async () => {
    try {
        const { Data: { Items } } = await Post('/Select/RoleType');
        roleOptions.value = Items || [];
    } catch (error) {
        ElMessage.error('获取角色列表失败');
    }
};

const handleDialogClosed = () => {
    resetFormData();
    editModalForm.value?.clearValidate();
};

const ShowCreateModal = () => {
    resetFormData();
    if (roleOptions.value.length > 0) {
        formData.RoleType = roleOptions.value[0].Code;
    }
    editorShow.value = true;
};

const ShowEditModal = async (Id) => {
    resetFormData();
    const { Data } = await Post('/User/Get', { Id });
    Object.assign(formData, Data || {});
    formData.RoleType = Data?.RoleType ?? '';
    formData.Password = '';
    editorShow.value = true;
};

const CreateOrEditForm = async () => {
    if (!editModalForm.value) return;
    editModalForm.value.validate(async (valid) => {
        if (!valid) return;
        const payload = { ...formData };
        const { Success } = await Post('/User/CreateOrEdit', payload);
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
        await ElMessageBox.confirm('确认删除该账号吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        });
        const { Success } = await Post('/User/Delete', { Id });
        if (Success) {
            ElMessage.success('删除成功');
            PaginationTableId.value?.Reload(searchForm);
        }
    } catch {
        // 用户取消操作
    }
};

const BatchDelete = async () => {
    const selectedRows = PaginationTableId.value?.GetSelectionRow() || [];
    const ids = selectedRows.map(x => x.Id);
    if (ids.length === 0) {
        ElMessage.warning('请先选择需要删除的账号');
        return;
    }
        try {
        await ElMessageBox.confirm('确认删除选中的账号吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
        });
        const { Success } = await Post('/User/BatchDelete', { Ids: ids });
            if (Success) {
            ElMessage.success('批量删除成功');
            PaginationTableId.value?.Reload(searchForm);
            }
    } catch {
        // 用户取消
    }
};

onMounted(() => {
    fetchRoleOptions();
});
</script>

<style scoped>
.user-page {
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