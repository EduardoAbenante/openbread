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
    if (!form.surname?.trim()) return setError("Los apellidos son obligatorios");
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
    <div className="ob-modal-form-container">
      <div className="ob-modal-header">
        <h2>{initial.id ? "Editar usuario" : "Nuevo usuario"}</h2>
        {error && <p className="ob-error">{error}</p>}
      </div>

      <form className="ob-modal-form" onSubmit={(e) => { e.preventDefault(); handleSubmit(); }}>
        <div className="ob-form-row">
          
          <div className="ob-form-left">
            <div className="half-field">
              <label htmlFor="op-nif">NIF *</label>
              <input id="op-nif" value={form.nif || ""} onChange={(e) => update("nif", e.target.value)} disabled={!!initial.id} required />
            </div>

            <div className="half-field">
              <label htmlFor="op-role">Rol *</label>
              <select id="op-role" value={form.role} onChange={(e) => update("role", e.target.value)} required>
                <option value="USER">Usuario estándar</option>
                <option value="ADMIN">Administrador</option>
              </select>
            </div>

            <div className="half-field">
              <label htmlFor="op-name">Nombre *</label>
              <input id="op-name" value={form.name || ""} onChange={(e) => update("name", e.target.value)} required />
            </div>

            <div className="half-field">
              <label htmlFor="op-surname">Apellidos *</label>
              <input id="op-surname" value={form.surname || ""} onChange={(e) => update("surname", e.target.value)} required />
            </div>

            <label htmlFor="op-email">Correo electrónico *</label>
            <input id="op-email" type="email" value={form.email || ""} onChange={(e) => update("email", e.target.value)} disabled={!!initial.id} required />

            {!initial.id && (
              <>
                <label htmlFor="op-pass">Contraseña *</label>
                <input id="op-pass" type="password" value={form.password || ""} onChange={(e) => update("password", e.target.value)} required />
              </>
            )}

            <div className="half-field">
              <label htmlFor="op-phone">Teléfono</label>
              <input id="op-phone" type="tel" value={form.phone || ""} onChange={(e) => update("phone", e.target.value)} />
            </div>

            <div className="half-field">
              <label htmlFor="op-postal">Código postal</label>
              <input id="op-postal" value={form.postalCode || ""} onChange={(e) => update("postalCode", e.target.value)} />
            </div>
          </div>

          <div className="ob-form-right">
            <ImageUploadZone 
              label="Foto de perfil"
              selectedFile={form.photoFile}
              existingImageUrl={form.avatarUrl} 
              onFileChange={(file) => update("photoFile", file)}
            />

            <div className="ob-modal-actions">
              <button type="button" className="danger" onClick={onCancel}>Cancelar</button>
              <button type="submit">Guardar operario</button>
            </div>
          </div>
        </div>
      </form>
    </div>
  );
}