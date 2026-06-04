<template>
    <!-- 页脚组件 -->
    <el-footer class="footer">
      <div class="footer-content">© 2026 医院床位预约管理系统 版权所有</div>
    </el-footer>
</template>
  
<script setup>
// 使用setup语法糖，无需手动返回
</script>
  
<style scoped lang="scss">
/* 页脚容器样式 */
.footer {
    background: linear-gradient(90deg, #1890ff 0%, #40a9ff 100%);
    padding: 20px 0;
}

/* 页脚内容样式 */
.footer-content {
    text-align: center;
    color: white;
    font-weight: bold;
}
</style>
