import axios from 'axios'

const api = axios.create({ baseURL: '/api' })

api.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
})

// 公开
export const listArticles = (params) => api.get('/article/list', { params })
export const getArticle = (id) => api.get(`/article/${id}`)
export const listComments = (articleId, params) =>
    api.get(`/comment/${articleId}`, { params })

// 需登录
export const addComment = (data) => api.post('/comment', data)

// 仅 ADMIN
export const createArticle = (data) => api.post('/admin/article', data)
export const updateArticle = (id, data) => api.put(`/admin/article/${id}`, data)
export const deleteArticle = (id) => api.delete(`/admin/article/${id}`)

export default api
