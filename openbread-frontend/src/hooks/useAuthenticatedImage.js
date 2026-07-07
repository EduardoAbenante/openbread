import { useEffect, useState } from 'react';
import api from '../api/axiosConfig';

export default function useAuthenticatedImage(src) {
  const [imageUrl, setImageUrl] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    let objectUrl = null;
    let isActive = true;

    const resetState = () => {
      if (!isActive) return;
      setImageUrl(null);
      setError(null);
      setLoading(false);
    };

    if (!src) {
      resetState();
      return;
    }

    if (!src.startsWith('/api')) {
      if (isActive) {
        setImageUrl(src);
        setError(null);
        setLoading(false);
      }
      return;
    }

    const loadImage = async () => {
      if (!isActive) return;
      setLoading(true);
      setError(null);

      try {
        const response = await api.get(src, { responseType: 'blob' });
        objectUrl = URL.createObjectURL(response.data);

        if (isActive) {
          setImageUrl(objectUrl);
        }
      } catch (err) {
        if (isActive) {
          setError(err);
          setImageUrl(null);
        }
      } finally {
        if (isActive) {
          setLoading(false);
        }
      }
    };

    loadImage();

    return () => {
      isActive = false;
      if (objectUrl) {
        URL.revokeObjectURL(objectUrl);
      }
    };
  }, [src]);

  return { imageUrl, loading, error };
}
