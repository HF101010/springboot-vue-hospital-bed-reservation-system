<template>
    <div>
        <el-container>
            <el-header style="background: linear-gradient(90deg, #1890ff 0%, #40a9ff 100%);width: 100%;position: fixed;z-index:1000;padding:0px;box-shadow: 0 2px 8px rgba(24, 144, 255, 0.2);">
                <div class="header-container">
                    <!-- 左侧logo和系统名称 -->
                    <div class="left-section">
                        <img class="logo" src="@/assets/logo.png" alt="logo">
                        <span class="system-name">医院床位预约管理系统</span>
                    </div>

                    <!-- 中间菜单区域 -->
                    <div class="menu-section">
                        <div class="menu-item" :class="{ active: active === '/Front/Reservation' }" @click="ToPath('/Front/Reservation')">
                            床位预约</div>
                        <div class="menu-item" :class="{ active: active === '/Front/UserCenter' }" @click="ToPath('/Front/UserCenter')">
                            账号管理</div>
                        <div class="menu-item" :class="{ active: active === '/Front/Notification' }" @click="ToPath('/Front/Notification')">
                            系统通知</div>
                    </div>
							   
                    <!-- 右侧用户信息 -->
                    <div class="right-section">
                        <template v-if="!Token">
                            <el-button type="text" @click="ToRegister()">注册</el-button>
                            <el-button type="text" @click="ToLogin()">登录</el-button>
                        </template>
	
                        <el-dropdown v-if="Token" trigger="click">
                            <div class="user-info">
                                <el-avatar :size="32" :src="UserInfo.ImageUrls || defaultAvatar" />
                                <span class="username">{{ UserInfo.UserName }}</span>
                            </div>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item @click="ToUserInfo()">
                                        <el-icon>
                                            <User />
                                        </el-icon>
                                        <span>个人中心</span>
                                    </el-dropdown-item>

                                    <el-dropdown-item @click="LoginOut()">
                                        <el-icon>
                                            <SwitchButton />
                                        </el-icon>
                                        <span>退出</span>
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                                     
                </div>
            </el-header>
            <div style="height: 50px;"></div>
            <el-main class="main-container main-box">
                <router-view></router-view>
            </el-main>
            <div style="height: 20px;"></div>
            <Footer></Footer>
        </el-container>
    </div>
</template>

<script setup>
import defaultAvatar from '@/assets/默认头像.png'; // 导入默认头像图片
import { useCommonStore } from '@/store';
import { SwitchButton, User } from '@element-plus/icons-vue';
import { computed, ref, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import Footer from "./Footer.vue";

// 获取store和router实例
const commonStore = useCommonStore()
const router = useRouter()
const route = useRoute()

// 响应式数据
const active = ref(route.path)

// 计算属性
const Token = computed(() => commonStore.Token)
const UserInfo = computed(() => commonStore.UserInfo)
const RoleType = computed(() => commonStore.RoleType)
const UserId = computed(() => commonStore.UserId)

// 方法定义
// 前往登录
const ToLogin = () => {
    router.push({
        path: "/Login"
    })
}

// 前往注册
const ToRegister = () => {
    router.push({
        path: "/Register"
    })
}

// 退出
const LoginOut = async () => {
    await commonStore.Logout()
    active.value = ''
    router.replace({
        path: "/Login"
    })
}

// 跳转
const ToPath = async (url) => {
    router.push({
        path: url
    })
    active.value = url
}

// 监听路由变化
watch(() => route.path, (newPath) => {
    active.value = newPath
}, { immediate: true })

// 跳转到用户信息
const ToUserInfo = async () => {
    router.push({
        path: "/Front/UserCenter"
    })
}

// 跳转到修改密码
const ToEditPassword = async () => {
    router.push({
        path: "/Front/PasswordEdit"
    })
}

// 返回上一个页面
const goBack = () => {
    router.go(-1)
}
</script>

<style scoped lang="scss">
/* 头部容器 */
.header-container {
    width: var(--content-width);
    height: 60px;
    margin: 0 auto;

    display: flex;
    justify-content: space-between;
    align-items: center;
}

/* 左侧区域 */
.left-section {
    display: flex;
    align-items: center;
    gap: 20px;
}

/* Logo样式 */
.logo {
    height: 50px;
    object-fit: contain;
}

/* 系统名称 */
.system-name {
    font-size: 18px;
    color: #ffffff;
    font-weight: 600;
    letter-spacing: 1px;
}

/* 中间菜单区域 */
.menu-section {
    display: flex;
    align-items: center;
    gap: 30px;
}

/* 菜单项样式 */
.menu-item {
    font-size: 15px;
    color: rgba(255, 255, 255, 0.9);
    cursor: pointer;
    padding: 0 4px;
    position: relative;
    transition: all 0.3s ease;
    height: 60px;
    line-height: 60px;
    overflow: hidden;
}

/* 菜单项悬停效果 */
.menu-item:hover {
    color: #ffffff;
    transform: translateY(-2px);
}

/* 菜单项下划线效果 */
.menu-item::before {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 2px;
    background-color: #ffffff;
    transform: scaleX(0);
    transform-origin: right;
    transition: transform 0.3s ease;
}

/* 菜单项悬停时下划线效果 */
.menu-item:hover::before {
    transform: scaleX(1);
    transform-origin: left;
}

/* 菜单项选中状态 */
.menu-item.active {
    color: #ffffff;
    font-weight: 500;
}

.menu-item.active::before {
    transform: scaleX(1);
}

/* 菜单项点击效果 */
.menu-item:active {
    transform: translateY(1px);
}

/* 右侧区域 */
.right-section {
    display: flex;
    align-items: center;
    gap: 16px;
}

/* 用户信息样式 */
.user-info {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    padding: 4px 8px;
    border-radius: 4px;
    transition: all 0.3s ease;
}

.user-info:hover {
    background-color: rgba(255, 255, 255, 0.15);
    transform: translateY(-1px);
}

/* 用户名样式 */
.username {
    font-size: 14px;
    color: #ffffff;
}

/* 下拉菜单项样式 */
:deep(.el-dropdown-menu__item) {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 16px;
    transition: all 0.3s ease;
}

:deep(.el-dropdown-menu__item:hover) {
    background-color: #f5f7fa;
    transform: translateX(4px);
}

/* 按钮样式 */
.el-button--text {
    font-size: 14px;
    color: #ffffff;
    transition: all 0.3s ease;
}

.el-button--text:hover {
    color: rgba(255, 255, 255, 0.8);
    transform: translateY(-1px);
}

/* 主容器样式 */
.main-container {
    position: relative;
    width: var(--content-width);
    margin: 0 auto;
    background: linear-gradient(135deg, #f0f7ff 0%, #e6f4ff 100%);
}

/* 主内容区域最小高度 */
.main-box {
    min-height: calc(100vh - 120px);
}

/* 主内容区域padding调整 */
.el-main {
    padding: 20px 0px 0px 0px !important;
    overflow: hidden;
}
</style>
