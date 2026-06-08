import { useState, useEffect } from 'react';

export function Modal({ isOpen, children }) {
  const [shouldRender, setShouldRender] = useState(isOpen);
  const [isClosing, setIsClosing] = useState(false);

  useEffect(() => {
    if (isOpen) {
      setShouldRender(true);
      setIsClosing(false);
    } else if (shouldRender) {
      setIsClosing(true);
      const timer = setTimeout(() => {
        setShouldRender(false);
      }, 200); 
      return () => clearTimeout(timer);
    }
  }, [isOpen, shouldRender]);

  if (!shouldRender) return null;

  const modalOverlayClass = "fixed inset-0 bg-[rgba(40,25,15,0.4)] backdrop-blur-[4px] flex justify-center items-center z-[1000] p-4";
  const fadeInClass = "animate-ob-fade-in";
  const fadeOutClass = "animate-ob-fade-out";

  return (
    <div className={`${modalOverlayClass} ${isClosing ? fadeOutClass : fadeInClass}`}>
      <div className={isClosing ? "animate-ob-slide-down" : "animate-ob-slide-up"}>
        {children}
      </div>
    </div>
  );
}