import { NavLink } from "react-router-dom";

const Sidebar = ({ role }) => {
  return (
    <div className="w-64 bg-white shadow-md">
      <div className="p-6 font-bold text-xl text-green-600 border-b">
        Mini ERP
      </div>

      <nav className="p-4 space-y-2">

        <NavLink
          to="/admin-dashboard"
          className="block px-4 py-2 rounded hover:bg-green-100"
        >
          Dashboard
        </NavLink>

        {role === "ADMIN" && (
          <>
            <NavLink to="/items" className="block px-4 py-2 rounded hover:bg-green-100">
              Items
            </NavLink>

            <NavLink to="/suppliers" className="block px-4 py-2 rounded hover:bg-green-100">
              Suppliers
            </NavLink>

            <NavLink to="/purchase-orders" className="block px-4 py-2 rounded hover:bg-green-100">
              Purchase Orders
            </NavLink>

            <NavLink to="/sales-orders" className="block px-4 py-2 rounded hover:bg-green-100">
              Sales Orders
            </NavLink>

            <NavLink to="/grr" className="block px-4 py-2 rounded hover:bg-green-100">
              GRR
            </NavLink>

            <NavLink to="/expenses" className="block px-4 py-2 rounded hover:bg-green-100">
              Expenses
            </NavLink>

            <NavLink to="/reports" className="block px-4 py-2 rounded hover:bg-green-100">
              Reports
            </NavLink>
          </>
        )}

        {role === "STAFF" && (
          <>
            <NavLink to="/inventory" className="block px-4 py-2 rounded hover:bg-green-100">
              Inventory
            </NavLink>

            <NavLink to="/suppliers" className="block px-4 py-2 rounded hover:bg-green-100">
              Suppliers
            </NavLink>

            <NavLink to="/purchase-orders" className="block px-4 py-2 rounded hover:bg-green-100">
              Purchase Orders
            </NavLink>

            <NavLink to="/sales-orders" className="block px-4 py-2 rounded hover:bg-green-100">
              Sales Orders
            </NavLink>

            <NavLink to="/reports" className="block px-4 py-2 rounded hover:bg-green-100">
              Reports
            </NavLink>
          </>
        )}

      </nav>
    </div>
  );
};

export default Sidebar;