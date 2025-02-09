import React from "react";

const UserInfo = (props) => {
  return (
    <>
      <div className="delivery-to">
        <span
          className="fa fa-trash-o"
          title="Delete"
          onClick={() => props.deleteCustomer(props.number)}
        ></span>
        <p>Name: {props.name}</p>
        <p>Number: {props.number}</p>
        <p>Address: {props.address}</p>
      </div>
    </>
  );
};

export default UserInfo;
