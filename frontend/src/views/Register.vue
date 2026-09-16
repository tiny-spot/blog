<template>
  <el-card class="auth-card">
    <h2>注册</h2>
    <el-form @submit.prevent="handleRegister">
      <el-form-item>
        <el-input v-model="form.email" placeholder="邮箱" />
      </el-form-item>
      <el-form-item>
        <el-input v-model="form.password" type="password" placeholder="密码" />
      </el-form-item>
      <el-button type="primary" native-type="submit" :loading="loading">注册</el-button>
    </el-form>
    <p>已有账号？<router-link to="/login">登录</router-link></p>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'
import { register } from '@/api/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const form = ref({ email: '', password: '' })
const loading = ref(false)

const handleRegister = async () => {
  loading.value = true
  try {
    await register(form.value)
    alert('注册成功，请登录')
    router.push('/login')
  } catch (error) {
    alert(error.response?.data?.error || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-card { max-width: 400px; margin: 50px auto; padding: 20px; }
</style>
