<template>
  <div class="auth-container">
    <div class="auth-card">
      <div>
        <router-link to="/"><img class="mx-auto h-12 w-auto" src="/assets/img/logo.png" alt="MeetSpace Logo"></router-link>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          Cadastre-se
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          Preencha os campos abaixo para criar sua conta
        </p>
      </div>
      <br />
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label for="name" class="form-label">Nome</label>
          <input
              id="name"
              v-model="name"
              type="text"
              class="form-input"
              placeholder="Seu nome"
              required
          />
        </div>
        <div class="form-group">
          <label for="email" class="form-label">Email</label>
          <input
              id="email"
              v-model="email"
              type="email"
              class="form-input"
              placeholder="mail@exemplo.com"
              required
          />
        </div>
        <div class="form-group">
          <label for="password" class="form-label">Senha</label>
          <input
              id="password"
              v-model="password"
              type="password"
              class="form-input"
              required
          />
        </div>
        <button type="submit" class="form-submit" :disabled="isLoading">
          {{ isLoading ? 'Cadastrando...' : 'Cadastrar' }}
        </button>
      </form>
      <p v-if="errorMessage" class="text-red-500 text-sm mt-2">{{ errorMessage }}</p>
      <p class="form-footer">
        Ao se cadastrar, você concorda com os <a href="#">Termos de Uso</a>.
      </p>
      <br />
      <div class="text-sm text-center">
        <span class="text-gray-600">Já tem uma conta?</span>
        <a href="/login" class="font-medium text-blue-600 hover:text-blue-500 ml-1">
          Conecte-se
        </a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../services/api'

const router = useRouter()
const name = ref('')
const email = ref('')
const password = ref('')
const errorMessage = ref('')
const isLoading = ref(false)

const handleRegister = async () => {
  try {
    isLoading.value = true
    errorMessage.value = ''
    const response = await register({ name: name.value, email: email.value, password: password.value })
    if (response.data.success) {
      localStorage.setItem('jwt_token', response.data.jwt)
      router.push('/dashboard')
    } else {
      errorMessage.value = response.data.message || 'Falha no cadastro. Por favor, tente novamente.'
    }
  } catch (error) {
    console.error('Registration failed:', error)
    errorMessage.value = error.response?.data?.message || 'Ocorreu um erro. Por favor, tente novamente mais tarde.'
  } finally {
    isLoading.value = false
  }
}
</script>
