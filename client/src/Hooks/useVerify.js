import { useEffect, useState } from "react";
import useGet from "./API/useGet";
import { useUser } from "../UserContext";
import { useLoginContext } from "./LoginContext";

const useVerify = () => {
  const [verified, setVerified] = useState(true);

  const { user } = useUser();
  const { setmodalOpen } = useLoginContext();
  const { data, loading, status } = useGet("user/view");

  useEffect(() => {
    if (status === 200 && data?.identifier === user) {
      setVerified(true);
      setmodalOpen(false);
    } else {
      setVerified(false);
    }
  }, [status, user, loading]);
  return {
    verified,
    setVerified,
  };
};

export default useVerify;
