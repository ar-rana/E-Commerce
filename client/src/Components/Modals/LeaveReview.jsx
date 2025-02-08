import React, { useState } from "react";
import Modal from "react-modal";
import Button1 from "../Buttons/Button1";
import { useUser } from "../../UserContext";

const LeaveReview = (props) => {
  const { user, token } = useUser();
  const [text, setText] = useState("");
  const [rating, setRating] = useState("");

  const reviewSubmithandler = (e) => {
    e.preventDefault();
    console.log('rating: ', rating, 'text: ', text);
    leaveReview();
  };

  const leaveReview = async () => {
    try {
      const res = await fetch(
        `${process.env.REACT_APP_BASE_URL}orders/review/${user}`, 
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            productId: props?.productId,
            rating: rating,
            review: text,
          })
        }
      );

      if (res.ok) {
        const responce = await res.text();
        alert(responce);
        props.setOpen(false);
      }
    } catch (err) {
      console.log(err.message);
    }
  }

  return (
    <>
      <Modal
        className="modal"
        isOpen={props.open}
        onRequestClose={() => props.setOpen(false)}
        ariaHideApp={false}
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
            <form>
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
                cols={36}
                placeholder="leave a review (char: 250)"
                maxlength="250"
                style={{marginTop: '4px', backgroundColor: '#eee3cb'}}
                onChange={(e) => setText(e.target.value)}
              />
              <Button1 type="submit" onClick={reviewSubmithandler} />
            </form>
          </div>
        </div>
      </Modal>
    </>
  );
};

export default LeaveReview;
