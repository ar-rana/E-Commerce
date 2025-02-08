import React, { useEffect, useState } from "react";
import ProductView from "../Components/ProductView";
import Footer from "../Components/Footer";
import Empty from "../Components/Empty";
import OrderCard from "../Components/OrderCard";
import useGet from "../Hooks/API/useGet";

const Wishlist = () => {
  const [empty, setEmpty] = useState(false);
  const [data, setData] = useState([]);
  
  const { data: products, status, loading } = useGet("list/WISHLIST");

  const handleDelete = (productId) => {
    const idx = data.findIndex((ele) => ele.productId === productId);
    data.splice(idx, 1);
    setData([...data]);
  };

  useEffect(() => {
    if (status === 404) {
      setEmpty(true);
    } else {
      setEmpty(false);
    }
    if (products) {
      setData(products);
    }
  }, [status, products]);
  return (
    <div className="wishlist">
      <div className="cart">
        <div className="main-cart">
          <div className="cart-products">
            {data?.map((obj, i) => (
              <OrderCard key={i} id={obj.productId} wish={true} product={obj} handleDelete={handleDelete} />
            ))}
            {empty ? <Empty /> : ""}
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
