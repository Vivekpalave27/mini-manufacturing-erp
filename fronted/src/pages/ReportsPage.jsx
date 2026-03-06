import { useState } from "react";
import axios from "../api/axiosConfig";

const ReportsPage = () => {
  const [reportType, setReportType] = useState("sales");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleGenerate = async () => {
    if (!startDate || !endDate) {
      setError("Start date and End date are required.");
      return;
    }

    if (new Date(startDate) > new Date(endDate)) {
      setError("Start date cannot be after End date.");
      return;
    }

    try {
      setLoading(true);
      setError("");
      setData(null);

      const response = await axios.get(
        `/api/reports/${reportType}?startDate=${startDate}&endDate=${endDate}`
      );

      setData(response.data);
    } catch (err) {
      setError(
        err.response?.data?.message || "Error generating report."
      );
    } finally {
      setLoading(false);
    }
  };

  // 🔥 Dynamic value mapping
  const getTotalCount = () => {
    if (!data) return 0;

    if (reportType === "sales") return data.totalOrders;
    if (reportType === "purchase") return data.totalOrders;
    if (reportType === "expense") return data.totalExpenses;

    return 0;
  };

  const getTotalAmount = () => {
    if (!data) return 0;

    if (reportType === "sales") return data.totalSalesAmount;
    if (reportType === "purchase") return data.totalPurchaseAmount;
    if (reportType === "expense") return data.totalExpenseAmount;

    return 0;
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">Reports</h1>

      {/* Filter Section */}
      <div className="bg-white p-6 rounded shadow mb-6">
        <div className="grid grid-cols-4 gap-4">
          <select
            className="border p-2 rounded"
            value={reportType}
            onChange={(e) => setReportType(e.target.value)}
          >
            <option value="sales">Sales Report</option>
            <option value="purchase">Purchase Report</option>
            <option value="expense">Expense Report</option>
          </select>

          <input
            type="date"
            className="border p-2 rounded"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
          />

          <input
            type="date"
            className="border p-2 rounded"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
          />

          <button
            onClick={handleGenerate}
            disabled={loading}
            className={`text-white rounded px-4 py-2 ${
              loading
                ? "bg-gray-400 cursor-not-allowed"
                : "bg-blue-600 hover:bg-blue-700"
            }`}
          >
            {loading ? "Generating..." : "Generate"}
          </button>
        </div>

        {error && (
          <p className="text-red-500 mt-3 text-sm">{error}</p>
        )}
      </div>

      {/* Result Section */}
      {loading && (
        <p className="text-gray-600">Loading report...</p>
      )}

      {data && (
        <div className="bg-white shadow rounded p-6">
          <h2 className="text-xl font-semibold mb-4">
            Report Summary
          </h2>

          {getTotalCount() === 0 ? (
            <p className="text-gray-500">
              No data found for selected date range.
            </p>
          ) : (
            <div className="grid grid-cols-2 gap-6">
              <div>
                <p className="text-gray-500">Total Count</p>
                <p className="text-2xl font-bold">
                  {getTotalCount()}
                </p>
              </div>

              <div>
                <p className="text-gray-500">Total Amount</p>
                <p className="text-2xl font-bold text-green-600">
                  ₹ {getTotalAmount()?.toLocaleString()}
                </p>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default ReportsPage;