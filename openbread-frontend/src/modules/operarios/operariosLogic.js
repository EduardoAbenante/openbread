export const buildOperarioPayload = (data = {}) => ({
  id: data.id,
  nif: data.nif,
  name: data.name || data.nombre,
  surname: data.surname || data.apellido,
  email: data.email,
  password: data.password,
  role: data.role,
  phone: data.phone || data.telefono,
  postalCode: data.postalCode || data.cp,
  active: data.active,
});

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
