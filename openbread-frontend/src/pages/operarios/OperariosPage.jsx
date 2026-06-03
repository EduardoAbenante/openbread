import { useEffect, useState, useMemo } from "react";
import Section from "../../components/common/Section";
import Card from "../../components/common/Card";
import OperariosTable from "../../modules/operarios/OperariosTable";
import OperariosForm from "../../modules/operarios/OperariosForm";
import OperariosDeleteModal from "../../modules/operarios/OperariosDeleteModal";
import OperariosActivateModal from "../../modules/operarios/OperariosActivateModal";
import { Modal } from "../../components/common/Modal"; 
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

  // 1. ESTADO DE FILTROS (Mapeados con los RequestParam de tu backend en Kotlin)
  const [filters, setFilters] = useState({
    nif: "",
    name: "",
    email: "",
    active: "" // Puede ser "", "true" o "false"
  });

  // 2. ESTADO DE ORDENACIÓN LOCAL (Atributo de la columna y dirección)
  const [sortConfig, setSortConfig] = useState({ key: null, direction: "asc" });

  // Función de carga conectada a la API que inyecta los filtros activos
  const load = async () => {
    const queryParams = {};
    Object.keys(filters).forEach((key) => {
      if (filters[key] !== "") {
        queryParams[key] = key === "active" ? filters[key] === "true" : filters[key];
      }
    });

    try {
      const data = await getOperarios(queryParams);
      setOperarios(data);
    } catch (error) {
      console.error("Error al consultar los operarios filtrados:", error);
    }
  };

  // 3. EFFECT CON DEBOUNCE PARA LA BÚSQUEDA ASÍNCRONA
  useEffect(() => {
    const delayDebounce = setTimeout(() => {
      load();
    }, 300);

    return () => clearTimeout(delayDebounce);
  }, [filters]);

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  const handleSort = (key) => {
    let direction = "asc";
    if (sortConfig.key === key && sortConfig.direction === "asc") {
      direction = "desc";
    }
    setSortConfig({ key, direction });
  };

  const sortedOperarios = useMemo(() => {
    let sortableItems = [...operarios];
    if (sortConfig.key !== null) {
      sortableItems.sort((a, b) => {
        const valA = a[sortConfig.key] ? a[sortConfig.key].toString().toLowerCase() : "";
        const valB = b[sortConfig.key] ? b[sortConfig.key].toString().toLowerCase() : "";

        if (valA < valB) return sortConfig.direction === "asc" ? -1 : 1;
        if (valA > valB) return sortConfig.direction === "asc" ? 1 : -1;
        return 0;
      });
    }
    return sortableItems;
  }, [operarios, sortConfig]);

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
      
      {/* BARRA DE FILTROS PREMIUM (Estilo SaaS 2026) */}
      <div className="ob-filters-panel">
        <div className="ob-filter-group">
          <label>Buscar NIF</label>
          <input
            type="text"
            name="nif"
            placeholder="Ej. 00000000A"
            value={filters.nif}
            onChange={handleFilterChange}
          />
        </div>
        <div className="ob-filter-group">
          <label>Nombre o Apellido</label>
          <input
            type="text"
            name="name"
            placeholder="Buscar por nombre..."
            value={filters.name}
            onChange={handleFilterChange}
          />
        </div>
        <div className="ob-filter-group">
          <label>Correo electrónico</label>
          <input
            type="text"
            name="email"
            placeholder="Ej. admin@openbread..."
            value={filters.email}
            onChange={handleFilterChange}
          />
        </div>
        <div className="ob-filter-group">
          <label>Estado</label>
          <select name="active" value={filters.active} onChange={handleFilterChange}>
            <option value="">Todos los estados</option>
            <option value="true">Activos</option>
            <option value="false">Inactivos</option>
          </select>
        </div>
      </div>

      <Card>
        <div className="ob-toolbar">
          <button onClick={() => setEditing({ nif: "", name: "", surname: "", phone: "", active: true })}>
            Nuevo operario
          </button>
        </div>

        <OperariosTable
          operarios={sortedOperarios}
          sortConfig={sortConfig}
          onSort={handleSort}
          onEdit={setEditing}
          onDelete={setDeleting}
          onActivate={handleActivateClick}
        />
      </Card>

      {/* ==========================================================================
          MODALES ENVOLTORIO PROTEGIDOS CONTRA RENDERIZADOS NULOS
          ========================================================================== */}
      
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