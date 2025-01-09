import React from "react";
import "./index.css";
import Navbar from "./Components/Navbar";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./View/Home";
import Contact from "./View/Contact";
import Cart from "./View/Cart";
import Error from "./View/Error";

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route exact path="/" element={<Home />} />
        <Route exact path="/connectwithus" element={<Contact />} />
        {/* <Route exact path="/cart:user_id" element={<Cart />} /> */}
        <Route exact path="/cart" element={<Cart />} />
        <Route exact path="*" element={<Error />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
