import React from "react";
import ContactForm from "../Components/ContactForm";
import Footer from "../Components/Footer";

const Contact = () => {
  return (
    <div className="contact">
      <br />
      <br />
      <h2 className="heading2">Please fill in any Query you may have</h2>
      <h2 className="heading2">...or help us improve and share a Feedback</h2>
      <br />
      <ContactForm />
      <Footer />
    </div>
  );
};

export default Contact;
