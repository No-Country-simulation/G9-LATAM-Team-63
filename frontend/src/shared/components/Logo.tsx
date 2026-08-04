import { APP_NAME, APP_TAGLINE } from '../../config/constants'

interface LogoProps {
  showTagline?: boolean
  className?: string
}

export default function Logo({ showTagline = true, className = '' }: LogoProps) {
  return (
    <div className={`logo ${className}`.trim()}>
      <svg className="logo__icon" width="36" height="36" viewBox="0 0 36 36" fill="none" aria-hidden="true">
        <rect width="36" height="36" rx="10" fill="url(#logoGradient)" />
        <path
          d="M18 6L12 18H16L14 30L24 16H19L21 6H18Z"
          fill="white"
          stroke="white"
          strokeWidth="1.5"
          strokeLinejoin="round"
        />
        <defs>
          <linearGradient id="logoGradient" x1="0" y1="0" x2="36" y2="36">
            <stop offset="0%" stopColor="#1e40af" />
            <stop offset="100%" stopColor="#2563eb" />
          </linearGradient>
        </defs>
      </svg>
      <div>
        <span className="logo__text">{APP_NAME}</span>
        {showTagline && <span className="logo__tagline">{APP_TAGLINE}</span>}
      </div>
    </div>
  )
}
