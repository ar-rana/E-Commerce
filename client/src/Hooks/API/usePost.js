import React, { useEffect, useState } from "react";
import { useUser } from "../UserContext";

const usePost = (URL, reqBody) => { // unused
  const { user, token } = useUser();

  const [data, setData] = useState("");
  const [error, setError] = useState("");
  const [status, setStatus] = useState("");
  const [loading, setLoading] = useState(false);

  const postRequest = async () => {
    setLoading(true);
    try {
      const res = await fetch(`${process.env.REACT_APP_BASE_URL}${URL}/${user}`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(reqBody),
      });
      setStatus(res.status);

      if (res.ok) {
        const response = await res.json();
        setData(response);
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    postRequest();
  }, [URL, reqBody]);

  return {
    data,
    status,
    loading,
    error
  }
};

export default usePost;
