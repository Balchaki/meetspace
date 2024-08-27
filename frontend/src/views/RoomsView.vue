<template>
  <div class="p-4 sm:p-6 lg:p-8 max-w-7xl mx-auto">
    <div class="mb-8 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
      <div>
        <h2 class="text-3xl font-bold text-gray-900">Salas</h2>
        <p class="mt-2 text-sm text-gray-600">
          Gerencie suas salas de reunião e veja sua disponibilidade.
        </p>
      </div>
      <button
        v-if="isAdmin"
        @click="openAddRoomModal"
        class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition duration-300 flex items-center"
      >
        <PlusIcon class="w-5 h-5 mr-2" />
        Adicionar Sala
      </button>
    </div>

    <div class="bg-white shadow-md rounded-lg overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
          <tr>
            <th
              v-for="header in visibleHeaders"
              :key="header.key"
              :class="header.key === 'actions' ? 'text-right' : 'text-left'"
              scope="col"
              class="px-6 py-3 text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              {{ header.label }}
            </th>
          </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
          <tr v-if="loading">
            <td
              :colspan="visibleHeaders.length"
              class="px-6 py-4 text-center text-sm text-gray-500"
            >
              <Loader2Icon class="animate-spin inline mr-2" />
              Carregando salas...
            </td>
          </tr>
          <tr v-else-if="error">
            <td
              :colspan="visibleHeaders.length"
              class="px-6 py-4 text-center text-sm text-red-500"
            >
              Erro ao carregar salas: {{ error }}
            </td>
          </tr>
          <tr
            v-for="room in filteredRooms"
            :key="room.roomId"
            class="hover:bg-gray-50"
          >
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm font-medium text-gray-900">
                {{ room.name }}
              </div>
              <div class="text-xs text-gray-500 sm:hidden">
                Capacidade: {{ room.capacity }}
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap hidden sm:table-cell">
              <div class="text-sm text-gray-500">{{ room.capacity }}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap hidden sm:table-cell">
                <span
                  :class="room.enabled ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'"
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                >
                  {{ room.enabled ? 'Ativa' : 'Inativa' }}
                </span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
              <div class="flex justify-end space-x-2">
                <button
                  @click="openBookModal(room)"
                  class="text-blue-600 hover:text-blue-900 transition duration-300"
                >
                  <CalendarIcon class="w-4 h-4" />
                  <span class="sr-only">Reservar</span>
                </button>
                <template v-if="isAdmin">
                  <button
                    @click="removeRoom(room)"
                    class="text-red-600 hover:text-red-900 transition duration-300"
                  >
                    <TrashIcon class="w-4 h-4" />
                    <span class="sr-only">Remover</span>
                  </button>
                  <button
                    @click="openEditRoomModal(room)"
                    class="text-purple-700 hover:text-purple-950 transition duration-300"
                  >
                    <FilePenLine class="w-4 h-4" />
                    <span class="sr-only">Editar</span>
                  </button>
                  <button
                    @click="toggleRoomStatus(room)"
                    :class="room.enabled ? 'text-yellow-600 hover:text-yellow-900' : 'text-green-600 hover:text-green-900'"
                    class="transition duration-300"
                  >
                    <PowerIcon class="w-4 h-4" />
                    <span class="sr-only">{{ room.enabled ? 'Desativar' : 'Ativar' }}</span>
                  </button>
                </template>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <BookRoomModal
      v-if="showBookModal"
      :room="selectedRoom"
      @close="closeBookModal"
      @book="handleBookRoom"
    />

    <AddRoomModal
      :is-open="isRoomModalOpen"
      :room="selectedRoom"
      @close="closeRoomModal"
      @save-room="handleSaveRoom"
    />

    <SuccessModal
      :is-open="isSuccessModal"
      @close="closeModal"
      :title="successModalTitle"
      :message="successModalMessage"
      :button-text="modalButtonText"
      :error="modalError"
    />

    <ConfirmModal
      :is-open="isConfirmModalOpen"
      :title="confirmModalTitle"
      :message="confirmModalMessage"
      :confirm-text="confirmModalConfirmText"
      :cancel-text="confirmModalCancelText"
      :show-cancel="confirmModalShowCancel"
      @confirm="handleConfirm"
      @cancel="handleCancel"
    />
  </div>
</template>

