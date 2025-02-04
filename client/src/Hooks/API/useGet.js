import React, { useEffect, useState } from "react";
import { useUser } from "../../UserContext";

const useGet = (url) => {
  const { user, token } = useUser();

  const [loading, setLoading] = useState(false);
  const [data, setData] = useState();
  const [error, setError] = useState("");

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
  }, [url]);

  return {
    data,
    loading,
    error
  };
};

export default useGet;
