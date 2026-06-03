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
    <div className="photo-upload-card">
      <span className="photo-upload-label">{label}</span>
      
      <label 
        className={`photo-dropzone ${isDragging ? 'dragging' : ''}`}
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
        onDrop={handleDrop}
      >
        <input 
          type="file" 
          accept={accept} 
          onChange={handleFileChange}
          className="photo-input-hidden"
        />
        
        {previewUrl ? (
          <div className="photo-preview-container">
            <img src={previewUrl} className="photo-preview" alt={label} />
            <div className="photo-preview-overlay">
              <span>Cambiar imagen</span>
            </div>
          </div>
        ) : (
          <div className="photo-placeholder-content">
            <div className="photo-icon-avatar">
              <span className="icon-plus">+</span>
            </div>
            <p className="photo-upload-text">
              <strong>Haz clic para subir</strong> o arrastra
            </p>
            <span className="photo-upload-subtext">
              {dimensionsText} ({maxSizeText})
            </span>
          </div>
        )}
      </label>
    </div>
  );
};