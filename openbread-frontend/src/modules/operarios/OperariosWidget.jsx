import Card from "../../components/common/Card";
import { useEffect, useState } from "react";
import { getOperarios } from "./OperariosApi";

export default function OperariosWidget() {
    const [count, setCount] = useState(0);

    useEffect(() => {
        getOperarios({ active: true }).then((data) => setCount(Array.isArray(data) ? data.length : 0));
    }, []);

    return (
        <Card title="Operarios activos">
            <p>{count} operarios</p>
        </Card>
    );  
}