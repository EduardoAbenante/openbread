import axios from "axios";

export const BACKEND_URL = "";

const api = axios.create({
  baseURL: BACKEND_URL,
});

const isPublicRoute = (url) => ["/auth/login"].some((route) => url?.includes(route));

api.interceptors.request.use(
  (config) => {
    if (!isPublicRoute(config.url)) {
      const token = localStorage.getItem("token");
      if (token) {
        config.headers = config.headers || {};
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 403) {
      localStorage.removeItem("token");
      window.location.assign("/login");
    }

    return Promise.reject(error);
  }
);

export default api;