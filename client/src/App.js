import React from "react";
import "./index.css";
import Navbar from "./Components/Navbar";
import Footer from "./Components/Footer";
import Button1 from "./Components/Buttons/Button1";
import Button2 from "./Components/Buttons/Button2";
import Button3 from "./Components/Buttons/Button3";
import Card from "./Components/Card";
import CategoryCard from "./Components/CategoryCard";
import Reviews from "./Components/Reviews";

function App() {
  return (
    <>
    <Navbar />
    <CategoryCard />
    <Reviews />
    {/* <Button1 text={"Click MEEEEEE"}/>
    <Button2 text={"Click MEEEEEE"} />
    <Button3 text={"Back to top"}/>
    <Card /> */}
    {/* <Footer /> */}
    </>
  );
}

export default App;
