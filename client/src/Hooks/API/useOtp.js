import React, { useEffect, useState } from "react";
import { useUser } from "../../UserContext";

const useOtp = () => {
  const { user, token } = useUser();

  const [data, setData] = useState("");
  const [status, setStatus] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const fetchRequest = async () => {
    setLoading(true);
    try {
      const res = await fetch(`${process.env.REACT_BASE_URL}request-otp/${user}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        }
      });
      setStatus(res.status);

      if (res.ok) {
        res.json();
        setData(res);
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRequest();
  }, []);

  return {
    data,
    status,
    loading,
    error
  }
};

export default useOtp;
