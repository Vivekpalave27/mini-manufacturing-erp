import { Routes, Route } from "react-router-dom";
import Login from "../pages/Login";
import Dashboard from "../pages/Dashboard";
import Layout from "../components/layout/Layout";
import ProtectedRoute from "./ProtectedRoute";
import Users from "../pages/Users";
import Items from "../pages/Items";
const AppRoutes = () => {
  return (
    <Routes>
      {/* Login Route */}
      <Route path="/" element={<Login />} />
      <Route path="/login" element={<Login />} />

      {/* Dashboard Route */}
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <Layout>
              <Dashboard />
            </Layout>
          </ProtectedRoute>
        }
      />
      <Route
  path="/users"
  element={
    <ProtectedRoute>
      <Layout>
        <Users />
      </Layout>
    </ProtectedRoute>
  }
/>

<Route
  path="/items"
  element={
    <ProtectedRoute>
      <Layout>
        <Items />
      </Layout>
    </ProtectedRoute>
  }
/>
    </Routes>
  );
};

export default AppRoutes;