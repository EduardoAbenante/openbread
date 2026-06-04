import Badge from "../../components/ui/Badge";
import Button from "../../components/ui/Button";

export default function OperariosTable({ operarios, sortConfig, onSort, onEdit, onDelete, onActivate }) {
  
  const renderSortIcon = (key) => {
    if (!sortConfig || sortConfig.key !== key) return <span className="inline-block ml-1.5 text-[0.7rem] transition-transform duration-200 text-[var(--color-primary)]">↕</span>;
    return sortConfig.direction === "asc" ? <span className="inline-block ml-1.5 text-[0.7rem] transition-transform duration-200 text-[var(--color-primary)]">▲</span> : <span className="inline-block ml-1.5 text-[0.7rem] transition-transform duration-200 text-[var(--color-primary)]">▼</span>;
  };

  const thClass = "bg-[rgba(123,75,42,0.08)] color-[#5c3a21] font-bold uppercase text-[0.75rem] tracking-[0.05em] p-4 border-b-2 border-b-[rgba(123,75,42,0.18)] text-left";
  const sortableThClass = `${thClass} cursor-pointer select-none transition-colors duration-200 hover:bg-[rgba(123,75,42,0.12)] hover:text-[var(--color-primary-dark)]`;
  const tdClass = "p-4 text-[var(--color-text)] align-middle border-b border-b-[rgba(123,75,42,0.08)]";

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
                <Badge variant={op.role === 'ADMIN' ? 'warning' : 'info'}>
                  {op.role === 'ADMIN' ? 'Administrador' : 'Estándar'}
                </Badge>
              </td>
              <td className={tdClass}>
                <Badge variant={op.active ? 'success' : 'gray'}>
                  {op.active ? "Activo" : "Inactivo"}
                </Badge>
              </td>
              <td className={tdClass}>
                <div className="flex gap-2 items-center justify-end">
                  <Button variant="outline" className="h-8 min-w-[82px] px-2 text-[0.815rem]" onClick={() => onEdit(op)}>
                    Editar
                  </Button>
                  {!op.active ? (
                    <Button variant="success" className="h-8 min-w-[82px] px-2 text-[0.815rem]" onClick={() => onActivate(op)}>
                      Activar
                    </Button>
                  ) : (
                    <Button variant="danger" className="h-8 min-w-[82px] px-2 text-[0.815rem]" onClick={() => onDelete(op)}>
                      Eliminar
                    </Button>
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