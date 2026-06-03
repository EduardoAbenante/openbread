import "./operarios.css";

import "./operarios.css";

export default function OperariosTable({ operarios, onEdit, onDelete }) {
  return (
    <table className="ob-table">
      <thead>
        <tr>
          <th>NIF</th>
          <th>Nombre</th>
          <th>Apellidos</th>
          <th>Teléfono</th>
          <th>Activo</th>
          <th>Acciones</th>
        </tr>
      </thead>

      <tbody>
        {operarios.map((op) => (
          <tr key={op.id}>
            <td>{op.nif}</td>
            <td>{op.name}</td>
            <td>{op.surname}</td>
            <td>{op.phone}</td>
            <td>{op.active ? "Sí" : "No"}</td>
            <td>
              <button onClick={() => onEdit(op)}>Editar</button>
              <button className="danger" onClick={() => onDelete(op)}>
                Eliminar
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
