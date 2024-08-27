<template>
  <Teleport to="body">
    <div v-if="isOpen" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-sm w-full relative">
        <button
          @click="onClose"
          class="absolute top-2 right-2 text-gray-400 hover:text-gray-600 transition-colors duration-200"
          aria-label="Fechar"
        >
          <XIcon :size="24" />
        </button>
        <div class="p-6 text-center">
          <div v-if="error" class="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <XIcon class="w-8 h-8 text-red-500" />
          </div>
          <div v-else class="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <CheckIcon class="w-8 h-8 text-green-500" />
          </div>
          <h2 class="text-2xl font-semibold text-gray-900 mb-2">{{ title }}</h2>
          <p class="text-gray-600 mb-6">{{ message }}</p>
          <button
            @click="onClose"
            class="w-full bg-green-500 text-white font-semibold py-2 px-4 rounded-md hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-opacity-50 transition duration-300"
          >
            {{ buttonText }}
          </button>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { XIcon, CheckIcon } from 'lucide-vue-next'

defineProps({
  isOpen: {
    type: Boolean,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  message: {
    type: String,
    required: true
  },
  buttonText: {
    type: String,
    required: true
  },
  error: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close'])

const onClose = () => {
  emit('close')
}
</script>
