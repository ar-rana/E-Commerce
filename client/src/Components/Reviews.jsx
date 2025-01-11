import React from "react";
import Review from "./Review.jsx";

const Reviews = () => {
  const data = [{ id: "123" }, { id: "12323" }, { id: "12345" }];
  const review =
    "This section will consiste of helpful, usefull and general reviews for the people visiting this website. Thank You";
  const name = "Name Here";
  return (
    <div className="review-container">
      <div className="review">
        {data.map((obj) => (
          <Review key={obj.id} name={name} review={review} stars={4}/>
        ))}
      </div>
    </div>
  );
};

export default Reviews;