<script setup>
import { ref, computed, inject, onMounted, watch } from 'vue';
import { useUserStore } from '../stores/user';
import BookRoomModal from '../components/BookRoomModal.vue';
import CancelBookingModal from '../components/CancelBookingModal.vue';
import AddRoomModal from '../components/AddRoomModal.vue';
import ConfirmModal from '../components/ConfirmModal.vue';
import SuccessModal from '../components/SuccessModal.vue';
import { enabledRooms, createRoom, updateRoom, createReserve as apiCreateReserve, toggleRoomStatus as apiToggleRoomStatus, removeRoom as apiRemoveRoom } from '../services/api.js';
import { PlusIcon, CalendarIcon, TrashIcon, PowerIcon, Loader2Icon, FilePenLine } from 'lucide-vue-next';

const userStore = useUserStore();
const isAdmin = inject('isAdmin', false);

const rooms = ref([]);
const loading = ref(true);
const error = ref(null);

const filter = ref('all');
const isRoomModalOpen = ref(false);
const tableHeaders = [
  { key: 'name', label: 'Nome' },
  { key: 'capacity', label: 'Capacidade' },
  { key: 'enabled', label: 'Status' },
  { key: 'actions', label: 'Ações' },
];

// Confirm Modal
const isConfirmModalOpen = ref(false);
const confirmModalTitle = ref('');
const confirmModalMessage = ref('');
const confirmModalConfirmText = ref('OK');
const confirmModalCancelText = ref('Cancelar');
const confirmModalShowCancel = ref(false);
const confirmModalState = ref(null);

const handleConfirm = () => {
  confirmModalState.value = true;
  isConfirmModalOpen.value = false;
};

const handleCancel = () => {
  confirmModalState.value = false;
  isConfirmModalOpen.value = false;
};

// Success Modal
const isSuccessModal = ref(false);
const successModalTitle = ref('');
const successModalMessage = ref('');
const modalButtonText = ref('OK');
const modalError = ref(false);

const visibleHeaders = computed(() => {
  return tableHeaders.filter(header =>
    ['name', 'actions'].includes(header.key) ||
    window.innerWidth >= 640
  );
});

const closeModal = () => {
  isSuccessModal.value = false;
};

const filteredRooms = computed(() => {
  if (filter.value === 'all') return rooms.value;
  return rooms.value.filter(room =>
    filter.value === 'enabled' ? room.enabled : !room.enabled
  );
});

const fetchRooms = async () => {
  try {
    loading.value = true;
    error.value = null;
    const response = await enabledRooms();
    if (response.data && response.data.rooms) {
      rooms.value = response.data.rooms;
    } else {
      throw new Error('Formato de resposta inválido');
    }
  } catch (err) {
    console.error('Error fetching rooms:', err);
    error.value = err.message || 'Ocorreu um erro ao carregar as salas';
  } finally {
    loading.value = false;
  }
};

onMounted(fetchRooms);

const setFilter = (newFilter) => {
  filter.value = newFilter;
};

const showBookModal = ref(false);
const showCancelModal = ref(false);
const selectedRoom = ref(null);

const openBookModal = (room) => {
  selectedRoom.value = room;
  showBookModal.value = true;
};

const openAddRoomModal = () => {
  selectedRoom.value = null;
  isRoomModalOpen.value = true;
};

const openEditRoomModal = (room) => {
  selectedRoom.value = room;
  isRoomModalOpen.value = true;
};

const closeRoomModal = () => {
  isRoomModalOpen.value = false;
  selectedRoom.value = null;
};

const handleSaveRoom = async (roomData) => {
  try {
    let response;
    if (roomData.roomId) {
      response = await updateRoom(roomData);
    } else {
      response = await createRoom(roomData);
    }

    if (response.data && response.data.success) {
      await fetchRooms();

      successModalTitle.value = roomData.roomId ? 'Sala Atualizada' : 'Sala Adicionada';
      successModalMessage.value = `A sala "${roomData.name}" foi ${roomData.roomId ? 'atualizada' : 'adicionada'} com sucesso.`;
      isSuccessModal.value = true;
      modalError.value = false;
    } else {
      throw new Error(`Falha ao ${roomData.roomId ? 'atualizar' : 'adicionar'} a sala.`);
    }
  } catch (error) {
    console.error('Error saving room:', error);
    successModalTitle.value = `Erro ao ${roomData.roomId ? 'atualizar' : 'adicionar'} sala`;
    successModalMessage.value = `Ocorreu um erro ao ${roomData.roomId ? 'atualizar' : 'adicionar'} a sala. Por favor, tente novamente.`;
    modalError.value = true;
    isSuccessModal.value = true;
  } finally {
    closeRoomModal();
  }
};

