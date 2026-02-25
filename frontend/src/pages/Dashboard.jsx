import { useEffect, useState } from "react";
import api from "../services/api";

const Dashboard = () => {
  const [items, setItems] = useState([]);

  useEffect(() => {
    const fetchItems = async () => {
      try {
        const response = await api.get("/items");
        console.log("Items response:", response.data);
        setItems(response.data);
      } catch (error) {
        console.log("API Error:", error);
      }
    };

    fetchItems();
  }, []);

  return (
    <div>
      <h1>Dashboard Page</h1>

      <h2>Items List:</h2>

      {items && items.length > 0 ? (
        <ul>
          {items.map((item) => (
            <li key={item.id}>
              {item.name} - ₹{item.price}
            </li>
          ))}
        </ul>
      ) : (
        <p>No items found</p>
      )}
    </div>
  );
};

export default Dashboard;