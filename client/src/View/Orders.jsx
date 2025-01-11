import React, { use, useState } from "react";
import ProductView from "../Components/ProductView";
import Footer from "../Components/Footer";
import Card from "../Components/Card";
import Empty from "../Components/Empty";
import LeaveReview from "../Components/Modals/LeaveReview";

const Orders = () => {
  const [open, setOpen] = useState(false);
  const [id, setID] = useState("");

  const openModel = (id) => {
    setOpen((prev) => !prev);
    setID(id);
  };

  const data = [
    { id: "123" },
    { id: "12323" },
    { id: "12345" },
    { id: "1222" },
  ];
  return (
    <div className="orders">
      <br />
      <h2 className="heading underline">Your Orders</h2>
      <div className="order-items">
        <div className="search-items">
          {data.map((obj) => (
            <Card
              key={obj.id}
              ordered={true}
              onClick={() => openModel(obj.id)}
            />
          ))}
          {/* <Empty /> */}
        </div>
      </div>
      <LeaveReview open={open} setOpen={setOpen} id={id} />
      <h2 className="heading underline">Continue Shopping...</h2>
      <ProductView />
      <Footer />
    </div>
  );
};

export default Orders;
