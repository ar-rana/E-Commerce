import React, { useState } from "react";
import Modal from "react-modal";
import Button1 from "../Buttons/Button1";

const LeaveReview = (props) => {
  const [text, setText] = useState("");
  const [rating, setRating] = useState("");

  const reviewSubmithandler = (e) => {
    e.preventDefault();
    console.log('rating: ', rating, 'text: ', text);
    props.setOpen(false);
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
              <p>Review</p>
              <span
                className="fa fa-times"
                onClick={() => props.setOpen(false)}
              ></span>
            </div>
          </div>
          <div className="modal-form">
            <form onSubmit={reviewSubmithandler}>
              <p className="message">Select Rating</p>
              <div className="rating-options"
              style={{display: 'flex', gap: '5px' }}>
                <input type="radio" id="one" name="rating" value="1" onChange={(e) => setRating(e.target.value)} required/>
                <label htmlFor="one">1</label>
                <input type="radio" id="two" name="rating" value="2" onChange={(e) => setRating(e.target.value)}/>
                <label htmlFor="two">2</label>
                <input type="radio" id="three" name="rating" value="3" onChange={(e) => setRating(e.target.value)}/>
                <label htmlFor="three">3</label>
                <input type="radio" id="four" name="rating" value="4" onChange={(e) => setRating(e.target.value)}/>
                <label htmlFor="four">4</label>
                <input type="radio" id="five" name="rating" value="5" onChange={(e) => setRating(e.target.value)}/>
                <label htmlFor="five">5</label>
              </div>
              <textarea
                rows={6}
                cols={37}
                placeholder="leave a review"
                style={{marginTop: '4px'}}
                onChange={(e) => setText(e.target.value)}
              />
              <Button1 type="submit" text="Submit" />
            </form>
          </div>
        </div>
      </Modal>
    </>
  );
};

export default LeaveReview;
