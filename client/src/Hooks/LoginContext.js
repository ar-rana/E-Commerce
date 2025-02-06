import { createContext, useContext, useEffect, useState } from "react";

const LoginModalContext = createContext();

export const LoginContext = ({ children }) => {
  const [modalOpen, setmodalOpen] = useState(false);

  return (
    <LoginModalContext.Provider value={{ modalOpen, setmodalOpen }} >
      {children}
    </LoginModalContext.Provider> 
  )
};

export const useLoginContext = () => {
  return useContext(LoginModalContext);
}
