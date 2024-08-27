import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    name: 'John Doe',
  }),
  actions: {
    setUser(user) {
      this.user = user
    },
    logout() {
      this.user = null
    },
  },
})
