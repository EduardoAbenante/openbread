import { useCallback, useMemo, useState } from 'react';

export function useAsyncState(initialValue = null) {
  const [data, setData] = useState(initialValue);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const run = useCallback(async (promiseFactory, options = {}) => {
    const { onSuccess, onError, resetData = false } = options;

    setLoading(true);
    setError(null);

    if (resetData) {
      setData(initialValue);
    }

    try {
      const result = await promiseFactory();
      setData(result);
      onSuccess?.(result);
      return result;
    } catch (err) {
      const normalizedError = err?.response?.data?.message || err?.message || 'Ha ocurrido un error';
      setError(normalizedError);
      onError?.(err);
      throw err;
    } finally {
      setLoading(false);
    }
  }, [initialValue]);

  return useMemo(() => ({ data, setData, loading, error, setError, run }), [data, loading, error, run, setData]);
}
