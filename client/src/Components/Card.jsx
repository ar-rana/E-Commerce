import React from "react";
import Button2 from "./Buttons/Button2";

const Card = () => {
  return (
    <div className="card">
      <div className="img-container">
        <img src="https://picsum.photos/200/300" alt="img" />
      </div>
      <div className="card-info">
        <span>Some Name</span>
        <div className="card-price">
          <span style={{ textDecoration: "line-through", color: "gray" }}>
            ₹ XX
          </span>
          <p>₹XXXX</p>
        </div>
        <Button2 text={"Checkout"} />
      </div>
    </div>
  );
};

export default Card;
