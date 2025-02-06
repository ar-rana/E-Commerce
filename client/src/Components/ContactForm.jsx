import React, { useState } from "react";
import Button1 from "./Buttons/Button1.jsx";
import { useUser } from "../UserContext.js";
import { useLoginContext } from "../Hooks/LoginContext.js";

const ContactForm = () => {
  const { user, token } = useUser();
  const { setmodalOpen } = useLoginContext();

  const [content, setContent] = useState("");

  const [name, setName] = useState("");
  const [orderId, setOrderId] = useState("");

  const onSubmitHandler = async (e) => {
    e.preventDefault();
    if (!user) {
      setmodalOpen(true);
    }
    if (content.trim() === "") return;
    const subject = user + ": " + name + ": " + orderId;

    sendFeedback(subject);
  };

  const sendFeedback = async (subject) => {
    try {
      const res = await fetch(
        `${process.env.REACT_APP_BASE_URL}email/feedback`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            subject: subject,
            content: content,
          }),
        }
      );

      if (res.status === 403) {
        setmodalOpen(true);
      } else if (res.status === 200) {
        setContent("");
        setName("");
        alert("Your message has been sent we will look into you feedback and respond Accordingly!!!");
      }
    } catch (err) {
      console.log(err.message);
    }
  };
  return (
    <>
      <div className="contact-col">
        <form>
          <input
            type="text"
            placeholder="Enter Your Name"
            required
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <input
            type="number"
            placeholder="Enter Order ID"
            value={orderId}
            onChange={(e) => setOrderId(e.target.value)}
          />
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            rows={8}
            placeholder="Send us Your Query or Feedback"
            required
          />
          <Button1
            text="Submit"
            type="submit"
            onClick={(e) => onSubmitHandler(e)}
          />
        </form>
      </div>
    </>
  );
};

export default ContactForm;
