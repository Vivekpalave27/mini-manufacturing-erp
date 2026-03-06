import { createContext, useState, useEffect } from "react";
import { jwtDecode } from "jwt-decode";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (token) {
      try {
        const decoded = jwtDecode(token);

        // 🔥 Check token expiry
        if (decoded.exp * 1000 > Date.now()) {
          setUser(decoded);
          localStorage.setItem("role", decoded.role); // ✅ store role
        } else {
          localStorage.removeItem("token");
          localStorage.removeItem("role");
          setUser(null);
        }
      } catch (error) {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        setUser(null);
      }
    }

    setLoading(false);
  }, []);

  const login = (token) => {
    localStorage.setItem("token", token);

    const decoded = jwtDecode(token);

    localStorage.setItem("role", decoded.role); // ✅ important
    setUser(decoded);

    return decoded;
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};