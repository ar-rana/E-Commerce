import React from "react";
import ProductView from "../Components/ProductView";
import Footer from "../Components/Footer";
import Button3 from "../Components/Buttons/Button3";
import Empty from "../Components/Empty";
import OrderCard from "../Components/OrderCard";

const Wishlist = () => {
  const data = [{ id: "123" }, { id: "12323" }, { id: "12345" }];

  // call all items from this page then send data to component,
  // other wise you will have too many requests to backend
  return (
    <div>
      <div className="cart">
        <div className="main-cart">
          <div className="cart-products">
            {data.map((obj) => (
              <OrderCard key={obj.id} id={obj.id} wish={true} />
            ))}
            <Empty />
          </div>
        </div>
        <h2 className="heading">You may also like...</h2>
        <div className="spacing">
          <ProductView />
        </div>
        <Footer />
      </div>
    </div>
  );
};

export default Wishlist;
