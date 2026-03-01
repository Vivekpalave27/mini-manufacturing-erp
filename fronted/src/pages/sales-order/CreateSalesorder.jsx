import React, { useEffect, useState } from "react";
import axios from "../../api/axiosConfig";
import { useNavigate } from "react-router-dom";

const CreateSalesOrder = () => {
  const navigate = useNavigate();

  const [customerName, setCustomerName] = useState("");
  const [items, setItems] = useState([]);
  const [availableItems, setAvailableItems] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchItems();
  }, []);

  const fetchItems = async () => {
    try {
      const response = await axios.get("/api/items");
      setAvailableItems(response.data);
    } catch (error) {
      console.error("Error fetching items:", error);
    }
  };

  const addRow = () => {
    setItems([
      ...items,
      {
        itemId: "",
        quantity: 1,
        price: 0,
        total: 0,
      },
    ]);
  };

  const removeRow = (index) => {
    const updated = [...items];
    updated.splice(index, 1);
    setItems(updated);
  };

  const handleItemChange = (index, itemId) => {
    const selectedItem = availableItems.find(
      (item) => item.id === Number(itemId)
    );

    const updated = [...items];
    updated[index].itemId = itemId;
    updated[index].price = selectedItem?.price || 0;
    updated[index].total =
      updated[index].price * updated[index].quantity;

    setItems(updated);
  };

  const handleQuantityChange = (index, quantity) => {
    const updated = [...items];
    updated[index].quantity = quantity;
    updated[index].total =
      updated[index].price * quantity;

    setItems(updated);
  };

  const calculateGrandTotal = () => {
    return items.reduce((sum, item) => sum + item.total, 0);
  };

  const handleSubmit = async () => {
    try {
      setError("");

      if (!customerName.trim()) {
        setError("Customer name is required.");
        return;
      }

      if (items.length === 0) {
        setError("Add at least one item.");
        return;
      }

      const payload = {
        customerName: customerName.trim(),
        items: items.map((item) => ({
          itemId: Number(item.itemId),
          quantity: Number(item.quantity),
          unitPrice: Number(item.price), // ✅ REQUIRED
        })),
      };

      console.log("Sending Payload:", payload);

      await axios.post("/api/sales-orders", payload);

      navigate("/sales-orders");

    } catch (error) {
      console.error("Error creating sales order:", error);
      setError("Failed to create sales order. Check backend validation.");
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold text-blue-600 mb-4">
        Create Sales Order
      </h1>

      <div className="bg-white shadow rounded-lg p-6">

        {error && (
          <div className="mb-4 text-red-600 font-medium">
            {error}
          </div>
        )}

        <div className="mb-4">
          <label className="block mb-1 font-medium">
            Customer Name
          </label>
          <input
            type="text"
            value={customerName}
            onChange={(e) => setCustomerName(e.target.value)}
            className="border p-2 w-full rounded"
          />
        </div>

        <table className="min-w-full table-auto mb-4">
          <thead>
            <tr>
              <th className="text-left">Item</th>
              <th>Qty</th>
              <th>Price</th>
              <th>Total</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {items.map((row, index) => (
              <tr key={index}>
                <td>
                  <select
                    value={row.itemId}
                    onChange={(e) =>
                      handleItemChange(index, e.target.value)
                    }
                    className="border p-2 rounded"
                  >
                    <option value="">Select Item</option>
                    {availableItems.map((item) => (
                      <option key={item.id} value={item.id}>
                        {item.name}
                      </option>
                    ))}
                  </select>
                </td>

                <td>
                  <input
                    type="number"
                    min="1"
                    value={row.quantity}
                    onChange={(e) =>
                      handleQuantityChange(
                        index,
                        Number(e.target.value)
                      )
                    }
                    className="border p-2 w-20 rounded"
                  />
                </td>

                <td>₹ {row.price}</td>
                <td>₹ {row.total}</td>

                <td>
                  <button
                    onClick={() => removeRow(index)}
                    className="text-red-500"
                  >
                    Remove
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        <button
          onClick={addRow}
          className="bg-gray-200 px-3 py-1 rounded mr-4"
        >
          + Add Item
        </button>

        <div className="mt-4 font-bold">
          Grand Total: ₹ {calculateGrandTotal()}
        </div>

        <button
          onClick={handleSubmit}
          className="bg-green-500 text-white px-4 py-2 rounded mt-4"
        >
          Create Order
        </button>

      </div>
    </div>
  );
};

export default CreateSalesOrder;