import { useEffect, useState } from "react";
import Section from "../../components/common/Section";
import Card from "../../components/common/Card";
import OperariosTable from "../../modules/operarios/OperariosTable";
import OperariosForm from "../../modules/operarios/OperariosForm";
import OperariosDeleteModal from "../../modules/operarios/OperariosDeleteModal";
import OperariosActivateModal from "../../modules/operarios/OperariosActivateModal";
import {
  getOperarios,
  createOperario,
  updateOperario,
  activateOperario,
  deleteOperario
} from "../../modules/operarios/OperariosApi";

export default function OperariosPage() {
  const [operarios, setOperarios] = useState([]);
  const [editing, setEditing] = useState(null);
  const [deleting, setDeleting] = useState(null);
  const [activating, setActivating] = useState(null);

  const load = () => getOperarios().then(setOperarios);

  useEffect(() => {
    load();
  }, []);

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
      <Card>
        <div className="ob-toolbar">
          <button onClick={() => setEditing({ nif: "", name: "", surname: "", phone: "", active: true })}>
            Nuevo operario
          </button>
        </div>

        <OperariosTable
          operarios={operarios}
          onEdit={setEditing}
          onDelete={setDeleting}
          onActivate={handleActivateClick}
        />
      </Card>

      {editing && (
        <OperariosForm
          initial={editing}
          onSubmit={handleSave}
          onCancel={() => setEditing(null)}
        />
      )}

      {deleting && (
        <OperariosDeleteModal
          operario={deleting}
          onConfirm={handleDelete}
          onCancel={() => setDeleting(null)}
        />
      )}

      {activating && (
        <OperariosActivateModal
          operario={activating}
          onConfirm={handleActivateConfirm}
          onCancel={() => setActivating(null)}
        />
      )}
    </Section>
  );
}
