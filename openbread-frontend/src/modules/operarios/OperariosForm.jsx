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
        />

        <input
          value={form.name}
          onChange={(e) => update("name", e.target.value)}
          placeholder="Nombre"
        />

        <input
          value={form.surname}
          onChange={(e) => update("surname", e.target.value)}
          placeholder="Apellidos"
        />

        <input
          value={form.phone}
          onChange={(e) => update("phone", e.target.value)}
          placeholder="Teléfono"
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
