import React from "react";

const Footer = () => {
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
          <button className="">Back to top</button>
        </div>
      </footer>
    </>
  );
};

export default Footer;
