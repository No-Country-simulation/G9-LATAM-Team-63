import { useEffect } from 'react'
import Hero from '../components/Hero'
import Stats from '../components/Stats'
import FeaturesSection from '../components/FeaturesSection'
import HowItWorks from '../components/HowItWorks'
import ApiDemo from '../components/ApiDemo'
import TechStack from '../components/TechStack'
import UseCases from '../components/UseCases'
import CTA from '../components/CTA'

export default function LandingPage() {
  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('visible')
          }
        })
      },
      { threshold: 0.1, rootMargin: '0px 0px -60px 0px' }
    )

    const elements = document.querySelectorAll('.fade-in-up')
    elements.forEach((el) => observer.observe(el))

    return () => {
      elements.forEach((el) => observer.unobserve(el))
    }
  }, [])

  return (
    <>
      <Hero />
      <Stats />
      <FeaturesSection />
      <HowItWorks />
      <ApiDemo />
      <TechStack />
      <UseCases />
      <CTA />
    </>
  )
}
