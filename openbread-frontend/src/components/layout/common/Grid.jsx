export default function Grid({ columns = 3, children }) {
    return (
        <div className="ob-grid" style={{ gridTemplateColumns: `repeat(${columns}, 1fr)`}} >
            {children}
        </div>
    )
}