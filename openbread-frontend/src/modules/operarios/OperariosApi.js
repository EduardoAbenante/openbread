import api from "../../api/axiosConfig";

export const getOperarios = async () => {
    const res = await api.get("users?role=OPERARIO");
    return res.data;
}

export const createOperario = async (data) => {
  const res = await api.post("/users", data);
  return res.data;
};

export const updateOperario = async (id, data) => {
  const res = await api.put(`/users/${id}`, data);
  return res.data;
};

export const deleteOperario = async (id) => {
  await api.delete(`/users/${id}`);
};