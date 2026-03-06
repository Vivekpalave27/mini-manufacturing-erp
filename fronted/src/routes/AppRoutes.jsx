import { Routes, Route } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../auth/AuthContext";

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

import GRRList from "../pages/grr/GRRList";
import CreateGRR from "../pages/grr/CreateGRR";

import SalesOrderList from "../pages/sales-order/SalesOrderList";
import CreateSalesOrder from "../pages/sales-order/CreateSalesOrder";

import ExpensePage from "../pages/ExpensePage";
import ReportsPage from "../pages/ReportsPage";

const LayoutWrapper = ({ children }) => {
  const { user } = useContext(AuthContext);

  if (user?.role === "ADMIN") {
    return <AdminLayout>{children}</AdminLayout>;
  }

  return <StaffLayout>{children}</StaffLayout>;
};

const AppRoutes = () => {
  return (
    <Routes>

      {/* PUBLIC */}
      <Route path="/" element={<Login />} />
      <Route path="/login" element={<Login />} />

      {/* ADMIN DASHBOARD */}
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

      {/* STAFF DASHBOARD */}
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

      {/* ADMIN ONLY */}
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
        path="/expenses"
        element={
          <ProtectedRoute allowedRoles={["ADMIN"]}>
            <AdminLayout>
              <ExpensePage />
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

      {/* SHARED ROUTES (ADMIN + STAFF) */}
      <Route
        path="/inventory"
        element={
          <ProtectedRoute allowedRoles={["ADMIN", "STAFF"]}>
            <LayoutWrapper>
              <Inventory />
            </LayoutWrapper>
          </ProtectedRoute>
        }
      />

      <Route
        path="/suppliers"
        element={
          <ProtectedRoute allowedRoles={["ADMIN", "STAFF"]}>
            <LayoutWrapper>
              <Suppliers />
            </LayoutWrapper>
          </ProtectedRoute>
        }
      />

      <Route
        path="/purchase-orders"
        element={
          <ProtectedRoute allowedRoles={["ADMIN", "STAFF"]}>
            <LayoutWrapper>
              <PurchaseOrders />
            </LayoutWrapper>
          </ProtectedRoute>
        }
      />

      <Route
        path="/sales-orders"
        element={
          <ProtectedRoute allowedRoles={["ADMIN", "STAFF"]}>
            <LayoutWrapper>
              <SalesOrderList />
            </LayoutWrapper>
          </ProtectedRoute>
        }
      />

      <Route
        path="/grr"
        element={
          <ProtectedRoute allowedRoles={["ADMIN", "STAFF"]}>
            <LayoutWrapper>
              <GRRList />
            </LayoutWrapper>
          </ProtectedRoute>
        }
      />

      <Route
        path="/reports"
        element={
          <ProtectedRoute allowedRoles={["ADMIN", "STAFF"]}>
            <LayoutWrapper>
              <ReportsPage />
            </LayoutWrapper>
          </ProtectedRoute>
        }
      />

    </Routes>
  );
};

export default AppRoutes;