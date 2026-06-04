<template>
    <div class="admin-layout">
        <!-- 顶部导航栏 -->
        
        <el-header class="header">
            <div class="header-left">
                <div class="logo">
                    <div class="logo-image">
                        <img src="@/assets/logo.png" alt="系统logo">
                    </div>
                    <div class="logo-content">
                        <h1 class="system-title">医院床位预约管理系统</h1>
                        <p class="system-subtitle">Hospital Bed Reservation Management System</p>
                    </div>
                </div>
            </div>
            <div class="header-right">
                <el-dropdown>
                    <div class="user-info">
                        <el-avatar :size="32" :src="UserInfo.ImageUrls || defaultAvatar" />
                        <div class="user-details">
                            <span class="username">{{ UserInfo.UserName }}</span>
                            <span class="role">{{ RoleType }}</span>
                        </div>
                        <el-icon><arrow-down /></el-icon>
                    </div>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item @click="handleUserInfo">
                                <el-icon>
                                    <User />
                                </el-icon>个人信息
                            </el-dropdown-item>
                            <el-dropdown-item @click="handlePasswordEdit">
                                <el-icon>
                                    <Lock />
                                </el-icon>修改密码
                            </el-dropdown-item>
                            <el-dropdown-item @click="handleLogout">
                                <el-icon>
                                    <SwitchButton />
                                </el-icon>退出登录
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
        </el-header>
 			  
        <!-- 主体内容区 -->
        <el-container class="main-container">
            <!-- 侧边栏 -->
            <el-aside width="200px">
                <el-menu default-active="1" :router="true" class="el-menu-vertical" :collapse="isCollapse"
                    background-color="transparent" text-color="#e6f4ff" active-text-color="#91caff">
                    <el-sub-menu index="UserList">
                        <template #title>
                            <el-icon>
                                <setting />
                            </el-icon>
                            <span>系统管理</span>
                        </template>
                        <el-menu-item index="/Admin/UserList">账号管理</el-menu-item>
                  
                    </el-sub-menu>
							 
 								 <el-menu-item index="/Admin/admissionList">
                               <el-icon>
                           			 <document />
                        			</el-icon>
                            入出院登记
                        </el-menu-item>
 								 <el-menu-item index="/Admin/bedList">
                               <el-icon>
                           			 <document />
                        			</el-icon>
                            床位信息
                        </el-menu-item>
 								 <el-menu-item index="/Admin/bed_reservationList">
                               <el-icon>
                           			 <document />
                        			</el-icon>
                            床位预约
                        </el-menu-item>
 								 <el-menu-item index="/Admin/notificationList">
                               <el-icon>
                           			 <document />
                        			</el-icon>
                            系统通知
                        </el-menu-item>
 								 <el-menu-item index="/Admin/userList">
                               <el-icon>
                           			 <document />
                        			</el-icon>
                            用户信息
                        </el-menu-item>
 								 <el-menu-item index="/Admin/wardList">
                               <el-icon>
                           			 <document />
                        			</el-icon>
                            病区信息
                        </el-menu-item>
 								 <el-menu-item index="/Admin/bedPoolList">
                               <el-icon>
                           			 <document />
                        			</el-icon>
                            床位池查看
                        </el-menu-item>
                </el-menu>
            </el-aside>
				 
            <!-- 主要内容区 -->
            <el-main class="main-content">
                <!-- 面包屑导航 -->
                <el-breadcrumb separator-class="el-icon-arrow-right" class="breadcrumb">
                    <el-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index" :to="item.path">
                        {{ item.title }}
                    </el-breadcrumb-item>
                    <!-- 返回链接 -->
                    <span class="back-link" @click="goBack">
                        <el-icon>
                            <back />
                        </el-icon>返回
                    </span>
                </el-breadcrumb>
                <router-view></router-view>
            </el-main>
             
        </el-container>
    </div>
</template>

