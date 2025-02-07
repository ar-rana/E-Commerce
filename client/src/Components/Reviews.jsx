import React, { useEffect, useState } from "react";
import Review from "./Review.jsx";
import useGet from "../Hooks/API/useGet.js";
import Button3 from "../Components/Buttons/Button3.jsx";

const Reviews = (props) => {
  const [data, setData] = useState([]);
  const [reviewIds, setReviewIds] = useState([]);
  const noReview = [
    {
      review: "Be the first to leave a review for this product.",
      rating: null,
    },
  ];

  const {data: reviews, status, loading } = useGet(`public/review/${props?.productId}`);

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
    setTimeout(() => {
      if (status !== 200) {
        setData(noReview);
      }
      if (reviews) {
        setData(reviews);
        setReviewIds([]);
        setReviewIds(reviews.map(review => review.identifier));
      }
    }, 500) 
  }, [status, reviews, loading]);

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
      <Button3 text="Load More"/>
    </div>
  );
};

export default Reviews;
