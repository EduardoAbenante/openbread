export default function Button({ 
  children, 
  variant = 'primary', 
  className = '', 
  ...props 
}) {
  const baseStyles = "h-[2.375rem] px-6 rounded-[0.375rem] font-medium text-[0.875rem] inline-flex items-center justify-center cursor-pointer transition-all duration-150 ease-in-out border";
  
  const variants = {
    primary: "bg-[var(--color-primary)] text-white border-transparent hover:bg-[var(--color-primary-dark)]",
    secondary: "bg-white text-[var(--color-text)] border-[var(--color-border)] hover:bg-[#f8fafc]",
    danger: "bg-[#fce8e6] text-[#c5221f] border-[#fad2cf] hover:bg-[#c5221f] hover:text-white hover:border-[#c5221f]",
    dangerGhost: "bg-[rgba(176,0,32,0.05)] text-[#b00020] border border-[rgba(176,0,32,0.15)] hover:bg-[#b00020] hover:text-white",
    success: "bg-[#3b6139] text-white border-transparent hover:bg-[#2d4a2b]",
    outline: "bg-white text-[var(--color-primary)] border-[rgba(123,75,42,0.35)] hover:bg-[rgba(123,75,42,0.08)] hover:border-[var(--color-primary)]",
  };

  const variantStyles = variants[variant] || variants.primary;

  return (
    <button 
      className={`${baseStyles} ${variantStyles} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
}
