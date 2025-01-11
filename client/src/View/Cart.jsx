import React, { useState } from "react";
import OrderCard from "../Components/OrderCard";
import Button3 from "../Components/Buttons/Button3";
import Productview from "../Components/ProductView.jsx";
import Footer from "../Components/Footer.jsx";
import Empty from "../Components/Empty.jsx";

const Cart = () => {
  const data = [{ id: "123" }, { id: "12323" }, { id: "12345" }];
  const [total, setTotal] = useState(0);

  // call all items from this page then send data to component, 
  // other wise you will have too many requests to backend
  return (
    <div className="cart">
      <div className="main-cart">
        <div className="cart-products">
          {data.map((obj) => (
            <OrderCard key={obj.id} id={obj.id} />
          ))}
          <Empty />
        </div>
        <div className="checkout">
          <h2>Total: ₹{total}</h2>
          <Button3 text={"Buy Now"} />
        </div>
      </div>
      <h2 className="heading">You may also like...</h2>
      <div className="spacing">
        <Productview />
      </div>
      <Footer />
    </div>
  );
};

export default Cart;
