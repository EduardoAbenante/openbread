export default function Badge({ 
  children, 
  variant = 'info', 
  className = '' 
}) {
  const baseStyles = "inline-flex items-center px-[0.65rem] py-[0.3rem] rounded-[0.375rem] text-[0.75rem] font-bold";
  
  const variants = {
    warning: "bg-[#fef3c7] text-[#92400e]",
    info: "bg-[#e0f2fe] text-[#075985]",
    success: "bg-[#e6f4ea] text-[#137333]",
    gray: "bg-[#f1f3f4] text-[#5f6368]",
  };

  const variantStyles = variants[variant] || variants.info;

  return (
    <span className={`${baseStyles} ${variantStyles} ${className}`}>
      {children}
    </span>
  );
}
