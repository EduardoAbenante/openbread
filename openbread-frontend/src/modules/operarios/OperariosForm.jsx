import { useState } from "react";

export default function OperariosForm({ initial, onSubmit, onCancel }) {
  const [form, setForm] = useState(initial);

  const update = (field, value) =>
    setForm({ ...form, [field]: value });

  return (
    <div className="ob-modal">
      <div className="ob-modal-content">
        <h2>{initial.id ? "Editar operario" : "Nuevo operario"}</h2>

        <input
          value={form.nif}
          onChange={(e) => update("nif", e.target.value)}
          placeholder="NIF"
          required
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

        <input
          value={form.email}
          onChange={(e) => update("email", e.target.value)}
          placeholder="Correo electrónico"
          required
        />

        {!initial.id && (
          <input
            type="password"
            value={form.password}
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

        <label>
          <input
            type="checkbox"
            checked={form.active}
            onChange={(e) => update("active", e.target.checked)}
          />
          Activo
        </label>

        <div className="ob-modal-actions">
          <button onClick={() => onSubmit(form)}>Guardar</button>
          <button className="danger" onClick={onCancel}>Cancelar</button>
        </div>
      </div>
    </div>
  );
}
