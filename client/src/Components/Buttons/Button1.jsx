import React from "react";

const Button1 = (props) => {
  return (
    <>
      <button className="button1" onClick={props.onClick}>
        {props.text}
      </button>
    </>
  );
};

export default Button1;
