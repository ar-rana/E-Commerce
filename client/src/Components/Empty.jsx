import React from "react";
import emptycart from "../Assets/emptycart.png";

const Empty = () => {
  return (
    <>
      <div className="order-card">
        <img src={emptycart} alt="image"></img>
        <div className="order-info">
          <span style={{ color: "gray", fontWeight: '800', fontSize: 'x-large'}}>This List is Empty</span>
        </div>
      </div>
    </>
  );
};

export default Empty;
