export default function Section({ title, children}) {
    return (
        <section className="ob-section">
            <h2>{title}</h2>
            {children}
        </section>
    )
}