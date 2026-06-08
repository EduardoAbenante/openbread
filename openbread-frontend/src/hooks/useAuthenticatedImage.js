import { useState, useEffect } from 'react';
import api from '../api/axiosConfig';

export default function useAuthenticatedImage(src) {
  const [imageUrl, setImageUrl] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!src) {
      setImageUrl(null);
      return;
    }

    // Si la imagen es una URL externa (no empieza por /api/media), no necesitamos autenticación
    if (!src.startsWith('/api/media')) {
      setImageUrl(src);
      return;
    }

    let objectUrl = null;
    const loadImage = async () => {
      setLoading(true);
      try {
        const response = await api.get(src, {
          responseType: 'blob'
        });
        objectUrl = URL.createObjectURL(response.data);
        setImageUrl(objectUrl);
        setError(null);
      } catch (err) {
        console.error("Error loading authenticated image:", err);
        setError(err);
        setImageUrl(null);
      } finally {
        setLoading(false);
      }
    };

    loadImage();

    return () => {
      if (objectUrl) {
        URL.revokeObjectURL(objectUrl);
      }
    };
  }, [src]);

  return { imageUrl, loading, error };
}
