import { apiFetch } from "../services/api";

export default function DeleteUserModal({ user, onClose, onDeleted }) {

    const deleteUser = async () => {
        const response = await apiFetch(`/api/users/${user.id}`, {
            method: "DELETE",
        });

        if (response.ok) {
            onDeleted();
            onClose();
        } else {
            alert("Error eliminant l'usuari");
        }
    };

    return (
        <div className="modal-backdrop">
            <div className="modal">

                {/* HEADER */}
                <div className="modal-header">
                    <h2 className="modal-title">Eliminar usuari</h2>

                    <button className="btn btn-ghost btn-icon" onClick={onClose}>
                        <i className="bi bi-x-lg"></i>
                    </button>
                </div>

                {/* BODY */}
                <div className="modal-body text-center flex-col gap-2">

                    <div className="flex-center" style={{ fontSize: "3rem", color: "#b3261e" }}>
                        <i className="bi bi-person-dash"></i>
                    </div>

                    <p style={{ fontSize: "1.1rem" }}>
                        Segur que vols eliminar l'usuari:
                    </p>

                    <strong style={{ fontSize: "1.15rem" }}>
                        {user.name} {user.surname}
                    </strong>

                    <p className="text-muted">
                        Aquesta acció no es pot desfer.
                    </p>
                </div>

                {/* FOOTER */}
                <div className="modal-footer">
                    <button className="btn btn-secondary" onClick={onClose}>
                        Cancel·lar
                    </button>

                    <button className="btn btn-danger" onClick={deleteUser}>
                        <i className="bi bi-trash"></i> Eliminar
                    </button>
                </div>

            </div>
        </div>
    );
}
