import Button from "./Button";

export default function ConfirmModal({ 
  title, 
  message, 
  onConfirm, 
  onCancel, 
  confirmText = "Confirmar", 
  cancelText = "Cancelar",
  confirmVariant = "primary"
}) {
  return (
    <div className="bg-[var(--color-surface)] p-8 rounded-[0.875rem] w-full max-w-[540px] text-center shadow-[0_20px_25px_-5px_rgba(40,25,15,0.15)] box-border">
      <div className="mb-6">
        <h2 className="text-[1.25rem] font-semibold text-[var(--color-primary)] m-0 tracking-[0.02em]">
          {title}
        </h2>
      </div>
      <p className="my-4 mb-10 text-[var(--color-text)] text-[0.9rem] opacity-80">
        {message}
      </p>

      <div className="flex justify-center gap-3 mt-8">
        <Button variant={confirmVariant} onClick={onConfirm}>
          {confirmText}
        </Button>
        <Button variant="secondary" onClick={onCancel}>
          {cancelText}
        </Button>
      </div>
    </div>
  );
}
