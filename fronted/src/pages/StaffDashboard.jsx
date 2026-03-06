import { useContext, useEffect, useState } from "react";
import { AuthContext } from "../auth/AuthContext";
import { useNavigate } from "react-router-dom";
import axios from "../api/axiosConfig";

function StaffDashboard() {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const [salesCount, setSalesCount] = useState(0);
  const [totalRevenue, setTotalRevenue] = useState(0);
  const [inventoryCount, setInventoryCount] = useState(0);
  const [lowStockCount, setLowStockCount] = useState(0);
  const [recentOrders, setRecentOrders] = useState([]);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {

      const salesRes = await axios.get("/api/sales-orders");
      const inventoryRes = await axios.get("/api/inventory");

      const salesOrders = salesRes.data || [];
      const inventory = inventoryRes.data || [];

      setSalesCount(salesOrders.length);

      const revenue = salesOrders.reduce(
        (sum, order) => sum + order.totalAmount,
        0
      );

      setTotalRevenue(revenue);

      setInventoryCount(inventory.length);

      const lowStock = inventory.filter(
        (item) => item.quantity < 10
      );

      setLowStockCount(lowStock.length);

      setRecentOrders(salesOrders.slice(0, 5));

    } catch (error) {
      console.error("Dashboard error:", error);
    }
  };

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <div className="p-10">

      {/* HEADER */}
      <h1 className="text-3xl font-bold text-green-600 mb-2">
        Staff Dashboard
      </h1>

      <p className="text-gray-600">Email: {user?.sub}</p>
      <p className="text-gray-600 mb-6">Role: {user?.role}</p>

      {/* KPI CARDS */}
      <div className="grid grid-cols-4 gap-6 mb-8">

        <div className="bg-white shadow rounded p-6">
          <p className="text-gray-500">Sales Orders</p>
          <p className="text-2xl font-bold">{salesCount}</p>
        </div>

        <div className="bg-white shadow rounded p-6">
          <p className="text-gray-500">Total Revenue</p>
          <p className="text-2xl font-bold text-green-600">
            ₹ {totalRevenue.toLocaleString()}
          </p>
        </div>

        <div className="bg-white shadow rounded p-6">
          <p className="text-gray-500">Inventory Items</p>
          <p className="text-2xl font-bold">{inventoryCount}</p>
        </div>

        <div className="bg-white shadow rounded p-6">
          <p className="text-gray-500">Low Stock</p>
          <p className="text-2xl font-bold text-red-600">
            {lowStockCount}
          </p>
        </div>

      </div>

      {/* RECENT ORDERS */}
      <div className="bg-white shadow rounded p-6">

        <h2 className="text-xl font-semibold mb-4">
          Recent Sales Orders
        </h2>

        <table className="w-full border">

          <thead>
            <tr className="bg-gray-100">
              <th className="p-2 text-left">Order No</th>
              <th className="p-2 text-left">Customer</th>
              <th className="p-2 text-left">Amount</th>
              <th className="p-2 text-left">Status</th>
            </tr>
          </thead>

          <tbody>
            {recentOrders.map((order) => (
              <tr key={order.id} className="border-t">

                <td className="p-2">{order.orderNumber}</td>

                <td className="p-2">{order.customerName}</td>

                <td className="p-2 text-green-600">
                  ₹ {order.totalAmount}
                </td>

                <td className="p-2">
                  <span className="bg-green-100 text-green-700 px-2 py-1 rounded text-sm">
                    {order.status}
                  </span>
                </td>

              </tr>
            ))}
          </tbody>

        </table>

      </div>

      

    </div>
  );
}

export default StaffDashboard;