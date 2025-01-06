import React from "react";
import "./index.css";
import Navbar from "./Components/Navbar";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./View/Home";
import Contact from "./View/Contact";

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route exact path="/" element={<Home />} />
        <Route exact path="/connectwithus" element={<Contact />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
