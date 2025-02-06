import React, { use, useEffect, useState } from "react";
import ProductView from "../Components/ProductView";
import Footer from "../Components/Footer";
import Card from "../Components/Card";
import Empty from "../Components/Empty";
import LeaveReview from "../Components/Modals/LeaveReview";
import useGet from "../Hooks/API/useGet";

const Orders = () => {
  const [open, setOpen] = useState(false);
  const [id, setID] = useState("");
  const [empty, setEmpty] = useState(false);

  const { data, status, loading } = useGet("orders/get");

  const openModel = (id) => {
    setOpen((prev) => !prev);
    setID(id);
  };

  useEffect(() => {
    if (!data) {
      setEmpty(true);
    } else {
      setEmpty(false);
    }
  }, [data, status]);
  return (
    <div className="orders">
      <br />
      <h2 className="heading underline">Your Orders</h2>
      <div className="order-items">
        <div className="search-items">
          {data?.map((obj) => (
            <Card
              key={obj.id}
              ordered={true}
              product={obj}
              onClick={() => openModel(obj.id)}
            />
          ))}
          {empty ? <Empty /> : ""}
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
