import React, { useEffect, useState } from "react";
import Button1 from "../Components/Buttons/Button1.jsx";
import Reviews from "../Components/Reviews.jsx";
import ProductView from "../Components/ProductView.jsx";
import Footer from "../Components/Footer";
import usePublicApi from "../Hooks/API/usePublicApi.js";
import { useLocation, useParams } from "react-router-dom";
import useImage from "../Hooks/useImage.js";

const ProductPage = () => {
  const { productId } = useParams();
  console.log("productId", productId);

  const { data : product } = usePublicApi(`getProduct?id=${productId}`);
  const { url } = useImage(product?.thumbnail, product?.thumbnailType);
  const { data : images } = usePublicApi(`images/${productId}`);

  const [spotLightImg, setSpotLightImg] = useState("");

  useEffect(() => {
    setSpotLightImg(url);
    window.scrollTo({
      top: 0
    });
  }, [productId, url])
  return (
    <div className="product-page">
      <div className="product-info">
        <div className="product-images">
          <img src={spotLightImg} alt="Product_Image" style={{borderRadius: '20px'}}/>
          <div className="images-list">
            <ul>
              {images?.map((image, i) => (
                <li key={i}><img src={`${process.env.REACT_APP_BASE_URL}${image}`} alt="Product_Image" onClick={() => setSpotLightImg(`${process.env.REACT_APP_BASE_URL}${image}`)}/></li>
              ))}
              <li><img src={url} alt="Product_Image" onClick={() => setSpotLightImg(url)}/></li>
            </ul>
          </div>
        </div>
        <div className="product-details">
          <h1>{product?.name}</h1>
          <p>{product?.category}</p>
          <div className="">
            <span style={{ textDecoration: "line-through", color: "gray" }}>
              ₹ {product?.basicPrice}
            </span>
            <p>₹ {product?.currentPrice}</p>
          </div>
          <div className="product-options">
            <Button1 text={"Add to Cart"} />
            <Button1 text={"Add to WishList"} />
          </div>
          <p className="message"></p>
        </div>
      </div>
      <h2 className="heading underline">Reviews</h2>
      <Reviews />
      <h2 className="heading underline">Continue exploring...</h2>
      <div className="spacing">
        <ProductView />
      </div>
      <Footer />
    </div>
  );
};

export default ProductPage;
