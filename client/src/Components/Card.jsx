import React, { useState } from "react";
import Button2 from "./Buttons/Button2.jsx";
import useImage from "../Hooks/useImage.js";
import Status from "../Components/Modals/Status.jsx";
import { useUser } from "../UserContext.js";

const Card = (props) => {
  const { url } = useImage(props.product?.thumbnail, props.product?.thumbnailType);
  const { user, token } = useUser();

  const [open, setOpen] = useState(false);
  const [order, setOrder] = useState(null);

  const getOrderStatus = async () => {
    try {
      const res = await fetch(
        `${process.env.REACT_APP_BASE_URL}orders/get/${user}?orderId=${props.product?.productId}`, 
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      if (res.ok) {
        const responce = await res.json();
        setOrder(responce);
        setOpen(true);
      }
    } catch (err) {
      console.log(err.message);
    }
  }

  return (
    <div className="card">
      <div className="img-container">
        <img src={url || null} alt="Product_Image" />
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
          <>
            <span>
              <Button2 text={"Leave a Review"} onClick={props.onClick} />
              <Button2 text={"Status"} onClick={getOrderStatus} />
            </span>
            <Status open={open} setOpen={setOpen} order={order}/>
          </>
        ) : (
          <Button2 text={"Checkout"} onClick={props.onClick} />
        )}
      </div>
    </div>
  );
};

export default Card;
