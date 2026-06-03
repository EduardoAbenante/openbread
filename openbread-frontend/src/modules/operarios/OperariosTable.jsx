import "./operarios.css";

export default function OperariosTable({ operarios, onEdit, onDelete, onActivate }) {
  return (
    <div className="ob-table-container">
      <table className="ob-table">
        <thead>
          <tr>
            <th>NIF</th>
            <th>Nombre Completo</th>
            <th>Correo electrónico</th>
            <th>Teléfono</th>
            <th>C.P.</th>
            <th>Estado</th>
            <th style={{ textAlign: "right" }}>Acciones</th>
          </tr>
        </thead>

        <tbody>
          {operarios.map((op) => (
            <tr key={op.id}>
              <td style={{ fontWeight: 500 }}>{op.nif}</td>
              <td>{`${op.name} ${op.surname}`}</td>
              <td>{op.email}</td>
              <td>{op.phone || "—"}</td>
              <td>{op.postalCode || "—"}</td>
              <td>
                <span className={`badge ${op.active ? "active" : "inactive"}`}>
                  {op.active ? "Activo" : "Inactivo"}
                </span>
              </td>
              <td>
                <div className="ob-actions-cell" style={{ justifyContent: "flex-end" }}>
                  <button className="edit-button" onClick={() => onEdit(op)}>
                    Editar
                  </button>
                  {!op.active ? (
                    <button className="activate-button" onClick={() => onActivate(op)}>
                      Activar
                    </button>
                  ) : (
                    <button className="danger" onClick={() => onDelete(op)}>
                      Eliminar
                    </button>
                  )}
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}