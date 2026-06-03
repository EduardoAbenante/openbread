export default function OperariosDeleteModal({ operario, onConfirm, onCancel }) {
  return (
    <div className="ob-modal-delete">
      <div className="ob-modal-header">
        <h2>Eliminar operario</h2>
      </div>
      <p style={{ margin: "1rem 0 2rem 0", color: "#475569", fontSize: "0.9rem" }}>
        ¿Seguro que deseas eliminar a <strong>{operario?.name} {operario?.surname}</strong>?
      </p>

      <div className="ob-modal-actions" style={{ justifyContent: "center" }}>
        <button className="danger" onClick={onConfirm}>Eliminar</button>
        <button onClick={onCancel}>Cancelar</button>
      </div>
    </div>
  );
}