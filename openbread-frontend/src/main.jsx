import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import App from "./App.jsx";
import Login from "./pages/Login.jsx";
import PrivateRoute from "./components/PrivateRoute.jsx";
import "./theme/easytraza.css";


ReactDOM.createRoot(document.getElementById("root")).render(
    <BrowserRouter>
        <Routes>
            <Route path="/login" element={<Login />} />

            <Route
                path="/"
                element={
                    <PrivateRoute>
                        <App />
                    </PrivateRoute>
                }
            />
        </Routes>
    </BrowserRouter>
);
