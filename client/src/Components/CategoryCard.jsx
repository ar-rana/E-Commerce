import React from "react";
import useImage from "../Hooks/useImage";

const CategoryCard = (props) => {
  const num = 100 - (props.productData?.currentPrice / props.productData?.basicPrice) * 100;
  let discount = num.toFixed(2);

  const imageUrl = "";
  const { url } = useImage(props.productData?.thumbnail, props.productData?.thumbnailType);
  const imgUrl = props.productView ? url : props.url;

  return (
    <div className="category-card" onClick={props.onClick}>
      {/* <img className={`${props.productView ? 'category-card-img': ''}`} src={imgUrl} alt="Thumbnail" /> */}
      <img className="category-card-img" src={imgUrl} alt="Thumbnail"/>
      <div className="layer">
        <div className="layer-tag">
          <h5>{props.text}</h5>
          {props.productView == true ? <h5>{discount}% OFF</h5> : ""}
        </div>
      </div>
    </div>
  );
};

export default CategoryCard;
