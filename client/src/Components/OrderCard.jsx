import React from "react";

const OrderCard = () => {
  const data = {
    name: "Some name for the item",
    base: "XXXX",
    price: "XXX",
    category: "Home Decore",
  };
  return (
    <>
      <div className="order-card">
        <img src="https://picsum.photos/200" alt="image"></img>
        <div className="order-info">
          <span>{data.name}</span>
          <span style={{ color: "gray" }}>{data.category}</span>
          <div className="card-price" style={{gap: '10%'}}>
            <span style={{ textDecoration: "line-through", color: "gray" }}>
              ₹{data.base}
            </span>
            <p>₹{data.price}</p>
          </div>
        </div>
      </div>
    </>
  );
};

export default OrderCard;
