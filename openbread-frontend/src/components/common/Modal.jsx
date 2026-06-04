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
  const fadeInClass = "animate-[obFadeIn_0.2s_cubic-bezier(0.16,1,0.3,1)_forwards]";
  const fadeOutClass = "animate-[obFadeOut_0.2s_cubic-bezier(0.16,1,0.3,1)_forwards]";

  return (
    <div className={`${modalOverlayClass} ${isClosing ? fadeOutClass : fadeInClass}`}>
      <div className={isClosing ? "animate-[obSlideDown_0.2s_cubic-bezier(0.16,1,0.3,1)_forwards]" : "animate-[obSlideUp_0.3s_cubic-bezier(0.34,1.56,0.64,1)_forwards]"}>
        {children}
      </div>
    </div>
  );
}