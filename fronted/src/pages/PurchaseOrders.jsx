import { useEffect, useState } from "react";
import axios from "../api/axiosConfig";

function PurchaseOrders() {
  const [purchaseOrders, setPurchaseOrders] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [items, setItems] = useState([]);

  const [showModal, setShowModal] = useState(false);

  const [formData, setFormData] = useState({
    supplierId: "",
    items: [],
  });

  useEffect(() => {
    fetchPurchaseOrders();
    fetchSuppliers();
    fetchItems();
  }, []);

  const fetchPurchaseOrders = async () => {
    const res = await axios.get("/api/purchase-orders");
    setPurchaseOrders(res.data);
  };

  const fetchSuppliers = async () => {
    const res = await axios.get("/api/suppliers");
    setSuppliers(res.data);
  };

  const fetchItems = async () => {
    const res = await axios.get("/api/items");
    setItems(res.data);
  };

  // ✅ Open modal with one item row
  const openModal = () => {
    setFormData({
      supplierId: "",
      items: [{ itemId: "", quantity: 1, unitPrice: 0 }],
    });
    setShowModal(true);
  };

  const addItemRow = () => {
    setFormData({
      ...formData,
      items: [...formData.items, { itemId: "", quantity: 1, unitPrice: 0 }],
    });
  };

  const removeItemRow = (index) => {
    const updated = [...formData.items];
    updated.splice(index, 1);
    setFormData({ ...formData, items: updated });
  };

  const updateItemRow = (index, field, value) => {
    const updated = [...formData.items];
    updated[index][field] = value;
    setFormData({ ...formData, items: updated });
  };

  const calculateTotal = () => {
    return formData.items.reduce(
      (sum, item) => sum + item.quantity * item.unitPrice,
      0
    );
  };

  const handleSubmit = async () => {
    if (!formData.supplierId) {
      alert("Please select supplier");
      return;
    }

    if (formData.items.length === 0) {
      alert("Add at least one item");
      return;
    }

    try {
      await axios.post("/api/purchase-orders", formData);
      setShowModal(false);
      fetchPurchaseOrders();
    } catch (err) {
      alert(err.response?.data?.message || "Error creating PO");
    }
  };

  const approvePO = async (id) => {
    await axios.put(`/api/purchase-orders/${id}/approve`);
    fetchPurchaseOrders();
  };

  return (
    <div className="p-6">
      <div className="flex justify-between mb-6">
        <h1 className="text-3xl font-bold text-blue-600">
          Purchase Orders
        </h1>

        <button
          onClick={openModal}
          className="bg-green-600 text-white px-4 py-2 rounded"
        >
          + Create PO
        </button>
      </div>

      <table className="w-full bg-white shadow rounded">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-3">ID</th>
            <th>Supplier</th>
            <th>Status</th>
            <th>Total</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {purchaseOrders.map((po) => (
            <tr key={po.id} className="text-center border-t">
              <td className="p-3">{po.id}</td>
              <td>{po.supplierName}</td>
              <td>{po.status}</td>
              <td>₹ {po.totalAmount}</td>
              <td>
                {po.status === "CREATED" && (
                  <button
                    onClick={() => approvePO(po.id)}
                    className="bg-blue-500 text-white px-3 py-1 rounded"
                  >
                    Approve
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {showModal && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-30">
          <div className="bg-white p-6 rounded shadow w-[700px]">
            <h2 className="text-xl font-bold mb-4">
              Create Purchase Order
            </h2>

            {/* Supplier */}
            <select
              value={formData.supplierId}
              className="w-full mb-4 p-2 border rounded"
              onChange={(e) =>
                setFormData({ ...formData, supplierId: e.target.value })
              }
            >
              <option value="">Select Supplier</option>
              {suppliers.map((s) => (
                <option key={s.id} value={s.id}>
                  {s.name}
                </option>
              ))}
            </select>

            {/* Items */}
            {formData.items.map((row, index) => (
              <div key={index} className="flex space-x-2 mb-3">

                <select
                  value={row.itemId}
                  className="flex-1 p-2 border rounded"
                  onChange={(e) =>
                    updateItemRow(index, "itemId", e.target.value)
                  }
                >
                  <option value="">Select Item</option>
                  {items.map((item) => (
                    <option key={item.id} value={item.id}>
                      {item.name}
                    </option>
                  ))}
                </select>

                <input
                  type="number"
                  value={row.quantity}
                  className="w-24 p-2 border rounded"
                  onChange={(e) =>
                    updateItemRow(index, "quantity", Number(e.target.value))
                  }
                />

                <input
                  type="number"
                  value={row.unitPrice}
                  className="w-24 p-2 border rounded"
                  onChange={(e) =>
                    updateItemRow(index, "unitPrice", Number(e.target.value))
                  }
                />

                <button
                  onClick={() => removeItemRow(index)}
                  className="bg-red-500 text-white px-3 rounded"
                >
                  X
                </button>

              </div>
            ))}

            <button
              onClick={addItemRow}
              className="bg-gray-600 text-white px-3 py-1 rounded mb-3"
            >
              + Add Item
            </button>

            <div className="font-bold mb-4">
              Total: ₹ {calculateTotal()}
            </div>

            <div className="flex justify-end space-x-2">
              <button
                onClick={() => setShowModal(false)}
                className="bg-gray-400 text-white px-4 py-2 rounded"
              >
                Cancel
              </button>

              <button
                onClick={handleSubmit}
                className="bg-blue-600 text-white px-4 py-2 rounded"
              >
                Create
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default PurchaseOrders;