import React from "react";

const Button3 = (props) => {
  return (
    <>
      <button className="button3" onClick={props.onClick}>
        <span>{props.text}</span>
      </button>
    </>
  );
};

export default Button3;
