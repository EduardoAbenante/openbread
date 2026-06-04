import { useEffect, useState, useCallback } from "react";
import Section from "../../components/common/Section";
import Card from "../../components/common/Card";
import OperariosTable from "../../modules/operarios/OperariosTable";
import OperariosForm from "../../modules/operarios/OperariosForm";
import OperariosDeleteModal from "../../modules/operarios/OperariosDeleteModal";
import OperariosActivateModal from "../../modules/operarios/OperariosActivateModal";
import { Modal } from "../../components/common/Modal"; 
import { useTableTools } from "../../hooks/useTableTools";
import {
  getOperarios,
  createOperario,
  updateOperario,
  activateOperario,
  deleteOperario
} from "../../modules/operarios/OperariosApi";

export default function OperariosPage() {
  const [operarios, setOperarios] = useState([]);
  const [loading, setLoading] = useState(false);
  
  // Estados para el control de ventanas modales
  const [editing, setEditing] = useState(null);
  const [deleting, setDeleting] = useState(null);
  const [activating, setActivating] = useState(null);

  // El Hook ahora SOLO se encarga de la ordenación de columnas en el cliente.
  // Pasamos un array vacío [] porque los campos de filtrado ya los procesa el Backend.
  const {
    searchTerm,
    setSearchTerm,
    statusFilter,
    setStatusFilter,
    sortConfig,
    handleSort,
    processedData: sortedOperarios
  } = useTableTools(operarios, []);

  // Función de carga conectada al backend unificado (Smart Search remoto)
  const load = useCallback(async () => {
    setLoading(true);
    const queryParams = {};
    
    // Si hay texto en el buscador, lo mandamos al parámetro unificado 'search'
    if (searchTerm.trim() !== "") {
      queryParams.search = searchTerm.trim();
    }
    
    // Mapeamos el selector al parámetro dinámico 'active'
    if (statusFilter === "active") queryParams.active = true;
    if (statusFilter === "inactive") queryParams.active = false;

    try {
      // Consume el endpoint: /user/users?search=...&active=...
      const data = await getOperarios(queryParams);
      setOperarios(data);
    } catch (error) {
      console.error("Error al consultar los operarios en OpenBread:", error);
    } finally {
      setLoading(false);
    }
  }, [searchTerm, statusFilter]);

  // EFFECT CON DEBOUNCE: Espera 300ms desde que el usuario deja de escribir antes de llamar a la API
  useEffect(() => {
    const delayDebounce = setTimeout(() => {
      load();
    }, 300);

    return () => clearTimeout(delayDebounce);
  }, [searchTerm, statusFilter, load]);

  // Operaciones de persistencia CRUD
  const handleSave = async (data) => {
    // Aseguramos que el mapeo interno coincida con el backend al crear/editar
    const payload = {
      id: data.id,
      nif: data.nif,
      name: data.name || data.nombre,
      surname: data.surname || data.apellido,
      email: data.email,
      password: data.password,
      role: data.role,
      phone: data.phone || data.telefono,
      postalCode: data.postalCode || data.cp,
      active: data.active
    };

    if (payload.id) await updateOperario(payload.id, payload);
    else await createOperario(payload);
    setEditing(null);
    load();
  };

  const handleDelete = async () => {
    await deleteOperario(deleting.id);
    setDeleting(null);
    load();
  };

  const handleActivateClick = (operario) => {
    setActivating(operario);
  };

  const handleActivateConfirm = async () => {
    await activateOperario(activating.id);
    setActivating(null);
    load();
  };

  const filterGroupClass = "flex flex-col gap-[0.4rem]";
  const filterInputClass = "w-full h-9 px-3 rounded-[0.375rem] border border-[var(--color-border)] bg-white text-[0.85rem] text-[var(--color-text)] outline-none transition-all duration-150 focus:border-[var(--color-primary)] focus:ring-[3px] focus:ring-[rgba(123,75,42,0.1)] appearance-none";
  const filterLabelClass = "text-[0.75rem] font-semibold text-[var(--color-primary)] opacity-80 uppercase tracking-[0.03em]";

  return (
    <Section title="Gestión de operarios">  
      
      {/* --- PANEL DE FILTROS OPTIMIZADO (SMART SEARCH REMOTO) --- */}
      <div className="bg-[var(--color-surface)] border border-[rgba(123,75,42,0.15)] rounded-[0.75rem] p-5 mb-6 grid grid-cols-[repeat(auto-fit,minmax(240px,1fr))] gap-4 items-end shadow-[0_2px_4px_rgba(40,25,15,0.02)]">
        <div className={filterGroupClass} style={{ gridColumn: 'span 2' }}>
          <label htmlFor="smartSearch" className={filterLabelClass}>Búsqueda rápida</label>
          <input
            id="smartSearch"
            type="text"
            className={filterInputClass}
            placeholder="Buscar por NIF, nombre, correo, teléfono o CP..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        <div className={filterGroupClass}>
          <label htmlFor="statusFilter" className={filterLabelClass}>Estado</label>
          <div className="relative">
            <select
              id="statusFilter"
              className={filterInputClass}
              value={statusFilter} 
              onChange={(e) => setStatusFilter(e.target.value)}
            >
              <option value="all">Todos los estados</option>
              <option value="active">Activos</option>
              <option value="inactive">Inactivos</option>
            </select>
            <div className="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none text-[0.65rem] text-[var(--color-primary)] opacity-60">
              ▼
            </div>
          </div>
        </div>
      </div>

      {/* --- CONTENEDOR PRINCIPAL --- */}
      <Card>
        <div className="flex justify-end mb-6">
          <button 
            className="px-5 h-10 rounded-[0.5rem] bg-[var(--color-primary)] text-white font-medium text-[0.875rem] cursor-pointer inline-flex items-center justify-center transition-all duration-200 cubic-bezier(0.16,1,0.3,1) shadow-[0_1px_2px_rgba(0,0,0,0.05)] hover:bg-[var(--color-primary-dark)] hover:-translate-y-[1px]"
            onClick={() => setEditing({ nif: "", name: "", surname: "", email: "", phone: "", postalCode: "", active: true })}
          >
            Nuevo operario
          </button>
        </div>

        {loading ? (
          <div style={{ textAlign: "center", padding: "2.5rem", color: "var(--color-primary)", fontWeight: 500 }}>
            Filtrando en servidor...
          </div>
        ) : (
          <OperariosTable
            operarios={sortedOperarios}
            sortConfig={sortConfig}
            onSort={handleSort}
            onEdit={setEditing}
            onDelete={setDeleting}
            onActivate={handleActivateClick}
          />
        )}
      </Card>

      {/* --- MODALES DE ACCIÓN PROTEGIDOS --- */}
      <Modal isOpen={Boolean(editing)}>
        {editing && (
          <OperariosForm
            initial={editing}
            onSubmit={handleSave}
            onCancel={() => setEditing(null)}
          />
        )}
      </Modal>

      <Modal isOpen={Boolean(deleting)}>
        {deleting && (
          <OperariosDeleteModal
            operario={deleting}
            onConfirm={handleDelete}
            onCancel={() => setDeleting(null)}
          />
        )}
      </Modal>

      <Modal isOpen={Boolean(activating)}>
        {activating && (
          <OperariosActivateModal
            operario={activating}
            onConfirm={handleActivateConfirm}
            onCancel={() => setActivating(null)}
          />
        )}
      </Modal>
    </Section>
  );
}