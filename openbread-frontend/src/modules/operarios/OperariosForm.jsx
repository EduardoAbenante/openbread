import { useState } from "react";
import { ImageUploadZone } from '../../components/common/ImageUploadZone';
import Button from "../../components/ui/Button";
import Input from "../../components/ui/Input";
import Select from "../../components/ui/Select";

export default function OperariosForm({ initial, onSubmit, onCancel }) {
  const [form, setForm] = useState({
    ...initial,
    role: initial.role || "USER",
    photoFile: null
  });

  const [error, setError] = useState("");

  const update = (field, value) => setForm({ ...form, [field]: value });

  const handleSubmit = async () => {
    if (!form.nif?.trim()) return setError("El NIF es obligatorio");
    if (!form.name?.trim()) return setError("El nombre es obligatorio");
    if (!form.surname?.trim()) return setError("Los apellidos es obligatorio");
    if (!form.email?.trim()) return setError("El correo es obligatorio");
    if (!initial.id && !form.password?.trim()) return setError("La contraseña es obligatoria");

    try {
      await onSubmit(form);
    } catch (err) {
      const serverMessage = err.response?.data?.message || err.response?.data?.error;
      const validationErrors = err.response?.data?.errors ? Object.entries(err.response.data.errors).map(([k, v]) => `${k}: ${v}`).join(", ") : null;
      setError(serverMessage || validationErrors || "Error inesperado");
    }
  };

  return (
    <div className="bg-[var(--color-surface)] p-8 rounded-[0.875rem] w-full max-w-[1080px] max-h-[90vh] overflow-y-auto text-left shadow-[0_20px_25px_-5px_rgba(40,25,15,0.15)] box-border">
      
      <div className="mb-6">
        <h2 className="text-[1.25rem] font-semibold text-[var(--color-primary)] m-0 uppercase tracking-[0.02em]">
          {initial.id ? "Editar usuario" : "Nuevo usuario"}
        </h2>
        {error && (
          <p className="text-[#b00020] bg-[rgba(176,0,32,0.05)] p-3 rounded-0.5rem font-medium text-[0.875rem] mt-3 rounded-[0.5rem]">
            {error}
          </p>
        )}
      </div>

      <form onSubmit={(e) => { e.preventDefault(); handleSubmit(); }}>
        <div className="grid grid-cols-1 md:grid-cols-[1.5fr_1fr] gap-6 md:gap-10">
          
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-5 gap-y-4">
            
            {/* NIF */}
            <Input 
              id="op-nif" 
              label="NIF *" 
              value={form.nif || ""} 
              onChange={(e) => update("nif", e.target.value)} 
              disabled={!!initial.id} 
              required 
            />

            {/* ROL */}
            <Select 
              id="op-role" 
              label="Rol *" 
              value={form.role} 
              onChange={(e) => update("role", e.target.value)} 
              required
            >
              <option value="USER">Usuario estándar</option>
              <option value="ADMIN">Administrador</option>
            </Select>

            {/* NOMBRE */}
            <Input 
              id="op-name" 
              label="Nombre *" 
              value={form.name || ""} 
              onChange={(e) => update("name", e.target.value)} 
              required 
            />

            {/* APELLIDOS */}
            <Input 
              id="op-surname" 
              label="Apellidos *" 
              value={form.surname || ""} 
              onChange={(e) => update("surname", e.target.value)} 
              required 
            />

            {/* EMAIL */}
            <Input 
              id="op-email" 
              type="email" 
              label="Correo electrónico *" 
              className="sm:col-span-2" 
              value={form.email || ""} 
              onChange={(e) => update("email", e.target.value)} 
              disabled={!!initial.id} 
              required 
            />

            {/* CONTRASEÑA */}
            {!initial.id && (
              <Input 
                id="op-pass" 
                type="password" 
                label="Contraseña *" 
                className="sm:col-span-2" 
                value={form.password || ""} 
                onChange={(e) => update("password", e.target.value)} 
                required 
              />
            )}

            {/* TELÉFONO */}
            <Input 
              id="op-phone" 
              type="tel" 
              label="Teléfono" 
              value={form.phone || ""} 
              onChange={(e) => update("phone", e.target.value)} 
            />

            {/* CÓDIGO POSTAL */}
            <Input 
              id="op-postal" 
              label="Código postal" 
              value={form.postalCode || ""} 
              onChange={(e) => update("postalCode", e.target.value)} 
            />
          </div>

          <div className="flex flex-col gap-6 h-full">
            <ImageUploadZone 
              label="Foto de perfil"
              selectedFile={form.photoFile}
              existingImageUrl={form.avatarUrl} 
              onFileChange={(file) => update("photoFile", file)}
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