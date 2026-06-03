export default function OperariosDeleteModal({ operario, onConfirm, onCancel }) {
  return (
    <div className="ob-modal">
      <div className="ob-modal-content">
        <div className="ob-modal-header">
          <h2>Eliminar operario</h2>
        </div>
        <p className="ob-delete-text">¿Seguro que deseas eliminar a {operario.name} {operario.surname}?</p>

        <div className="ob-modal-actions">
          <button className="danger" onClick={onConfirm}>Eliminar</button>
          <button onClick={onCancel}>Cancelar</button>
        </div>
      </div>
    </div>
  );
}
