import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import './global.css'

// Import Lucide Vue Next
import * as LucideIcons from 'lucide-vue-next'

const app = createApp(App)

// Register Lucide icons globally
for (const [key, icon] of Object.entries(LucideIcons)) {
  app.component(key, icon)
}

app.use(createPinia())
app.use(router)
app.mount('#app')
