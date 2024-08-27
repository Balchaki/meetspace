import * as jwtDecode from 'jwt-decode';

export const isTokenValid = (token) => {
  if (!token) {
    return false;
  }

  try {
    const decodedToken = jwtDecode.jwtDecode(token);
    const currentTime = Date.now() / 1000;
    return decodedToken.exp && decodedToken.exp > currentTime;
  } catch (error) {
    console.error('Erro ao decodificar o token:', error);
    return false;
  }
};

export const getUser = () => {
  const token = localStorage.getItem('jwt_token');
  if (token) {
    try {
      return jwtDecode.jwtDecode(token);
    } catch (error) {
      console.error('Erro ao decodificar o token:', error);
    }
  }
  return null;
};
