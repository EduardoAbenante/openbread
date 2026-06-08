import { forwardRef, useState } from "react";

const Input = forwardRef(({ label, error, helperText, className = "", onFocus, onBlur, ...props }, ref) => {
  const [isFocused, setIsFocused] = useState(false);
  const labelClass = "block font-medium text-[var(--color-text)] opacity-70 text-[0.815rem] mb-1.5";
  const inputClass = "w-full h-[2.375rem] px-3.5 rounded-[0.375rem] border border-[var(--color-border)] bg-white text-[0.875rem] text-[var(--color-text)] outline-none transition-all duration-150 ease-in-out focus:border-[var(--color-primary)] focus:ring-[3px] focus:ring-[rgba(123,75,42,0.15)] disabled:bg-[#f8fafc] disabled:text-[#94a3b8] disabled:border-[var(--color-border)] disabled:cursor-not-allowed appearance-none";

  const handleFocus = (e) => {
    setIsFocused(true);
    if (onFocus) onFocus(e);
  };

  const handleBlur = (e) => {
    setIsFocused(false);
    if (onBlur) onBlur(e);
  };

  return (
    <div className={`w-full min-w-0 ${className}`}>
      {label && (
        <label htmlFor={props.id} className={labelClass}>
          {label}
        </label>
      )}
      <input 
        ref={ref} 
        className={inputClass} 
        onFocus={handleFocus}
        onBlur={handleBlur}
        {...props} 
      />
      <div className="min-h-[1.25rem] mt-1 w-full text-left">
        {error ? (
          <p className="text-[#b00020] text-[0.75rem] font-medium leading-tight select-none animate-ob-slide-fade-down">
            {error}
          </p>
        ) : (helperText && isFocused) ? (
          <p className="text-[var(--color-text)] opacity-50 text-[0.75rem] leading-tight break-words whitespace-normal animate-ob-slide-fade-down">
            {helperText}
          </p>
        ) : null}
      </div>
    </div>
  );
});

Input.displayName = "Input";

export default Input;