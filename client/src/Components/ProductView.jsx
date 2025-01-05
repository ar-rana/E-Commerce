import React from 'react'
import CategoryCard from './CategoryCard'

const ProductView = () => {
  return (
    <div className='horizontal-slide'>
      <div className="horizontal-slide-content">
        <CategoryCard />
        <CategoryCard />
        <CategoryCard />
        <CategoryCard />
        <CategoryCard />
        <CategoryCard />
      </div>
    </div>
  )
}

export default ProductView
