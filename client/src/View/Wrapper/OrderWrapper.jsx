import React, { useEffect } from "react";
import Orders from "../Orders";
import { useUser } from "../../UserContext";
import { useLoginContext } from "../../Hooks/LoginContext";
import useVerify from "../../Hooks/useVerify";
import loadingSvg from "../../Assets/loadingSvg.svg";

const OrderWrapper = () => {
  const { user, token } = useUser();
  const { modalOpen, setmodalOpen } = useLoginContext();

  const { verified } = useVerify();

  const checkAuthority = () => {
    if (!user || !token || !verified) {
      setmodalOpen(true);
    }
  };
  useEffect(() => {
    setTimeout(checkAuthority, 500);
    if (verified) setmodalOpen(false);
  }, [user, token, verified, modalOpen]);

  if (user != null && token != null && verified) {
    return <Orders />;
  } else {
    return <img className="loading-image" src={loadingSvg} alt="Loading..." />;
  }
};

export default OrderWrapper;
