import { useEffect, useState } from "react";
import axios from "../api/axiosConfig";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  CartesianGrid,
} from "recharts";

function AdminDashboard() {
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const response = await axios.get("/api/dashboard/summary");

      // ✅ Map backend fields correctly
      setSummary({
        totalSales: response.data.totalSalesAmount,
        totalPurchase: response.data.totalPurchaseAmount,
        totalExpense: response.data.totalExpenseAmount,
        totalItems: response.data.totalItems,
        totalSuppliers: response.data.totalSuppliers,
        lowStockCount: response.data.lowStockItemsCount,
      });

    } catch (err) {
      setError("Failed to load dashboard data.");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="text-lg font-semibold">Loading dashboard...</div>;
  }

  if (error) {
    return <div className="text-red-500 font-semibold">{error}</div>;
  }

  const chartData = [
    { name: "Sales", value: summary?.totalSales || 0 },
    { name: "Purchase", value: summary?.totalPurchase || 0 },
  ];

  return (
    <div>
      <h1 className="text-3xl font-bold text-blue-600 mb-8">
        Admin Dashboard
      </h1>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <SummaryCard title="Total Sales" value={summary?.totalSales} color="blue" currency />
        <SummaryCard title="Total Purchases" value={summary?.totalPurchase} color="green" currency />
        <SummaryCard title="Total Expenses" value={summary?.totalExpense} color="red" currency />
        <SummaryCard title="Total Items" value={summary?.totalItems} color="purple" />
        <SummaryCard title="Total Suppliers" value={summary?.totalSuppliers} color="yellow" />
        <SummaryCard title="Low Stock Items" value={summary?.lowStockCount} color="orange" />
      </div>

      {/* Low Stock Alert */}
      <div className="mt-10">
        <h2 className="text-xl font-semibold mb-4 text-red-600">
          Low Stock Alert
        </h2>

        {summary?.lowStockCount > 0 ? (
          <div className="bg-red-100 text-red-700 p-4 rounded shadow">
            ⚠ {summary.lowStockCount} items are low in stock.
          </div>
        ) : (
          <div className="bg-green-100 text-green-700 p-4 rounded shadow">
            ✅ All items are sufficiently stocked.
          </div>
        )}
      </div>

      {/* Chart Section */}
      <div className="mt-12">
        <h2 className="text-xl font-semibold mb-4">
          Sales vs Purchase Overview
        </h2>

        <div className="bg-white p-6 rounded shadow">
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="value" fill="#4F46E5" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}

/* Reusable Summary Card */
function SummaryCard({ title, value, color, currency }) {
  const colors = {
    blue: "bg-blue-500",
    green: "bg-green-500",
    red: "bg-red-500",
    purple: "bg-purple-500",
    yellow: "bg-yellow-500",
    orange: "bg-orange-500",
  };

  const formattedValue = currency
    ? `₹ ${Number(value || 0).toLocaleString()}`
    : value ?? 0;

  return (
    <div className="bg-white shadow-md rounded-lg p-6">
      <h3 className="text-gray-600 text-sm">{title}</h3>
      <div
        className={`mt-3 text-white text-xl font-bold px-4 py-2 rounded ${colors[color]}`}
      >
        {formattedValue}
      </div>
    </div>
  );
}

export default AdminDashboard;