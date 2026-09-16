<template>
  <div class="container">
    <div class="header">
      <h1>文章列表</h1>
      <div v-if="!isLogin">
        <router-link to="/login">登录</router-link> |
        <router-link to="/register">注册</router-link>
      </div>
      <div v-else>
        欢迎，{{ email }}
        <el-button size="small" @click="logout">退出</el-button>
      </div>
    </div>

    <div class="search">
      <el-input v-model="keyword" placeholder="搜索标题" clearable @keyup.enter="search" />
      <el-button @click="search">搜索</el-button>
    </div>

    <el-card v-for="a in articles" :key="a.id" class="article-card">
      <h3 @click="$router.push(`/article/${a.id}`)">{{ a.title }}</h3>
      <p>{{ stripHtml(a.content) }}...</p>
      <small>{{ a.createTime }}</small>
    </el-card>

    <el-empty v-if="!articles.length" description="暂无文章" />

    <el-pagination
      v-if="total > 0"
      background
      layout="prev, pager, next, total"
      :total="total"
      :page-size="size"
      :current-page="page"
      @current-change="handlePageChange"
      class="pagination"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { listArticles } from '@/api/article'

const router = useRouter()
const articles = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const keyword = ref('')
const isLogin = computed(() => !!localStorage.getItem('token'))
const email = localStorage.getItem('email')

const stripHtml = (html) => {
  if (!html) return ''
  return html.replace(/<[^>]+>/g, '').slice(0, 80)
}

const load = async () => {
  const { data } = await listArticles({
    page: page.value,
    size: size.value,
    keyword: keyword.value
  })
  articles.value = data.records
  total.value = data.total
}

const search = () => {
  page.value = 1
  load()
}

const handlePageChange = (p) => {
  page.value = p
  load()
}

const logout = () => {
  localStorage.clear()
  router.push('/login')
}

onMounted(load)
</script>

<style scoped>
.container { max-width: 800px; margin: 30px auto; }
.header { display: flex; justify-content: space-between; align-items: center; }
.search { display: flex; gap: 10px; margin: 15px 0; }
.article-card { margin-bottom: 15px; cursor: pointer; }
.pagination { margin-top: 20px; text-align: center; }
</style>
