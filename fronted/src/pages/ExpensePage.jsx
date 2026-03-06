import { useEffect, useState } from "react";
import axios from "../api/axiosConfig";

const ExpensePage = () => {
  const [expenses, setExpenses] = useState([]);
  const [formData, setFormData] = useState({
    category: "",
    amount: "",
    description: "",
    expenseDate: ""
  });

  const [selectedFile, setSelectedFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [uploadingId, setUploadingId] = useState(null);
  const [error, setError] = useState("");

  // Fetch Expenses
  const fetchExpenses = async () => {
    try {
      const response = await axios.get("/api/expenses");
      setExpenses(response.data);
    } catch (err) {
      console.error("Error fetching expenses");
    }
  };

  useEffect(() => {
    fetchExpenses();
  }, []);

  // Create Expense
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.category || !formData.amount || !formData.expenseDate) {
      setError("Category, Amount and Date are required.");
      return;
    }

    if (Number(formData.amount) <= 0) {
      setError("Amount must be greater than 0.");
      return;
    }

    try {
      setLoading(true);
      setError("");

      await axios.post("/api/expenses", {
        category: formData.category,
        amount: Number(formData.amount),
        description: formData.description,
        expenseDate: formData.expenseDate
      });

      setFormData({
        category: "",
        amount: "",
        description: "",
        expenseDate: ""
      });

      fetchExpenses();
    } catch (err) {
      setError(err.response?.data?.message || "Error creating expense");
    } finally {
      setLoading(false);
    }
  };

  // Delete Expense
  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this expense?"))
      return;

    try {
      await axios.delete(`/api/expenses/${id}`);
      fetchExpenses();
    } catch (err) {
      alert("Error deleting expense");
    }
  };

  // Upload Receipt
  const handleUpload = async (id) => {
    if (!selectedFile) {
      alert("Please select a file first.");
      return;
    }

    const data = new FormData();
    data.append("file", selectedFile);

    try {
      setUploadingId(id);

      await axios.post(`/api/expenses/${id}/receipt`, data, {
        headers: { "Content-Type": "multipart/form-data" }
      });

      setSelectedFile(null);
      fetchExpenses();
    } catch (err) {
      alert("Upload failed");
    } finally {
      setUploadingId(null);
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">Expense Management</h1>

      {/* Create Expense Form */}
      <form onSubmit={handleSubmit} className="bg-white p-6 rounded shadow mb-6">
        <div className="grid grid-cols-4 gap-4">
          <input
            type="text"
            placeholder="Category"
            className="border p-2 rounded"
            value={formData.category}
            onChange={(e) =>
              setFormData({ ...formData, category: e.target.value })
            }
          />

          <input
            type="number"
            placeholder="Amount"
            className="border p-2 rounded"
            value={formData.amount}
            onChange={(e) =>
              setFormData({ ...formData, amount: e.target.value })
            }
          />

          <input
            type="date"
            className="border p-2 rounded"
            value={formData.expenseDate}
            onChange={(e) =>
              setFormData({ ...formData, expenseDate: e.target.value })
            }
          />

          <input
            type="text"
            placeholder="Description"
            className="border p-2 rounded"
            value={formData.description}
            onChange={(e) =>
              setFormData({ ...formData, description: e.target.value })
            }
          />
        </div>

        {error && (
          <p className="text-red-500 text-sm mt-2">{error}</p>
        )}

        <button
          type="submit"
          disabled={loading}
          className={`mt-4 px-4 py-2 rounded text-white ${
            loading
              ? "bg-gray-400 cursor-not-allowed"
              : "bg-blue-600 hover:bg-blue-700"
          }`}
        >
          {loading ? "Saving..." : "Create Expense"}
        </button>
      </form>

      {/* Expense List */}
      <table className="w-full bg-white shadow rounded">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-3 text-left">Category</th>
            <th className="p-3 text-left">Amount</th>
            <th className="p-3 text-left">Date</th>
            <th className="p-3 text-left">Description</th>
            <th className="p-3 text-left">Receipt</th>
            <th className="p-3 text-left">Action</th>
          </tr>
        </thead>

        <tbody>
          {expenses.map((expense) => (
            <tr key={expense.id} className="border-t">
              <td className="p-3">{expense.category}</td>
              <td className="p-3 text-green-600 font-semibold">
                ₹ {expense.amount?.toLocaleString()}
              </td>
              <td className="p-3">{expense.expenseDate}</td>
              <td className="p-3">{expense.description}</td>

              <td className="p-3">
                {expense.receiptPath ? (
                  <a
                    href={`http://localhost:8080/${expense.receiptPath}`}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-blue-600 underline"
                  >
                    View Receipt
                  </a>
                ) : (
                  <div>
                    <input
                      type="file"
                      onChange={(e) =>
                        setSelectedFile(e.target.files[0])
                      }
                    />
                    <button
                      onClick={() => handleUpload(expense.id)}
                      disabled={uploadingId === expense.id}
                      className="bg-green-600 text-white px-2 py-1 mt-2 rounded"
                    >
                      {uploadingId === expense.id
                        ? "Uploading..."
                        : "Upload"}
                    </button>
                  </div>
                )}
              </td>

              <td className="p-3">
                <button
                  onClick={() => handleDelete(expense.id)}
                  className="bg-red-500 text-white px-3 py-1 rounded"
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ExpensePage;