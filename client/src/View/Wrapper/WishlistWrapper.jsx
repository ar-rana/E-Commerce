import { useEffect } from "react";
import { useUser } from "../../UserContext";
import { useLoginContext } from "../../Hooks/LoginContext";
import useVerify from "../../Hooks/useVerify";
import Wishlist from "../Wishlist.jsx";
import loadingSvg from "../../Assets/loadingSvg.svg";
import React from "react";

const WishlistWrapper = () => {
  const { user, token } = useUser();
  const { modalOpen, setmodalOpen } = useLoginContext();

  const { verified } = useVerify();

  const checkAuthority = () => {
    if ((!user || !token || !verified)) {
      setmodalOpen(true);
    }
  };
  useEffect(() => {
    setTimeout(checkAuthority, 500);
    if (verified) setmodalOpen(false);
  }, [user, token, verified, modalOpen]);

  if (user != null && token != null && verified) {
    return <Wishlist />;
  } else {
    return <img className="loading-image" src={loadingSvg} alt="Loading..." />;
  }
};

export default WishlistWrapper;
