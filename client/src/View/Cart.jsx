import React, { useEffect, useState } from "react";
import OrderCard from "../Components/OrderCard";
import Button3 from "../Components/Buttons/Button3";
import Productview from "../Components/ProductView.jsx";
import Footer from "../Components/Footer.jsx";
import Empty from "../Components/Empty.jsx";
import Button2 from "../Components/Buttons/Button2.jsx";
import UserInfoForm from "../Components/Modals/UserInfoForm.jsx";
import CustomerInfo from "../Components/CustomerInfo.jsx";
import useGet from "../Hooks/API/useGet.js";

const Cart = () => {
  const [total, setTotal] = useState(0);
  const [open, setOpen] = useState(false);
  const [empty, setEmpty] = useState(false);
  const [data, setData] = useState([]);
  const [productIds, setProductIds] = useState([]);

  const [customers, setCustomers] = useState([]);
  const [sendingto, setSendingto] = useState();

  const { data: products, status, loading } = useGet("list/CART");

  const toggleMenu = () => {
    setOpen((prev) => !prev);
  };

  const deleteCustomer = (number) => {
    if (customers.length > 0) {
      const newList = customers.filter((obj) => obj.number !== number);
      setCustomers(newList);
      localStorage.setItem("customers", JSON.stringify(newList));
    }
    setSendingto("");
  };

  const handleDelete = (productId) => {
    const idx = data.findIndex((ele) => ele.productId === productId);
    data.splice(idx, 1);
    setData([...data]);
  };

  useEffect(() => {
    const list = localStorage.getItem("customers");
    if (list) {
      setCustomers(JSON.parse(list));
    }
    if (status === 404 || status === 403) {
      setEmpty(true);
    } else {
      setEmpty(false);
    }

    if (products) {
      setData(products);
      setProductIds([]);
      setTotal(0);
      setProductIds(products.map(product => product.productId));
      setTotal(products.reduce((total, ele) => total + ele.currentPrice, 0)); //reduce returns single val without changing array
    }
  }, [open, customers.length, products, status]);

  return (
    <div className="cart">
      <div className="main-cart">
        <div className="cart-products">
          {data?.map((obj, i) => (
            <OrderCard key={i} id={obj.productId} product={obj} handleDelete={handleDelete}/>
          ))}
          {empty ? <Empty /> : ""}
        </div>
        <div className="cart-info">
          <div className="checkout">
            <h2>Total: ₹{total}</h2>
            <Button3 text={"Buy Now"} />
          </div>
          <div className="user-info">
            <h3 className="message">Customers: </h3>
            {customers.length > 0 ? (
              <form className="">
                {customers.map((obj, i) => (
                  <div
                    key={i}
                    style={{ display: "flex", gap: "10px" }}
                    onChange={(e) => setSendingto(e.target.value)}
                    required
                  >
                    <input
                      type="radio"
                      id={i}
                      name="customer"
                      value={JSON.stringify(obj)}
                    />
                    <label htmlFor={i} style={{ width: "100%" }}>
                      <CustomerInfo
                        name={obj.name}
                        number={obj.number}
                        email={obj.email}
                        address={obj.address}
                        deleteCustomer={deleteCustomer}
                      />
                    </label>
                  </div>
                ))}
              </form>
            ) : (
              <span>No Customers, Please add a customer to proceed</span>
            )}
            <Button2 text={"Add person"} onClick={toggleMenu} />
            <UserInfoForm open={open} setOpen={setOpen} />
          </div>
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
