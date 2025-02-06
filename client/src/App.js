import React from "react";
import "./index.css";
import Navbar from "./Components/Navbar.jsx";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./View/Home.jsx";
import Contact from "./View/Contact.jsx";
import Cart from "./View/Cart.jsx";
import Error from "./View/Error.jsx";
import Search from "./View/Search.jsx";
import ProductPage from "./View/ProductPage.jsx";
import Orders from "./View/Orders.jsx";
import { LoginContext } from "./Hooks/LoginContext.js";
import WishlistWrapper from "./View/Wrapper/WishlistWrapper.jsx";
import OrderWrapper from "./View/Wrapper/OrderWrapper.jsx";

function App() {
  return (
    <BrowserRouter>
      <LoginContext>
        <Navbar />
        <Routes>
          <Route exact path="/" element={<Home />} />
          <Route exact path="/connectwithus" element={<Contact />} />
          <Route exact path="/cart" element={<Cart />} />
          <Route exact path="/wishlist" element={<WishlistWrapper />} />
          <Route exact path="/search" element={<Search />} />
          <Route exact path="/product/:productId" element={<ProductPage />} />
          {/* <Route exact path="/product" element={<ProductPage />} /> */}
          <Route exact path="/orders" element={<OrderWrapper />} />
          <Route exact path="*" element={<Error />} />
        </Routes>
      </LoginContext>
    </BrowserRouter>
  );
}

export default App;
