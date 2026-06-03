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

  // Procesamiento combinado: Filtrado + Ordenación (Memoizado)
  const processedData = useMemo(() => {
    let result = [...initialData];

    // 1. Filtrado por Estado (Activo / Inactivo)
    if (statusFilter !== "all") {
      const targetStatus = statusFilter === "active";
      result = result.filter(item => {
        // Soporta tanto 'activo' como 'active' según mapee tu backend
        const itemStatus = item.activo !== undefined ? item.activo : item.active;
        return itemStatus === targetStatus;
      });
    }

    // 2. Filtro "Smart Search" (Busca el término en cualquiera de los campos configurados)
    if (searchTerm.trim() !== "") {
      const target = searchTerm.toLowerCase().trim();
      result = result.filter(item => 
        searchFields.some(field => {
          const value = item[field];
          return value !== undefined && value !== null && String(value).toLowerCase().includes(target);
        })
      );
    }

    // 3. Ordenación Avanzada (C.P., Textos, Números)
    if (sortConfig.key !== null) {
      result.sort((a, b) => {
        let valA = a[sortConfig.key];
        let valB = b[sortConfig.key];

        // Normalización si son textos para evitar problemas con mayúsculas/tildes
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
  }, [initialData, searchTerm, statusFilter, sortConfig]);

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