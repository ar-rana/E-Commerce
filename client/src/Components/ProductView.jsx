import React, { useState } from "react";
import CategoryCard from "./CategoryCard";
import usePublicApi from "../Hooks/API/usePublicApi";
import { useNavigate } from "react-router-dom";

const ProductView = () => {
  const navigate = useNavigate();
  const { data, status } = usePublicApi("products/random");
  console.log(status);

  const navigateToProduct = (productId) => {
    navigate(`/product/${productId}`);
  }

  return (
    <div className="horizontal-slide">
      <div className="horizontal-slide-content">
        {data
          ? data.map((product) => {
              if (product?.virtualStock > 0) {
                return (
                  <CategoryCard
                    key={product.productId}
                    productView={true}
                    text={product.name}
                    productData={product}
                    onClick={() => navigateToProduct(product.productId)}
                  />
                );
              }
            })
          : ""}
      </div>
    </div>
  );
};

export default ProductView;
