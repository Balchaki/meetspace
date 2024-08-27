<template>
  <nav class="bg-white shadow-md">
    <div class="container mx-auto px-4">
      <div class="flex justify-between items-center py-4">
        <div class="flex items-center">
          <router-link to="/" class="text-xl font-bold text-blue-600">MeetSpace</router-link>
          <router-link to="/dashboard" class="ml-6 text-gray-600 hover:text-gray-800">Dashboard</router-link>
          <router-link v-if="isAdmin" to="/admin" class="ml-4 text-gray-600 hover:text-gray-800">Admin</router-link>
        </div>
        <button @click="logout" class="bg-red-500 hover:bg-red-600 text-white font-medium py-2 px-4 rounded">
          Logout
        </button>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/user';

const userStore = useUserStore();
const router = useRouter();

const isAdmin = computed(() => userStore.user?.role === 'ADMIN');

const logout = () => {
  userStore.logout();
  router.push('/login');
};
</script>
