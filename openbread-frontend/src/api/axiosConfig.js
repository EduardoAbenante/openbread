import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080", 
});

api.interceptors.request.use(
  (config) => {
    // No enviar token en login
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

    // Si el token está caducado → 403
    if (error.response?.status === 403) {
      console.warn("Token expirado. Cerrando sesión…");

      // Evitar loops infinitos
      if (!originalRequest._retry) {
        originalRequest._retry = true;
      }

      // Limpiar sesión
      localStorage.removeItem("token");

      // Redirigir al login
      window.location.href = "/login";
    }

    return Promise.reject(error);
  }
);

export default api;
