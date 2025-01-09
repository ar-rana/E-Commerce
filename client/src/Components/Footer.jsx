import React from "react";
import Button2 from "./Buttons/Button2";

const Footer = () => {
  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  };
  return (
    <>
      <footer className="footer">
        <div className="footer-links">
          <ul>
            <li>
              <a href="/">Home</a>
            </li>
            <li>
              <a href="/connectwithus">Contact</a>
            </li>
          </ul>
        </div>
        <div className="footer-socials">
          <div className="fa fa-linkedin"></div>
          <div className="fa fa-github"></div>
          <div>
            <Button2 text={"Back to top"} onClick={scrollToTop}/>
          </div>
        </div>
      </footer>
    </>
  );
};

export default Footer;
