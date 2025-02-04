import React from "react";
import Hero from "../Components/Hero";
import ProductView from "../Components/ProductView";
import CategoryCard from "../Components/CategoryCard";
import Footer from "../Components/Footer";
import homedecore from "../Assets/homedecore.png";
import outdoordecore from "../Assets/outdoordecore.png";
import aesthetic from "../Assets/aesthetic.png";
import { useNavigate } from "react-router-dom";

const Home = () => {
  const navigate = useNavigate();
  const onClickCategory = (category) => {
    navigate("/search", {state : {category : category}})
  }
  return (
    <div className="home">
      <Hero />
      <h2 className="heading underline">Start Exploring From here...</h2>
      <div className="spacing">
        <ProductView />
      </div>
      <h2 className="heading underline">Shop Based on Category</h2>
      <div className="home-category">
        <CategoryCard text={"Home decore"} url={homedecore} onClick={() => onClickCategory("homedecore")} />
        <CategoryCard text={"Outdoor decore"} url={outdoordecore} onClick={() => onClickCategory("outdoordecore")} />
        <CategoryCard text={"Aesthetic Pieces"} url={aesthetic} onClick={() => onClickCategory("aesthtic")} />
      </div>
      <Footer />
    </div>
  );
};

export default Home;
