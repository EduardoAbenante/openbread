import api from "../../api/axiosConfig";

export const getOperarios = async (params = {}) => {
  const res = await api.get("/user/users", { params });
  return res.data;
};

export const createOperario = async (data) => {
  const res = await api.post("/user", data);
  return res.data;
};

export const updateOperario = async (id, data) => {
  const res = await api.put(`/user/${id}`, data);
  return res.data;
};

export const updateOperarioRole = async (id, role) => {
  const res = await api.put(`/user/${id}/role`, { role });
  return res.data;
};

export const updateOperarioPassword = async (id, password) => {
  const res = await api.put(`/user/${id}/password`, { password });
  return res.data;
};

export const uploadOperarioAvatar = async(id, file) => {
  const formData = new FormData();
  formData.append('photoFile', file);

  const res = await api.post(`/user/${id}/photo`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
  return res.data;
}

export const activateOperario = async (id) => {
  const res = await api.put(`/user/${id}/activate`);
  return res.data;
};

export const deleteOperario = async (id) => {
  await api.delete(`/user/${id}`);
};
