import React, { useEffect, useState } from "react";
import axios from "../../api/axiosConfig";
import { useNavigate } from "react-router-dom";

const CreatePurchaseBill = () => {
  const navigate = useNavigate();

  const [billNumber, setBillNumber] = useState("");
  const [supplierName, setSupplierName] = useState("");
  const [suppliers, setSuppliers] = useState([]);
  const [totalAmount, setTotalAmount] = useState("");
  const [billDate, setBillDate] = useState("");
  const [error, setError] = useState("");

  // ✅ Load suppliers + auto bill number
  useEffect(() => {
    generateBillNumber();
    loadSuppliers();
  }, []);

  const generateBillNumber = () => {
    const generated =
      "PB-" + Math.random().toString(36).substring(2, 8);
    setBillNumber(generated);

    const today = new Date().toISOString().split("T")[0];
    setBillDate(today);
  };

  const loadSuppliers = async () => {
    try {
      const response = await axios.get("/api/suppliers");
      setSuppliers(response.data);
    } catch (err) {
      console.error("Error loading suppliers:", err);
    }
  };

  const handleSubmit = async () => {
    try {
      setError("");

      if (!supplierName) {
        setError("Please select supplier.");
        return;
      }

      if (!totalAmount || totalAmount <= 0) {
        setError("Total amount must be greater than 0.");
        return;
      }

      const payload = {
        id: 0,
        billNumber,
        supplierName,
        totalAmount: Number(totalAmount),
        status: "CREATED",
        billDate,
      };

      await axios.post("/api/purchase-bills", payload);

      navigate("/purchase-bills");
    } catch (error) {
      console.error("Error creating purchase bill:", error);
      setError("Failed to create purchase bill.");
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold text-blue-600 mb-4">
        Create Purchase Bill
      </h1>

      <div className="bg-white shadow rounded-lg p-6 max-w-lg">

        {error && (
          <div className="mb-4 p-3 bg-red-100 text-red-700 rounded">
            {error}
          </div>
        )}

        <div className="mb-4">
          <label className="block mb-1 font-medium">
            Bill Number
          </label>
          <input
            type="text"
            value={billNumber}
            disabled
            className="border p-2 w-full rounded bg-gray-100"
          />
        </div>

        {/* ✅ Supplier Dropdown */}
        <div className="mb-4">
          <label className="block mb-1 font-medium">
            Supplier *
          </label>
          <select
            value={supplierName}
            onChange={(e) => setSupplierName(e.target.value)}
            className="border p-2 w-full rounded"
          >
            <option value="">Select Supplier</option>
            {suppliers.map((supplier) => (
              <option key={supplier.id} value={supplier.name}>
                {supplier.name}
              </option>
            ))}
          </select>
        </div>

        <div className="mb-4">
          <label className="block mb-1 font-medium">
            Total Amount *
          </label>
          <input
            type="number"
            value={totalAmount}
            onChange={(e) => setTotalAmount(e.target.value)}
            className="border p-2 w-full rounded"
          />
        </div>

        <div className="mb-4">
          <label className="block mb-1 font-medium">
            Bill Date
          </label>
          <input
            type="date"
            value={billDate}
            disabled
            className="border p-2 w-full rounded bg-gray-100"
          />
        </div>

        <button
          onClick={handleSubmit}
          className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded"
        >
          Create Bill
        </button>
      </div>
    </div>
  );
};

export default CreatePurchaseBill;