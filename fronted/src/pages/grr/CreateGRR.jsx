import { useEffect, useState } from "react";
import axios from "../../api/axiosConfig";
import { useNavigate } from "react-router-dom";

const CreateGRR = () => {
  const navigate = useNavigate();

  const [purchaseOrders, setPurchaseOrders] = useState([]);
  const [itemsList, setItemsList] = useState([]);
  const [selectedPO, setSelectedPO] = useState("");
  const [loading, setLoading] = useState(false);

  const [items, setItems] = useState([
    { itemId: "", quantity: 1, unitPrice: 0 },
  ]);

  useEffect(() => {
    fetchApprovedPOs();
    fetchItems();
  }, []);

  const fetchApprovedPOs = async () => {
    try {
      const res = await axios.get("/api/purchase-orders");
      const approved = res.data.filter(
        (po) => po.status === "APPROVED"
      );
      setPurchaseOrders(approved);
    } catch (error) {
      console.error("Error loading purchase orders", error);
    }
  };

  const fetchItems = async () => {
    try {
      const res = await axios.get("/api/items");
      setItemsList(res.data);
    } catch (error) {
      console.error("Error loading items", error);
    }
  };

  const addRow = () => {
    setItems([...items, { itemId: "", quantity: 1, unitPrice: 0 }]);
  };

  const removeRow = (index) => {
    const updated = [...items];
    updated.splice(index, 1);
    setItems(updated);
  };

  const updateRow = (index, field, value) => {
    const updated = [...items];
    updated[index][field] = value;
    setItems(updated);
  };

  const handleSubmit = async () => {
    if (!selectedPO) {
      alert("Please select Purchase Order");
      return;
    }

    if (items.length === 0) {
      alert("Add at least one item");
      return;
    }

    for (let item of items) {
      if (!item.itemId || item.quantity <= 0 || item.unitPrice <= 0) {
        alert("Please fill all item details correctly");
        return;
      }
    }

    const payload = {
      purchaseOrderId: Number(selectedPO),
      items: items.map((item) => ({
        itemId: Number(item.itemId),
        quantity: Number(item.quantity),
        unitPrice: Number(item.unitPrice),
      })),
    };

    try {
      setLoading(true);

      // 🔥 Step 1: Create GRR
      const res = await axios.post("/api/grr", payload);
      const createdGRR = res.data;

      // 🔥 Step 2: Receive GRR (Update Stock)
      await axios.put(`/api/grr/${createdGRR.id}/receive`);

      alert("GRR Created & Stock Updated Successfully");

      navigate("/inventory");

    } catch (error) {
      console.error("Error creating GRR:", error);
      alert(error.response?.data || "Error creating GRR");
    } finally {
      setLoading(false);
    }
  };

  const totalAmount = items.reduce(
    (sum, item) => sum + item.quantity * item.unitPrice,
    0
  );

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold text-blue-600 mb-4">
        Create GRR (Manual)
      </h1>

      {/* Purchase Order */}
      <select
        value={selectedPO}
        onChange={(e) => setSelectedPO(e.target.value)}
        className="border p-2 w-full rounded mb-4"
      >
        <option value="">Select Approved PO</option>
        {purchaseOrders.map((po) => (
          <option key={po.id} value={po.id}>
            {po.poNumber}
          </option>
        ))}
      </select>

      {/* Items */}
      {items.map((row, index) => (
        <div key={index} className="flex space-x-2 mb-3">

          <select
            value={row.itemId}
            onChange={(e) =>
              updateRow(index, "itemId", e.target.value)
            }
            className="flex-1 p-2 border rounded"
          >
            <option value="">Select Item</option>
            {itemsList.map((item) => (
              <option key={item.id} value={item.id}>
                {item.name}
              </option>
            ))}
          </select>

          <input
            type="number"
            min="1"
            value={row.quantity}
            onChange={(e) =>
              updateRow(index, "quantity", Number(e.target.value))
            }
            className="w-24 p-2 border rounded"
          />

          <input
            type="number"
            min="1"
            value={row.unitPrice}
            onChange={(e) =>
              updateRow(index, "unitPrice", Number(e.target.value))
            }
            className="w-24 p-2 border rounded"
          />

          <button
            onClick={() => removeRow(index)}
            className="bg-red-500 text-white px-3 rounded"
          >
            X
          </button>
        </div>
      ))}

      <button
        onClick={addRow}
        className="bg-gray-600 text-white px-3 py-1 rounded mb-4"
      >
        + Add Item
      </button>

      <div className="font-bold mb-4">
        Total: ₹ {totalAmount}
      </div>

      <button
        onClick={handleSubmit}
        disabled={loading}
        className="bg-green-600 text-white px-6 py-2 rounded"
      >
        {loading ? "Processing..." : "Create GRR"}
      </button>
    </div>
  );
};

export default CreateGRR;