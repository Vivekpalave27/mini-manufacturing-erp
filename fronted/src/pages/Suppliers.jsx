import { useEffect, useState } from "react";
import axios from "../api/axiosConfig";

function Suppliers() {
  const [suppliers, setSuppliers] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editingSupplier, setEditingSupplier] = useState(null);

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    address: "",
  });

  useEffect(() => {
    fetchSuppliers();
  }, []);

  const fetchSuppliers = async () => {
    const res = await axios.get("/api/suppliers");
    setSuppliers(res.data);
  };

  const openAddModal = () => {
    setEditingSupplier(null);
    setFormData({
      name: "",
      email: "",
      phone: "",
      address: "",
    });
    setShowModal(true);
  };

  const openEditModal = (supplier) => {
    setEditingSupplier(supplier);
    setFormData({
      name: supplier.name,
      email: supplier.email,
      phone: supplier.phone,
      address: supplier.address,
    });
    setShowModal(true);
  };

  const handleSubmit = async () => {
    try {
      if (editingSupplier) {
        await axios.put(`/api/suppliers/${editingSupplier.id}`, formData);
      } else {
        await axios.post("/api/suppliers", formData);
      }

      setShowModal(false);
      fetchSuppliers();
    } catch (err) {
      alert(err.response?.data?.message || "Something went wrong");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this supplier?")) return;

    await axios.delete(`/api/suppliers/${id}`);
    fetchSuppliers();
  };

  return (
    <div className="p-6">
      <div className="flex justify-between mb-6">
        <h1 className="text-3xl font-bold text-blue-600">
          Suppliers Management
        </h1>

        <button
          onClick={openAddModal}
          className="bg-green-600 text-white px-4 py-2 rounded"
        >
          + Add Supplier
        </button>
      </div>

      <table className="w-full bg-white shadow rounded">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-3">ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Address</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {suppliers.map((supplier) => (
            <tr key={supplier.id} className="text-center border-t">
              <td className="p-3">{supplier.id}</td>
              <td>{supplier.name}</td>
              <td>{supplier.email}</td>
              <td>{supplier.phone}</td>
              <td>{supplier.address}</td>
              <td className="space-x-2">
                <button
                  onClick={() => openEditModal(supplier)}
                  className="bg-blue-500 text-white px-3 py-1 rounded"
                >
                  Edit
                </button>

                <button
                  onClick={() => handleDelete(supplier.id)}
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
              {editingSupplier ? "Edit Supplier" : "Add Supplier"}
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
              type="email"
              placeholder="Email"
              className="w-full mb-3 p-2 border rounded"
              value={formData.email}
              onChange={(e) =>
                setFormData({ ...formData, email: e.target.value })
              }
            />

            <input
              type="text"
              placeholder="Phone"
              className="w-full mb-3 p-2 border rounded"
              value={formData.phone}
              onChange={(e) =>
                setFormData({ ...formData, phone: e.target.value })
              }
            />

            <textarea
              placeholder="Address"
              className="w-full mb-3 p-2 border rounded"
              value={formData.address}
              onChange={(e) =>
                setFormData({ ...formData, address: e.target.value })
              }
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
                {editingSupplier ? "Update" : "Add"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Suppliers;