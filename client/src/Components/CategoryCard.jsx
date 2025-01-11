import React from "react";

const CategoryCard = (props) => {
  return (
    <div className="category-card">
      <img src="https://picsum.photos/200/200" alt="error" />
      <div className="layer">
        <h3>{props.text}</h3>
      </div>
    </div>
  );
};

export default CategoryCard;
