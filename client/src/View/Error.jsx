import React from "react";
import pagenotfound from "../Assets/pagenotfound.svg";
import Footer from "../Components/Footer";

const Error = () => {
  return (
    <div className="errorPage">
      <div className="error-container">
        <img src={pagenotfound} alt="Page Not Found" />
        <h1 className="heading3">Page Not Found</h1>
      </div>
      <Footer />
    </div>
  );
};

export default Error;
