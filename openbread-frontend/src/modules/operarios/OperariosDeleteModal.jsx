export default function OperariosDeleteModal({ operario, onConfirm, onCancel }) {
  return (
    <div className="ob-modal">
      <div className="ob-modal-content">
        <h2>Eliminar operario</h2>
        <p>¿Seguro que deseas eliminar a {operario.name} {operario.surname}?</p>

        <div className="ob-modal-actions">
          <button className="danger" onClick={onConfirm}>Eliminar</button>
          <button onClick={onCancel}>Cancelar</button>
        </div>
      </div>
    </div>
  );
}
