export default function Grid({ columns = 3, children }) {
    return (
        <div className="grid gap-6 mt-4" style={{ gridTemplateColumns: `repeat(${columns}, 1fr)`}} >
            {children}
        </div>
    )
}