import React from 'react';
import Button3 from './Buttons/Button3';
import logo1 from "../Assets/logo1.png";

const Hero = () => {
  const scrollToExplore = () => {
    window.scrollTo({
      top: 550,
      behavior: "smooth",
    });
  }
  return (
    <div className='hero'>
      <div className='hero-box'>
        <img src={logo1} alt='logo' className='logo'/>
        <h1>E-Commerce</h1>
        <p>Welcome to our humble abode, please explore and find what fits you best</p>
        <Button3 text={"Start Exploring"} onClick={scrollToExplore}/>
      </div>
    </div>
  )
}

export default Hero
