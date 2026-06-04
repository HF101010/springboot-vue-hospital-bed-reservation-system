<template>
    <div class="user-center">
        <!-- 面包屑导航 -->
        <div class="breadcrumb">
            <span class="breadcrumb-label">您当前的位置:</span>
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>个人中心</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <!-- 用户信息卡片 -->
        <div class="user-card">
            <div class="user-banner" :style="{ backgroundImage: `url(${userInfo.BackgroundImage})` }">
                <div class="user-avatar">
                    <img :src="userInfo.ImageUrls || defaultAvatar" :alt="userInfo.Name">
                </div>
            </div>
            <div class="user-info">
                <div class="user-basic">
                    <h2>{{ userInfo.Name }}</h2>
                    <span class="role-tag">{{ userInfo.RoleTypeFormat }}</span>
                </div>
                <p class="user-intro">{{ userInfo.Introduction }}</p>
                <div class="user-details">
                    <div class="detail-item">
                        <i class="el-icon-user"></i>
                        <span>{{ userInfo.UserName }}</span>
                    </div>
                    <div class="detail-item">
                        <i class="el-icon-phone"></i>
                        <span>{{ userInfo.PhoneNumber }}</span>
                    </div>
                    <div class="detail-item">
                        <i class="el-icon-message"></i>
                        <span>{{ userInfo.Email }}</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- 功能菜单和内容区域 -->
        <div class="user-content">
            <div class="function-menu">
                <div class="menu-scroll">
                    <div class="menu-item" v-for="(item, index) in menuItems" :key="index"
                        :class="{ active: currentMenu === item.key }" @click="switchMenu(item.key)">
                        <span class="menu-icon">{{ item.icon }}</span>
                        <span>{{ item.name }}</span>
                    </div>
                </div>
            </div>

            <!-- 内容显示区域 -->
            <div class="content-area">
                <Suspense>
                    <template #default>
                        <component :is="currentComponent"></component>
                    </template>
                    <template #fallback>
                        <div class="loading">加载中...</div>
                    </template>
                </Suspense>
            </div>
        </div>
    </div>
</template>

<script setup>
import defaultAvatar from '@/assets/默认头像.png'; // 导入默认头像图片
import { useCommonStore } from '@/store';
import { computed, defineAsyncComponent, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// 获取store和router实例
const commonStore = useCommonStore()
const router = useRouter()
const route = useRoute()

// 从store获取用户信息
const userInfo = computed(() => commonStore.UserInfo)

// 菜单相关数据
const currentMenu = ref('UserInfoEdit')
const menuItems = [
    { name: '个人资料', icon: '👤', key: 'UserInfoEdit' },
    { name: '修改密码', icon: '🔒', key: 'PasswordEdit' },
   
]

// 预加载组件
const UserInfoEdit = defineAsyncComponent(() => import("@/views/Front/UserInfoEdit.vue"))
const PasswordEdit = defineAsyncComponent(() => import("@/views/Front/PasswordEdit.vue"))

// 动态组件计算属性
const currentComponent = computed(() => {
    const componentMap = {
        UserInfoEdit,
        PasswordEdit,
      
    }
    return componentMap[currentMenu.value]
})

// 切换菜单方法
const switchMenu = (key) => {
    currentMenu.value = key
    router.replace({
        query: {
            currentMenu: key
        }
    })
}

// 组件挂载时设置当前菜单
onMounted(() => {
    currentMenu.value = route.query.currentMenu || 'UserInfoEdit'
})
</script>

<style scoped lang="scss">
/* 整体容器 */
.user-center {

    background-color: #f5f5f5;
    min-height: 100vh;
}


/* 用户信息卡片 */
.user-card {
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    overflow: hidden;
}

/* 用户背景横幅 */
.user-banner {
    height: 80px;
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    position: relative;
}

/* 用户头像 */
.user-avatar {
    position: absolute;
    bottom: -40px;
    left: 40px;

    img {
        width: 80px;
        height: 80px;
        border-radius: 50%;
        border: 4px solid #fff;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    }
}

/* 用户信息区域 */
.user-info {
    padding: 50px 40px 30px;
}

/* 用户基本信息 */
.user-basic {
    display: flex;
    align-items: center;
    margin-bottom: 10px;

    h2 {
        margin: 0;
        margin-right: 10px;
    }
}



/* 角色标签 */
.role-tag {
    padding: 2px 10px;
    background: #409EFF;
    color: #fff;
    border-radius: 12px;
    font-size: 12px;
}

/* 用户简介 */
.user-intro {
    color: #666;
    margin-bottom: 20px;
}

/* 用户详细信息 */
.user-details {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
}

.detail-item {
    display: flex;
    align-items: center;
    color: #666;

    i {
        margin-right: 5px;
    }
}

/* 用户内容区域布局 */
.user-content {
    display: flex;
    gap: 20px;
    margin-top: 20px;
}

/* 功能菜单样式调整 */
.function-menu {
    flex: 0 0 250px;
    /* 固定宽度250px */
    background: #fff;
    border-radius: 8px;
    padding: 20px;
}

/* 菜单滚动区域改为垂直布局 */
.menu-scroll {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

/* 菜单项样式调整 */
.menu-item {
    flex-direction: row;
    justify-content: flex-start;
    padding: 12px 20px;
    border-radius: 6px;
    cursor: pointer;

    .menu-icon {
        margin-right: 10px;
        font-size: 18px;
    }

    &.active {
        background: var(--el-color-primary);
        color: #fff;

    }
}

/* 内容显示区域 */
.content-area {
    flex: 1;
    /* 占据剩余所有空间 */
    border-radius: 8px;
    min-height: 500px;

    overflow: hidden;

}

/* 加载状态样式 */
.loading {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 200px;
    color: #666;
    font-size: 16px;
}
</style>