import { useState } from "react";
import { ImageUploadZone } from '../../components/common/ImageUploadZone';

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

  // Clases reutilizables para evitar repetir código en etiquetas internas
  const labelClass = "block font-medium text-[var(--color-text)] opacity-70 text-[0.815rem] mb-1.5";
  
  const inputClass = "w-full h-[38px] px-3.5 rounded-[0.375rem] border border-[var(--color-border)] bg-white text-[0.875rem] text-[var(--color-text)] outline-none transition-all duration-150 ease-in-out focus:border-[var(--color-primary)] focus:ring-[3px] focus:ring-[rgba(123,75,42,0.15)] disabled:bg-[#f8fafc] disabled:text-[#94a3b8] disabled:border-[var(--color-border)] disabled:cursor-not-allowed appearance-none";

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
            <div className="sm:col-span-1">
              <label htmlFor="op-nif" className={labelClass}>NIF *</label>
              <input id="op-nif" className={inputClass} value={form.nif || ""} onChange={(e) => update("nif", e.target.value)} disabled={!!initial.id} required />
            </div>

            {/* ROL */}
            <div className="sm:col-span-1">
              <label htmlFor="op-role" className={labelClass}>Rol *</label>
              <div className="relative">
                <select id="op-role" className={inputClass} value={form.role} onChange={(e) => update("role", e.target.value)} required>
                  <option value="USER">Usuario estándar</option>
                  <option value="ADMIN">Administrador</option>
                </select>
                <div className="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none text-[0.7rem] text-[var(--color-primary)] opacity-60">
                  ▼
                </div>
              </div>
            </div>

            {/* NOMBRE */}
            <div className="sm:col-span-1">
              <label htmlFor="op-name" className={labelClass}>Nombre *</label>
              <input id="op-name" className={inputClass} value={form.name || ""} onChange={(e) => update("name", e.target.value)} required />
            </div>

            {/* APELLIDOS */}
            <div className="sm:col-span-1">
              <label htmlFor="op-surname" className={labelClass}>Apellidos *</label>
              <input id="op-surname" className={inputClass} value={form.surname || ""} onChange={(e) => update("surname", e.target.value)} required />
            </div>

            {/* EMAIL (Ocupa todo el ancho - col-span-2) */}
            <div className="sm:col-span-2">
              <label htmlFor="op-email" className={labelClass}>Correo electrónico *</label>
              <input id="op-email" type="email" className={inputClass} value={form.email || ""} onChange={(e) => update("email", e.target.value)} disabled={!!initial.id} required />
            </div>

            {/* CONTRASEÑA (Ocupa todo el ancho - col-span-2) */}
            {!initial.id && (
              <div className="sm:col-span-2">
                <label htmlFor="op-pass" className={labelClass}>Contraseña *</label>
                <input id="op-pass" type="password" className={inputClass} value={form.password || ""} onChange={(e) => update("password", e.target.value)} required />
              </div>
            )}

            {/* TELÉFONO */}
            <div className="sm:col-span-1">
              <label htmlFor="op-phone" className={labelClass}>Teléfono</label>
              <input id="op-phone" type="tel" className={inputClass} value={form.phone || ""} onChange={(e) => update("phone", e.target.value)} />
            </div>

            {/* CÓDIGO POSTAL */}
            <div className="sm:col-span-1">
              <label htmlFor="op-postal" className={labelClass}>Código postal</label>
              <input id="op-postal" className={inputClass} value={form.postalCode || ""} onChange={(e) => update("postalCode", e.target.value)} />
            </div>
          </div>

          <div className="flex flex-col gap-6 h-full">
            <ImageUploadZone 
              label="Foto de perfil"
              selectedFile={form.photoFile}
              existingImageUrl={form.avatarUrl} 
              onFileChange={(file) => update("photoFile", file)}
            />

            <div className="flex justify-end gap-3 mt-auto pt-4 md:pt-0">
              {/* Botón Cancelar */}
              <button 
                type="button" 
                onClick={onCancel}
                className="h-[38px] px-6 rounded-[0.375rem] font-medium text-[0.875rem] inline-flex items-center justify-center cursor-pointer transition-all duration-150 ease-in-out bg-[rgba(176,0,32,0.05)] text-[#b00020] border border-[rgba(176,0,32,0.15)] hover:bg-[#b00020] hover:text-white"
              >
                Cancelar
              </button>
              
              {/* Botón Guardar */}
              <button 
                type="submit"
                className="h-[38px] px-6 rounded-[0.375rem] font-medium text-[0.875rem] inline-flex items-center justify-center cursor-pointer transition-all duration-150 ease-in-out bg-[var(--color-primary)] text-white border-none hover:bg-[var(--color-primary-dark)]"
              >
                Guardar operario
              </button>
            </div>
          </div>

        </div>
      </form>
    </div>
  );
}