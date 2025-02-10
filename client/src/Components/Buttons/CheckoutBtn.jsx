import React, { useEffect, useState } from "react";
import { useUser } from "../../UserContext.js";
import logo1 from "../../Assets/logo1.png";

const CheckoutBtn = (props) => {
  const { user, token } = useUser();
  const [id, setId] = useState(null);
  const [total, setTotal] = useState(null);
  const [ref, setRef] = useState(null);

  const [rzpPaymentId, setRzpPaymentId] = useState(null);
  const [rzpSign, setRzpSign] = useState(null);


  const loadRzpScript = (src) => {
    return new Promise((resolve) => {
      const script = document.createElement("script");
      script.src = src;
      script.onload = () => {
        resolve(true);
      };
      script.onerror = () => {
        resolve(false);
      };
      document.body.appendChild(script);
    });
  };

  const onClickHandler = async () => {
    if (!props.data) {
      alert("Please add a customer.");
      return;
    }
    const data = await JSON.parse(props.data);
    payment(data);
  }

  const payment = async (data) => {
    const res = await loadRzpScript(
      "https://checkout.razorpay.com/v1/checkout.js"
    );
    if (!res) {
      alert("Failed to load Script. Please check you internet connection.");
      return;
    }

    fetchOrderId(data);
  };

  const fetchOrderId = async (data) => {
    try {
      const res = await fetch(
        `${process.env.REACT_APP_BASE_URL}orders/new/${user}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            address: data.address,
            contact: data.number,
            name: data.name,
          }),
        }
      );
      console.log(res.status);

      if (res.ok) {
        const response = await res.json();
        console.log(response);
        setId(response.id);
        setRef(response.ref);
        setTotal(response.total);

        const options = {
          key: `${process.env.REACT_APP_PAYMENT_ID}`,
          amount: response.total,
          currency: "INR",
          name: "E-Commerce",
          description: "Test Transaction",
          image: logo1,
          order_id: response.id,
          handler: async function (response) {
            const data = {
              orderCreationId: id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpayOrderId: response.razorpay_order_id,
              razorpaySignature: response.razorpay_signature,
            };
            setRzpPaymentId(data.razorpayPaymentId);
            setRzpSign(data.razorpaySignature);

            successHandler();
          },
          prefill: {
            email: user,
          },
          theme: {
            color: "#d7c0ae",
          },
        };

        const paymentObject = new window.Razorpay(options);
        paymentObject.open();
      } else {
        alert("Order placement failed, please try again.");
      }
    } catch (err) {
      console.log(err.message);
    }
  };

  const successHandler = async () => {
    try {
      const res = await fetch(
        `${process.env.REACT_APP_BASE_URL}orders/success/${user}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            razorpay_payment_id: rzpPaymentId,
            razorpay_order_id: id,
            razorpay_signature: rzpSign,
            referenceId: ref
          }),
        }
      );

      if (res.ok) {
        window.location.href("/");
        alert("Payment Successfull, you will be receiving you receipt shortly.");
      }
    } catch(ex) {
      console.log(ex.message);
    }
  }
  
  return (
    <>
      <button className="button3" onClick={onClickHandler}>
        <span>Buy Now</span>
      </button>
    </>
  );
};

export default CheckoutBtn;
