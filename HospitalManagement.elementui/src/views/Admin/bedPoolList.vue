<template>
  <div class="bed-pool-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="选择科室">
          <el-select
            v-model="searchForm.department"
            placeholder="请选择科室"
            filterable
            clearable
            :loading="departmentLoading"
            style="width: 200px"
            @change="handleDepartmentChange"
          >
            <el-option
              v-for="item in departmentOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="loadBedPool">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-if="bedPoolData.department" class="summary-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">{{ getDepartmentLabel(bedPoolData.department) }} - 床位池概览</span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-statistic title="总床位数" :value="bedPoolData.totalBeds || 0">
            <template #prefix>
              <el-icon style="vertical-align: -0.125em">
                <House />
              </el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="可用床位" :value="bedPoolData.availableBeds || 0">
            <template #prefix>
              <el-icon style="vertical-align: -0.125em" color="#67c23a">
                <Check />
              </el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="已占用" :value="bedPoolData.occupiedBeds || 0">
            <template #prefix>
              <el-icon style="vertical-align: -0.125em" color="#f56c6c">
                <Close />
              </el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="维修中" :value="bedPoolData.maintenanceBeds || 0">
            <template #prefix>
              <el-icon style="vertical-align: -0.125em" color="#e6a23c">
                <Tools />
              </el-icon>
            </template>
          </el-statistic>
        </el-col>
      </el-row>
    </el-card>

    <el-card v-if="bedPoolData.wards && bedPoolData.wards.length > 0" class="ward-list-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">病区详情</span>
        </div>
      </template>
      <el-collapse v-model="activeWards" accordion>
        <el-collapse-item v-for="ward in bedPoolData.wards" :key="ward.wardId" :name="ward.wardId">
          <template #title>
            <div class="ward-header">
              <span class="ward-name">{{ ward.wardName }}</span>
              <el-tag type="info" size="small" style="margin-left: 10px">总床位 {{ ward.totalBeds }}</el-tag>
              <el-tag type="success" size="small" style="margin-left: 5px">可用: {{ ward.availableBeds }}</el-tag>
              <el-tag type="danger" size="small" style="margin-left: 5px">占用: {{ ward.occupiedBeds }}</el-tag>
              <el-tag type="warning" size="small" style="margin-left: 5px">维修: {{ ward.maintenanceBeds }}</el-tag>
            </div>
          </template>
          <div class="bed-list">
            <el-table :data="ward.beds" border stripe style="width: 100%">
              <el-table-column prop="bedNumber" label="床位编号" width="120" />
              <el-table-column label="床位状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="statusTagType(getStatusValue(row))">
                    {{ getStatusLabel(getStatusValue(row)) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="remark" label="备注" />
            </el-table>
          </div>
        </el-collapse-item>
      </el-collapse>
    </el-card>

    <el-empty v-if="!bedPoolData.department" description="请选择科室查看床位池信息" />
    <el-empty v-else-if="bedPoolData.wards && bedPoolData.wards.length === 0" description="该科室暂无病区信息" />
  </div>
</template>

<script setup>
import { Post } from '@/api/http'
import { getDepartmentLabel, toDepartmentOption } from '@/utils/department'
import { getBedStatusLabel, getBedStatusTagType } from '@/utils/status'
import { Check, Close, House, Refresh, Search, Tools } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

const searchForm = reactive({
  department: ''
})

const bedPoolData = reactive({
  department: '',
  wards: [],
  totalBeds: 0,
  availableBeds: 0,
  occupiedBeds: 0,
  maintenanceBeds: 0
})

const departmentOptions = ref([])
const departmentLoading = ref(false)
const activeWards = ref([])

const fetchDepartments = async () => {
  departmentLoading.value = true
  try {
    const { Data: { Items } } = await Post('/ward/List', { Page: 1, Limit: 1000 })
    const wards = Items || []
    const deptSet = new Set()
    wards.forEach(item => {
      if (item.department) {
        deptSet.add(item.department)
      }
    })
    departmentOptions.value = Array.from(deptSet).map(toDepartmentOption)
  } catch (error) {
    ElMessage.error('获取科室列表失败')
  } finally {
    departmentLoading.value = false
  }
}

const loadBedPool = async () => {
  if (!searchForm.department) {
    ElMessage.warning('请先选择科室')
    return
  }

  try {
    const { Data, Success } = await Post('/ward/GetBedPool', {
      department: searchForm.department
    })
    if (Success && Data) {
      Object.assign(bedPoolData, Data)
      if (Data.wards && Data.wards.length > 0) {
        activeWards.value = [Data.wards[0].wardId]
      }
    } else {
      ElMessage.error('获取床位池信息失败')
    }
  } catch (error) {
    ElMessage.error('获取床位池信息失败：' + (error.message || '未知错误'))
  }
}

const handleDepartmentChange = () => {
  if (searchForm.department) {
    loadBedPool()
  } else {
    resetSearch()
  }
}

const resetSearch = () => {
  searchForm.department = ''
  Object.assign(bedPoolData, {
    department: '',
    wards: [],
    totalBeds: 0,
    availableBeds: 0,
    occupiedBeds: 0,
    maintenanceBeds: 0
  })
  activeWards.value = []
}

const getStatusValue = (row) => {
  if (!row || typeof row !== 'object') {
    return null
  }

  let statusValue = null
  if (row.Status !== undefined && row.Status !== null && row.Status !== '') {
    statusValue = String(row.Status).trim()
  } else if (row.status !== undefined && row.status !== null && row.status !== '') {
    statusValue = String(row.status).trim()
  } else if (row.OrginValue && typeof row.OrginValue === 'object') {
    if (row.OrginValue.Status !== undefined && row.OrginValue.Status !== null && row.OrginValue.Status !== '') {
      statusValue = String(row.OrginValue.Status).trim()
    } else if (row.OrginValue.status !== undefined && row.OrginValue.status !== null && row.OrginValue.status !== '') {
      statusValue = String(row.OrginValue.status).trim()
    }
  }

  if (!statusValue && row.OrginValue && typeof row.OrginValue === 'object' && Object.keys(row.OrginValue).length > 0) {
    const orginKeys = Object.keys(row.OrginValue)
    const statusKey = orginKeys.find(k => k.toLowerCase() === 'status')
    if (statusKey && row.OrginValue[statusKey] !== undefined && row.OrginValue[statusKey] !== null && row.OrginValue[statusKey] !== '') {
      statusValue = String(row.OrginValue[statusKey]).trim()
    }
  }

  return statusValue || null
}

const getStatusLabel = (value) => {
  return getBedStatusLabel(value)
}

const statusTagType = (status) => {
  return getBedStatusTagType(status)
}

onMounted(() => {
  fetchDepartments()
})
</script>

<style scoped>
.bed-pool-page {
  padding: 0;
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 0;
}

.summary-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.ward-list-card {
  margin-bottom: 20px;
}

.ward-header {
  display: flex;
  align-items: center;
  width: 100%;
}

.ward-name {
  font-weight: 600;
  font-size: 15px;
  color: #303133;
}

.bed-list {
  padding: 10px 0;
}

:deep(.el-statistic__head) {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

:deep(.el-statistic__number) {
  font-size: 24px;
  font-weight: 600;
}

:deep(.el-collapse-item__header) {
  font-size: 15px;
  padding: 15px 20px;
}

:deep(.el-collapse-item__content) {
  padding: 15px 20px;
}
</style>