const openCancelModal = (room) => {
  selectedRoom.value = room;
  showCancelModal.value = true;
};

const closeCancelModal = () => {
  showCancelModal.value = false;
  selectedRoom.value = null;
};

const closeBookModal = () => {
  showBookModal.value = false;
};

const handleBookRoom = async (bookingDetails) => {
  try {
    let response;
    response = await apiCreateReserve(bookingDetails);
    if (response.data && response.data.success) {
      successModalTitle.value = 'Reserva realizada';
      successModalMessage.value = `A sua reserva foi realizada com sucesso!`;
      isSuccessModal.value = true;
      modalError.value = false;
    } else {
      throw new Error(`Falha ao reservar a sala.`);
    }
  } catch (error) {
    console.error('Error reserving room:', error);
    successModalTitle.value = `Erro ao reservar sala`;
    successModalMessage.value = `Ocorreu um erro ao reservar a sala. Por favor, tente novamente.`;
    modalError.value = true;
    isSuccessModal.value = true;
  } finally {
    closeBookModal();
  }
  console.log('Reservando sala:', bookingDetails);
  closeBookModal();
};

const removeRoom = async (room) => {
  confirmModalTitle.value = 'Confirmar Ação';
  confirmModalMessage.value = `Tem certeza que deseja remover a sala "${room.name}"?\nEssa ação não pode ser revertida.`;
  confirmModalConfirmText.value = 'Sim';
  confirmModalCancelText.value = 'Não';
  confirmModalShowCancel.value = true;
  isConfirmModalOpen.value = true;

  await new Promise(resolve => {
    const unwatch = watch(isConfirmModalOpen, (newVal, oldVal) => {
      if (oldVal && !newVal) {
        unwatch();
        resolve(confirmModalState.value);
      }
    });
  });

  if (confirmModalState.value) {
    try {
      const response = await apiRemoveRoom(room.roomId);
      if (response.data && response.data.success) {
        await fetchRooms();

        successModalTitle.value = 'Sala Removida';
        successModalMessage.value = `A sala "${room.name}" foi removida com sucesso.`;
        isSuccessModal.value = true;
        modalError.value = false;
      } else {
        throw new Error('Falha na API');
      }
    } catch (err) {
      console.error('Error removing room:', err);
      successModalTitle.value = 'Erro';
      successModalMessage.value = `Ocorreu um erro ao remover a sala "${room.name}". Tente novamente mais tarde.`;
      isSuccessModal.value = true;
      modalError.value = true;
    }
  }

  confirmModalState.value = null;
  confirmModalShowCancel.value = false;
};

const toggleRoomStatus = async (room) => {
  confirmModalTitle.value = 'Confirmar Ação';
  confirmModalMessage.value = `Tem certeza que deseja ${room.enabled ? "desativar" : "ativar"} a sala "${room.name}"?\nAs reservas e histórico serão mantidos.`;
  confirmModalConfirmText.value = 'Sim';
  confirmModalCancelText.value = 'Não';
  confirmModalShowCancel.value = true;
  isConfirmModalOpen.value = true;

  await new Promise(resolve => {
    const unwatch = watch(isConfirmModalOpen, (newVal, oldVal) => {
      if (oldVal &&

        !newVal) {
        unwatch();
        resolve(confirmModalState.value);
      }
    });
  });

  if (confirmModalState.value) {
    try {
      const response = await apiToggleRoomStatus({ roomId: room.roomId, enabled: !room.enabled });
      if (response.data && response.data.success) {
        await fetchRooms();

        successModalTitle.value = 'Sala Atualizada';
        successModalMessage.value = `A sala "${room.name}" foi ${room.enabled ? "desativada" : "ativada"} com sucesso.`;
        isSuccessModal.value = true;
        modalError.value = false;
      } else {
        throw new Error('Falha na API');
      }
    } catch (err) {
      console.error('Error updating room status:', err);
      successModalTitle.value = 'Erro';
      successModalMessage.value = `Ocorreu um erro ao atualizar o status da sala "${room.name}".`;
      isSuccessModal.value = true;
      modalError.value = true;
    }
  }

  confirmModalState.value = null;
  confirmModalShowCancel.value = false;
};
</script>
