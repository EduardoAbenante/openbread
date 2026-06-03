import { NavLink } from "react-router-dom";

export default function Sidebar() {
  return (
    <aside className="sidebar">
      <h2>OpenBread</h2>
      <nav>
        <NavLink to="/app/dashboard">Dashboard</NavLink>
      </nav>
    </aside>
  );
}
