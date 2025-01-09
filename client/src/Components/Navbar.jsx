import React, { useState } from "react";
import { Link } from "react-router-dom";
import Button1 from "./Buttons/Button1";

const Navbar = () => {
  const [open, setOpen] = new useState(false);

  const toggleMenu = () => {
    setOpen((prev) => !prev);
  };
  return (
    <>
      <nav className="nav-container">
        <div className="nav-name">
          <h2>E-Commerce</h2>
        </div>
        <div className="nav-search">
          <svg
            className="search-icon"
            aria-hidden="true"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 20 20"
          >
            <path
              stroke="currentColor"
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"
            />
          </svg>
          <input className="search-input" type="text" placeholder="Search..." />
        </div>
        <div className="nav-menu">
          <ul className="nav-options">
            <li className="underline">
              <a href="/">Home</a>
            </li>
            <li className="underline">
              <a href="/connectwithus">Contact</a>
            </li>
            <li className="underline">
              <Link to="/cart">
                <span
                  style={{ color: "#b4846c", fontSize: "24px" }}
                  className="fa fa-shopping-cart"
                ></span>
              </Link>
            </li>
            <li className="dropdown">
              <Link to="#">
                <span
                  style={{ color: "#b4846c", fontSize: "24px" }}
                  className="fa fa-user-circle-o"
                ></span>
              </Link>
              <ul className="dropdown-menu">
                <li className="underline">
                  <a href="/wishlist">Wishlist</a>
                </li>
                <li>
                  <Button1 text={"Logout"} />
                </li>
              </ul>
            </li>
          </ul>
        </div>
        <button onClick={toggleMenu} className="menu-pop">
          <svg
            className="w-5 h-5"
            aria-hidden="true"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 17 14"
          >
            <path
              stroke="currentColor"
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M1 1h15M1 7h15M1 13h15"
            />
          </svg>
        </button>
      </nav>
      <div className={`responsive-menu ${open ? "active" : ""}`}>
        <ul className="nav-options">
          <li className="underline">
            <a href="/">Home</a>
          </li>
          <li className="underline">
            <a href="/connectwithus">Contact</a>
          </li>
          <li className="underline">
            <a href="/cart">Cart</a>
          </li>
          <li className="underline">
            <a href="#">WishList</a>
          </li>
          <li>
            <Button1 text={"Logout"} />
          </li>
        </ul>
      </div>
      <div className="responsive-search">
        <svg
          className="search-icon"
          aria-hidden="true"
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 20 20"
        >
          <path
            stroke="currentColor"
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth="2"
            d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"
          />
        </svg>
        <input className="search-input" type="text" placeholder="Search..." />
      </div>
    </>
  );
};

export default Navbar;
