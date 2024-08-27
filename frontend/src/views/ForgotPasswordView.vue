<template>
  <div class="auth-container">
    <div class="auth-card">
      <h1 class="auth-title">Recupere sua senha</h1>
      <p class="auth-subtitle">Insira seu email para receber instruções de recuperação</p>
      <form @submit.prevent="handleForgotPassword">
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
        <button type="submit" class="form-submit">Enviar instruções</button>
      </form>
      <p class="form-footer">
        Lembrou sua senha? <a href="/login">Faça login</a>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const email = ref('')

const handleForgotPassword = async () => {
  console.log('Forgot password for:', email.value)
}
</script>
