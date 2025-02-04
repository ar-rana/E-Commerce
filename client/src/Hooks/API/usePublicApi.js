import React, { useEffect, useState } from "react";

const usePublicApi = (url) => {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState();
  const [status, setStatus] = useState("");
  const [error, setError] = useState("");

  const fetchRequest = async () => {
    setLoading(true);
    try {
      const res = await fetch(`${process.env.REACT_APP_BASE_URL}public/${url}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
      setStatus(res.status);

      if (res.ok) {
        const responseData = await res.json();
        setData(responseData);
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };
  
  useEffect(() => {
    if (url == null) return; 
    fetchRequest(url);
  }, [url]);

  return {
    data,
    status,
    loading,
    error,
  };
};

export default usePublicApi;
