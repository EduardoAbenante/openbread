import { useState } from "react";
import { apiFetch } from "../services/api";
import "../theme/easytraza.css";
import "./login-modern.css";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);

    const handleLogin = async (e) => {
        e.preventDefault();
        setError("");

        if (!email || !password) {
            setError("Please fill in all fields");
            return;
        }

        setLoading(true);

        const response = await apiFetch("http://localhost:8080/auth/login", {
            method: "POST",
            body: JSON.stringify({ email, password })
        });

        setLoading(false);

        if (!response.ok) {
            setError("Invalid credentials");
            return;
        }

        const data = await response.json();
        localStorage.setItem("token", data.token);
        window.location.href = "/";
    };

    return (
        <div className="page">
            <div className="pattern" />

            <div className="card">

                <div className="header">
                    <h1 className="title">OpenBread</h1>
                    <p className="subtitle">Sign in to your dashboard</p>
                </div>

                <div className="header-divider" />

                {error && (
                    <div className="error visible">
                        <i className="bi bi-exclamation-circle" />
                        <span>{error}</span>
                    </div>
                )}

                <form onSubmit={handleLogin} noValidate>

                    <div className="field-group">
                        <label htmlFor="email">
                            <i className="bi bi-envelope" />
                            Email address
                        </label>
                        <input
                            id="email"
                            type="email"
                            placeholder="you@company.com"
                            autoComplete="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </div>

                    <div className="field-group">
                        <label htmlFor="password">
                            <i className="bi bi-lock" />
                            Password
                        </label>
                        <div className="input-wrap">
                            <input
                                id="password"
                                type={showPassword ? "text" : "password"}
                                placeholder="••••••••"
                                autoComplete="current-password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                            <button
                                type="button"
                                className="toggle-eye"
                                aria-label={showPassword ? "Hide password" : "Show password"}
                                onClick={() => setShowPassword((v) => !v)}
                            >
                                <i className={showPassword ? "bi bi-eye-slash" : "bi bi-eye"} />
                            </button>
                        </div>
                    </div>

                    <div className="forgot-row">
                        <a className="forgot" tabIndex={0}>Forgot password?</a>
                    </div>

                    <button className="btn-enter" type="submit" disabled={loading}>
                        {loading ? (
                            <>
                                <i className="bi bi-arrow-clockwise spin" />
                                Signing in…
                            </>
                        ) : (
                            <>
                                <i className="bi bi-arrow-right" />
                                Sign in
                            </>
                        )}
                    </button>

                </form>

                <div className="footer">
                    <span className="brand">OPENBREAD</span>
                    <span className="version">v0.0.1</span>
                </div>

            </div>
        </div>
    );
}