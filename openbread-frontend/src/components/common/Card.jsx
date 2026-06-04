export default function Card({ title, children}) {
    return (
        <div className="bg-[var(--color-surface)] p-6 rounded-[0.8rem] shadow-[0_2px_8px_rgba(0,0,0,0.1)]">
            {title && <h3 className="mb-4 text-[var(--color-primary)] text-[1.2rem] font-semibold">{title}</h3>}
            <div> {children} </div>
        </div>
    )
}