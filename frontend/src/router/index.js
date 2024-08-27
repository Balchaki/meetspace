import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import RoomsView from '../views/RoomsView.vue'
import ReservesView from '../views/ReservesView.vue'
import DashboardLayout from '../components/DashboardLayout.vue'
import ProfileView from '../views/ProfileView.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterView
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: DashboardLayout,
    children: [
      {
        path: '',
        name: 'Rooms',
        component: RoomsView,
        meta: {
          requiresAuth: true,
          title: 'Dashboard > Salas'
        }
      },
      {
        path: 'reservas',
        name: 'Reserves',
        component: ReservesView,
        meta: {
          requiresAuth: true,
          title: 'Dashboard > Minhas Reservas'
        }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: ProfileView,
        meta: {
          requiresAuth: true,
          title: 'Dashboard > Meu Perfil'
        }
      }
    ],
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
