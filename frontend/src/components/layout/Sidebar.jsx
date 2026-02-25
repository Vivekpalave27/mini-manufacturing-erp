import { Link } from "react-router-dom";

const Sidebar = () => {
  return (
    <div style={{
      width: "200px",
      backgroundColor: "#1f2937",
      color: "white",
      padding: "20px"
    }}>
      <h2>Mini ERP</h2>
      <nav style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
        <Link to="/dashboard" style={{ color: "white" }}>Dashboard</Link>
      </nav>
    </div>
  );
};

export default Sidebar;