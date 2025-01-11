import React, { useState } from "react";
import Modal from "react-modal";
import Button1 from "../Buttons/Button1";

const SignIn = (props) => {
  const [email, setEmail] = useState("");
  const [otp, setOTP] = useState("");
  const [warning, setWarning] = useState("");
  const [needOTP, setNeedOTP] = useState(false);

  const validateEmail = (e) => {
    e.preventDefault();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (emailRegex.test(email)) {
      setNeedOTP(true);
      setWarning("");
    } else {
      setWarning("Invalid Email");
    }
  };

  const LoginSubmitHandler = (e) => {
    e.preventDefault();
    if (!otp) {
      setWarning("Please enter the OTP");
    } else {
      setWarning("");
      console.log("Login successful with OTP:", otp);
      props.setOpen(false);
    }
  };

  return (
    <>
      <Modal
        className="modal"
        isOpen={props.open}
        onRequestClose={() => props.setOpen(false)}
      >
        <div className="modal-container">
          <div className="">
            <div className="modal-close">
              <p>Login</p>
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
                <Button1 type="submit" text="Submit" />
              </form>
            )}
          </div>
        </div>
      </Modal>
    </>
  );
};

export default SignIn;
