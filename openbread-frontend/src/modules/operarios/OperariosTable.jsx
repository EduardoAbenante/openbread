export default function OperariosTable({ operarios, sortConfig, onSort, onEdit, onDelete, onActivate }) {
  
  const renderSortIcon = (key) => {
    if (!sortConfig || sortConfig.key !== key) return <span className="inline-block ml-1.5 text-[0.7rem] transition-transform duration-200 text-[var(--color-primary)]">↕</span>;
    return sortConfig.direction === "asc" ? <span className="inline-block ml-1.5 text-[0.7rem] transition-transform duration-200 text-[var(--color-primary)]">▲</span> : <span className="inline-block ml-1.5 text-[0.7rem] transition-transform duration-200 text-[var(--color-primary)]">▼</span>;
  };

  const thClass = "bg-[rgba(123,75,42,0.08)] color-[#5c3a21] font-bold uppercase text-[0.75rem] tracking-[0.05em] p-4 border-b-2 border-b-[rgba(123,75,42,0.18)] text-left";
  const sortableThClass = `${thClass} cursor-pointer select-none transition-colors duration-200 hover:bg-[rgba(123,75,42,0.12)] hover:text-[var(--color-primary-dark)]`;
  const tdClass = "p-4 text-[var(--color-text)] align-middle border-b border-b-[rgba(123,75,42,0.08)]";
  const btnBaseClass = "h-8 min-w-[82px] px-2 rounded-[0.375rem] text-[0.815rem] font-semibold cursor-pointer inline-flex items-center justify-center transition-all duration-150 border border-transparent";

  return (
    <div className="w-full overflow-x-auto rounded-[0.75rem] border border-[rgba(123,75,42,0.2)] bg-[var(--color-surface)]">
      <table className="w-full border-collapse text-left text-[0.875rem]">
        <thead>
          <tr>
            <th className={sortableThClass} onClick={() => onSort("nif")}>
              NIF {renderSortIcon("nif")}
            </th>
            <th className={sortableThClass} onClick={() => onSort("name")}>
              Nombre Completo {renderSortIcon("name")}
            </th>
            <th className={sortableThClass} onClick={() => onSort("email")}>
              Correo electrónico {renderSortIcon("email")}
            </th>
            <th className={sortableThClass} onClick={() => onSort("phone")}>
              Teléfono {renderSortIcon("phone")}
            </th>
            <th className={sortableThClass} onClick={() => onSort("postalCode")}>
              C.P. {renderSortIcon("postalCode")}
            </th>
            <th className={sortableThClass} onClick={() => onSort("role")}>
              Rol {renderSortIcon("role")}
            </th>
            <th className={sortableThClass} onClick={() => onSort("active")}>
              Estado {renderSortIcon("active")}
            </th>
            <th className={thClass} style={{ textAlign: "right" }}>Acciones</th>
          </tr>
        </thead>

        <tbody>
          {operarios.map((op, idx) => (
            <tr key={op.id} className={`hover:bg-[rgba(123,75,42,0.04)] transition-colors duration-150 ${idx === operarios.length - 1 ? 'border-b-0' : ''}`}>
              <td className={tdClass} style={{ fontWeight: 500 }}>{op.nif}</td>
              <td className={tdClass}>{`${op.name} ${op.surname}`}</td>
              <td className={tdClass}>{op.email}</td>
              <td className={tdClass}>{op.phone || "—"}</td>
              <td className={tdClass}>{op.postalCode || "—"}</td>
              <td className={tdClass}>
                <span className={`inline-flex items-center px-[0.65rem] py-[0.3rem] rounded-[0.375rem] text-[0.75rem] font-bold ${op.role === 'ADMIN' ? 'bg-[#fef3c7] text-[#92400e]' : 'bg-[#e0f2fe] text-[#075985]'}`}>
                  {op.role === 'ADMIN' ? 'Administrador' : 'Estándar'}
                </span>
              </td>
              <td className={tdClass}>
                <span className={`inline-flex items-center px-[0.65rem] py-[0.3rem] rounded-[0.375rem] text-[0.75rem] font-semibold ${op.active ? "bg-[#e6f4ea] text-[#137333]" : "bg-[#f1f3f4] text-[#5f6368]"}`}>
                  {op.active ? "Activo" : "Inactivo"}
                </span>
              </td>
              <td className={tdClass}>
                <div className="flex gap-2 items-center justify-end">
                  <button className={`${btnBaseClass} bg-white text-[var(--color-primary)] border-[rgba(123,75,42,0.35)] hover:bg-[rgba(123,75,42,0.08)] hover:border-[var(--color-primary)]`} onClick={() => onEdit(op)}>
                    Editar
                  </button>
                  {!op.active ? (
                    <button className={`${btnBaseClass} bg-[#3b6139] text-white hover:bg-[#2d4a2b]`} onClick={() => onActivate(op)}>
                      Activar
                    </button>
                  ) : (
                    <button className={`${btnBaseClass} bg-[#fce8e6] text-[#c5221f] border-[#fad2cf] hover:bg-[#c5221f] hover:text-white hover:border-[#c5221f]`} onClick={() => onDelete(op)}>
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