import { useEffect, useState } from "react";
import axios from "../../api/axiosConfig";
import { useNavigate } from "react-router-dom";

const GRRList = () => {
  const [grrs, setGrrs] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchGRRs();
  }, []);

  const fetchGRRs = async () => {
    try {
      const res = await axios.get("/api/grr");
      setGrrs(res.data);
    } catch (error) {
      console.error("Error fetching GRRs:", error);
    }
  };

  return (
    <div className="p-6">
      <div className="flex justify-between mb-4">
        <h1 className="text-2xl font-bold text-blue-600">GRR</h1>

        <button
          onClick={() => navigate("/grr/create")}
          className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded"
        >
          + Create GRR
        </button>
      </div>

      <table className="w-full bg-white shadow rounded">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-2">GRR No</th>
            <th>Status</th>
            <th>Total</th>
            <th>Date</th>
          </tr>
        </thead>

        <tbody>
          {grrs.length === 0 ? (
            <tr>
              <td colSpan="4" className="text-center p-4">
                No GRR records found
              </td>
            </tr>
          ) : (
            grrs.map((grr) => (
              <tr key={grr.id} className="text-center border-t">
                <td className="p-2">{grr.grrNumber}</td>
                <td>{grr.status}</td>
                <td>₹ {grr.totalAmount}</td>
                <td>{grr.createdDate || "-"}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default GRRList;