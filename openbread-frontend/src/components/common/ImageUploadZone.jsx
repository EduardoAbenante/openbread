import { useMemo, useState } from 'react';

export const ImageUploadZone = ({
  label = "Foto de perfil",
  selectedFile = null,
  existingImageUrl = null,
  onFileChange,
  accept = "image/*",
  maxSizeText = "Máx. 2MB",
  dimensionsText = "JPG, PNG o WEBP"
}) => {
  const [isDragging, setIsDragging] = useState(false);

  const previewUrl = useMemo(() => {
    if (selectedFile) return URL.createObjectURL(selectedFile);
    if (existingImageUrl) return existingImageUrl;
    return null;
  }, [selectedFile, existingImageUrl]);

  // Manejadores de arrastre
  const handleDragOver = (e) => {
    e.preventDefault();
    setIsDragging(true);
  };

  const handleDragLeave = () => {
    setIsDragging(false);
  };

  const handleDrop = (e) => {
    e.preventDefault();
    setIsDragging(false);
    
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      const file = e.dataTransfer.files[0];
      // Validar que sea una imagen antes de pasarla
      if (file.type.startsWith('image/')) {
        onFileChange(file);
      }
    }
  };

  const handleFileChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      onFileChange(e.target.files[0]);
    }
  };

  return (
    <div className="flex flex-col gap-3 flex-1">
      <span className="font-medium text-[var(--color-text)] opacity-70 text-[0.815rem]">{label}</span>
      
      <label 
        className={`flex flex-col items-center justify-center flex-1 min-h-[220px] border-2 border-dashed rounded-[0.75rem] cursor-pointer p-6 text-center transition-all duration-200 relative overflow-hidden box-border ${isDragging ? 'bg-[rgba(123,75,42,0.08)] border-[var(--color-primary)] scale-[0.99]' : 'border-[rgba(123,75,42,0.18)] bg-[rgba(123,75,42,0.02)] hover:bg-[rgba(123,75,42,0.05)] hover:border-[var(--color-primary)]'}`}
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
        onDrop={handleDrop}
      >
        <input 
          type="file" 
          accept={accept} 
          onChange={handleFileChange}
          className="hidden"
        />
        
        {previewUrl ? (
          <div className="w-full h-full absolute inset-0">
            <img src={previewUrl} className="w-full h-full object-cover transition-transform duration-200 hover:scale-[1.02]" alt={label} />
            <div className="absolute inset-0 bg-[rgba(40,25,15,0.6)] flex items-center justify-center text-white text-[0.875rem] font-semibold opacity-0 transition-opacity duration-200 hover:opacity-100">
              <span className="text-white">Cambiar imagen</span>
            </div>
          </div>
        ) : (
          <div className="flex flex-col items-center gap-2">
            <div className="w-12 h-12 rounded-full bg-[rgba(123,75,42,0.08)] flex items-center justify-center text-[var(--color-primary)] mb-1">
              <span className="text-[1.25rem] font-semibold">+</span>
            </div>
            <p className="text-[0.875rem] text-[var(--color-text)] opacity-80 m-0">
              <strong className="text-[var(--color-primary)] font-semibold">Haz clic para subir</strong> o arrastra
            </p>
            <span className="text-[0.75rem] text-[var(--color-text)] opacity-50">
              {dimensionsText} ({maxSizeText})
            </span>
          </div>
        )}
      </label>
    </div>
  );
};