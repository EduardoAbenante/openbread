import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { userCreateSchema } from "./OperarioValidator"; 
import { ImageUploadZone } from '../../components/common/ImageUploadZone';

export default function OperariosForm({ initial, onSubmit, onCancel }) {
  const [serverError, setServerError] = useState("");

  // 1. CONFIGURACIÓN DE REACT HOOK FORM
  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors, isSubmitting }
  } = useForm({
    resolver: zodResolver(userCreateSchema), 
    defaultValues: {
      id: initial?.id || null,
      nif: initial?.nif || "",
      role: initial?.role === "USER",
      name: initial?.name || "",
      surname: initial?.surname || "",
      email: initial?.email || "",
      password: initial?.id ? "Dummy1!" : "", 
      phone: initial?.phone || "",
      postalCode: initial?.postalCode || "",
      photoFile: null,
      avatarUrl: initial?.avatarUrl || null
    }
  });

  const photoFileWatch = watch("photoFile");
  const avatarUrlWatch = watch("avatarUrl");

  const onFormSubmit = async (data) => {
    setServerError("");
    try {
      if (initial?.id) {
        delete data.password;
      }
      await onSubmit(data);
    } catch (err) {
      const serverMessage = err.response?.data?.message || err.response?.data?.error;
      const validationErrors = err.response?.data?.errors 
        ? Object.entries(err.response.data.errors).map(([k, v]) => `${k}: ${v}`).join(", ") 
        : null;
      setServerError(serverMessage || validationErrors || "Error inesperado");
    }
  };

  return (
    <div className="ob-modal-form-container">
      <div className="ob-modal-header">
        <h2>{initial.id ? "Editar usuario" : "Nuevo usuario"}</h2>
        {serverError && <p className="ob-error">{serverError}</p>}
      </div>

      <form className="ob-modal-form" onSubmit={handleSubmit(onFormSubmit)}>
        <div className="ob-form-row">
          
          {/* COLUMNA IZQUIERDA: CAMPOS DE TEXTO */}
          <div className="ob-form-left">
            
            <div className="half-field">
              <label htmlFor="op-nif">NIF *</label>
              <input 
                id="op-nif" 
                className={errors.nif ? "input-error" : ""}
                disabled={!!initial.id} 
                {...register("nif")}
              />
              {errors.nif && <span className="error-message">{errors.nif.message}</span>}
            </div>

            <div className="half-field">
              <label htmlFor="op-role">Rol *</label>
              <select id="op-role" {...register("role")}>
                <option value="USER">Usuario estándar</option>
                <option value="ADMIN">Administrador</option>
              </select>
              {errors.role && <span className="error-message">{errors.role.message}</span>}
            </div>

            <div className="half-field">
              <label htmlFor="op-name">Nombre *</label>
              <input 
                id="op-name" 
                className={errors.name ? "input-error" : ""}
                {...register("name")} 
              />
              {errors.name && <span className="error-message">{errors.name.message}</span>}
            </div>

            <div className="half-field">
              <label htmlFor="op-surname">Apellidos *</label>
              <input 
                id="op-surname" 
                className={errors.surname ? "input-error" : ""}
                {...register("surname")} 
              />
              {errors.surname && <span className="error-message">{errors.surname.message}</span>}
            </div>

            {/* El email hereda de forma nativa el span 2 del CSS global de OpenBread */}
            <div className="full-width-field-group">
              <label htmlFor="op-email">Correo electrónico *</label>
              <input 
                id="op-email" 
                type="email" 
                className={errors.email ? "input-error" : ""}
                disabled={!!initial.id} 
                {...register("email")}
              />
              {errors.email && <span className="error-message">{errors.email.message}</span>}
            </div>

            {/* 🔥 CONTRASEÑA: Ahora va suelta y ocupa todo el ancho, igual que el Email */}
            {!initial.id && (
              <div className="full-width-field-group">
                <label htmlFor="op-pass">Contraseña *</label>
                <input 
                  id="op-pass" 
                  type="password" 
                  className={errors.password ? "input-error" : ""}
                  {...register("password")}
                />
                {/* El error se renderiza aquí mismo, abarcando de forma natural todo el ancho */}
                {errors.password && <span className="error-message">{errors.password.message}</span>}
              </div>
            )}

            {/* 🔥 NUEVO SUB-GRID: Exclusivo para poner Teléfono y Código Postal en paralelo abajo */}
            <div className="ob-bottom-fields-shared-row">
              
              {/* TELÉFONO */}
              <div className="half-field-inner">
                <label htmlFor="op-phone">Teléfono</label>
                <input 
                  id="op-phone" 
                  type="tel" 
                  className={errors.phone ? "input-error" : ""}
                  {...register("phone")}
                />
                {errors.phone && <span className="error-message">{errors.phone.message}</span>}
              </div>

              {/* CÓDIGO POSTAL */}
              <div className="half-field-inner">
                <label htmlFor="op-postal">Código postal</label>
                <input 
                  id="op-postal" 
                  className={errors.postalCode ? "input-error" : ""}
                  {...register("postalCode")}
                />
                {errors.postalCode && <span className="error-message">{errors.postalCode.message}</span>}
              </div>

            </div>
          </div>

          {/* COLUMNA DERECHA: FOTO Y ACCIONES */}
          <div className="ob-form-right">
            <ImageUploadZone 
              label="Foto de perfil"
              selectedFile={photoFileWatch}
              existingImageUrl={avatarUrlWatch} 
              onFileChange={(file) => setValue("photoFile", file)}
            />

            <div className="ob-modal-actions">
              <button type="button" className="danger" onClick={onCancel}>Cancelar</button>
              <button type="submit" disabled={isSubmitting}>
                {isSubmitting ? "Guardando..." : "Guardar operario"}
              </button>
            </div>
          </div>

        </div>
      </form>
    </div>
  );
}