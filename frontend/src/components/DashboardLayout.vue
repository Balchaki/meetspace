<template>
  <div class="flex flex-col h-screen bg-gray-100 lg:flex-row">
    <aside :class="[
      'fixed inset-y-0 left-0 z-50 w-64 bg-gray-50 border-r border-gray-200 overflow-y-auto transition-transform duration-300 ease-in-out',
      {'translate-x-0': sidebarAberta, '-translate-x-full': !sidebarAberta},
      'lg:translate-x-0 lg:static',
      {'lg:hidden': !sidebarAberta}
    ]">
      <div class="flex flex-col h-full">
        <div class="p-4 border-b border-gray-200">
          <div class="flex items-center justify-center">
            <img src="/assets/img/logo.png" alt="MeetSpace Logo" class="h-8 w-auto">
          </div>
        </div>
        <nav class="flex-grow mt-4 px-4">
          <router-link
            to="/dashboard/profile"
            class="block py-2 px-4 text-gray-600 hover:bg-gray-200 hover:text-gray-900 rounded-md transition duration-150 ease-in-out focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            Meu Perfil
          </router-link>
          <router-link
            to="/dashboard"
            class="block py-2 px-4 text-gray-600 hover:bg-gray-200 hover:text-gray-900 rounded-md transition duration-150 ease-in-out focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            Salas
          </router-link>
          <router-link
            to="/dashboard/reservas"
            class="block py-2 px-4 text-gray-600 hover:bg-gray-200 hover:text-gray-900 rounded-md transition duration-150 ease-in-out focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            Minhas Reservas
          </router-link>
        </nav>
        <div class="p-4 border-t border-gray-200">
          <button @click="logoutRequest" class="w-full bg-red-500 text-white py-2 px-4 rounded-md hover:bg-red-600 transition duration-300 ease-in-out focus:outline-none focus:ring-2 focus:ring-red-500">
            Desconectar-se
          </button>
        </div>
      </div>
    </aside>

    <div class="flex flex-col flex-1 overflow-hidden">
      <header class="bg-white shadow-sm lg:hidden">
        <div class="px-4 py-3 flex justify-between items-center">
          <button @click="toggleMenuMobile" class="text-gray-500 hover:text-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500">
            <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>
          <img src="/assets/img/logo.png" alt="Logo MeetSpace" class="h-8 w-auto">
          <button class="text-gray-500 hover:text-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500">
            <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
          </button>
        </div>
      </header>

      <div v-if="menuMobileAberto" class="lg:hidden fixed inset-0 z-50 bg-gray-600 bg-opacity-75" @click="toggleMenuMobile">
        <div class="fixed inset-y-0 left-0 w-64 bg-gray-50 border-r border-gray-200 flex flex-col" @click.stop>
          <div class="p-4 border-b border-gray-200">
            <div class="flex items-center justify-center">
              <img src="/assets/img/logo.png" alt="MeetSpace Logo" class="h-8 w-auto">
            </div>
          </div>
          <nav class="flex-1 mt-4 px-4 overflow-y-auto">
            <router-link
              to="/dashboard"
              class="block py-2 px-4 text-gray-600 hover:bg-gray-200 hover:text-gray-900 rounded-md transition duration-150 ease-in-out focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              Salas
            </router-link>

          </nav>
          <div style="margin-bottom: 13%" class="p-4 border-t border-gray-200">
            <button @click="logoutRequest" class="w-full bg-red-500 text-white py-2 px-4 rounded-md hover:bg-red-600 transition duration-300 ease-in-out focus:outline-none focus:ring-2 focus:ring-red-500">
              Desconectar-se
            </button>
          </div>
        </div>
      </div>

      <div class="flex-1 overflow-x-hidden overflow-y-auto">
        <header class="bg-white shadow-sm hidden lg:block">
          <div class="py-4 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
            <div class="flex items-center">
              <button @click="toggleSidebar" class="mr-4 text-gray-500 hover:text-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500">
                <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                </svg>
              </button>
              <h2 class="text-lg font-semibold text-gray-900">{{ tituloPaginaAtual }}</h2>
            </div>
            <div class="flex items-center">
              <span class="mr-4">Bem-vindo, {{ nomeUsuario }}</span>
              <button class="bg-gray-200 p-2 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500">
                <svg class="h-6 w-6 text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
              </button>
            </div>
          </div>
        </header>


        <div class="bg-white shadow-sm lg:hidden">
          <div class="py-4 px-4">
            <h2 class="text-lg font-semibold text-gray-900">Dashboard > Salas</h2>
          </div>
        </div>


        <main :class="['bg-gray-100 transition-all duration-300 ease-in-out']">
          <div class="py-6 px-4 sm:px-6 lg:px-8">
            <router-view></router-view>
          </div>
        </main>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, provide, computed } from 'vue'
import { useUserStore } from '../stores/user';
import { useRouter, useRoute } from 'vue-router';
import { isTokenValid } from '../utils/auth';
import { user } from '../services/api';
import { logout } from '../services/api';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
const nomeUsuario = ref('user');
const isAdmin = ref(false);
const email = ref('example@mail.com');
const menuMobileAberto = ref(false);
const sidebarAberta = ref(true);

const tituloPaginaAtual = computed(() => {
  return route.meta.title || 'Dashboard';
});

const verificarAutenticacao = async () => {
  const token = localStorage.getItem('jwt_token');
  if (!token || !isTokenValid(token)) {
    logoutRequest();
  } else {
    try {
      const response = await user();
      nomeUsuario.value = response.data.name;
      isAdmin.value = response.data.isAdmin;
      email.value = response.data.email;
      userStore.setUser({ name: nomeUsuario.value, email: email.value, isAdmin: isAdmin.value });
    } catch (error) {
      console.error('Erro ao obter dados do usuário:', error);
      logoutRequest();
    }
  }
};

onMounted(() => {
  verificarAutenticacao();
  sidebarAberta.value = window.innerWidth >= 1024;
});

const logoutRequest = () => {
  logout();
  userStore.logout();
  localStorage.removeItem('jwt_token');
  router.push('/login');
};

const toggleMenuMobile = () => {
  menuMobileAberto.value = !menuMobileAberto.value;
};

const toggleSidebar = () => {
  sidebarAberta.value = !sidebarAberta.value;
};

provide('isAdmin', computed(() => isAdmin.value));
provide('nomeUsuario', computed(() => nomeUsuario.value));
provide('email', computed(() => email.value));
</script>

logoutRequest
