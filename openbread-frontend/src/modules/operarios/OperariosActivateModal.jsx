export default function OperariosActivateModal({ operario, onConfirm, onCancel }) {
  return (
    <div className="ob-modal-alert">
      <div className="ob-modal-header">
        <h2>Activar operario</h2>
      </div>
      <p style={{ margin: "1rem 0 2rem 0", color: "#475569", fontSize: "0.9rem" }}>
        ¿Deseas activar a <strong>{operario?.name} {operario?.surname}</strong>?
      </p>

      <div className="ob-modal-actions" style={{ justifyContent: "center" }}>
        <button className="activate-button" onClick={onConfirm}>Activar</button>
        <button onClick={onCancel}>Cancelar</button>
      </div>
    </div>
  );
}