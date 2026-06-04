export default function Section({ title, children}) {
    return (
        <section className="mb-8">
            <h2 className="text-[var(--color-primary)] mb-4 uppercase tracking-[0.02em] font-semibold text-[1.25rem]">{title}</h2>
            {children}
        </section>
    )
}