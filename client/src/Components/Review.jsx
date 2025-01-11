import React from "react";

const Review = (props) => {
  return (
    <>
      <div className="review-col">
        <span className="fa fa-user-circle" />
        <div>
          <p>{props.review}</p>
          <h3>{props.name}</h3>
          <p className="fa fa-star" style={{color : '#b4846c'}}> {props.stars}</p>
        </div>
      </div>
    </>
  );
};

export default Review;
