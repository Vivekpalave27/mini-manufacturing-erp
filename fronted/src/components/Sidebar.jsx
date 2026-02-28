import { NavLink } from "react-router-dom";

const Sidebar = ({ role }) => {
  const linkClass = ({ isActive }) =>
    `block px-4 py-2 rounded transition ${
      isActive
        ? "bg-green-500 text-white"
        : "hover:bg-green-100 text-gray-700"
    }`;

  return (
    <div className="w-64 bg-white shadow-md min-h-screen">
      <div className="p-6 font-bold text-xl text-green-600 border-b">
        Mini ERP
      </div>

      <nav className="p-4 space-y-2">

        {/* Dashboard (Role Based) */}
        {role === "ADMIN" && (
          <NavLink to="/admin-dashboard" className={linkClass}>
            Dashboard
          </NavLink>
        )}

        {role === "STAFF" && (
          <NavLink to="/staff-dashboard" className={linkClass}>
            Dashboard
          </NavLink>
        )}

        {/* ADMIN MENU */}
        {role === "ADMIN" && (
          <>
            <NavLink to="/items" className={linkClass}>
              Items
            </NavLink>

            <NavLink to="/inventory" className={linkClass}>
              Inventory
            </NavLink>

            <NavLink to="/suppliers" className={linkClass}>
              Suppliers
            </NavLink>

            <NavLink to="/purchase-orders" className={linkClass}>
              Purchase Orders
            </NavLink>

            <NavLink to="/sales-orders" className={linkClass}>
              Sales Orders
            </NavLink>

            <NavLink to="/grr" className={linkClass}>
              GRR
            </NavLink>

            <NavLink to="/expenses" className={linkClass}>
              Expenses
            </NavLink>

            <NavLink to="/reports" className={linkClass}>
              Reports
            </NavLink>
          </>
        )}

        {/* STAFF MENU */}
        {role === "STAFF" && (
          <>
            <NavLink to="/inventory" className={linkClass}>
              Inventory
            </NavLink>

            <NavLink to="/suppliers" className={linkClass}>
              Suppliers
            </NavLink>

            <NavLink to="/purchase-orders" className={linkClass}>
              Purchase Orders
            </NavLink>

            <NavLink to="/sales-orders" className={linkClass}>
              Sales Orders
            </NavLink>

            <NavLink to="/reports" className={linkClass}>
              Reports
            </NavLink>
          </>
        )}

      </nav>
    </div>
  );
};

export default Sidebar;