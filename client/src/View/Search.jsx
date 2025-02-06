import React, { useEffect, useState } from "react";
import Card from "../Components/Card.jsx";
import Button1 from "../Components/Buttons/Button1.jsx";
import Footer from "../Components/Footer.jsx";
import { useLocation, useNavigate } from "react-router-dom";
import usePublicApi from "../Hooks/API/usePublicApi.js";
import loadingSvg from "../Assets/loadingSvg.svg";

const Search = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const query = location.state?.query || "";
  const category = location.state?.category || "";
  const [data, setData] = useState([]);
  const [noResult, setNoResult] = useState(false);

  const { data: productsByCategory, loading } = usePublicApi( category ? `getProduct/${category}`: null);

  const fetchSearch = async () => {
    try {
      const res = await fetch(`${process.env.REACT_APP_BASE_URL}public/search`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          query: query,
        })
      });
      console.log(res.status);

      if (res.ok) {
        setNoResult(false);
        const resJson = await res.json();
        setData(resJson);
        console.log("res: ", res);
      } else if (res.status !== 200) {
        setNoResult(true);
      }
    } catch (e) {
      console.log(e.message);
    }
  }

  console.log("category: ", category);
  console.log("query: ", query);

  useEffect(() => {
    if (category) {
      setData(productsByCategory);
    } else {
      fetchSearch();
    }
  }, [query, productsByCategory]);

  return (
    <div className="search">
      <div className="search-container">
        <div className="filter">
          <h2 className="">Filters</h2>
          <form action="" style={{ paddingLeft: "8px" }}>
            <p style={{ fontWeight: "500" }}>Category:</p>
            <input
              type="radio"
              id="home_decore"
              name="category"
              value="home_decore"
            />
            <label htmlFor="home_decore">Home Decore</label>
            <br />
            <input
              type="radio"
              id="outdoor_decore"
              name="category"
              value="outdoor_decore"
            />
            <label htmlFor="outdoor_decore">Outdoor Decore</label>
            <br />
            <input
              type="radio"
              id="aesthetic"
              name="category"
              value="aesthetic"
            />
            <label htmlFor="aesthetic">Aesthetic</label>
            <br />
            <br />
            <p style={{ fontWeight: "500" }}>Price:</p>
            <input type="radio" id="h_to_l" name="price" value="h_to_l" />
            <label htmlFor="h_to_l">High to Low</label>
            <br />
            <input type="radio" id="l_to_h" name="price" value="l_to_h" />
            <label htmlFor="l_to_h">Low to High</label>
            <br />
            <br />
            <Button1 text="Submit" type="submit" />
          </form>
        </div>
        <div className="search-items">
          {loading ? (
            <img className="loading-image" style={{ top: "12rem"}}src={loadingSvg} alt="Loading..." />
          ): ""}
          {noResult ? (
            <h2 style={{ marginTop: "2rem"}}>No Product Found 🥺</h2>
          ) : data?.map((product) => (
            <Card key={product.productId} id={product.productId} product={product} onClick={() => navigate(`../product/${product.productId}`)}/>
          ))}
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Search;
