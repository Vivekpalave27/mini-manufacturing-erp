import { useNavigate } from "react-router-dom";

const Navbar = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  return (
    <div
      style={{
        height: "60px",
        backgroundColor: "#f3f4f6",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "0 20px"
      }}
    >
      <h3>Welcome to Mini Manufacturing ERP</h3>
      <button
        onClick={handleLogout}
        style={{
          padding: "6px 12px",
          cursor: "pointer"
        }}
      >
        Logout
      </button>
    </div>
  );
};

export default Navbar;
