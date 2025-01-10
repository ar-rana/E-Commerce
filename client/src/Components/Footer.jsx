import React from "react";
import Button2 from "./Buttons/Button2";
import { Link } from "react-router-dom";

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
            <li>
              <a href="/cart">Cart</a>
            </li>
            <li>
              <a href="/wishlist">WishList</a>
            </li>
          </ul>
        </div>
        <div className="footer-socials">
          <a
            href="https://www.linkedin.com/in/-aryan-rana/"
            target="_blank"
            className="fa fa-linkedin"
          ></a>
          <a
            href="https://github.com/ar-rana"
            target="_blank"
            className="fa fa-github"
          ></a>
          <div>
            <Button2 text={"Back to top"} onClick={scrollToTop} />
          </div>
        </div>
      </footer>
    </>
  );
};

export default Footer;
