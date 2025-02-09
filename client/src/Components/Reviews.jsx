import React, { useEffect, useState } from "react";
import Review from "./Review.jsx";
import useGet from "../Hooks/API/useGet.js";
import Button3 from "../Components/Buttons/Button3.jsx";

const Reviews = (props) => {
  const [data, setData] = useState([]);
  const [status, setStatus] = useState(null);
  const [reviewIds, setReviewIds] = useState([]);
  const noReview = [
    {
      review: "Be the first to leave a review for this product.",
      rating: null,
    },
  ];

  const fetchRequest = async () => {
    try {
      const res = await fetch(`${process.env.REACT_APP_BASE_URL}public/review/${props?.productId}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      setStatus(res.status);

      if (res.ok) {
        const responce = await res.json();
        setData(responce);
        setReviewIds([]);
        setReviewIds(responce.map(review => review.identifier));
      } else {
        setData(noReview);
      }
    } catch (err) {
      console.log(err.message);
    }
  };

  const postRequest = async () => {
    try {
      const res = await fetch(`${process.env.REACT_APP_BASE_URL}review/new/${props?.productId}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          reviewIds: reviewIds
        }),
      });

      if (res.ok) {
        const response = await res.json();
        setData([...data, response]);
        setReviewIds([...reviewIds, response.map(review => review.identifier)])
      }
    } catch (err) {
      console.log(err.message);
    }
  };

  useEffect(() => {
    fetchRequest();
  }, []);

  const loadmoreReviews = () => {
    postRequest();
  }

  return (
    <div className="review-container">
      <div className="review">
        {data?.map((review, i) => (
          <Review
            key={i}
            name={"Our Customer"}
            review={review.review}
            stars={review.rating}
          />
        ))}
      </div>
      <Button3 text="Load More" onClick={loadmoreReviews}/>
    </div>
  );
};

export default Reviews;
