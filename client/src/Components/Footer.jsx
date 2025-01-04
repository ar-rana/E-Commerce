import React from "react";
import Button3 from "./Buttons/Button3";

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
              <a href="#">Home</a>
            </li>
            <li>
              <a href="#">About</a>
            </li>
            <li>
              <a href="#">Contact</a>
            </li>
          </ul>
        </div>
        <div className="footer-socials">
          <div className="fa fa-linkedin"></div>
          <div className="fa fa-github"></div>
          <div>
            <Button3 text={"Back to top"} onClick={scrollToTop}/>
          </div>
        </div>
      </footer>
    </>
  );
};

export default Footer;
