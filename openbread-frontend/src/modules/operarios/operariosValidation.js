export const getPasswordHelper = (password) => {
  const requirements = [];

  if (!password || password.length < 6) requirements.push('mín. 6 caracteres');
  if (!password || !/[A-Z]/.test(password)) requirements.push('una mayúscula');
  if (!password || !/\d/.test(password)) requirements.push('un número');
  if (!password || !/[!@#$%^&*(),.?":{}|<>]/.test(password)) requirements.push('un carácter especial');

  return requirements.length === 0 ? '¡Contraseña segura!' : `Falta: ${requirements.join(', ')}`;
};

export const getNifHelper = (nif, isEditMode = false) => {
  if (isEditMode) return '';

  const nifRegex = /^[0-9XYZ][0-9]{7}[TRWAGMYFPDXBNJZSQVHLCKE]$/i;
  if (!nif || !nifRegex.test(nif)) return '8 números y letra final (o X/Y/Z inicial)';
  return 'Formato válido';
};

export const getNameHelper = (val) => {
  if (val && /\d/.test(val)) return 'No se permiten números';
  return 'Solo letras permitidas';
};

export const getEmailHelper = (val, isEditMode = false) => {
  if (isEditMode) return '';

  const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;
  if (!val || !emailRegex.test(val)) return 'Ejemplo: usuario@dominio.com';
  return 'Formato correcto';
};
