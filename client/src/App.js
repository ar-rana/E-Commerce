import React from "react";
import "./index.css";
import Navbar from "./Components/Navbar";
import Footer from "./Components/Footer";
import Button1 from "./Components/Buttons/Button1";
import Button2 from "./Components/Buttons/Button2";
import Button3 from "./Components/Buttons/Button3";

function App() {
  return (
    <>
    <Navbar />
    <Button1 text={"Click MEEEEEE"}/>
    <Button2 text={"Click MEEEEEE"} />
    <Button3 text={"Click MEEEEEE"}/>
    <Footer />
    </>
  );
}

export default App;
