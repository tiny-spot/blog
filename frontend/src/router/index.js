import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: () => import('@/views/ArticleList.vue') },
    { path: '/article/:id', component: () => import('@/views/ArticleDetail.vue') },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') },
    {
      path: '/admin',
      component: () => import('@/views/admin/AdminLayout.vue'),
      meta: { requiresAuth: true, role: 'ADMIN' },
      children: [
        { path: 'articles', component: () => import('@/views/admin/ArticleManage.vue') },
        { path: 'article/edit/:id?', component: () => import('@/views/admin/ArticleEdit.vue') }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')

  if (to.meta.requiresAuth && !token) return next('/login')
  if (to.meta.role && to.meta.role !== role) return next('/')
  next()
})

export default router
