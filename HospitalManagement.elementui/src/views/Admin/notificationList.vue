<template>
    <div>
<!-- 编辑对话框 -->
        <el-dialog :title="formData.Id ? '修改系统通知' : '添加系统通知'" v-model="editorShow" width="50%" :lock-scroll="true"
            min-height="500px">
            <el-form v-if="editorShow" ref="editModalForm" :rules="editModalFormRules" :model="formData" label-width="140px"
                size="default">             
              <el-row :gutter="10" class="edit-from-body">
                </el-row>

                <el-row type="flex" justify="end" align="bottom">
                    <el-form-item>
                        <el-button size="default" type="primary" plain @click="CreateOrEditForm()">确 定</el-button>
                        <el-button size="default" @click="editorShow = false">取 消</el-button>
                    </el-form-item>
                </el-row>
            </el-form>
        </el-dialog>




        <!-- 数据表格 -->
        <PaginationTable ref="PaginationTableId" url="/notification/List" :column="columnList" :where="where">
            <template v-slot:header>
                <el-form :inline="true" :model="searchForm" class="search-form">
                    <el-form-item label="标题">
                        <el-input v-model="searchForm.Keyword" placeholder="请输入标题关键字" clearable style="width: 200px" />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" :icon="Search" @click="SearchClick">查询</el-button>
                        <el-button :icon="Refresh" @click="ResetClick">重置</el-button>
                    </el-form-item>
                </el-form>
            </template>
            <template #IsReadTag="{ row }">
                <el-tag :type="row.is_read ? 'success' : 'warning'">{{ row.is_read ? '已读' : '未读' }}</el-tag>
            </template>
            <template v-slot:Operate="{ row }">
                <el-space>
                    <el-button type="danger" text size="small" @click="ShowDeleteModal(row.Id)">删除</el-button>
                </el-space>
            </template>
        </PaginationTable>
        
              
              
    </div>
</template>

<script setup>
import { Post,PostSingleUpdate } from '@/api/http';
import { ColumnType } from '@/components/Tables/columnTypes';
import { useCommonStore } from "@/store";

import { Delete, Edit, Refresh, Search } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {onMounted,onBeforeMount,nextTick, watch,computed, reactive, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();
const commonStore = useCommonStore();
// 计算属性
const Token = computed(() => commonStore.Token)
const UserInfo = computed(() => commonStore.UserInfo)
const RoleType = computed(() => commonStore.RoleType)
const UserId = computed(() => commonStore.UserId)
  
//表格条件
const where = reactive({});
// 搜索表单数据
const searchForm = reactive({
    Keyword: ''
});

// 编辑表单数据
const formData = reactive({});

// 编辑对话框显示状态
const editorShow = ref(false);

// 表单引用
const editModalForm = ref(null);

// 表格列配置
const columnList = ref([
  {
    key: "Id",
    hidden: true,
  },
  {
    title: "标题",
    key: "title",
    type: ColumnType.SHORTTEXT,
    width: "200px",
  },
  {
    title: "内容",
    key: "content",
    type: ColumnType.LONGTEXT,
    width: "300px",
  },
  {
    title: "发送人",
    key: "sender_idDto.real_name",
    type: ColumnType.SHORTTEXT,
    width: "120px",
  },
  {
    title: "接收人",
    key: "receiver_idDto.real_name",
    type: ColumnType.SHORTTEXT,
    width: "120px",
  },
  {
    title: "发送时间",
    key: "create_time",
    type: ColumnType.DATE,
    width: "180px",
  },
  {
    title: "是否已读",
    key: "IsReadTag",
    type: ColumnType.USERDEFINED,
    width: "100px",
  },
  {
    title: "操作",
    width:"200px",
    key: "Operate",
    type: ColumnType.USERDEFINED,
  },
            ]);

// 表单验证规则
const editModalFormRules = reactive({
 });


// 表格引用
const PaginationTableId = ref(null);

// 显示编辑对话框
const ShowEditModal = async (Id) => {

    const { Data } = await Post(`/notification/Get`, { Id: Id });
    
    Object.assign(formData, Data);

    editorShow.value = true;

};

// 创建或编辑表单
const CreateOrEditForm = async () => {
    if (!editModalForm.value) return;

    await editModalForm.value.validate(async valid => {
        if (valid) {
            const { Success } = await Post(`/notification/CreateOrEdit`, formData);

            if (Success) {
                editorShow.value = false;
                PaginationTableId.value.Reload(searchForm);
                ElMessage.success('操作成功');
            }
        }
    });
};

// 搜索点击
const SearchClick = () => {
    PaginationTableId.value.Reload(searchForm);
};

// 重置搜索条件
const ResetClick = () => {
    Object.keys(searchForm).forEach(key => {
        searchForm[key] = undefined;
    });
    PaginationTableId.value.Reload(searchForm);
};

// 显示删除确认框
const ShowDeleteModal = async (Id) => {

    try {
        await ElMessageBox.confirm('确认删除该信息吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        const { Success } = await Post(`/notification/Delete`, { Id: Id });
        if (Success) {
            PaginationTableId.value.Reload(searchForm);
            ElMessage.success('删除成功');
        }
    }
    catch (error) {
        ElMessage.warning('用户放弃了操作');
    }
};

// 批量删除
const BatchDelete = async () => {
    const ids = PaginationTableId.value.GetSelectionRow().map(x => x.Id);
    if (ids.length > 0) {
        try {
            await ElMessageBox.confirm('确认删除所选的行数据吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })


            const { Success } = await Post(`/notification/BatchDelete`, { Ids: ids });
            if (Success) {
                PaginationTableId.value.Reload(searchForm);
                ElMessage.success('删除成功');
            }
        }
        catch (error) {
            ElMessage.warning('用户放弃了操作');
        }
    }
    else {
        ElMessage.warning('用户放弃了操作');
    }
};
onBeforeMount(() => {
   
});
</script>

<style scoped>

</style>