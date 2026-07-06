import api from "../../api/axiosConfig";

export const getOperarios = async (params = {}) => {
  const res = await api.get("/users", { params });
  return res.data;
};

export const getOperario = async (id) => {
  const res = await api.get(`/users/${id}`);
  return res.data;
};

export const createOperario = async (data) => {
  const res = await api.post("/users", data);
  return res.data;
};

export const updateOperario = async (id, data) => {
  const res = await api.put(`/users/${id}`, data);
  return res.data;
};

export const updateOperarioRole = async (id, role) => {
  const res = await api.put(`/users/${id}/role`, { role });
  return res.data;
};

export const updateOperarioPassword = async (id, password) => {
  const res = await api.put(`/users/${id}/password`, { password });
  return res.data;
};

export const uploadOperarioAvatar = async(id, file) => {
  const formData = new FormData();
  formData.append('avatarFile', file);

  const res = await api.post(`/users/${id}/avatar`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
  return res.data;
}

export const activateOperario = async (id) => {
  const res = await api.put(`/users/${id}/activate`);
  return res.data;
};

export const deleteOperario = async (id) => {
  await api.delete(`/users/${id}`);
};
