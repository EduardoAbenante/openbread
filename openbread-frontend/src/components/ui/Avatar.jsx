import { BACKEND_URL } from '../../api/axiosConfig';

export default function Avatar({ src, alt = "Usuario", size = "md", className = "" }) {
  const imageUrl = src 
    ? `${BACKEND_URL}${src}` 
    : "https://cdn-icons-png.flaticon.com/512/149/149071.png"; // Puedes cambiarlo por un SVG local

  // 2. Gestionamos dinámicamente los tamaños usando Tailwind
  const sizeClasses = {
    sm: "w-8 h-8 text-xs",
    md: "w-10 h-10 text-sm",
    lg: "w-16 h-16 text-base",
    xl: "w-24 h-24 text-lg"
  };

  return (
    <div className={`relative inline-flex items-center justify-center overflow-hidden bg-gray-100 rounded-full border border-[var(--color-border)] ${sizeClasses[size]} ${className}`}>
      <img 
        src={imageUrl} 
        alt={alt} 
        className="w-full h-full object-cover"
        onError={(e) => {
          e.target.src = "https://cdn-icons-png.flaticon.com/512/149/149071.png";
        }}
      />
    </div>
  );
}