import { useNavigate } from "react-router-dom";

const Login = () => {
  const navigate = useNavigate();

  const handleLogin = () => {
    // simulate backend JWT token
    localStorage.setItem("token", "fake-jwt-token");
    navigate("/dashboard");
  };

  return (
    <div style={{ padding: "40px" }}>
      <h1>Login Page</h1>
      <button
        onClick={handleLogin}
        style={{
          padding: "10px 20px",
          marginTop: "20px",
          cursor: "pointer"
        }}
      >
        Login
      </button>
    </div>
  );
};

export default Login;