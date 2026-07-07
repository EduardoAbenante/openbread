import Card from "../../components/common/Card";
import { useEffect } from "react";
import { useAsyncState } from "../../hooks/useAsyncState";
import { getOperarios } from "./OperariosApi";

export default function OperariosWidget() {
    const { data: count, setData: setCount, loading, error, run } = useAsyncState(0);

    useEffect(() => {
        run(() => getOperarios({ active: true }).then((data) => Array.isArray(data) ? data.length : 0));
    }, [run]);

    return (
        <Card title="Operarios activos">
            {loading ? <p>Cargando...</p> : error ? <p>{error}</p> : <p>{count} operarios</p>}
        </Card>
    );
}