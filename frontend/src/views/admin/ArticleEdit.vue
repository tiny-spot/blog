<template>
  <div>
    <h2>{{ isEdit ? '编辑文章' : '新建文章' }}</h2>
    <el-form label-width="60px">
      <el-form-item label="标题">
        <el-input v-model="form.title" />
      </el-form-item>
      <el-form-item label="内容">
        <RichEditor v-model="form.content" />
      </el-form-item>
      <el-button type="primary" @click="save" :loading="saving">保存</el-button>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getArticle, createArticle, updateArticle } from '@/api/article'
import RichEditor from '@/components/RichEditor.vue'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)
const form = ref({ title: '', content: '' })
const saving = ref(false)

onMounted(async () => {
  if (isEdit.value) {
    const { data } = await getArticle(route.params.id)
    form.value = { title: data.title, content: data.content }
  }
})

const save = async () => {
  saving.value = true
  try {
    if (isEdit.value) {
      await updateArticle(route.params.id, form.value)
    } else {
      await createArticle(form.value)
    }
    router.push('/admin/articles')
  } catch (e) {
    alert(e.response?.data?.error || '保存失败')
  } finally {
    saving.value = false
  }
}
</script>
