import { useState } from "react";

export default function OperariosForm({ initial, onSubmit, onCancel }) {
  const [form, setForm] = useState({
    ...initial,
    role: initial.role || "USER",
    photoFile: null
  });

  const [error, setError] = useState("");

  const update = (field, value) =>
    setForm({ ...form, [field]: value });

  const handleSubmit = async () => {
    if (!form.nif.trim()) return setError("El NIF es obligatorio");
    if (!form.name.trim()) return setError("El nombre es obligatorio");
    if (!form.surname.trim()) return setError("Los apellidos son obligatorios");
    if (!form.email.trim()) return setError("El correo es obligatorio");
    if (!initial.id && !form.password?.trim())
      return setError("La contraseña es obligatoria");

    try {
      await onSubmit(form);
    } catch (err) {
      if (err.response?.data?.message) {
        setError(err.response.data.message);
      } else if (err.response?.data?.errors) {
        setError(err.response.data.errors.join(", "));
      } else {
        setError("Error inesperado");
      }
    }
  };

  return (
    <div className="ob-modal" role="dialog" aria-modal="true" aria-labelledby="operarios-form-title">
      <div className="ob-modal-content">
        <div style={{ width: "100%" }}>
          <h2 id="operarios-form-title">{initial.id ? "Editar usuario" : "Nuevo usuario"}</h2>
          {error && <p className="ob-error">{error}</p>}
        </div>

        <form
          className="ob-modal-form"
          onSubmit={(e) => {
            e.preventDefault();
            handleSubmit();
          }}
        >
          <div className="ob-form-row">
            <div className="ob-form-left">
              <label htmlFor="operario-nif">NIF *</label>
              <input
                id="operario-nif"
                value={form.nif}
                onChange={(e) => update("nif", e.target.value)}
                disabled={!!initial.id}
                required
                aria-required="true"
              />

              <label htmlFor="operario-name">Nombre *</label>
              <input
                id="operario-name"
                value={form.name}
                onChange={(e) => update("name", e.target.value)}
                required
                aria-required="true"
              />

              <label htmlFor="operario-surname">Apellidos *</label>
              <input
                id="operario-surname"
                value={form.surname}
                onChange={(e) => update("surname", e.target.value)}
                required
                aria-required="true"
              />

              <label htmlFor="operario-email">Correo electrónico *</label>
              <input
                id="operario-email"
                type="email"
                value={form.email}
                onChange={(e) => update("email", e.target.value)}
                disabled={!!initial.id}
                required
                aria-required="true"
              />

              {!initial.id && (
                <>
                  <label htmlFor="operario-password">Contraseña *</label>
                  <input
                    id="operario-password"
                    type="password"
                    value={form.password || ""}
                    onChange={(e) => update("password", e.target.value)}
                    required
                    aria-required="true"
                  />
                </>
              )}

              <label htmlFor="operario-phone">Teléfono</label>
              <input
                id="operario-phone"
                type="tel"
                value={form.phone || ""}
                onChange={(e) => update("phone", e.target.value)}
              />

              <label htmlFor="operario-postal">Código postal</label>
              <input
                id="operario-postal"
                value={form.postalCode || ""}
                onChange={(e) => update("postalCode", e.target.value)}
              />

              <label htmlFor="operario-role">Rol *</label>
              <select
                id="operario-role"
                value={form.role}
                onChange={(e) => update("role", e.target.value)}
                required
                aria-required="true"
              >
                <option value="USER">Usuario estándar</option>
                <option value="ADMIN">Administrador</option>
              </select>
            </div>

            <div className="ob-form-right">
              <label htmlFor="operario-photo">Foto de perfil</label>
              <input
                id="operario-photo"
                type="file"
                accept="image/*"
                onChange={(e) => update("photoFile", e.target.files[0])}
              />

              {form.photoFile && (
                <img
                  src={URL.createObjectURL(form.photoFile)}
                  className="photo-preview"
                  alt="Vista previa de la foto de perfil"
                />
              )}

              <div className="ob-modal-actions">
                <button type="submit">Guardar</button>
                <button type="button" className="danger" onClick={onCancel}>
                  Cancelar
                </button>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}
