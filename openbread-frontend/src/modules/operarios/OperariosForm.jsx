import { useState } from "react";
import { useForm, Controller } from "react-hook-form";
import { ImageUploadZone } from '../../components/common/ImageUploadZone';
import Button from "../../components/ui/Button";
import Input from "../../components/ui/Input";
import Select from "../../components/ui/Select";

export default function OperariosForm({ initial, onSubmit, onCancel }) {
  const { register, handleSubmit, control, setValue, watch, formState: { errors } } = useForm({
    defaultValues: {
      ...initial,
      name: initial.name || initial.nombre || "",
      surname: initial.surname || initial.apellido || "",
      phone: initial.phone || initial.telefono || "",
      postalCode: initial.postalCode || initial.cp || "",
      role: initial.role || "USER",
      photoFile: null,
      photoUrl: initial.photoUrl || (initial.photoName ? `/api/media/avatar/${initial.photoName}` : null)
    }
  });

  const [serverError, setServerError] = useState("");
  const photoUrl = watch("photoUrl");

  const onFormSubmit = async (data) => {
    try {
      setServerError("");
      const result = await onSubmit(data);
      
      if (result && (result.photoName || result.avatarUrl)) {
        const photoName = result.photoName || result.avatarUrl?.split('/').pop();
        setValue("photoName", photoName);
        setValue("photoUrl", `/api/media/avatar/${photoName}`);
        setValue("photoFile", null);
      }
    } catch (err) {
      const serverMessage = err.response?.data?.message || err.response?.data?.error;
      const validationErrors = err.response?.data?.errors 
        ? Object.entries(err.response.data.errors).map(([k, v]) => `${k}: ${v}`).join(", ") 
        : null;
          
      setServerError(serverMessage || validationErrors || "Error inesperado al conectar con el servidor de OpenBread");
    }
  };

  return (
    <div className="bg-[var(--color-surface)] p-8 rounded-[0.875rem] w-full max-w-[1080px] max-h-[90vh] overflow-y-auto text-left shadow-[0_20px_25px_-5px_rgba(40,25,15,0.15)] box-border">
      
      <div className="mb-6">
        <h2 className="text-[1.25rem] font-semibold text-[var(--color-primary)] m-0 uppercase tracking-[0.02em]">
          {initial.id ? "Editar usuario" : "Nuevo usuario"}
        </h2>
        {serverError && (
          <p className="text-[#b00020] bg-[rgba(176,0,32,0.05)] p-3 rounded-0.5rem font-medium text-[0.875rem] mt-3 rounded-[0.5rem]">
            {serverError}
          </p>
        )}
      </div>

      <form onSubmit={handleSubmit(onFormSubmit)}>
        <div className="grid grid-cols-1 md:grid-cols-[1.5fr_1fr] gap-6 md:gap-10">
          
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-5 gap-y-4">
            
            {/* NIF */}
            <Input 
              id="op-nif" 
              label="NIF *" 
              {...register("nif", { 
                required: "El NIF es obligatorio",
                pattern: {
                  value: /^[0-9XYZ][0-9]{7}[TRWAGMYFPDXBNJZSQVHLCKE]$/i,
                  message: "Formato de NIF o NIE no válido"
                }
              })}
              error={errors.nif?.message}
              disabled={!!initial.id} 
            />

            {/* ROL */}
            <Select 
              id="op-role" 
              label="Rol *" 
              {...register("role", { required: "El rol es obligatorio" })}
              error={errors.role?.message}
            >
              <option value="USER">Usuario estándar</option>
              <option value="ADMIN">Administrador</option>
            </Select>

            {/* NOMBRE */}
            <Input 
              id="op-name" 
              label="Nombre *" 
              {...register("name", { 
                required: "El nombre es obligatorio",
                pattern: {
                  value: /^[^0-9]+$/,
                  message: "El nombre no puede contener números"
                }
              })}
              error={errors.name?.message}
            />

            {/* APELLIDOS */}
            <Input 
              id="op-surname" 
              label="Apellidos *" 
              {...register("surname", { 
                required: "Los apellidos son obligatorios",
                pattern: {
                  value: /^[^0-9]+$/,
                  message: "Los apellidos no pueden contener números"
                }
              })}
              error={errors.surname?.message}
            />

            {/* EMAIL */}
            <Input 
              id="op-email" 
              type="email" 
              label="Correo electrónico *" 
              className="sm:col-span-2" 
              {...register("email", { 
                required: "El correo es obligatorio",
                pattern: {
                  value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                  message: "Correo electrónico no válido"
                }
              })}
              error={errors.email?.message}
              disabled={!!initial.id} 
            />

            {/* CONTRASEÑA */}
            <Input 
              id="op-pass" 
              type="password" 
              label={initial.id ? "Nueva contraseña (opcional)" : "Contraseña *"} 
              className="sm:col-span-2" 
              {...register("password", { 
                required: initial.id ? false : "La contraseña es obligatoria",
                minLength: {
                  value: 6,
                  message: "Mínimo 6 caracteres"
                },
                validate: {
                  hasUpperCase: (v) => !v || /[A-Z]/.test(v) || "Debe tener al menos una mayúscula",
                  hasNumber: (v) => !v || /\d/.test(v) || "Debe tener al menos un número",
                  hasSpecialChar: (v) => !v || /[!@#$%^&*(),.?":{}|<>]/.test(v) || "Debe tener al menos un carácter especial"
                }
              })}
              error={errors.password?.message}
            />

            {/* TELÉFONO */}
            <Input 
              id="op-phone" 
              type="tel" 
              label="Teléfono" 
              {...register("phone")}
            />

            {/* CÓDIGO POSTAL */}
            <Input 
              id="op-postal" 
              label="Código postal" 
              {...register("postalCode")}
            />
          </div>

          <div className="flex flex-col gap-6 h-full">
            <Controller
              name="photoFile"
              control={control}
              render={({ field }) => (
                <ImageUploadZone 
                  label="Foto de perfil"
                  selectedFile={field.value}
                  existingImageUrl={photoUrl} 
                  onFileChange={field.onChange}
                />
              )}
            />

            <div className="flex justify-end gap-3 mt-auto pt-4 md:pt-0">
              <Button type="button" variant="dangerGhost" onClick={onCancel}>
                Cancelar
              </Button>
              
              <Button type="submit">
                Guardar operario
              </Button>
            </div>
          </div>

        </div>
      </form>
    </div>
  );
}