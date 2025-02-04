import React, { useEffect, useState } from "react";
import { useUser } from "../../UserContext";

const useDelete = () => {
  const { user, token } = useUser();

  const [status, setStatus] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const fetchRequest = async (url) => {
    try {
      const res = await fetch(url, {
        method: "DELETE",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        }});

      setStatus(res.status);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const combineUrlAndFetch = () => {
    setLoading(true);
    const url = `${process.env.REACT_APP_BASE_URL}${props.URL}/${user}`;
    fetchRequest(url, body);
  };

  useEffect(() => {
    combineUrlAndFetch();
  }, []);

  return {
    status,
    loading,
    error,
  };
};

export default useDelete;
