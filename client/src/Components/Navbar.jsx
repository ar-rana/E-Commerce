import React, { useState } from "react";

const Navbar = () => {
  const [open, setOpen] = new useState(false);

  const toggleMenu = () => {
    setOpen((prev) => !prev);
  }
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
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"
            />
          </svg>
          <input className="search-input" type="text" placeholder="Search..." />
        </div>
        <div className="nav-menu">
          <ul className="nav-options">
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
        <button onClick={toggleMenu} className="menu-pop">
          <svg
            class="w-5 h-5"
            aria-hidden="true"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 17 14"
          >
            <path
              stroke="currentColor"
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M1 1h15M1 7h15M1 13h15"
            />
          </svg>
        </button>
      </nav>
      <div className={`responsive-menu ${open? "active": ""}`}>
        <ul className="nav-options">
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
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"
          />
        </svg>
        <input className="search-input" type="text" placeholder="Search..." />
      </div>
    </>
  );
};

export default Navbar;
