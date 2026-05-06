import axios from "axios"

const request = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 30000
})

// 请求拦截器
request.interceptors.request.use(config => {
  const user = localStorage.getItem("user")
  if (user) {
    const token = JSON.parse(user).token
    if (token) {
      config.headers.Authorization = token
    }
  }
  return config
})

// 响应拦截器
request.interceptors.response.use(
  res => res.data,
  err => Promise.reject(err)
)

export default request