export const resolveUserId = (value) => {
  if (typeof value === 'number') return value;
  if (typeof value === 'string' && /^\d+$/.test(value)) return Number(value);
  if (value && typeof value === 'object') {
    return value.id ?? value.userId ?? null;
  }
  return null;
};
