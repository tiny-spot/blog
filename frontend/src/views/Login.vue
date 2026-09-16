<template>
  <el-card class="auth-card">
    <h2>登录</h2>
    <el-form @submit.prevent="handleLogin">
      <el-form-item>
        <el-input v-model="form.email" placeholder="邮箱" />
      </el-form-item>
      <el-form-item>
        <el-input v-model="form.password" type="password" placeholder="密码" />
      </el-form-item>
      <el-button type="primary" native-type="submit" :loading="loading">登录</el-button>
    </el-form>
    <p>还没有账号？<router-link to="/register">注册</router-link></p>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'
import { login } from '@/api/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const form = ref({ email: '', password: '' })
const loading = ref(false)

const handleLogin = async () => {
  loading.value = true
  try {
    const { data } = await login(form.value)
    localStorage.setItem('token', data.token)
    localStorage.setItem('email', data.email)
    localStorage.setItem('role', data.role)

    if (data.role === 'ADMIN') {
      router.push('/admin/articles')
    } else {
      router.push('/')
    }
  } catch (error) {
    alert(error.response?.data?.error || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-card { max-width: 400px; margin: 50px auto; padding: 20px; }
</style>
