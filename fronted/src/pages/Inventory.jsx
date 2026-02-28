import { useEffect, useState } from "react";
import axios from "../api/axiosConfig";

function Inventory() {
  const [inventory, setInventory] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchInventory();
  }, []);

  const fetchInventory = async () => {
    try {
      const res = await axios.get("/api/inventory");
      setInventory(res.data);
    } catch (error) {
      console.error("Error fetching inventory", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="text-lg font-semibold">Loading inventory...</div>;
  }

  return (
    <div>
      <h1 className="text-3xl font-bold text-blue-600 mb-6">
        Inventory Overview
      </h1>

      <table className="w-full bg-white shadow rounded">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-3">Item Name</th>
            <th>SKU</th>
            <th>Current Stock</th>
          </tr>
        </thead>
        <tbody>
          {inventory.map((item) => (
            <tr
              key={item.id}
              className={`text-center border-t ${
                item.stock < 10 ? "bg-red-50" : ""
              }`}
            >
              <td className="p-3">{item.name}</td>
              <td>{item.sku}</td>
              <td
                className={`font-semibold ${
                  item.stock < 10 ? "text-red-600" : "text-green-600"
                }`}
              >
                {item.stock}
                {item.stock < 10 && " ⚠ LOW"}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Inventory;