export default function Input({ label, error, className = "", ...props }) {
  const labelClass = "block font-medium text-[var(--color-text)] opacity-70 text-[0.815rem] mb-1.5";
  const inputClass = "w-full h-[2.375rem] px-3.5 rounded-[0.375rem] border border-[var(--color-border)] bg-white text-[0.875rem] text-[var(--color-text)] outline-none transition-all duration-150 ease-in-out focus:border-[var(--color-primary)] focus:ring-[3px] focus:ring-[rgba(123,75,42,0.15)] disabled:bg-[#f8fafc] disabled:text-[#94a3b8] disabled:border-[var(--color-border)] disabled:cursor-not-allowed appearance-none";

  return (
    <div className={className}>
      {label && (
        <label htmlFor={props.id} className={labelClass}>
          {label}
        </label>
      )}
      <input className={inputClass} {...props} />
      {error && <p className="text-[#b00020] text-[0.75rem] mt-1">{error}</p>}
    </div>
  );
}
