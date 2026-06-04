export default function OperariosActivateModal({ operario, onConfirm, onCancel }) {
  return (
    <div className="bg-[var(--color-surface)] p-8 rounded-[0.875rem] w-full max-w-[540px] text-center shadow-[0_20px_25px_-5px_rgba(40,25,15,0.15)] box-border">
      <div className="mb-6">
        <h2 className="text-[1.25rem] font-semibold text-[var(--color-primary)] m-0 uppercase tracking-[0.02em]">Activar operario</h2>
      </div>
      <p className="my-4 mb-10 text-[var(--color-text)] text-[0.9rem] opacity-80">
        ¿Deseas activar a <strong>{operario?.name} {operario?.surname}</strong>?
      </p>

      <div className="flex justify-center gap-3 mt-8">
        <button className="h-[38px] px-6 rounded-[0.375rem] font-medium text-[0.875rem] cursor-pointer inline-flex items-center justify-center transition-all duration-150 bg-[#3b6139] text-white hover:bg-[#2d4a2b]" onClick={onConfirm}>Activar</button>
        <button className="h-[38px] px-6 rounded-[0.375rem] font-medium text-[0.875rem] cursor-pointer inline-flex items-center justify-center transition-all duration-150 bg-white text-[var(--color-text)] border border-[var(--color-border)] hover:bg-gray-50" onClick={onCancel}>Cancelar</button>
      </div>
    </div>
  );
}