import React from "react";

const Reviews = () => {
  return (
    <div className="review-container">
      <div className="review">
        <div className="review-col">
          <span className="fa fa-user-circle" />
          <div>
            <p>
              This section will consiste of helpful, usefull and general reviews
              for the people visiting this website. Thank You
            </p>
            <h3>Name Here</h3>
          </div>
        </div>
        <div className="review-col">
          <span className="fa fa-user-circle" />
          <div>
            <p>
              This section will consiste of helpful, usefull and general
              <br /> reviews for the people visiting this website.Thank You
            </p>
            <h3>Name Here</h3>
          </div>
        </div>
        <div className="review-col">
          <span className="fa fa-user-circle" />
          <div>
            <p>
              This section will consiste of helpful, usefull and <br />
              generalreviews for the people visiting this website.Thank You
            </p>
            <h3>Name Here</h3>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Reviews;
