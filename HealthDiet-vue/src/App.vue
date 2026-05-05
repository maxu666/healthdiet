<template>
  <router-view v-if="route.path === '/login'" />
  <el-container v-else class="layout-container">

    <el-header class="layout-header">
      <Header />
    </el-header>

    <el-container class="layout-body">

      <el-aside class="layout-aside">
        <LeftMenu />
      </el-aside>

      <el-main class="layout-main">
        <router-view />
      </el-main>

    </el-container>

    <el-footer class="layout-footer">
      <Footer />
    </el-footer>

  </el-container>
</template>

<script setup>
import Header from './components/Header.vue'
import LeftMenu from './components/LeftMenu.vue'
import Footer from './components/Footer.vue'
import { useRoute } from 'vue-router'

const route = useRoute()
</script>

<style scoped>
.layout-container {
  height: 100vh;
  background: #f0f2f5;
}

.layout-header {
  padding: 0;
  height: 60px;
  background: #f9a916;
}

.layout-body {
  height: calc(100vh - 60px - 40px);
}

.layout-aside {
  width: 200px;
  background: #fff7ed;
  overflow: hidden;
}

.layout-main {
  background: #fffaf5;
  padding: 20px;
  overflow-y: auto;
}

.layout-footer {
  padding: 0;
  height: 40px;
  background: #304156;
}
</style>