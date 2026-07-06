import { useState, useEffect } from 'react';
import api from '../api/axiosConfig';

export default function useAuthenticatedImage(src) {
  const [imageUrl, setImageUrl] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    console.log('[useAuthenticatedImage] Hook ejecutado. src=', src);
    
    if (!src) {
      console.log('[useAuthenticatedImage] src es nulo/vacío, limpiando');
      setImageUrl(null);
      return;
    }

    // Si la imagen es una URL externa (no empieza por /api), no necesitamos autenticación
    if (!src.startsWith('/api')) {
      console.log('[useAuthenticatedImage] URL externa, usándola directamente:', src);
      setImageUrl(src);
      return;
    }

    let objectUrl = null;
    const loadImage = async () => {
      setLoading(true);
      try {
        console.log('[useAuthenticatedImage] Iniciando GET a:', src);
        const token = localStorage.getItem('token');
        console.log('[useAuthenticatedImage] Token en localStorage:', token ? 'presente' : 'FALTA');
        
        const response = await api.get(src, {
          responseType: 'blob'
        });
        
        console.log('[useAuthenticatedImage] Respuesta recibida:', response.status, response.statusText);
        console.log('[useAuthenticatedImage] Tipo MIME:', response.headers['content-type']);
        
        objectUrl = URL.createObjectURL(response.data);
        console.log('[useAuthenticatedImage] Blob URL creada:', objectUrl);
        setImageUrl(objectUrl);
        setError(null);
      } catch (err) {
        console.error("[useAuthenticatedImage] ❌ Error completo:", {
          status: err.response?.status,
          statusText: err.response?.statusText,
          message: err.message,
          headers: err.response?.headers,
          data: err.response?.data
        });
        setError(err);
        setImageUrl(null);
      } finally {
        setLoading(false);
      }
    };

    loadImage();

    return () => {
      if (objectUrl) {
        console.log('[useAuthenticatedImage] Limpiando Blob URL:', objectUrl);
        URL.revokeObjectURL(objectUrl);
      }
    };
  }, [src]);

  return { imageUrl, loading, error };
}