<script setup>
import defaultAvatar from '@/assets/默认头像.png'; // 导入默认头像图片
import { useCommonStore } from '@/store';
import { ArrowDown, Back, Document, Lock, Setting, SwitchButton, User } from '@element-plus/icons-vue';
import { computed, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const commonStore = useCommonStore();
const router = useRouter();

// 计算属性
const Token = computed(() => commonStore.Token)
const UserInfo = computed(() => commonStore.UserInfo)
const RoleType = computed(() => commonStore.RoleType)
const UserId = computed(() => commonStore.UserId)

// 控制侧边栏折叠状态
const isCollapse = ref(false)

// 面包屑数据
const breadcrumbList = ref([])
const route = useRoute()

// 监听路由变化，更新面包屑
watch(
    () => route.matched,
    (matched) => {
        // 如果不是首页，添加首页到面包屑
        if (!isHome(matched[0])) {
            breadcrumbList.value = [
                { title: '控制台', path: '/' },
                ...matched.map(item => ({
                    title: item.meta.title || item.name,
                    path: item.path
                }))
            ]
        } else {
            breadcrumbList.value = matched.map(item => ({
                title: item.meta.title || item.name,
                path: item.path
            }))
        }
    },
    { immediate: true }
)

function isHome(route) {
    return route.path === "/Admin";
}
function handleLogout() {
    commonStore.Logout();
    router.push("/Login");
}
function goBack() {
    router.back();
}
function handleUserInfo() {
    router.push("/Admin/UserPerson");
}
function handlePasswordEdit() {
    router.push("/Admin/PasswordEdit");
}
</script>

<style scoped lang="scss">
/* 整体布局样式 */
.admin-layout {
    height: 100vh;
    display: flex;
    flex-direction: column;
}

/* 顶部导航栏样式 */
.header {
    background: linear-gradient(90deg, #1890ff 0%, #40a9ff 100%);
    border-bottom: none;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    height: 60px;
    box-shadow: 0 2px 8px rgba(24, 144, 255, 0.2);
}

/* 左侧区域样式 */
.header-left {
    display: flex;
    align-items: center;
}

/* Logo样式 */
.logo {
    display: flex;
    align-items: center;
    gap: 15px;
    padding: 5px 0;
}

.logo-image {
    height: 40px;
    display: flex;
    align-items: center;
}

.logo-image img {
    height: 100%;
    width: auto;
    object-fit: contain;
}

.logo-content {
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.system-title {
    font-size: 18px;
    font-weight: 600;
    color: #ffffff;
    margin: 0;
    line-height: 1.2;
    letter-spacing: 1px;
}

.system-subtitle {
    font-size: 11px;
    color: rgba(255, 255, 255, 0.85);
    margin: 0;
    line-height: 1.2;
}

/* 右侧用户信息样式 */
.header-right {
    display: flex;
    align-items: center;
}

.user-info {
    display: flex;
    align-items: center;
    gap: 12px;
    cursor: pointer;
    padding: 4px 8px;
    border-radius: 4px;
    transition: background-color 0.3s;
}

.user-info:hover {
    background-color: rgba(255, 255, 255, 0.15);
}

.user-details {
    display: flex;
    flex-direction: column;
    line-height: 1.2;
}

.username {
    font-size: 14px;
    color: #ffffff;
    font-weight: 500;
}

.role {
    font-size: 12px;
    color: rgba(255, 255, 255, 0.8);
}

/* 下拉菜单项样式 */
:deep(.el-dropdown-menu__item) {
    display: flex;
    align-items: center;
    gap: 8px;
}

:deep(.el-dropdown-menu__item .el-icon) {
    margin-right: 4px;
}

/* 主体容器样式 */
.main-container {
    flex: 1;
    overflow: hidden;
}

/* 侧边栏样式 */
.el-aside {
    background: linear-gradient(180deg, #1f3a5f 0%, #2c5282 100%);
    height: 100%;
}

/* 菜单样式 */
.el-menu-vertical {
    border-right: none;
}

/* 主内容区样式 */
.main-content {
    background: linear-gradient(135deg, #f0f7ff 0%, #e6f4ff 100%);
    padding: 20px;
    height: calc(100vh - 60px);
    overflow-y: scroll;
    box-sizing: border-box;
}

/* 面包屑样式 */
.breadcrumb {
    margin-bottom: 20px;
    padding: 10px;
    align-items: center;
    display: flex;
    background-color: #fff;
    border-radius: 4px;
}

/* 返回链接样式 */
.back-link {
    margin-left: auto;
    cursor: pointer;
    color: #1890ff;
    display: flex;
    align-items: center;
    gap: 4px;
}

.back-link:hover {
    color: #40a9ff;
}
</style>
