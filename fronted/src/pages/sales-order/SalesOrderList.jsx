import React, { useEffect, useState, useContext } from "react";
import axios from "../../api/axiosConfig";
import { AuthContext } from "../../auth/AuthContext";
import { useNavigate } from "react-router-dom";

const SalesOrderList = () => {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();

  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [loadingId, setLoadingId] = useState(null);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await axios.get("/api/sales-orders");
      setOrders(response.data);
    } catch (error) {
      console.error("Error fetching sales orders:", error);
      setError("Failed to load sales orders.");
    } finally {
      setLoading(false);
    }
  };

  const handleConfirm = async (id) => {
    try {
      setError("");
      setLoadingId(id);

      await axios.put(`/api/sales-orders/${id}/confirm`);

      fetchOrders();
    } catch (error) {
      console.error("Error confirming sales order:", error);

      const backendMessage =
        error.response?.data?.message ||
        "Failed to confirm order. Check stock availability.";

      setError(backendMessage);
    } finally {
      setLoadingId(null);
    }
  };

  const getStatusBadge = (status) => {
    if (status === "CREATED") {
      return (
        <span className="px-3 py-1 text-sm rounded-full bg-yellow-100 text-yellow-800">
          CREATED
        </span>
      );
    }

    if (status === "CONFIRMED") {
      return (
        <span className="px-3 py-1 text-sm rounded-full bg-green-100 text-green-800">
          CONFIRMED
        </span>
      );
    }

    if (status === "CANCELLED") {
      return (
        <span className="px-3 py-1 text-sm rounded-full bg-red-100 text-red-800">
          CANCELLED
        </span>
      );
    }

    return status;
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold text-blue-600 mb-4">
        Sales Orders
      </h1>

      {/* 🔥 Error Banner */}
      {error && (
        <div className="mb-4 p-3 bg-red-100 text-red-700 rounded">
          {error}
        </div>
      )}

      {/* Create Button */}
      {user?.role === "ADMIN" && (
        <div className="mb-4 text-right">
          <button
            onClick={() => navigate("/sales-orders/create")}
            className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded"
          >
            + Create Sales Order
          </button>
        </div>
      )}

      {loading ? (
        <p>Loading sales orders...</p>
      ) : (
        <div className="bg-white shadow rounded-lg overflow-x-auto">
          <table className="min-w-full table-auto">
            <thead className="bg-gray-100">
              <tr>
                <th className="px-4 py-2 text-left">Order No</th>
                <th className="px-4 py-2 text-left">Customer</th>
                <th className="px-4 py-2 text-left">Total</th>
                <th className="px-4 py-2 text-left">Status</th>
                <th className="px-4 py-2 text-left">Order Date</th>
                <th className="px-4 py-2 text-left">Action</th>
              </tr>
            </thead>

            <tbody>
              {orders.map((order) => (
                <tr key={order.id} className="border-t">
                  <td className="px-4 py-2">
                    {order.orderNumber ||
                      order.salesOrderNumber ||
                      `SO-${order.id}`}
                  </td>

                  <td className="px-4 py-2">{order.customerName}</td>

                  <td className="px-4 py-2">
                    ₹ {order.totalAmount?.toLocaleString()}
                  </td>

                  <td className="px-4 py-2">
                    {getStatusBadge(order.status)}
                  </td>

                  <td className="px-4 py-2">
                    {order.orderDate || order.createdDate
                      ? new Date(
                          order.orderDate || order.createdDate
                        ).toLocaleDateString()
                      : "-"}
                  </td>

                  <td className="px-4 py-2">
                    {user?.role === "ADMIN" &&
                      order.status === "CREATED" && (
                        <button
                          onClick={() => handleConfirm(order.id)}
                          disabled={loadingId === order.id}
                          className={`px-3 py-1 rounded text-sm text-white ${
                            loadingId === order.id
                              ? "bg-gray-400 cursor-not-allowed"
                              : "bg-blue-500 hover:bg-blue-600"
                          }`}
                        >
                          {loadingId === order.id
                            ? "Processing..."
                            : "Confirm"}
                        </button>
                      )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {orders.length === 0 && (
            <p className="p-4 text-gray-500">
              No sales orders found.
            </p>
          )}
        </div>
      )}
    </div>
  );
};

export default SalesOrderList;