import api from "../../api/axiosConfig.js";

export const createAvatarUploadRequest = (file) => {
  const formData = new FormData();
  formData.append("avatarFile", file);

  return {
    formData,
    config: {
      headers: {},
    },
  };
};

export const getOperarios = async (params = {}) => {
  const res = await api.get("/users", { params });
  return res.data;
};

export const getOperario = async (id) => {
  const res = await api.get(`/users/${id}`);
  return res.data;
};

export const resolveUserId = (value) => {
  if (typeof value === "number") return value;
  if (typeof value === "string" && /^\d+$/.test(value)) return Number(value);
  if (value && typeof value === "object") {
    return value.id ?? value.userId ?? null;
  }
  return null;
};

export const createOperario = async (data) => {
  const res = await api.post("/users", data);
  return resolveUserId(res.data);
};

export const updateOperario = async (id, data) => {
  const res = await api.put(`/users/${id}`, data);
  return resolveUserId(res.data);
};

export const updateOperarioRole = async (id, role) => {
  const res = await api.put(`/users/${id}/role`, { role });
  return res.data;
};

export const updateOperarioPassword = async (id, password) => {
  const res = await api.put(`/users/${id}/password`, { password });
  return res.data;
};

export const uploadOperarioAvatar = async (id, file) => {
  const { formData, config } = createAvatarUploadRequest(file);

  const res = await api.post(`/users/${id}/avatar`, formData, config);
  return res.data;
};

export const activateOperario = async (id) => {
  const res = await api.put(`/users/${id}/activate`);
  return res.data;
};

export const deleteOperario = async (id) => {
  await api.delete(`/users/${id}`);
};
