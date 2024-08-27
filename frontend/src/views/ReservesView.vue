<template>
  <div class="p-4 sm:p-6 lg:p-8 max-w-7xl mx-auto">
    <div class="mb-8 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
      <div>
        <h2 class="text-3xl font-bold text-gray-900">Reservas</h2>
        <p class="mt-2 text-sm text-gray-600">
          Gerencie suas reservas.
        </p>
      </div>
    </div>

    <div class="bg-white shadow-md rounded-lg overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
          <tr>
            <th v-for="header in tableHeaders" :key="header.key" :class="header.key === 'actions' ? 'text-right' : 'text-left'" scope="col" class="px-6 py-3 text-xs font-medium text-gray-500 uppercase tracking-wider">
              {{ header.label }}
            </th>
          </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
          <tr v-if="loading">
            <td :colspan="tableHeaders.length" class="px-6 py-4 text-center text-sm text-gray-500">
              <Loader2Icon class="animate-spin inline mr-2" />
              Carregando reservas...
            </td>
          </tr>
          <tr v-else-if="error">
            <td :colspan="tableHeaders.length" class="px-6 py-4 text-center text-sm text-red-500">
              Erro ao carregar reservas: {{ error }}
            </td>
          </tr>
          <tr v-else-if="reserves.length === 0">
            <td :colspan="tableHeaders.length" class="px-6 py-4 text-center text-sm text-gray-500">
              Nenhuma reserva encontrada.
            </td>
          </tr>
          <tr v-for="reserve in reserves" :key="reserve.reserveId" class="hover:bg-gray-50">
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm font-medium text-gray-900">{{ reserve.room }}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm font-medium text-gray-900">{{ reserve.user }}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm text-gray-500">{{ formatDate(reserve.startDate) }}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm text-gray-500">{{ formatDate(reserve.endDate) }}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
              <div class="flex justify-end space-x-2">
                <button @click="cancelReserve(reserve)" class="text-red-600 hover:text-red-900 transition duration-300">
                  <TrashIcon class="w-4 h-4" />
                  <span class="sr-only">Cancelar Reserva</span>
                </button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <CancelBookingModal
      v-if="showCancelModal"
      :reserve="selectedReserve"
      @close="closeCancelModal"
      @confirm="handleCancelBooking(selectedReserve.reserveId)"
      :room="selectedReserve.room"
    />

    <SuccessModal
      :is-open="isSuccessModal"
      @close="closeModal"
      :title="successModalTitle"
      :message="successModalMessage"
      :button-text="modalButtonText"
      :error="modalError"
    />

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useUserStore } from '../stores/user';
import CancelBookingModal from '../components/CancelBookingModal.vue';
import { myReserve, cancelReserve as apiCancelReserve } from '../services/api.js';
import { TrashIcon, Loader2Icon } from 'lucide-vue-next';
import { format, parseISO } from 'date-fns';
import SuccessModal from '../components/SuccessModal.vue'

const userStore = useUserStore();

const reserves = ref([]);
const loading = ref(true);
const error = ref(null);

const tableHeaders = [
  { key: 'room', label: 'Sala' },
  { key: 'user', label: 'Responsável' },
  { key: 'startDate', label: 'Início' },
  { key: 'endDate', label: 'Fim' },
  { key: 'actions', label: 'Ações' },
];


// Success Modal
const isSuccessModal = ref(false);
const successModalTitle = ref('');
const successModalMessage = ref('');
const modalButtonText = ref('OK');
const modalError = ref(false);
const closeModal = () => {
  isSuccessModal.value = false;
};

const showCancelModal = ref(false);
const selectedReserve = ref(null);

const fetchReserves = async () => {
  loading.value = true;
  error.value = null;
  try {
    const response = await myReserve();
    reserves.value = response.data;
  } catch (err) {
    console.error('Error fetching reserves:', err);
    error.value = err.message || 'Ocorreu um erro ao carregar as reservas';
  } finally {
    loading.value = false;
  }
};

onMounted(fetchReserves);

const formatDate = (dateString) => {
  try {
    const date = parseISO(dateString);
    return format(date, 'dd/MM/yyyy HH:mm');
  } catch (error) {
    console.error('Error formatting date:', error);
    return 'Data inválida';
  }
};

const cancelReserve = (reserve) => {
  selectedReserve.value = reserve;
  showCancelModal.value = true;
};

const closeCancelModal = () => {
  showCancelModal.value = false;
  selectedReserve.value = null;
};

const handleCancelBooking = async (reserveId) => {
  try {
    showCancelModal.value = false
    let response = await apiCancelReserve(reserveId);
    if(response.data && response.data.success){
      reserves.value = reserves.value.filter(reserve => reserve.reserveId !== reserveId);
      successModalTitle.value = "Reserva Cancelada";
      successModalMessage.value = `Sua reserva foi cancelada com sucesso!`;
      isSuccessModal.value = true;
      modalError.value = false;
      closeCancelModal();
    } else {
      successModalTitle.value = "Erro";
      successModalMessage.value = `Ocorreu um erro ao cancelar a reserva. Por favor, tente novamente.`;
      isSuccessModal.value = true;
      modalError.value = true;
    }
  } catch (err) {
    successModalTitle.value = "Erro";
    successModalMessage.value = `Ocorreu um erro ao cancelar a reserva. Por favor, tente novamente.`;
    isSuccessModal.value = true;
    modalError.value = true;
  }
};
</script>
