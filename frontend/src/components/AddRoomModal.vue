<template>
  <Teleport to="body">
    <div v-if="isOpen" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full relative">
        <button
          @click="onClose"
          class="absolute top-2 right-2 text-gray-400 hover:text-gray-600 transition-colors duration-200"
          aria-label="Fechar"
        >
          <XIcon :size="24" />
        </button>
        <div class="p-6">
          <h2 class="text-2xl font-semibold text-gray-900 mb-4">
            {{ isEditing ? 'Editar Sala' : 'Adicionar Nova Sala' }}
          </h2>
          <form @submit.prevent="handleSubmit">
            <div class="mb-4">
              <label for="roomName" class="block text-sm font-medium text-gray-700 mb-1">
                Nome da Sala
              </label>
              <input
                id="roomName"
                v-model="roomName"
                type="text"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                placeholder="Digite o nome da sala"
              />
            </div>
            <div class="mb-6">
              <label for="roomCapacity" class="block text-sm font-medium text-gray-700 mb-1">
                Capacidade
              </label>
              <input
                id="roomCapacity"
                v-model="roomCapacity"
                type="number"
                required
                min="1"
                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                placeholder="Digite a capacidade da sala"
              />
            </div>
            <div class="flex justify-end space-x-3">
              <button
                type="button"
                @click="onClose"
                class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
              >
                Cancelar
              </button>
              <button
                type="submit"
                class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
              >
                {{ isEditing ? 'Salvar' : 'Adicionar' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { XIcon } from 'lucide-vue-next'

const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true
  },
  room: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['close', 'saveRoom'])

const roomName = ref('')
const roomCapacity = ref(1)

const isEditing = computed(() => !!props.room)

const resetForm = () => {
  roomName.value = ''
  roomCapacity.value = 1
}

const onClose = () => {
  emit('close')
  resetForm()
}

const handleSubmit = () => {
  if (roomName.value.trim() && roomCapacity.value > 0) {
    const roomData = {
      name: roomName.value.trim(),
      capacity: parseInt(roomCapacity.value, 10)
    }

    if (isEditing.value) {
      roomData.roomId = props.room.roomId
    }

    emit('saveRoom', roomData)
    onClose()
  }
}

watch(() => props.room, (newRoom) => {
  if (newRoom) {
    roomName.value = newRoom.name
    roomCapacity.value = newRoom.capacity
  } else {
    resetForm()
  }
}, { immediate: true })
</script>
