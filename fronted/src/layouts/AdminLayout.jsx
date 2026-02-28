
import { Link } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import Topbar from "../components/Topbar";
<Link to="/inventory">Inventory</Link>
const AdminLayout = ({ children }) => {
  return (
    <div className="flex min-h-screen bg-gray-100">

      <Sidebar role="ADMIN" />

      <div className="flex-1 flex flex-col">
        <Topbar />
        <main className="p-6 flex-1 overflow-auto">
          {children}
        </main>
      </div>

    </div>
  );
};

export default AdminLayout;