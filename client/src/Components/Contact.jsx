import React from "react";
import Button1 from "./Buttons/Button1.jsx";

const Contact = () => {
  return (
    <>
      <div className="contact-col">
        <form action="">
          <input type="text" placeholder="Enter Your Name" required/>
          <input type="email" placeholder="Enter Email " />
          <input type="number" placeholder="Enter Order ID" />
          <textarea
            rows={8}
            placeholder="Send us Your Query or Feedback"
            defaultValue={""}
            required
          />
          <Button1 text="Submit" type="submit"/>
        </form>
      </div>
    </>
  );
};

export default Contact;
