export default function OperariosActivateModal({ operario, onConfirm, onCancel }) {
  return (
    <div className="ob-modal">
      <div className="ob-modal-content">
        <div className="ob-modal-header">
          <h2>Activar operario</h2>
        </div>
        <p className="ob-delete-text">
          ¿Deseas activar a {operario.name} {operario.surname}?
        </p>

        <div className="ob-modal-actions">
          <button className="activate-button" onClick={onConfirm}>Activar</button>
          <button onClick={onCancel}>Cancelar</button>
        </div>
      </div>
    </div>
  );
}
