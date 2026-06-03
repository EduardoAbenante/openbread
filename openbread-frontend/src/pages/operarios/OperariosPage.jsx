import { useEffect, useState } from "react";
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
  
  // Estados para el control de ventanas modales
  const [editing, setEditing] = useState(null);
  const [deleting, setDeleting] = useState(null);
  const [activating, setActivating] = useState(null);

  // Invocación del Custom Hook con los campos por los que el buscador "Smart Search" rastreará
// Asegúrate de que el hook en OperariosPage consuma los nombres exactos de tu objeto de Kotlin:
  const {
    searchTerm,
    setSearchTerm,
    statusFilter,
    setStatusFilter,
    sortConfig,
    handleSort,
    processedData: filteredAndSortedOperarios
  } = useTableTools(operarios, ["nif", "name", "email", "phone", "postalCode"]);

  // Carga inicial de datos de la API (Sin filtros en backend para delegar en el Smart Search del cliente)
  const load = async () => {
    try {
      const data = await getOperarios();
      setOperarios(data);
    } catch (error) {
      console.error("Error al consultar los operarios:", error);
    }
  };

  useEffect(() => {
    load();
  }, []);

  // Operaciones de persistencia CRUD
  const handleSave = async (data) => {
    if (data.id) await updateOperario(data.id, data);
    else await createOperario(data);
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

  return (
    <Section title="Gestión de operarios">  
      
      {/* --- PANEL DE FILTROS OPTIMIZADO (SMART SEARCH) --- */}
      <div className="ob-filters-panel">
        <div className="ob-filter-group" style={{ gridColumn: 'span 2' }}>
          <label htmlFor="smartSearch">Búsqueda rápida</label>
          <input
            id="smartSearch"
            type="text"
            placeholder="Buscar por NIF, nombre, correo, teléfono o CP..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        <div className="ob-filter-group">
          <label htmlFor="statusFilter">Estado</label>
          <select
            id="statusFilter"
            value={statusFilter} 
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <option value="all">Todos los estados</option>
            <option value="active">Activos</option>
            <option value="inactive">Inactivos</option>
          </select>
        </div>
      </div>

      {/* --- CONTENEDOR PRINCIPAL --- */}
      <Card>
        <div className="ob-toolbar">
          <button onClick={() => setEditing({ nif: "", nombre: "", apellido: "", email: "", telefono: "", cp: "", activo: true })}>
            Nuevo operario
          </button>
        </div>

        {/* Le inyectamos los datos procesados, la configuración y el manejador del hook */}
        <OperariosTable
          operarios={filteredAndSortedOperarios}
          sortConfig={sortConfig}
          onSort={handleSort}
          onEdit={setEditing}
          onDelete={setDeleting}
          onActivate={handleActivateClick}
        />
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