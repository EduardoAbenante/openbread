export default function OperariosTable({ operarios, sortConfig, onSort, onEdit, onDelete, onActivate }) {
  
  const renderSortIcon = (key) => {
    if (!sortConfig || sortConfig.key !== key) return <span className="sort-icon">↕</span>;
    return sortConfig.direction === "asc" ? <span className="sort-icon">▲</span> : <span className="sort-icon">▼</span>;
  };

  return (
    <div className="ob-table-container">
      <table className="ob-table">
        <thead>
          <tr>
            <th className="sortable" onClick={() => onSort("nif")}>
              NIF {renderSortIcon("nif")}
            </th>
            <th className="sortable" onClick={() => onSort("name")}>
              Nombre Completo {renderSortIcon("name")}
            </th>
            <th className="sortable" onClick={() => onSort("email")}>
              Correo electrónico {renderSortIcon("email")}
            </th>
            <th className="sortable" onClick={() => onSort("phone")}>
              Teléfono {renderSortIcon("phone")}
            </th>
            <th className="sortable" onClick={() => onSort("postalCode")}>
              C.P. {renderSortIcon("postalCode")}
            </th>
            <th className="sortable" onClick={() => onSort("role")}>
              Rol {renderSortIcon("role")}
            </th>
            <th className="sortable" onClick={() => onSort("active")}>
              Estado {renderSortIcon("active")}
            </th>
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
                <span className={`badge-role ${op.role === 'ADMIN' ? 'role-admin' : 'role-standard'}`}>
                  {op.role === 'ADMIN' ? 'Administrador' : 'Estándar'}
                </span>
              </td>
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
          {operarios.length === 0 && (
            <tr>
              <td colSpan="8" style={{ textAlign: "center", padding: "2.5rem", color: "#94a3b8" }}>
                No se encontraron operarios con los criterios seleccionados.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}