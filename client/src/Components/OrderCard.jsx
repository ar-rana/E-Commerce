import React from "react";
import useImage from "../Hooks/useImage";
import { useNavigate } from "react-router-dom";
import { useUser } from "../UserContext";
import { useLoginContext } from "../Hooks/LoginContext";

const OrderCard = (props) => {
  const { user, token } = useUser();
  const { url } = useImage(props.product?.thumbnail, props.product?.thumbnailType);
  const navigate = useNavigate();

  const { modalOpen, setmodalOpen } = useLoginContext();

  const toProduct = () => {
    navigate(`/product/${props.product?.productId}`);
  };

  const onDelete = (e) => {
    e.preventDefault();
    deleteRequest();
  }

  const moveToCart = (e) => {
    e.preventDefault();
    postRequest();
  }

  const postRequest = async () => {
    try {
      const res = await fetch(`${process.env.REACT_APP_BASE_URL}list/move/WISHLIST/CART`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          identifier: user,
          productId: props.product?.productId
        }),
      });
      console.log(res.status);
      console.log(modalOpen);
      if (res.status === 403) setmodalOpen(true);
      console.log(modalOpen);

      const response = await res.text();
      console.log("resp: " , response);
      if (response) {
        props.handleDelete(props.product?.productId);
      }
    } catch (err) {
      console.log(err.message);
    }
  };

  const deleteRequest = async () => {
    try {
      const res = await fetch(
        `${process.env.REACT_APP_BASE_URL}list/${props.wish ? "WISHLIST" : "CART"}/${props.product?.productId}/${user}`,
        {
          method: "DELETE",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      console.log(res.status);
      console.log(modalOpen);
      if (res.status === 403) setmodalOpen(true);
      console.log(modalOpen);

      if (res.ok) {
        props.handleDelete(props.product?.productId);
      }
    } catch (err) {
      console.log(err.message);
    }
  };

  return (
    <>
      <div className="order-card">
        <img src={url || null} alt="image" onClick={toProduct}></img>
        <div className="order-info">
          <span>{props.product?.name}</span>
          <span style={{ color: "gray" }}>{props.product?.category}</span>
          <div className="card-price" style={{ gap: "10%" }}>
            <span style={{ textDecoration: "line-through", color: "gray" }}>
              ₹{props.product?.basicPrice}
            </span>
            <p>₹{props.product?.currentPrice}</p>
          </div>
          <span
            style={{ top: "25px", color: "red", fontSize: "18px" }}
            className="fa fa-trash-o position-cart"
            title="Delete"
            onClick={(e) => onDelete(e)}
          ></span>
          {props.wish ? (
            <span
              style={{ top: "50px", fontSize: "18px" }}
              className="fa fa-plus-square position-cart"
              title="Add to Cart"
              onClick={(e) => moveToCart(e)}
            ></span>
          ) : (
            ""
          )}
        </div>
      </div>
    </>
  );
};

export default OrderCard;
