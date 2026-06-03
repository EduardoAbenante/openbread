import Card from "../../components/common/Card";
import { useEffect, useState } from "react";
import { getOperarios } from "./OperariosApi";

export default function OperariosWidget() {
    const [count, setCount] = useState(0);

    useEffect(() => {
        getOperarios().then((data) => setCount(data.length));
    }, []);

    return (
        <Card title="Operarios activos">
            <p>{count} operarios</p>
        </Card>
    );  
}