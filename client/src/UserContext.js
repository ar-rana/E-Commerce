import { createContext, useContext, useEffect, useState } from "react";

export const UserContext = createContext();

export function UserProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);

  useEffect(() => {
    const user = localStorage.getItem("user");
    const token = localStorage.getItem("session_token");
    if (user) {
      setUser(user);
    }
    if (token) {
      setToken(token);
    }
  }, [user, token]);

  return (
    <UserContext.Provider value={{ user, token, setToken, setUser }}>
      {children}
    </UserContext.Provider>
  );
}

export function useUser() {
  return useContext(UserContext);
}
