<template>
    <div class="notification-page">
        <PaginationTable ref="PaginationTableId" url="/notification/MyList" :column="columnList" :where="where">
            <template #header>
                <el-form :inline="true" :model="searchForm" class="search-form">
                    <el-form-item label="标题">
                        <el-input v-model="searchForm.Keyword" placeholder="请输入标题关键字" clearable style="width: 200px" />
                    </el-form-item>
                    <el-form-item label="是否已读">
                        <el-select v-model="searchForm.IsRead" placeholder="全部" clearable style="width: 120px">
                            <el-option label="未读" :value="false" />
                            <el-option label="已读" :value="true" />
                        </el-select>
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
            <template #Operate="{ row }">
                <el-space>
                    <el-button type="primary" text size="small" @click="ShowDetail(row.Id)">查看详情</el-button>
                    <el-button v-if="!row.is_read" type="success" text size="small" @click="MarkAsRead(row.Id)">标记已读</el-button>
                </el-space>
            </template>
        </PaginationTable>

        <el-dialog title="通知详情" v-model="detailShow" width="600px" destroy-on-close>
            <div v-if="detailData" class="notification-detail">
                <div class="detail-item">
                    <span class="label">标题：</span>
                    <span class="value">{{ detailData.title }}</span>
                </div>
                <div class="detail-item">
                    <span class="label">内容：</span>
                    <div class="value content">{{ detailData.content }}</div>
                </div>
                <div class="detail-item">
                    <span class="label">发送时间：</span>
                    <span class="value">{{ detailData.create_time }}</span>
                </div>
                <div class="detail-item">
                    <span class="label">状态：</span>
                    <el-tag :type="detailData.is_read ? 'success' : 'warning'">
                        {{ detailData.is_read ? '已读' : '未读' }}
                    </el-tag>
                </div>
            </div>
            <template #footer>
                <el-button v-if="detailData && !detailData.is_read" type="primary" @click="MarkAsRead(detailData.Id)">标记已读</el-button>
                <el-button @click="detailShow = false">关 闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { Post } from '@/api/http'
import { ColumnType } from '@/components/Tables/columnTypes'
import { Refresh, Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

const where = reactive({})

const searchForm = reactive({
    Keyword: '',
    IsRead: null
})

const columnList = ref([
    { key: 'Id', hidden: true },
    { title: '标题', key: 'title', type: ColumnType.SHORTTEXT, width: '200px' },
    { title: '内容', key: 'content', type: ColumnType.LONGTEXT, width: '300px' },
    { title: '发送时间', key: 'create_time', type: ColumnType.DATE, width: '180px' },
    { title: '是否已读', key: 'IsReadTag', type: ColumnType.USERDEFINED, width: '100px' },
    { title: '操作', key: 'Operate', type: ColumnType.USERDEFINED, width: '180px' }
])

const PaginationTableId = ref(null)
const detailShow = ref(false)
const detailData = ref(null)

const SearchClick = () => {
    PaginationTableId.value?.Reload(searchForm)
}

const ResetClick = () => {
    Object.keys(searchForm).forEach(key => {
        if (key === 'IsRead') {
            searchForm[key] = null
        } else {
            searchForm[key] = ''
        }
    })
    PaginationTableId.value?.Reload(searchForm)
}

const ShowDetail = async (Id) => {
    try {
        const { Data } = await Post('/notification/Get', { Id })
        detailData.value = Data
        detailShow.value = true
        // 如果未读，自动标记为已读
        if (Data && !Data.is_read) {
            await MarkAsRead(Id, false)
        }
    } catch (error) {
        ElMessage.error('获取通知详情失败')
    }
}

const MarkAsRead = async (Id, showMessage = true) => {
    try {
        const { Success } = await Post('/notification/MarkAsRead', { Id })
        if (Success) {
            if (showMessage) {
                ElMessage.success('标记已读成功')
            }
            if (detailData.value && detailData.value.Id === Id) {
                detailData.value.is_read = true
            }
            PaginationTableId.value?.Reload(searchForm)
        }
    } catch (error) {
        ElMessage.error('标记已读失败')
    }
}

onMounted(() => {
    // 页面加载时自动刷新
})
</script>

<style scoped>
.notification-page {
    padding: 20px;
    background: white;
    border-radius: 8px;
    min-height: calc(100vh - 200px);
}

.search-form {
    margin-bottom: 10px;
}

.notification-detail {
    padding: 20px 0;
}

.detail-item {
    margin-bottom: 20px;
    display: flex;
    align-items: flex-start;
}

.detail-item .label {
    font-weight: bold;
    color: #606266;
    min-width: 80px;
}

.detail-item .value {
    color: #303133;
    flex: 1;
}

.detail-item .content {
    white-space: pre-wrap;
    word-break: break-word;
    line-height: 1.6;
    padding: 10px;
    background: #f5f7fa;
    border-radius: 4px;
}
</style>




