import React, { useState } from "react";
import Button2 from "./Buttons/Button2.jsx";
import useImage from "../Hooks/useImage.js";
import Status from "../Components/Modals/Status.jsx";
import { useUser } from "../UserContext.js";
import LeaveReview from "./Modals/LeaveReview.jsx";

const Card = (props) => {
  const { url } = useImage(props.product?.thumbnail || props.product?.product.thumbnail, 
    props.product?.thumbnailType || props.product?.product.thumbnailType);
  const { user, token } = useUser();

  const [open, setOpen] = useState(false);
  const [openreview, setOpenReview] = useState(false);

  const getOrderStatus = async () => {
    setOpen(true);
  };

  const reviewHandler = () => {
    setOpenReview(true);
  };

  return (
    <div className="card">
      <div className="img-container">
        <img src={url || null} alt="Product_Image" />
      </div>
      <div className="card-info">
        <span>{props.product?.name || props.product?.product.name}</span>
        <div className="card-price">
          <span style={{ textDecoration: "line-through", color: "gray" }}>
            ₹ {props.product?.basicPrice || props.product?.product.basicPrice}
          </span>
          <p>₹{props.product?.currentPrice || props.product?.product.currentPrice}</p>
        </div>
        {props.ordered ? (
          <>
            <span>
              <Button2 text={"Leave a Review"} onClick={reviewHandler} />
              <Button2 text={"Status"} onClick={getOrderStatus} />
            </span>
            <Status open={open} setOpen={setOpen} order={props.product} />
            <LeaveReview open={openreview} setOpen={setOpenReview} productId={props.product?.product.productId}/>
          </>
        ) : (
          <Button2 text={"Checkout"} onClick={props.onClick} />
        )}
      </div>
    </div>
  );
};

export default Card;
