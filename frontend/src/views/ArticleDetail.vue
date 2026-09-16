<template>
  <div class="container" v-if="article">
    <h1>{{ article.title }}</h1>
    <p class="meta">{{ article.createTime }}</p>
    <div class="content" v-html="article.content"></div>

    <hr />
    <h3>评论（{{ commentTotal }}）</h3>

    <div v-if="isLogin" class="comment-form">
      <el-input v-model="newComment" type="textarea" placeholder="写下你的评论..." />
      <el-button type="primary" @click="submitComment" :loading="submitting">发表评论</el-button>
    </div>
    <div v-else class="tip">
      <router-link to="/login">登录</router-link> 后参与评论
    </div>

    <div v-for="c in comments" :key="c.id" class="comment">
      <strong>{{ c.email }}</strong>
      <span class="time">{{ c.createTime }}</span>
      <p>{{ c.content }}</p>
    </div>

    <el-pagination
      v-if="commentTotal > 0"
      background
      layout="prev, pager, next, total"
      :total="commentTotal"
      :page-size="commentSize"
      :current-page="commentPage"
      @current-change="handleCommentPageChange"
      class="pagination"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getArticle, listComments, addComment } from '@/api/article'

const route = useRoute()
const article = ref(null)
const comments = ref([])
const commentTotal = ref(0)
const commentPage = ref(1)
const commentSize = ref(20)
const newComment = ref('')
const submitting = ref(false)
const isLogin = computed(() => !!localStorage.getItem('token'))

const loadComments = async () => {
  const { data } = await listComments(route.params.id, {
    page: commentPage.value,
    size: commentSize.value
  })
  comments.value = data.records
  commentTotal.value = data.total
}

const handleCommentPageChange = (p) => {
  commentPage.value = p
  loadComments()
}

onMounted(async () => {
  const { data } = await getArticle(route.params.id)
  article.value = data
  await loadComments()
})

const submitComment = async () => {
  if (!newComment.value.trim()) return
  submitting.value = true
  try {
    await addComment({ articleId: route.params.id, content: newComment.value })
    newComment.value = ''
    commentPage.value = 1
    await loadComments()
  } catch (e) {
    alert(e.response?.data?.error || '评论失败')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.container { max-width: 800px; margin: 30px auto; }
.meta { color: #999; font-size: 13px; }
.content { margin: 20px 0; line-height: 1.8; }
.content :deep(img) { max-width: 100%; height: auto; }
.content :deep(blockquote) {
  border-left: 4px solid #ddd;
  padding-left: 10px;
  color: #666;
  margin: 10px 0;
}
.content :deep(pre) {
  background: #f6f8fa;
  padding: 10px;
  border-radius: 4px;
  overflow-x: auto;
}
.comment-form { margin: 15px 0; }
.comment-form .el-button { margin-top: 10px; }
.tip { color: #999; margin: 15px 0; }
.comment { border-bottom: 1px solid #eee; padding: 10px 0; }
.comment .time { color: #999; font-size: 12px; margin-left: 10px; }
.pagination { margin-top: 20px; text-align: center; }
</style>
