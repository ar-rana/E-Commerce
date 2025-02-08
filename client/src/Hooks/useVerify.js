import { useEffect, useState } from "react";
import useGet from "./API/useGet";
import { useUser } from "../UserContext";
import { useLoginContext } from "./LoginContext";

const useVerify = () => {
  const [verified, setVerified] = useState(true);

  const { user, setUser, setToken } = useUser();
  const { setmodalOpen } = useLoginContext();
  const { data, loading, status } = useGet("user/view");

  const logout = () => {
    localStorage.removeItem(process.env.REACT_APP_TOKEN_KEY);
    localStorage.removeItem(process.env.REACT_APP_USER_KEY);
    setToken(null);
    setUser(null);
    setTimeout(() => window.location.reload, 500);
  }

  useEffect(() => {
    if (status === 200 && data?.identifier === user) {
      setVerified(true);
      setmodalOpen(false);
    } else {
      if (status === 403) {
        logout();
      }
      setVerified(false);
    }
  }, [status, user, loading]);
  return {
    verified,
    setVerified,
  };
};

export default useVerify;
