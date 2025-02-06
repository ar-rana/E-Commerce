import React, { useEffect, useState } from "react";
import { useUser } from "../../UserContext";

const useDelete = (url) => { // unused
  const { user, token } = useUser();

  const [status, setStatus] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const deleteRequest = async () => {
    setLoading(true);
    try {
      const res = await fetch(`${process.env.REACT_APP_BASE_URL}${url}/${user}`, {
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

  useEffect(() => {
    fetchRequest();
  }, [user, token, url]);

  return {
    status,
    loading,
    error,
  };
};

export default useDelete;
