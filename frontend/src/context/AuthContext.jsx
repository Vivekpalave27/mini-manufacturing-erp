import { createContext, useContext, useState } from "react";
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [role, setRole] = useState(null);

  const login = (token) => {
    localStorage.setItem("token", token);

    const decoded = jwtDecode(token);
    setRole(decoded.role);   // 🔥 gets "ADMIN"
  };

  const logout = () => {
    localStorage.removeItem("token");
    setRole(null);
  };

  return (
    <AuthContext.Provider value={{ role, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};