<template>
  <el-container>
    <el-aside width="200px">
      <el-menu router :default-active="$route.path">
        <el-menu-item index="/admin/articles">文章管理</el-menu-item>
      </el-menu>
    </el-aside>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>
