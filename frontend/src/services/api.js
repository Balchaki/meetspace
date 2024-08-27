import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwt_token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

/*
Authentication API's
 */
export const login = (credentials) => api.post('/auth/login', credentials)
export const register = (userData) => api.post('/auth/register', userData)
export const logout = () => {
  const token = localStorage.getItem('jwt_token');

  if (!token) {
    console.warn('No token found in localStorage');
    return Promise.resolve();
  }

  return api.post('/auth/logout', null, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
    .then(response => {
      localStorage.removeItem('jwt_token');
      return response;
    })
    .catch(error => {
      console.error('Logout failed:', error);
      throw error;
    });
};

/*
User API's
 */
export const user = () => api.get('/users/me')
export const updatePassword = (userPassword) => api.put('/users/update-password', userPassword)
export const updateUser = (userDTO) => api.put('/users/update', userDTO)

/*
Rooms API's
 */
export const enabledRooms = () => api.get('/rooms/all')
export const toggleRoomStatus = (roomData) => api.put('/rooms/update-status', roomData)
export const removeRoom = (roomId) => api.delete(`/rooms/${roomId}`)
export const updateRoom = (roomData) => api.put(`/rooms/`, roomData)
export const createRoom = (roomData) => api.post('/rooms/create', roomData)
/*
Reserve API's
 */

export const createReserve = (reserveData) => api.post('/reserve/create', reserveData)
export const myReserve = () => api.get('/reserve/')
export const cancelReserve = (reserveId) => api.delete(`/reserve/${reserveId}`)
export const avaliableDates = (roomId) => api.get(`/reserve/available-dates?roomId=${roomId}`)
export const avaliableTimes = (roomId, date) => api.get(`/reserve/available-times?roomId=${roomId}&date=${date}`)

export default api
