import { z } from "zod";

export const userCreateSchema = z.object({
  nif: z
    .string()
    .min(1, "El NIF es obligatorio")
    .regex(/^[0-9]{8}[A-Z]$/, "Formato de NIF inválido (ej: 12345678A)"),
  
  name: z
    .string()
    .min(1, "El nombre es obligatorio")
    .regex(/^[^0-9]*$/, "El nombre no puede contener números"),
  
  surname: z
    .string()
    .min(1, "El apellido es obligatorio")
    .regex(/^[^0-9]*$/, "El apellido no puede contener números"),
  
  email: z
    .string()
    .min(1, "El correo electrónico es obligatorio")
    .email("El formato del correo electrónico no es válido"),
  
  password: z
    .string()
    .min(6, "La contraseña debe tener al menos 6 caracteres")
    .regex(
      /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{6,}$/,
      "Debe incluir al menos una letra, un número y un carácter especial (@$!%*#?&)"
    ),
  
  // .transform convierte un string vacío en undefined para que el backend lo reciba como null si no se rellena
  phone: z
    .string()
    .optional()
    .transform(val => val === "" ? undefined : val),
    
  postalCode: z
    .string()
    .optional()
    .transform(val => val === "" ? undefined : val),
    
  role: z.enum(["ADMIN", "USER"], {
    errorMap: () => ({ message: "Selecciona un rol válido" }),
  }),
});