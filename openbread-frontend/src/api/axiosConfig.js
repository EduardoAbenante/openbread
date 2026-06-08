import axios from "axios";

export const BACKEND_URL = "http://localhost:8080"; 

const api = axios.create({
  baseURL: BACKEND_URL, 
});

api.interceptors.request.use(
  (config) => {
    // No enviar token en las rutas públicas de autenticación
    if (!config.url.includes("/auth/login")) {
      const token = localStorage.getItem("token");
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,

  async (error) => {
    const originalRequest = error.config;

    // Si el token está caducado o no es válido (Spring Security suele lanzar 403 Forbidden)
    if (error.response?.status === 403) {
      console.warn("Sesión inválida o token expirado. Cerrando sesión…");

      // Evitar loops infinitos en reintentos
      if (!originalRequest._retry) {
        originalRequest._retry = true;
      }

      // Limpiar datos de sesión del navegador
      localStorage.removeItem("token");

      // Redirigir de forma segura al login
      window.location.href = "/login";
    }

    return Promise.reject(error);
  }
);

// 3. Exportación por defecto de la instancia configurada
export default api;