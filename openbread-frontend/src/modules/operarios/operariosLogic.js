export const buildOperarioPayload = (data = {}) => {
  const payload = {
    ...(data.id && data.id.toString().trim() !== "" ? { id: data.id } : {}),
    nif: data.nif,
    name: data.name || data.nombre,
    surname: data.surname || data.apellido,
    email: data.email,
    role: data.role,
    phone: data.phone || data.telefono || null, 
    postalCode: data.postalCode || data.cp || null, 
    active: data.active !== undefined ? data.active : true,
  };

  if (data.password && data.password.trim() !== "") {
    payload.password = data.password;
  } else {
    delete payload.password;
  }

  return payload;
};

export const createNewOperarioForm = () => ({
  nif: '',
  name: '',
  surname: '',
  email: '',
  phone: '',
  postalCode: '',
  role: 'USER',
  active: true,
  password: '',
  photoFile: null,
  photoUrl: null,
});