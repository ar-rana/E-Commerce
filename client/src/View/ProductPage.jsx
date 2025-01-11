import React from "react";
import Button1 from "../Components/Buttons/Button1.jsx";
import Reviews from "../Components/Reviews.jsx";
import ProductView from "../Components/ProductView.jsx";
import Footer from "../Components/Footer";

const ProductPage = () => {
  const images = [
    { id: "123" },
    { id: "12323" },
    { id: "12345" },
    { id: "13423" },
    { id: "3587" },
    { id: "1347" },
    { id: "0783" },
    { id: "13423" },
    { id: "3587" },
    { id: "1347" },
    { id: "0783" }
  ];
  return (
    <div className="product-page">
      <div className="product-info">
        <div className="product-images">
          <img src="https://picsum.photos/200" alt="Product_Image" />
          <div className="images-list">
            <ul>
              {images.map((obj, i) => (
                <li key={i}><img src="https://picsum.photos/200/300" alt="Product_Image" /></li>
              ))}
            </ul>
          </div>
        </div>
        <div className="product-details">
          <h1>Product Name</h1>
          <p>Product Category</p>
          <div className="">
            <span style={{ textDecoration: "line-through", color: "gray" }}>
              ₹ XXXX
            </span>
            <p>₹ XXX</p>
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
