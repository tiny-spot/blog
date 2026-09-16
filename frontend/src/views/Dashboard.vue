<template>
  <div style="text-align:center;margin-top:100px">
    <h1>登录成功 🎉</h1>
    <p>当前用户：{{ email }}</p>

    <el-card style="max-width:500px;margin:20px auto;text-align:left">
      <h3>调用受保护接口 /api/user/me</h3>
      <el-button @click="loadMe" :loading="loadingMe">获取我的信息</el-button>
      <pre v-if="meResult">{{ meResult }}</pre>
    </el-card>

    <el-card style="max-width:500px;margin:20px auto;text-align:left">
      <h3>调用受保护接口 /api/user/profile</h3>
      <el-input v-model="nickname" placeholder="输入昵称" style="margin-bottom:10px" />
      <el-button @click="saveProfile" :loading="loadingProfile">更新资料</el-button>
      <pre v-if="profileResult">{{ profileResult }}</pre>
    </el-card>

    <el-button type="danger" @click="logout">退出登录</el-button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getMe, updateProfile } from '@/api/auth'

const router = useRouter()
const email = ref(localStorage.getItem('email') || '')

const meResult = ref('')
const loadingMe = ref(false)

const nickname = ref('')
const profileResult = ref('')
const loadingProfile = ref(false)

const loadMe = async () => {
  loadingMe.value = true
  try {
    const { data } = await getMe()
    meResult.value = JSON.stringify(data, null, 2)
  } catch (e) {
    meResult.value = '请求失败：' + (e.response?.data?.error || e.message)
  } finally {
    loadingMe.value = false
  }
}

const saveProfile = async () => {
  loadingProfile.value = true
  try {
    const { data } = await updateProfile({ nickname: nickname.value })
    profileResult.value = JSON.stringify(data, null, 2)
  } catch (e) {
    profileResult.value = '请求失败：' + (e.response?.data?.error || e.message)
  } finally {
    loadingProfile.value = false
  }
}

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('email')
  router.push('/login')
}
</script>
