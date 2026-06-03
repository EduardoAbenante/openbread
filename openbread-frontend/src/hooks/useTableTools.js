import { useState, useMemo } from "react";

export function useTableTools(initialData = [], searchFields = []) {
  // Estado de búsqueda global y filtro de estado
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("all"); // "all", "active", "inactive"

  // Estado de ordenación
  const [sortConfig, setSortConfig] = useState({ key: null, direction: "asc" });

  // Manejador del clic en las cabeceras
  const handleSort = (key) => {
    let direction = "asc";
    if (sortConfig.key === key && sortConfig.direction === "asc") {
      direction = "desc";
    }
    setSortConfig({ key, direction });
  };

  // Procesamiento combinado: Filtrado Opcional + Ordenación (Memoizado)
  const processedData = useMemo(() => {
    let result = [...initialData];

    // 1. FILTRADO POR ESTADO (Solo actúa si no se está filtrando ya en Backend)
    // Como para Operarios lo filtramos en Kotlin, esto se vuelve inocuo o de apoyo.
    if (statusFilter !== "all" && (!searchFields || searchFields.length === 0)) {
      const targetStatus = statusFilter === "active";
      result = result.filter(item => {
        const itemStatus = item.active !== undefined ? item.active : item.activo;
        return itemStatus === targetStatus;
      });
    }

    // 2. FILTRO "SMART SEARCH" EN CLIENTE (¡EL ARREGLO CRUCIAL!)
    // Añadimos la condición 'searchFields.length > 0'. 
    // Si el array viene vacío (porque busca el backend), salta este bloque sin destruir los datos.
    if (searchTerm.trim() !== "" && searchFields && searchFields.length > 0) {
      const target = searchTerm.toLowerCase().trim();
      result = result.filter(item => 
        searchFields.some(field => {
          const value = item[field];
          return value !== undefined && value !== null && String(value).toLowerCase().includes(target);
        })
      );
    }

    // 3. ORDENACIÓN AVANZADA LOCAL
    // Esto se mantiene intacto y óptimo: ordena cualquier columna cliqueada al instante
    if (sortConfig.key !== null) {
      result.sort((a, b) => {
        let valA = a[sortConfig.key];
        let valB = b[sortConfig.key];

        // Normalización para strings (evita fallos con mayúsculas/minúsculas)
        if (typeof valA === "string") valA = valA.toLowerCase();
        if (typeof valB === "string") valB = valB.toLowerCase();

        if (valA === undefined || valA === null) return 1;
        if (valB === undefined || valB === null) return -1;

        if (valA < valB) return sortConfig.direction === "asc" ? -1 : 1;
        if (valA > valB) return sortConfig.direction === "asc" ? 1 : -1;
        return 0;
      });
    }

    return result;
  }, [initialData, searchTerm, statusFilter, sortConfig, searchFields]);

  return {
    searchTerm,
    setSearchTerm,
    statusFilter,
    setStatusFilter,
    sortConfig,
    handleSort,
    processedData
  };
}