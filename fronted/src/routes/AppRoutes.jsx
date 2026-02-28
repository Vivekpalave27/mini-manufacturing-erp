import { Routes, Route } from "react-router-dom";
import Login from "../pages/Login";
import ProtectedRoute from "../components/ProtectedRoute";

import AdminLayout from "../layouts/AdminLayout";
import StaffLayout from "../layouts/StaffLayout";

import AdminDashboard from "../pages/AdminDashboard";
import StaffDashboard from "../pages/StaffDashboard";
import Items from "../pages/Items";
import Suppliers from "../pages/Suppliers";
import PurchaseOrders from "../pages/PurchaseOrders";
import Inventory from "../pages/Inventory";

const AppRoutes = () => {
  return (
    <Routes>

      {/* Public Route */}
      <Route path="/" element={<Login />} />
      <Route path="/login" element={<Login />} />

      {/* ADMIN ROUTES */}
      <Route
        path="/admin-dashboard"
        element={
          <ProtectedRoute allowedRoles={["ADMIN"]}>
            <AdminLayout>
              <AdminDashboard />
            </AdminLayout>
          </ProtectedRoute>
        }
      />

      <Route
        path="/items"
        element={
          <ProtectedRoute allowedRoles={["ADMIN"]}>
            <AdminLayout>
              <Items />
            </AdminLayout>
          </ProtectedRoute>
        }
      />

      <Route
        path="/suppliers"
        element={
          <ProtectedRoute allowedRoles={["ADMIN"]}>
            <AdminLayout>
              <Suppliers />
            </AdminLayout>
          </ProtectedRoute>
        }
      />

      <Route
        path="/purchase-orders"
        element={
          <ProtectedRoute allowedRoles={["ADMIN"]}>
            <AdminLayout>
              <PurchaseOrders />
            </AdminLayout>
          </ProtectedRoute>
        }
      />

      <Route
        path="/inventory"
        element={
          <ProtectedRoute allowedRoles={["ADMIN", "STAFF"]}>
            <AdminLayout>
              <Inventory />
            </AdminLayout>
          </ProtectedRoute>
        }
      />

      {/* STAFF ROUTES */}
      <Route
        path="/staff-dashboard"
        element={
          <ProtectedRoute allowedRoles={["STAFF"]}>
            <StaffLayout>
              <StaffDashboard />
            </StaffLayout>
          </ProtectedRoute>
        }
      />

    </Routes>
  );
};

export default AppRoutes;