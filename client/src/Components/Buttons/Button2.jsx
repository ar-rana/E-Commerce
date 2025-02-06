import React from "react";

const Button2 = (props) => {
  return (
    <>
      <button className="button2" onClick={props.onClick} disabled={props.disable}>{props.text}</button>
    </>
  );
};

export default Button2;
