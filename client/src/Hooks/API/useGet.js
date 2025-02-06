import React, { useEffect, useState } from "react";
import { useUser } from "../../UserContext";

const useGet = (url) => {
  const { user, token } = useUser();

  const [loading, setLoading] = useState(false);
  const [data, setData] = useState();
  const [error, setError] = useState("");
  const [status, setStatus] = useState("");

  const fetchRequest = async () => {
    setLoading(true);
    try {
      const res = await fetch(`${process.env.REACT_APP_BASE_URL}${url}/${user}`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      setStatus(res.status);

      if (res.ok) {
        const responce = await res.json();
        setData(responce);
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!url || !user || !token) return;
    fetchRequest();
  }, [url, user, token, status]);

  return {
    data,
    loading,
    error,
    status
  };
};

export default useGet;
