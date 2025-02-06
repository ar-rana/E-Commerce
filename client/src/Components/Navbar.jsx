import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Button1 from "./Buttons/Button1.jsx";
import SignIn from "./Modals/SignIn.jsx";
import { useUser } from "../UserContext.js";
import { useLoginContext } from "../Hooks/LoginContext.js";

const Navbar = () => {
  const { user, token, setUser, setToken } = useUser();
  const navigate = useNavigate();

  const [open, setOpen] = useState(false);
  const { modalOpen, setmodalOpen } = useLoginContext();
  const [search, setSearch] = useState("");

  const quickSearch = [
    "Home decore",
    "Outdoor decore",
    "Aesthetic Art",
  ];

  const toggleMenu = () => {
    setOpen((prev) => !prev);
  };

  const searchItem = (e) => {
    if (!search || search.trim() === "") return;
    console.log("Searching.....", search);
    navigate("/search", { state: { query: search }, replace: true });
  };

  const logout = () => {
    localStorage.removeItem(process.env.REACT_APP_TOKEN_KEY);
    localStorage.removeItem(process.env.REACT_APP_USER_KEY);
    setToken(null);
    setUser(null);
    setTimeout(() => window.location.reload, 500);
  }

  useEffect(() => {
    console.log("user: ", user, "token: ", token);
    setSearch("");
  }, [user, token]);

  return (
    <>
      <nav className="nav-container">
        <div className="nav-name">
          <a href="/">
            <h2>E-Commerce</h2>
          </a>
        </div>
        <div className="nav-search">
          <svg
            onClick={(e) => searchItem(e)}
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
          <input
            className="search-input"
            type="text"
            placeholder="Search..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <ul className="searchable-list">
            {quickSearch.map((item, i) => (
              <li key={i} onClick={() => setSearch(item)} value={item}>
                {item}
              </li>
            ))}
          </ul>
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
                  title="cart"
                  style={{ color: "#b4846c", fontSize: "24px" }}
                  className="fa fa-shopping-cart"
                ></span>
              </Link>
            </li>
            <li className="dropdown">
              <span
                style={{ color: "#b4846c", fontSize: "24px" }}
                className="fa fa-user-circle-o"
              ></span>
              <ul className="dropdown-menu">
                <li className="underline">
                  <a href="/wishlist">Wishlist</a>
                </li>
                <li className="underline">
                  <a href="/orders">Orders</a>
                </li>
                <li>
                  {user != null && token != null ? (
                    <Button1 text="Logout" onClick={logout}/>
                  ) : (
                    <Button1 text="Login" onClick={() => setmodalOpen((prev) => !prev)}/>
                  )}
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
            <a href="/wishlist">WishList</a>
          </li>
          <li className="underline">
            <a href="/orders">Orders</a>
          </li>
          <li>
            {user != null && token != null ? (
              <Button1 text="Logout" onClick={logout} />
            ) : (
              <Button1 text="Login" onClick={() => setmodalOpen((prev) => !prev)}/>
            )}
          </li>
        </ul>
      </div>
      <div className="responsive-search">
        <svg
          onClick={(e) => searchItem(e)}
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
        <input
          className="search-input"
          type="text"
          placeholder="Search..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <ul className="responsive-searchable searchable-list">
          <li onClick={() => setSearch("homeDecore")}>Home Decore</li>
          <li onClick={() => setSearch("outdoorDecore")}>Outdoor decore</li>
          <li onClick={() => setSearch("Aesthetic Art")}>Aesthetic Art</li>
        </ul>
      </div>
      <SignIn open={modalOpen} setOpen={setmodalOpen} />
    </>
  );
};

export default Navbar;
