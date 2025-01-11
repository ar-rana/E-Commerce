import React from "react";
import Hero from "../Components/Hero";
import ProductView from "../Components/ProductView";
import CategoryCard from "../Components/CategoryCard";
import Footer from "../Components/Footer";

const Home = () => {
  return (
    <div className="home">
      <Hero />
      <h2 className="heading underline">Start Exploring From here...</h2>
      <div className="spacing">
        <ProductView />
      </div>
      <h2 className="heading underline">Shop Based on Category</h2>
      <div className="home-category">
        <CategoryCard text={"Home decore"} />
        <CategoryCard text={"Outdoor decore"} />
        <CategoryCard text={"Aesthetic Pieces"} />
      </div>
      <Footer />
    </div>
  );
};

export default Home;
