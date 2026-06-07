import { useEffect, useState, useCallback } from "react";
import Section from "../../components/common/Section";
import Card from "../../components/common/Card";
import OperariosTable from "../../modules/operarios/OperariosTable";
import OperariosForm from "../../modules/operarios/OperariosForm";
import { Modal } from "../../components/common/Modal"; 
import ConfirmModal from "../../components/ui/ConfirmModal";
import Button from "../../components/ui/Button";
import Input from "../../components/ui/Input";
import Select from "../../components/ui/Select";
import { useTableTools } from "../../hooks/useTableTools";
import {
  getOperarios,
  createOperario,
  updateOperario,
  uploadOperarioAvatar,
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
    // Payload estructurado según los DTOs de Spring Boot (UserCreateDTO / UserUpdateDTO)
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

    try {
      let userId = payload.id;

      // 1. Persistencia de datos de texto planos
      if (payload.id) {
        // Tu PUT devuelve ResponseEntity<Long>
        userId = await updateOperario(payload.id, payload);
      } else {
        // Tu POST devuelve ResponseEntity<Long>
        userId = await createOperario(payload);
      }

      // 2. Si Spring Boot procesó el usuario correctamente y hay una foto en cola, la subimos
      if (data.photoFile && userId) {
        await uploadOperarioAvatar(userId, data.photoFile);
      }

      setEditing(null);
      load(); // Recarga la cuadrícula con el nuevo operario y su miniatura actualizada
    } catch (error) {
      console.error("Error al guardar el operario o su avatar:", error);
      // Lanza el error hacia el formulario para que el bloque catch de OperariosForm lo muestre en pantalla
      throw error;
    }
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
      
      {/* --- PANEL DE FILTROS OPTIMIZADO (SMART SEARCH REMOTO) --- */}
      <div className="bg-[var(--color-surface)] border border-[rgba(123,75,42,0.15)] rounded-[0.75rem] p-5 mb-6 grid grid-cols-[repeat(auto-fit,minmax(240px,1fr))] gap-4 items-end shadow-[0_2px_4px_rgba(40,25,15,0.02)]">
        <Input
          id="smartSearch"
          label="Búsqueda rápida"
          className="md:col-span-2"
          placeholder="Buscar por NIF, nombre, correo, teléfono o CP..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />

        <Select
          id="statusFilter"
          label="Estado"
          value={statusFilter} 
          onChange={(e) => setStatusFilter(e.target.value)}
        >
          <option value="all">Todos los estados</option>
          <option value="active">Activos</option>
          <option value="inactive">Inactivos</option>
        </Select>
      </div>

      {/* --- CONTENEDOR PRINCIPAL --- */}
      <Card>
        <div className="flex justify-end mb-6">
          <Button 
            className="px-5 h-10 rounded-[0.5rem] shadow-[0_1px_2px_rgba(0,0,0,0.05)] hover:-translate-y-[1px]"
            onClick={() => setEditing({ nif: "", name: "", surname: "", email: "", phone: "", postalCode: "", active: true })}
          >
            Nuevo operario
          </Button>
        </div>

        {loading ? (
          <div className="text-center p-10 text-[var(--color-primary)] font-medium">
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
          <ConfirmModal
            title="Eliminar operario"
            message={<>¿Seguro que deseas eliminar a <strong>{deleting.name} {deleting.surname}</strong>?</>}
            confirmText="Eliminar"
            confirmVariant="danger"
            onConfirm={handleDelete}
            onCancel={() => setDeleting(null)}
          />
        )}
      </Modal>

      <Modal isOpen={Boolean(activating)}>
        {activating && (
          <ConfirmModal
            title="Activar operario"
            message={<>¿Deseas activar a <strong>{activating.name} {activating.surname}</strong>?</>}
            confirmText="Activar"
            confirmVariant="success"
            onConfirm={handleActivateConfirm}
            onCancel={() => setActivating(null)}
          />
        )}
      </Modal>
    </Section>
  );
}