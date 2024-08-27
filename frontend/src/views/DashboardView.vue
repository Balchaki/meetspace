<template>
  <div class="flex h-screen bg-gray-100">

    <aside class="w-64 bg-white shadow-md flex flex-col">
      <div class="p-4">
        <div class="flex items-center flex-shrink-0">
          <img src="/assets/img/logo.png" alt="MeetSpace Logo" class="h-8 w-auto sm:h-10">
        </div>
      </div>
      <nav class="flex-grow mt-4">
        <a href="#" class="block py-2 px-4 text-gray-600 hover:bg-blue-50 hover:text-blue-600">Salas</a>
      </nav>
      <div class="p-4">
        <button @click="logout" class="w-full bg-red-500 text-white py-2 px-4 rounded hover:bg-red-600 transition duration-300">
          Desconectar-se
        </button>
      </div>
    </aside>


    <main class="flex-1 overflow-x-hidden overflow-y-auto">

      <header class="bg-white shadow-sm">
        <div class="max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
          <h2 class="text-lg font-semibold text-gray-900">Dashboard > Salas</h2>
          <div class="flex items-center">
            <span class="mr-4">Bem-vindo, {{ userName }}</span>
            <button class="bg-gray-200 p-2 rounded-full">
            </button>
          </div>
        </div>
      </header>


      <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">

        <div class="mb-4 flex justify-between items-center">
          <div class="space-x-2">
            <button
              v-for="option in filterOptions"
              :key="option.value"
              @click="setFilter(option.value)"
              class="px-3 py-1 bg-white border rounded-md text-sm transition duration-300"
              :class="{ 'bg-blue-100 text-blue-600': filter === option.value }"
            >
              {{ option.label }}
            </button>
          </div>
          <div class="space-x-2">
            <button class="px-3 py-1 bg-white border rounded-md text-sm hover:bg-gray-100 transition duration-300">Filtrar</button>
            <button class="px-3 py-1 bg-white border rounded-md text-sm hover:bg-gray-100 transition duration-300">Exportar</button>
            <button v-if="isAdmin" class="px-3 py-1 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700 transition duration-300">Adicionar Sala</button>
          </div>
        </div>


        <div class="bg-white shadow-md rounded-lg overflow-hidden">
          <div class="px-4 py-5 border-b border-gray-200 sm:px-6">
            <h3 class="text-lg leading-6 font-medium text-gray-900">Salas</h3>
            <p class="mt-1 text-sm text-gray-500">
              Gerencie suas salas de reunião e veja sua disponibilidade.
            </p>
          </div>
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
            <tr>
              <th v-for="header in tableHeaders" :key="header.key" scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                {{ header.label }}
              </th>
            </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="room in filteredRooms" :key="room.id">
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{{ room.name }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  <span :class="statusClass(room.status)" class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full">
                    {{ translateStatus(room.status) }}
                  </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{{ room.capacity }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{{ room.bookings }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{{ formatDate(room.createdAt) }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                <button
                  @click="bookRoom(room)"
                  v-if="room.status === 'Available'"
                  class="text-blue-600 hover:text-blue-900 mr-2 transition duration-300"
                >
                  Reservar
                </button>
                <button
                  @click="cancelBooking(room)"
                  v-if="room.status === 'Booked'"
                  class="text-red-600 hover:text-red-900 transition duration-300"
                >
                  Cancelar
                </button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '../stores/user';
import { useRouter } from 'vue-router';
import { isTokenValid, getUser } from '../utils/auth';

const router = useRouter();
const userStore = useUserStore();
const userName = ref('Usuário');
const isAdmin = ref(false);

const checkAuth = () => {
  const token = localStorage.getItem('jwt_token');
  if (!token || !isTokenValid(token)) {
    logout();
  } else {
    const user = getUser();
    if (user) {
      userName.value = user.name || 'Usuário';
      isAdmin.value = user.role === 'ADMIN';
      userStore.setUser(user);
    }
  }
};

onMounted(() => {
  checkAuth();
});

const logout = () => {
  userStore.logout();
  localStorage.removeItem('jwt_token');
  router.push('/');
};
const rooms = ref([
  { id: 1, name: 'Sala de Conferência A', status: 'Available', capacity: 10, bookings: 25, createdAt: '2023-07-12 10:42 AM' },
  { id: 2, name: 'Sala de Reuniões', status: 'Booked', capacity: 15, bookings: 100, createdAt: '2023-10-18 03:21 PM' },
  // Adicione mais dados de salas conforme necessário
]);

const filter = ref('all');

const filterOptions = [
  { value: 'all', label: 'Todas' },
  { value: 'available', label: 'Disponíveis' },
  { value: 'booked', label: 'Reservadas' },
];

const tableHeaders = [
  { key: 'name', label: 'Nome' },
  { key: 'status', label: 'Status' },
  { key: 'capacity', label: 'Capacidade' },
  { key: 'bookings', label: 'Reservas' },
  { key: 'createdAt', label: 'Criada em' },
  { key: 'actions', label: 'Ações' },
];

const filteredRooms = computed(() => {
  if (filter.value === 'all') return rooms.value;
  return rooms.value.filter(room => room.status.toLowerCase() === filter.value);
});

const setFilter = (newFilter) => {
  filter.value = newFilter;
};

const statusClass = (status) => {
  return {
    'bg-green-100 text-green-800': status === 'Available',
    'bg-red-100 text-red-800': status === 'Booked'
  };
};

const translateStatus = (status) => {
  return status === 'Available' ? 'Disponível' : 'Reservada';
};

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return new Intl.DateTimeFormat('pt-BR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).format(date);
};

</script>
