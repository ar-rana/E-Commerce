import React from 'react'
import CategoryCard from './CategoryCard'

const ProductView = () => {
  return (
    <div className='horizontal-slide'>
      <div className="horizontal-slide-content">
        <CategoryCard text={"Checkout"}/>
        <CategoryCard text={"Checkout"}/>
        <CategoryCard text={"Checkout"}/>
        <CategoryCard text={"Checkout"}/>
        <CategoryCard text={"Checkout"}/>
        <CategoryCard text={"Checkout"}/>
      </div>
    </div>
  )
}

export default ProductView
