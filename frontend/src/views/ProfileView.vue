<template>
  <div class="max-w-2xl mx-auto p-4 space-y-6">
    <div class="bg-white shadow rounded-lg p-6">
      <h2 class="text-2xl font-bold mb-4">Perfil do Usuário</h2>
      <p class="text-gray-600 mb-6">Atualize suas informações pessoais aqui.</p>
      <form @submit.prevent="updateProfile" class="space-y-4">
        <div class="space-y-2">
          <label for="name" class="block text-sm font-medium text-gray-700">Nome</label>
          <input id="name" v-model="userData.name" placeholder="Seu nome" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" />
        </div>
        <div class="space-y-2">
          <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
          <input id="email" v-model="userData.email" type="email" placeholder="seu@email.com" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" />
        </div>
        <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
          Atualizar Perfil
        </button>
      </form>
    </div>

    <div class="bg-white shadow rounded-lg p-6 mt-6">
      <h2 class="text-2xl font-bold mb-4">Alterar Senha</h2>
      <p class="text-gray-600 mb-6">Atualize sua senha para manter sua conta segura.</p>
      <form @submit.prevent="changePassword" class="space-y-4">
        <div class="space-y-2">
          <label for="current-password" class="block text-sm font-medium text-gray-700">Senha Atual</label>
          <input id="current-password" v-model="currentPassword" type="password" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" />
        </div>
        <div class="space-y-2">
          <label for="new-password" class="block text-sm font-medium text-gray-700">Nova Senha</label>
          <input id="new-password" v-model="newPassword" type="password" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" />
        </div>
        <div class="space-y-2">
          <label for="confirm-password" class="block text-sm font-medium text-gray-700">Confirmar Nova Senha</label>
          <input id="confirm-password" v-model="confirmPassword" type="password" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" />
        </div>
        <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
          Alterar Senha
        </button>
      </form>
    </div>
  </div>
  <SuccessModal
    :is-open="isSuccessModal"
    @close="closeModal"
    :title="modalTitle"
    :message="modalMessage"
    :button-text="modalButtonText"
    :error="modalError"
  />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user';
import SuccessModal from '../components/SuccessModal.vue';
import { updatePassword, updateUser, user } from '../services/api.js';

const userStore = useUserStore();

const userData = ref({ name: '', email: '' })
const currentPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const isSuccessModal = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalButtonText = ref('OK')
const modalError = ref(false)

onMounted(async () => {
  try {
    const response = await user();
    if (response.data) {
      userData.value = response.data;
    }
  } catch (error) {
    console.error('Error fetching user data:', error);
    showModal('Erro', 'Não foi possível carregar os dados do usuário.', true);
  }
})

const updateProfile = async () => {
  try {
    const response = await updateUser(userData.value);
    if (response.data && response.data.success) {
      showModal('Perfil atualizado', 'Seu perfil foi atualizado com sucesso.', false);
    } else {
      throw new Error('Update failed');
    }
  } catch (error) {
    console.error('Error updating profile:', error);
    showModal('Erro', 'Ocorreu um erro ao atualizar o perfil. Por favor, tente novamente.', true);
  }
}

const changePassword = async () => {
  if (newPassword.value !== confirmPassword.value) {
    showModal('Erro', 'As senhas não coincidem. Por favor, tente novamente.', true);
    return;
  }
  try {
    const response = await updatePassword({
      password: currentPassword.value,
      newPassword: newPassword.value
    });
    if (response.data) {
      showModal('Sucesso!', response.data.message, !response.data.success);
    } else {
      throw new Error('Password update failed');
    }
  } catch (error) {
    console.error('Error changing password:', error);
    showModal('Erro', 'Ocorreu um erro ao alterar a senha. Por favor, tente novamente.', true);
  }
}

const showModal = (title, message, isError) => {
  modalTitle.value = title;
  modalMessage.value = message;
  modalError.value = isError;
  isSuccessModal.value = true;
}

const closeModal = () => {
  isSuccessModal.value = false;
}
</script>
