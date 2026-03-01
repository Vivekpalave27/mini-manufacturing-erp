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

// ✅ GRR
import GRRList from "../pages/grr/GRRList";
import CreateGRR from "../pages/grr/CreateGRR";

// ✅ Sales Order
import SalesOrderList from "../pages/sales-order/SalesOrderList";
import CreateSalesOrder from "../pages/sales-order/CreateSalesOrder";

const AppRoutes = () => {
  return (
    <Routes>

      {/* Public Routes */}
      <Route path="/" element={<Login />} />
      <Route path="/login" element={<Login />} />

      {/* ================= ADMIN ROUTES ================= */}

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

      {/* ================= SHARED ROUTES (ADMIN + STAFF) ================= */}

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

      {/* ================= GRR ROUTES ================= */}

      <Route
        path="/grr"
        element={
          <ProtectedRoute allowedRoles={["ADMIN", "STAFF"]}>
            <AdminLayout>
              <GRRList />
            </AdminLayout>
          </ProtectedRoute>
        }
      />

      <Route
        path="/grr/create"
        element={
          <ProtectedRoute allowedRoles={["ADMIN"]}>
            <AdminLayout>
              <CreateGRR />
            </AdminLayout>
          </ProtectedRoute>
        }
      />

      {/* ================= SALES ORDER ROUTES ================= */}

      <Route
        path="/sales-orders"
        element={
          <ProtectedRoute allowedRoles={["ADMIN", "STAFF"]}>
            <AdminLayout>
              <SalesOrderList />
            </AdminLayout>
          </ProtectedRoute>
        }
      />

      <Route
        path="/sales-orders/create"
        element={
          <ProtectedRoute allowedRoles={["ADMIN"]}>
            <AdminLayout>
              <CreateSalesOrder />
            </AdminLayout>
          </ProtectedRoute>
        }
      />

      {/* ================= STAFF ROUTES ================= */}

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