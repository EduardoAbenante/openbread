import { useState } from 'react'
import './App.css'

function App() {
  const [id, setId] = useState(null);

  const createUser = async () => {
    const user = {
      nif: "12345678A",
      name: "Eduardo",
      surname: "Abenante",
      email: "edu@example.com",
      password: "123456",
      phone: "666777888",
      postalCode: "08225",
      role: "ADMIN"
    };

    const response = await fetch("http://localhost:8080/user", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(user)
    });

    const newId = await response.json();
    setId(newId);
  };

  return (
      <div>
        <h1>OpenBread Frontend</h1>
        <button onClick={createUser}>Crear usuario de prueba</button>
        {id && <p>Usuario creado con ID: {id}</p>}
      </div>
  );
}

export default App;

