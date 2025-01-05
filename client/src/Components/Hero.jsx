import React from 'react';
import Button3 from './Buttons/Button3';
import logo1 from "../Assets/logo1.png";
import logo2 from "../Assets/logo2.png"

const Hero = () => {
  return (
    <div className='hero'>
      <div className='hero-box'>
        <img src={logo1} alt='logo' className='logo'/>
        <h1>E-Commerce</h1>
        <p>Welcome to our humble abode, please explore and find what fits you best</p>
        <Button3 text={"Start Exploring"} />
      </div>
    </div>
  )
}

export default Hero
