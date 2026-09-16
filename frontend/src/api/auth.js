import axios from 'axios'

const api = axios.create({ baseURL: '/api' })

api.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
})

// 响应拦截器：401 自动跳登录
api.interceptors.response.use(
    res => res,
    err => {
        if (err.response?.status === 401) {
            localStorage.removeItem('token')
            window.location.href = '/login'
        }
        return Promise.reject(err)
    }
)

export const register = (data) => api.post('/auth/register', data)
export const login = (data) => api.post('/auth/login', data)

// 新增：需要鉴权的接口
export const getMe = () => api.get('/user/me')
export const updateProfile = (data) => api.post('/user/profile', data)
