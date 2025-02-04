import React from "react";
import Button2 from "./Buttons/Button2";
import useImage from "../Hooks/useImage";

const Card = (props) => {
  const { url } = useImage(props.product?.thumbnail, props.product?.thumbnailType);
  return (
    <div className="card">
      <div className="img-container">
        <img src={url} alt="Product_Image" />
      </div>
      <div className="card-info">
        <span>{props.product?.name}</span>
        <div className="card-price">
          <span style={{ textDecoration: "line-through", color: "gray" }}>
            ₹ {props.product?.basicPrice}
          </span>
          <p>₹{props.product?.currentPrice}</p>
        </div>
        {props.ordered ? (
          <Button2 text={"Leave a Review"} onClick={props.onClick}/>
        ) : (
          <Button2 text={"Checkout"} onClick={props.onClick}/>
        )}
      </div>
    </div>
  );
};

export default Card;
