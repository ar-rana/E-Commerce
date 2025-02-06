import React, { useState } from "react";
import Modal from "react-modal";
import Button1 from "../Buttons/Button1.jsx";
import Button2 from "../Buttons/Button2.jsx";
import { useLoginContext } from "../../Hooks/LoginContext.js";
import { useUser } from "../../UserContext.js";

const SignIn = (props) => {
  const [email, setEmail] = useState("");
  const [otp, setOTP] = useState("");
  const [warning, setWarning] = useState("");
  const [disable, setDisable] = useState(true);
  const [needOTP, setNeedOTP] = useState(false);

  const [status, setStatus] = useState(null);

  const { setmodalOpen } = useLoginContext();
  const { setToken, setUser } = useUser();

  const validateEmail = (e) => {
    e.preventDefault();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (emailRegex.test(email)) {
        setWarning("");
        fetchOtpRequest();
        setTimeout(() => {
          setDisable(false);
        }, 30000);
    } else {
      setWarning("Invalid Email");
    }
  };

  const successfulLoginProtocol = (response) => {
    console.log(response);
    localStorage.setItem("user", email);
    localStorage.setItem("session_token", response);
    setToken(response);
    setUser(email);
    setOTP("");
    setEmail("");
    setNeedOTP(false);
    setTimeout(() => {
      setmodalOpen(false);
    } , 500);
  }

  const verifyOtp = async () => {
    try {
      const res = await fetch(`${process.env.REACT_APP_BASE_URL}user/customer`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        }, body: JSON.stringify({
          identifier: email,
          otp: otp
        })
      }); 
      setStatus(res.status);
  
      if (res.ok) {
        const response = await res.text();
        successfulLoginProtocol(response);
      } else {
        const response = await res.text();
        setWarning(response);
      }
    } catch (e) {
      console.log(e.message);
      setWarning(e.message);
    }
  }

  const fetchOtpRequest = async () => {
    try {
      const res = await fetch(`${process.env.REACT_APP_BASE_URL}user/request-otp/${email}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        }
      });
      setStatus(res.status);

      if (res.ok) {
        const response = await res.text();
        setWarning(response);
        setNeedOTP(true);
      } else {
        const response = await res.text();
        setWarning(response);
      }
    } catch (err) {
      setWarning(err.message);
    }
  };

  const LoginSubmitHandler = (e) => {
    e.preventDefault();
    if (!otp) {
      setWarning("Please enter the OTP");
    } else {
      setWarning("");
      verifyOtp();
    }
  };

  const resendOtp = () => {
    setDisable(true);
    fetchOtpRequest();
    setTimeout(() => {
      setDisable(false);
    }, 30000);
  }

  return (
    <>
      <Modal
        className="modal"
        isOpen={props.open}
        onRequestClose={() => props.setOpen(false)}
        ariaHideApp={false}
      >
        <div className="modal-container">
          <div className="">
            <div className="modal-close">
              <p>Login Using OTP</p>
              <span
                className="fa fa-times"
                onClick={() => props.setOpen(false)}
              ></span>
            </div>
          </div>
          <div className="modal-form">
            {!needOTP && (
              <form onSubmit={validateEmail}>
                <input
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="modal-input"
                  type="text"
                  placeholder="Enter email"
                />
                <p className="message">{warning}</p>
                <Button1 type="submit" text="Submit" />
              </form>
            )}
            {needOTP && (
              <form onSubmit={LoginSubmitHandler}>
                <input
                  value={otp}
                  onChange={(e) => setOTP(e.target.value)}
                  className="modal-input"
                  type="number"
                  placeholder="Enter OTP"
                />
                <p className="message">{warning}</p>
                <small>Resend OTP after 30 seconds</small>
                <Button1 type="submit" text="Submit" />
                <Button2 text="Resend OTP" onClick={resendOtp} disable={disable}/>
              </form>
            )}
          </div>
        </div>
      </Modal>
    </>
  );
};

export default SignIn;
