import { useState } from "react";

export default function OperariosForm({ initial, onSubmit, onCancel }) {
  const [form, setForm] = useState({
    ...initial,
    role: initial.role || "USER",   // 👈 Garantiza que role SIEMPRE existe
  });

  const update = (field, value) =>
    setForm({ ...form, [field]: value });

  return (
    <div className="ob-modal">
      <div className="ob-modal-content">
        <h2>{initial.id ? "Editar usuario" : "Nuevo usuario"}</h2>

        {/* NIF (no editable en edición) */}
        <input
          value={form.nif}
          onChange={(e) => update("nif", e.target.value)}
          placeholder="NIF"
          required
          disabled={!!initial.id}
        />

        <input
          value={form.name}
          onChange={(e) => update("name", e.target.value)}
          placeholder="Nombre"
          required
        />

        <input
          value={form.surname}
          onChange={(e) => update("surname", e.target.value)}
          placeholder="Apellidos"
          required
        />

        {/* Email (no editable en edición) */}
        <input
          value={form.email}
          onChange={(e) => update("email", e.target.value)}
          placeholder="Correo electrónico"
          required
          disabled={!!initial.id}
        />

        {/* Password solo en creación */}
        {!initial.id && (
          <input
            type="password"
            value={form.password || ""}
            onChange={(e) => update("password", e.target.value)}
            placeholder="Contraseña"
            required
          />
        )}

        <input
          value={form.phone || ""}
          onChange={(e) => update("phone", e.target.value)}
          placeholder="Teléfono"
        />

        <input
          value={form.postalCode || ""}
          onChange={(e) => update("postalCode", e.target.value)}
          placeholder="Código postal"
        />

        <input
          value={form.photoUrl || ""}
          onChange={(e) => update("photoUrl", e.target.value)}
          placeholder="URL foto"
        />

        {/* Selector de rol (obligatorio en creación y edición) */}
        <select
          value={form.role}
          onChange={(e) => update("role", e.target.value)}
          required
        >
          <option value="USER">Usuario estándar</option>
          <option value="ADMIN">Administrador</option>
        </select>

        <div className="ob-modal-actions">
          <button onClick={() => onSubmit(form)}>Guardar</button>
          <button className="danger" onClick={onCancel}>Cancelar</button>
        </div>
      </div>
    </div>
  );
}
