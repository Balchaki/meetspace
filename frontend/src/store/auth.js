import { defineStore } from 'pinia'
import api from '../services/api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: null,
  }),
  actions: {
    async login(email, password) {
      try {
        const response = await api.post('/api/auth/login', { email, password })
        this.token = response.data.jwt
        localStorage.setItem('jwt_token', this.token)
        // Fetch user data here
      } catch (error) {
        console.error('Login failed:', error)
        throw error
      }
    },
    logout() {
      this.user = null
      this.token = null
      localStorage.removeItem('jwt_token')
    }
  }
})
