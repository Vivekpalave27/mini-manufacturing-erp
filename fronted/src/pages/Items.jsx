import { useEffect, useState } from "react";
import axios from "../api/axiosConfig";

function Items() {
  const [items, setItems] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editingItem, setEditingItem] = useState(null);

  const [formData, setFormData] = useState({
    name: "",
    sku: "",
    unit: "",
    stockQty: 0,
    price: "",
  });

  useEffect(() => {
    fetchItems();
  }, []);

  const fetchItems = async () => {
    const res = await axios.get("/api/items");
    setItems(res.data);
  };

  const openAddModal = () => {
    setEditingItem(null);
    setFormData({
      name: "",
      sku: "",
      unit: "",
      stockQty: 0,
      price: "",
    });
    setShowModal(true);
  };

  const openEditModal = (item) => {
    setEditingItem(item);
    setFormData({
      name: item.name,
      sku: item.sku,
      unit: item.unit,
      stockQty: item.stockQty,
      price: item.price,
    });
    setShowModal(true);
  };

  const handleSubmit = async () => {
    try {
      if (editingItem) {
        await axios.put(`/api/items/${editingItem.id}`, formData);
      } else {
        await axios.post("/api/items", formData);
      }

      setShowModal(false);
      fetchItems();
    } catch (err) {
      alert(err.response?.data?.message || "Something went wrong");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this item?")) return;

    await axios.delete(`/api/items/${id}`);
    fetchItems();
  };

  return (
    <div className="p-6">
      <div className="flex justify-between mb-6">
        <h1 className="text-3xl font-bold text-blue-600">
          Items Management
        </h1>

        <button
          onClick={openAddModal}
          className="bg-green-600 text-white px-4 py-2 rounded"
        >
          + Add Item
        </button>
      </div>

      <table className="w-full bg-white shadow rounded">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-3">ID</th>
            <th>Name</th>
            <th>SKU</th>
            <th>Unit</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {items.map((item) => (
            <tr
              key={item.id}
              className={`text-center border-t ${
                item.stockQty < 10 ? "bg-red-50" : ""
              }`}
            >
              <td className="p-3">{item.id}</td>
              <td>{item.name}</td>
              <td>{item.sku}</td>
              <td>{item.unit}</td>
              <td>₹ {item.price}</td>

              <td
                className={`font-semibold ${
                  item.stockQty < 10 ? "text-red-600" : "text-green-600"
                }`}
              >
                {item.stockQty}
                {item.stockQty < 10 && (
                  <span className="ml-2 text-xs bg-red-200 text-red-700 px-2 py-1 rounded">
                    LOW
                  </span>
                )}
              </td>

              <td className="space-x-2">
                <button
                  onClick={() => openEditModal(item)}
                  className="bg-blue-500 text-white px-3 py-1 rounded"
                >
                  Edit
                </button>

                <button
                  onClick={() => handleDelete(item.id)}
                  className="bg-red-500 text-white px-3 py-1 rounded"
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-30">
          <div className="bg-white p-6 rounded shadow w-96">
            <h2 className="text-xl font-bold mb-4">
              {editingItem ? "Edit Item" : "Add New Item"}
            </h2>

            <input
              type="text"
              placeholder="Name"
              className="w-full mb-3 p-2 border rounded"
              value={formData.name}
              onChange={(e) =>
                setFormData({ ...formData, name: e.target.value })
              }
            />

            <input
              type="text"
              placeholder="SKU"
              className="w-full mb-3 p-2 border rounded"
              value={formData.sku}
              onChange={(e) =>
                setFormData({ ...formData, sku: e.target.value })
              }
            />

            <input
              type="text"
              placeholder="Unit"
              className="w-full mb-3 p-2 border rounded"
              value={formData.unit}
              onChange={(e) =>
                setFormData({ ...formData, unit: e.target.value })
              }
            />

            <input
              type="number"
              placeholder="Price"
              className="w-full mb-3 p-2 border rounded"
              value={formData.price}
              onChange={(e) =>
                setFormData({ ...formData, price: e.target.value })
              }
            />

            <input
              type="number"
              disabled
              className="w-full mb-3 p-2 border bg-gray-100 rounded"
              value={formData.stockQty}
            />

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
                {editingItem ? "Update" : "Add"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Items;