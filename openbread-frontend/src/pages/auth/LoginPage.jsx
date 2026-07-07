import { useState } from "react";
import { login } from "../../api/authApi";
import { useAuthStore } from "../../store/authStore";
import { useNavigate } from "react-router-dom";
import { useAsyncState } from "../../hooks/useAsyncState";
import "./login.css";

export default function LoginPage() {
  const setToken = useAuthStore((state) => state.setToken);
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const { error, setError, loading, run } = useAsyncState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = await run(() => login(email, password));
      setToken(token);
      navigate("/app/dashboard");
    } catch {
      setError("Credenciales incorrectas");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h1>OpenBread ERP</h1>
        <form onSubmit={handleSubmit}>
          <input
            type="email"
            placeholder="Correo electrónico"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Contraseña"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button type="submit" disabled={loading}>{loading ? "Entrando..." : "Iniciar sesión"}</button>
        </form>
        {error && <p className="error">{error}</p>}
        <a href="/forgot-password" className="forgot-link">
          ¿Olvidaste tu contraseña?
        </a>
      </div>
    </div>
  );
}
