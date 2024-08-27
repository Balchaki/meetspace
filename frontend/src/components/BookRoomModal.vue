<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full" id="my-modal">
    <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
      <div class="mt-3 text-center">
        <h3 class="text-lg leading-6 font-medium text-gray-900">Reservar Sala</h3>
        <button @click="$emit('close')" class="absolute top-2 right-2 text-gray-500 hover:text-gray-700">
          <span class="sr-only">Fechar</span>
          <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
        <div class="mt-2 px-7 py-3">
          <p class="text-sm text-gray-500">
            Reservando: {{ room.name }}
          </p>
          <form @submit.prevent="handleBooking" class="mt-4">
            <div v-if="step === 'start'" class="mb-4">
              <label class="block text-sm font-medium text-gray-700">Selecione a data de início</label>
              <div class="mt-2 grid grid-cols-7 gap-1">
                <div v-for="(day, index) in calendarDays" :key="index" class="text-center">
                  <button
                    type="button"
                    @click="selectDate(day)"
                    :disabled="!isDateAvailable(day)"
                    :class="[
                      'w-8 h-8 rounded-full focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500',
                      isSelectedDate(day) ? 'bg-blue-500 text-white' : '',
                      !isDateAvailable(day) ? 'bg-gray-200 text-gray-400 cursor-not-allowed' : '',
                      getDayStatusClass(day)
                    ]"
                  >
                    {{ day.getDate() }}
                  </button>
                </div>
              </div>
            </div>
            <div v-if="step === 'start' && startDate" class="mb-4">
              <label class="block text-sm font-medium text-gray-700">Selecione o horário de início</label>
              <div class="grid grid-cols-4 gap-2 mt-2">
                <button
                  v-for="time in availableStartTimes"
                  :key="time.value"
                  type="button"
                  @click="selectTime(time)"
                  :class="[
                    'p-2 text-sm rounded transition-all duration-200 ease-in-out',
                    time.value === startTime ? 'ring-2 ring-blue-500 ring-offset-2' : '',
                    'bg-green-500 text-white hover:bg-green-600'
                  ]"
                >
                  {{console.log(time.value)}}
                  {{ formatTime(time.value) }}
                </button>
              </div>
            </div>
            <div v-if="step === 'end'" class="mb-4">
              <p class="block text-sm font-medium text-gray-700">Data selecionada: {{ formatDate(startDate) }}</p>
              <p class="block text-sm font-medium text-gray-700 mt-2">Horário de início: {{ formatTime(startTime) }}</p>
              <label class="block text-sm font-medium text-gray-700 mt-4">Selecione o horário de encerramento</label>
              <div class="grid grid-cols-4 gap-2 mt-2">
                <button
                  v-for="time in availableEndTimes"
                  :key="time.value"
                  type="button"
                  @click="selectEndTime(time)"
                  :class="[
                    'p-2 text-sm rounded transition-all duration-200 ease-in-out',
                    time.value === endTime ? 'ring-2 ring-blue-500 ring-offset-2' : '',
                    'bg-green-500 text-white hover:bg-green-600'
                  ]"
                  :disabled="isEndTimeDisabled(time)"
                >
                  {{ formatTime(time.value) }}
                </button>
              </div>
            </div>
            <div class="items-center px-4 py-3">
              <button
                v-if="step === 'start'"
                type="button"
                @click="advanceToEndTime"
                class="px-4 py-2 bg-blue-500 text-white text-base font-medium rounded-md w-full shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-300"
                :disabled="!isStartDateValid"
              >
                Avançar
              </button>
              <button
                v-else
                type="submit"
                class="px-4 py-2 bg-blue-500 text-white text-base font-medium rounded-md w-full shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-300"
                :disabled="!isFormValid"
              >
                Confirmar Reserva
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { format, addDays, isSameDay, parseISO } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import { avaliableDates, avaliableTimes } from '../services/api.js';

const props = defineProps({
  room: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['close', 'book']);

const step = ref('start');
const startDate = ref(null);
const startTime = ref(null);
const endTime = ref(null);
const availableDatesData = ref({});
const availableTimesData = ref([]);

const calendarDays = computed(() => {
  const today = new Date();
  return Array.from({ length: 30 }, (_, i) => addDays(today, i));
});

const availableStartTimes = computed(() => {
  if (!startDate.value) return [];
  return availableTimesData.value.map(time => {
    const [year, month, day, hour, minute] = time;
    const date = new Date(year, month - 1, day, hour, minute);
    return {
      value: format(date, 'HH:mm'),
      label: format(date, 'HH:mm')
    };
  });
});
const availableEndTimes = computed(() => {
  if (!startTime.value) return [];
  const startIndex = availableTimesData.value.findIndex(time => {
    const [year, month, day, hour, minute] = time;
    const date = new Date(year, month - 1, day, hour, minute);
    return format(date, 'HH:mm') === startTime.value;
  });
  return availableTimesData.value.slice(startIndex + 1).map(time => {
    const [year, month, day, hour, minute] = time;
    const date = new Date(year, month - 1, day, hour, minute);
    return {
      value: format(date, 'HH:mm'),
      label: format(date, 'HH:mm')
    };
  });
});

const isStartDateValid = computed(() => startDate.value && startTime.value);
const isFormValid = computed(() => startDate.value && startTime.value && endTime.value);

onMounted(async () => {
  try {
    const response = await avaliableDates(props.room.roomId);
    availableDatesData.value = response.data.availableDates;
  } catch (error) {
    console.error('Failed to fetch available dates:', error);
  }
});

async function selectDate(date) {
  startDate.value = date;
  startTime.value = null;
  endTime.value = null;
  try {
    const response = await avaliableTimes(props.room.roomId, format(date, 'yyyy-MM-dd'));
    availableTimesData.value = response.data.availableTimes;
  } catch (error) {
    console.error('Failed to fetch available times:', error);
  }
}

function selectTime(time) {
  startTime.value = time.value;
}

function selectEndTime(time) {
  endTime.value = time.value;
}

function isDateAvailable(date) {
  const dateString = format(date, 'yyyy-MM-dd');
  return availableDatesData.value[dateString] > 0;
}

function getDayStatusClass(date) {
  const dateString = format(date, 'yyyy-MM-dd');
  const status = availableDatesData.value[dateString];
  if (status === 2) return 'bg-green-500';
  if (status === 1) return 'bg-yellow-500';
  return 'bg-red-500';
}

function isSelectedDate(date) {
  return startDate.value && isSameDay(date, startDate.value);
}

function isEndTimeDisabled(time) {
  return time.value <= startTime.value;
}

function advanceToEndTime() {
  if (isStartDateValid.value) {
    step.value = 'end';
  }
}

function formatDate(date) {
  if (!date) return '';
  return format(date, "dd 'de' MMMM 'de' yyyy", { locale: ptBR });
}

function formatTime(time) {
  if (!time) return '';
  return time;
}

function handleBooking() {
  if (isFormValid.value) {
    const [startHours, startMinutes] = startTime.value.split(':');
    const [endHours, endMinutes] = endTime.value.split(':');

    const startDateTime = new Date(startDate.value);
    startDateTime.setHours(parseInt(startHours), parseInt(startMinutes), 0, 0);

    const endDateTime = new Date(startDate.value);
    endDateTime.setHours(parseInt(endHours), parseInt(endMinutes), 0, 0);

    emit('book', {
      roomId: props.room.roomId,
      startDate: startDateTime,
      endDate: endDateTime,
    });
  }
}
</script>
