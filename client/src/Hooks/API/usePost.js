import React, { useEffect, useState } from "react";
import { useUser } from "../UserContext";

const usePost = (URL, reqBody) => {
  const { user, token } = useUser();

  const [data, setData] = useState("");
  const [error, setError] = useState("");
  const [status, setStatus] = useState("");
  const [loading, setLoading] = useState(false);

  const fetchRequest = async () => {
    setLoading(true);
    try {
      const res = await fetch(`${process.env.REACT_BASE_URL}${URL}/${user}`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(reqBody),
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
  }, [URL, reqBody]);

  return {
    data,
    status,
    loading,
    error
  }
};

export default usePost;
