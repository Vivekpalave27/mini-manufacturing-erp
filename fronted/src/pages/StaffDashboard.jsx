import { useContext } from "react";
import { AuthContext } from "../auth/AuthContext";
import { useNavigate } from "react-router-dom";

function StaffDashboard() {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();          // remove token + clear user
    navigate("/");     // redirect to login
  };

  return (
    <div className="p-10">
      <h1 className="text-3xl font-bold text-green-600">
        Staff Dashboard
      </h1>

      <p>Email: {user?.sub}</p>
      <p>Role: {user?.role}</p>

      <button
        onClick={handleLogout}
        className="mt-6 bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
      >
        Logout
      </button>
    </div>
  );
}

export default StaffDashboard;