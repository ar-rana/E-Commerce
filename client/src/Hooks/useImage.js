import React, { useEffect, useState } from "react";

const useImage = (base64, type) => {
  const [url, setUrl] = useState("");

  useEffect(() => {
    const format = "data:image/" + type + ";base64," + base64;
    setUrl(format);
  }, [type, base64]);

  return {
    url,
  };
};

export default useImage;
