import React, { useEffect, useState } from "react";
import Button1 from "../Components/Buttons/Button1.jsx";
import Reviews from "../Components/Reviews.jsx";
import ProductView from "../Components/ProductView.jsx";
import Footer from "../Components/Footer";
import usePublicApi from "../Hooks/API/usePublicApi.js";
import { useParams } from "react-router-dom";
import useImage from "../Hooks/useImage.js";
import { useLoginContext } from "../Hooks/LoginContext.js";
import { useUser } from "../UserContext.js";

const ProductPage = () => {
  const { productId } = useParams();
  const { setmodalOpen } = useLoginContext();
  const { user, token } = useUser();

  console.log("productId", productId);

  const { data: product } = usePublicApi(`getProduct?id=${productId}`);
  const { url } = useImage(product?.thumbnail, product?.thumbnailType);
  const { data: images } = usePublicApi(`images/${productId}`);

  const [spotLightImg, setSpotLightImg] = useState("");

  const postRequest = async (listType) => {
    try {
      const res = await fetch(`${process.env.REACT_APP_BASE_URL}list/add/${listType}`, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            identifier: user,
            productId: productId,
          }),
        }
      );

      if (res.ok) {
        const response = await res.json();
        alert(`Added to ${listType}!!`);
      } else if (res.status === 403) {
        setmodalOpen(true);
      }
    } catch (err) {
      console.log(err.message);
    }
  };

  useEffect(() => {
    setSpotLightImg(url);
    window.scrollTo({
      top: 0,
    });
  }, [productId, url]);
  return (
    <div className="product-page">
      <div className="product-info">
        <div className="product-images">
          <img
            src={spotLightImg}
            alt="Product_Image"
            style={{ borderRadius: "20px" }}
          />
          <div className="images-list">
            <ul>
              {images?.map((image, i) => (
                <li key={i}>
                  <img
                    src={`${process.env.REACT_APP_BASE_URL}${image}`}
                    alt="Product_Image"
                    onClick={() =>
                      setSpotLightImg(
                        `${process.env.REACT_APP_BASE_URL}${image}`
                      )
                    }
                  />
                </li>
              ))}
              <li>
                <img
                  src={url}
                  alt="Product_Image"
                  onClick={() => setSpotLightImg(url)}
                />
              </li>
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
            <Button1 text={"Add to Cart"} onClick={() => {
              postRequest("CART");
            }}/>
            <Button1 text={"Add to WishList"} onClick={() => {
              postRequest("WISHLIST");
            }}/>
          </div>
          <p className="message"></p>
        </div>
      </div>
      <h2 className="heading underline">Reviews</h2>
      <Reviews productId={productId}/>
      <h2 className="heading underline">Continue exploring...</h2>
      <div className="spacing">
        <ProductView />
      </div>
      <Footer />
    </div>
  );
};

export default ProductPage;
