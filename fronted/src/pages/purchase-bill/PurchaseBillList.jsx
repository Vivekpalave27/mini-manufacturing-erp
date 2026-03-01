import { useEffect, useState } from "react";
import axios from "../../api/axiosConfig";
import { useNavigate } from "react-router-dom";

const PurchaseBillList = () => {
  const [bills, setBills] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchBills();
  }, []);

  const fetchBills = async () => {
    try {
      const res = await axios.get("/api/purchase-bills");
      setBills(res.data);
    } catch (error) {
      console.error("Error fetching purchase bills:", error);
    }
  };

  return (
    <div className="p-6">
      <div className="flex justify-between mb-4">
        <h1 className="text-2xl font-bold text-blue-600">
          Purchase Bills
        </h1>

        <button
          onClick={() => navigate("/purchase-bills/create")}
          className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded"
        >
          + Create Bill
        </button>
      </div>

      <table className="w-full bg-white shadow rounded">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-2">Bill No</th>
            <th>Supplier</th>
            <th>Total</th>
            <th>Status</th>
            <th>Date</th>
          </tr>
        </thead>

        <tbody>
          {bills.length === 0 ? (
            <tr>
              <td colSpan="5" className="text-center p-4">
                No Purchase Bills found
              </td>
            </tr>
          ) : (
            bills.map((bill) => (
              <tr key={bill.id} className="text-center border-t">
                <td className="p-2">{bill.billNumber}</td>
                <td>{bill.supplierName}</td>
                <td>₹ {bill.totalAmount}</td>
                <td>{bill.status}</td>
                <td>{bill.billDate || "-"}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default PurchaseBillList;