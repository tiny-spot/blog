<template>
  <div>
    <div class="toolbar">
      <h2>文章管理</h2>
      <el-button type="primary" @click="$router.push('/admin/article/edit')">新建文章</el-button>
    </div>

    <el-table :data="articles" style="width:100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="createTime" label="创建时间" width="200" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/admin/article/edit/${row.id}`)">编辑</el-button>
          <el-button size="small" type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      layout="prev, pager, next, total"
      :total="total"
      :page-size="size"
      :current-page="page"
      @current-change="handlePageChange"
      style="margin-top:20px;justify-content:flex-end"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listArticles, deleteArticle } from '@/api/article'

const articles = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)

const load = async () => {
  const { data } = await listArticles({ page: page.value, size: size.value })
  articles.value = data.records
  total.value = data.total
}

const handlePageChange = (p) => {
  page.value = p
  load()
}

const remove = async (id) => {
  if (!confirm('确认删除？')) return
  await deleteArticle(id)
  load()
}

onMounted(load)
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; }
</style>
