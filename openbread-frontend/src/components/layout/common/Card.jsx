export default function Card({ title, children}) {
    return (
        <div className="ob-card">
            {title && <h3 className="ob-card-title">{title}</h3>}
            <div className="ob-card-content"> {children} </div>
        </div>
    )
}