<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full flex items-center justify-center" id="my-modal">
    <div class="relative p-5 border w-96 shadow-lg rounded-md bg-white">
      <div class="mt-3 text-center">
        <h3 class="text-lg leading-6 font-medium text-gray-900">Cancelar Reserva</h3>
        <div class="mt-2 px-7 py-3">
          <p class="text-sm text-gray-500">
            Tem certeza que deseja cancelar a reserva da sala {{ room.name }}?
          </p>
          <b>Essa ação não pode ser revertida.</b>
        </div>
        <div class="items-center px-4 py-3">
          <button
            id="ok-btn"
            class="px-4 py-2 bg-red-500 text-white text-base font-medium rounded-md w-full shadow-sm hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-300"
            @click="confirmCancel"
          >
            Confirmar Cancelamento
          </button>
          <button
            id="cancel-btn"
            class="mt-3 px-4 py-2 bg-gray-300 text-gray-800 text-base font-medium rounded-md w-full shadow-sm hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-300"
            @click="$emit('close')"
          >
            Voltar
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { inject } from 'vue'

const props = defineProps({
  room: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['close', 'confirm']);

const confirmCancel = () => {
  emit('confirm', props.room.id);
};
</script>
