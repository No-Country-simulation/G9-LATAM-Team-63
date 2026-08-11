import { APP_NAME, APP_TAGLINE } from '../../config/constants'

interface LogoProps {
  showTagline?: boolean
  className?: string
}

export default function Logo({ showTagline = true, className = '' }: LogoProps) {
  return (
    <div className={`logo ${className}`.trim()}>
      <img
        src="/logo-energia.png"
        alt="Logo EnergiAI"
        className="logo__icon"
        width={40}
        height={40}
      />
      <div>
        <span className="logo__text">{APP_NAME}</span>
        {showTagline && <span className="logo__tagline">{APP_TAGLINE}</span>}
      </div>
    </div>
  )
}
