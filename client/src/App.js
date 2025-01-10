import React from "react";
import "./index.css";
import Navbar from "./Components/Navbar.jsx";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./View/Home.jsx";
import Contact from "./View/Contact.jsx";
import Cart from "./View/Cart.jsx";
import Error from "./View/Error.jsx";
import Wishlist from "./View/Wishlist.jsx";
import Search from "./View/Search.jsx";
import ProductPage from "./View/ProductPage.jsx"

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route exact path="/" element={<Home />} />
        <Route exact path="/connectwithus" element={<Contact />} />
        {/* <Route exact path="/cart:user_id" element={<Cart />} /> */}
        <Route exact path="/cart" element={<Cart />} />
        <Route exact path="/wishlist" element={<Wishlist />} />
        {/* <Route exact path="/search:id" element={<Search />} /> */}
        <Route exact path="/search" element={<Search />} />
        {/* <Route exact path="/product:id" element={<ProductPage />} /> */}
        <Route exact path="/product" element={<ProductPage />} />
        <Route exact path="*" element={<Error />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
