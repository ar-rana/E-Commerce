import React, { useEffect, useState } from "react";
import Button2 from "../Buttons/Button2";
import Modal from "react-modal";

const UserInfoForm = (props) => {
  const [name, setName] = useState("");
  const [number, setNumber] = useState("");
  const [address, setAddress] = useState("");

  const userInfoHandler = (e) => {
    e.preventDefault();
    const newCustomer = {
      name: name,
      number: number,
      address: address
    }
    let newList = [];
    const list = localStorage.getItem("customers");

    if (list) {
      const existingCustomers = JSON.parse(list);
      newList = [...existingCustomers, newCustomer];
    } else {
      newList = [newCustomer];
    }
    localStorage.setItem("customers", JSON.stringify(newList));
    console.log(localStorage.getItem("customers"));
    props.setOpen(false);
  };

  return (
    <>
      <Modal
        style={{
          content: {
            top: '7rem',
          },
        }}
        className="modal"
        isOpen={props.open}
        onRequestClose={() => props.setOpen(false)}
        ariaHideApp={false}
      >
        <div className="modal-container">
          <div className="">
            <div className="modal-close">
              <p>Add Customer</p>
              <span
                className="fa fa-times"
                onClick={() => props.setOpen(false)}
              ></span>
            </div>
          </div>
          <div className="modal-form">
            <form onSubmit={userInfoHandler}>
              <p className="message">Add a customer</p>
              <input
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="modal-input"
                type="text"
                placeholder="Enter name"
              />
              <input
                value={number}
                onChange={(e) => setNumber(e.target.value)}
                className="modal-input"
                type="number"
                placeholder="Enter contact number"
              />
              <textarea
                rows={6}
                cols={36}
                value={address}
                placeholder="Enter address"
                style={{ marginTop: "4px", backgroundColor: '#eee3cb' }}
                onChange={(e) => setAddress(e.target.value)}
              />
              <Button2 type="submit" text="Ok"/>
            </form>
          </div>
        </div>
      </Modal>
    </>
  );
};

export default UserInfoForm;
