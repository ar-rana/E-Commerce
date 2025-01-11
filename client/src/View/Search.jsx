import React from "react";
import Card from "../Components/Card.jsx";
import Button1 from "../Components/Buttons/Button1.jsx";
import Footer from "../Components/Footer.jsx";

const Search = () => {
  const data = [
    { id: "123" },
    { id: "12323" },
    { id: "12345" },
    { id: "1222" },
    { id: "11235" },
    { id: "1133" },
    { id: "23112" },
    { id: "7895" },
    { id: "44673" },
    { id: "0784" },
    { id: "05765" },
  ];
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
          {data.map((obj) => (
            <Card key={obj.id} id={obj.id} />
          ))}
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Search;
