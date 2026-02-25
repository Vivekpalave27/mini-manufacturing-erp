import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

const Sidebar = () => {
  const { role } = useAuth();

  return (
    <div style={{ width: "200px", background: "#1e2a38", color: "white", padding: "20px" }}>
      <h2>Mini ERP</h2>

      <ul style={{ listStyle: "none", padding: 0 }}>

        {/* Dashboard - All roles */}
        <li>
          <Link to="/dashboard" style={{ color: "white" }}>
            Dashboard
          </Link>
        </li>

        {/* ADMIN ONLY */}
        {role === "ADMIN" && (
          <li>
            <Link to="/users" style={{ color: "white" }}>
              Manage Users
            </Link>
          </li>
        )}

        {/* STAFF + ADMIN */}
        {(role === "ADMIN" || role === "STAFF") && (
          <li>
            <Link to="/items" style={{ color: "white" }}>
              Manage Items
            </Link>
          </li>
        )}

      </ul>
    </div>
  );
};

export default Sidebar;