<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8 bg-white p-10 rounded-lg shadow-md">
      <div>
        <router-link to="/"><img class="mx-auto h-12 w-auto" src="/assets/img/logo.png" alt="MeetSpace Logo"></router-link>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          Conecte-se
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          Faça login para acessar sua conta
        </p>
      </div>
      <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
        <div class="rounded-md shadow-sm -space-y-px">
          <div>
            <label for="email" class="sr-only">Email</label>
            <input
                id="email"
                v-model="email"
                name="email"
                type="email"
                autocomplete="email"
                required
                class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                placeholder="Email"
            />
          </div>
          <div>
            <label for="password" class="sr-only">Senha</label>
            <input
                id="password"
                v-model="password"
                name="password"
                type="password"
                autocomplete="current-password"
                required
                class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                placeholder="Senha"
            />
          </div>
        </div>

        <div v-if="errorMessage" class="text-red-600 text-sm text-center">
          {{ errorMessage }}
        </div>

        <div>
          <button
              type="submit"
              :disabled="isLoading"
              class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span class="absolute left-0 inset-y-0 flex items-center pl-3">
              <!-- Lock icon -->
              <svg class="h-5 w-5 text-blue-500 group-hover:text-blue-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd" />
              </svg>
            </span>
            {{ isLoading ? 'Entrando...' : 'Entrar' }}
          </button>
        </div>
      </form>
      <div class="text-sm text-center">
        <span class="text-gray-600">Não tem uma conta?</span>
        <a href="/register" class="font-medium text-blue-600 hover:text-blue-500 ml-1">
          Cadastre-se
        </a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../services/api'

const router = useRouter()
const email = ref('')
const password = ref('')
const errorMessage = ref('')
const isLoading = ref(false)

const handleLogin = async () => {
  errorMessage.value = ''
  isLoading.value = true

  try {
    const response = await login({ email: email.value, password: password.value })
    if (response.data) {
      if(response.data.success) {
        localStorage.setItem('jwt_token', response.data.jwt)
        router.push('/dashboard')
      }else{
        errorMessage.value = response.data.message;
      }
    }
  } catch (error) {
    console.error('Login failed:', error)
    if (error.response && error.response.status === 401) {
      errorMessage.value = error.response.data.message;
    } else {
      errorMessage.value = 'Ocorreu um erro ao tentar fazer login. Por favor, tente novamente mais tarde.'
    }
  } finally {
    isLoading.value = false
  }
}
</script>
