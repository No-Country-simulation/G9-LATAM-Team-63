import type { CSSProperties } from 'react'

type IconName =
  | 'chart' | 'lightbulb' | 'dollar' | 'robot' | 'plug' | 'cloud'
  | 'house' | 'store' | 'building'
  | 'clipboard' | 'zap' | 'trend-up'
  | 'snake' | 'coffee' | 'atom'
  | 'alert' | 'check' | 'star'
  | 'lock' | 'leaf' | 'python'
  | 'users' | 'clock' | 'flame' | 'snowflake'
  | 'tv' | 'car' | 'download' | 'copy'

interface IconProps {
  name: IconName
  size?: number
  className?: string
  style?: CSSProperties
}

const paths: Record<IconName, string> = {
  chart: `<path d="M4 20V10l4 4 4-6 4 4 4-2v10H4z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  lightbulb: `<path d="M9.663 17h4.674M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.272 0l-.548.547A3.518 3.518 0 0014 17.5v.5h-4v-.5a3.518 3.518 0 00-.888-2.29l-.548-.547z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  dollar: `<circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="2" fill="none"/><path d="M8 8.5h3.5a2 2 0 010 4H8m3.5 0H15a2 2 0 010 4H8" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  robot: `<rect x="4" y="6" width="16" height="14" rx="3" stroke="currentColor" stroke-width="2" fill="none"/><circle cx="9" cy="11" r="1.5" fill="currentColor"/><circle cx="15" cy="11" r="1.5" fill="currentColor"/><path d="M9 16a3 3 0 016 0" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" fill="none"/><path d="M10 3l2 3 2-3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  plug: `<path d="M12 6v4m0 4v3M8 10V6a2 2 0 114 0v4m-6 0h8a1 1 0 011 1v1a5 5 0 01-10 0v-1a1 1 0 011-1z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  cloud: `<path d="M17.5 19H9a7 7 0 116.5-9.5A5.5 5.5 0 1117.5 19z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  house: `<path d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  store: `<path d="M3 21h18M3 7v1a3 3 0 006 0V7m0 0V3h12v4a3 3 0 01-6 0V7m0 0H9m0 0V3H3v4a3 3 0 006 0M3 21V11m18 10V11M3 11h18" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  building: `<rect x="4" y="3" width="16" height="18" rx="2" stroke="currentColor" stroke-width="2" fill="none"/><path d="M9 7h2v2H9V7zm4 0h2v2h-2V7zM9 12h2v2H9v-2zm4 0h2v2h-2v-2z" fill="currentColor"/>`,
  clipboard: `<path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 7h4m-4 3h4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  zap: `<path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  'trend-up': `<path d="M3 16l4-4 4 4 6-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/><path d="M17 10h3v3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  snake: `<path d="M4 12c0-4.4 3.6-8 8-8s8 3.6 8 8-3.6 8-8 8" stroke="currentColor" stroke-width="2" stroke-linecap="round" fill="none"/><path d="M4 12h4l2-3 2 6 2-3 2 3h2" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  coffee: `<path d="M18 8h1a3 3 0 010 6h-1M2 8h16v6a4 4 0 01-4 4H6a4 4 0 01-4-4V8zM6 2v2M10 2v2M14 2v2" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  atom: `<circle cx="12" cy="12" r="2" fill="currentColor"/><ellipse cx="12" cy="12" rx="10" ry="4" stroke="currentColor" stroke-width="1.5" fill="none" transform="rotate(0 12 12)"/><ellipse cx="12" cy="12" rx="10" ry="4" stroke="currentColor" stroke-width="1.5" fill="none" transform="rotate(60 12 12)"/><ellipse cx="12" cy="12" rx="10" ry="4" stroke="currentColor" stroke-width="1.5" fill="none" transform="rotate(-60 12 12)"/>`,
  alert: `<path d="M12 9v4m0 4h.01M10.29 3.86l-8.1 14c-.6 1.04.15 2.14 1.21 2.14h16.2c1.06 0 1.81-1.1 1.21-2.14l-8.1-14c-.6-1.04-1.82-1.04-2.42 0z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  check: `<path d="M5 13l4 4L19 7" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  star: `<path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z" stroke="currentColor" stroke-width="1.5" stroke-linejoin="round" fill="none"/>`,
  lock: `<rect x="5" y="11" width="14" height="10" rx="2" stroke="currentColor" stroke-width="2" fill="none"/><path d="M8 11V7a4 4 0 118 0v4" stroke="currentColor" stroke-width="2" stroke-linecap="round" fill="none"/>`,
  leaf: `<path d="M11 20A7 7 0 019.5 6.5C11 5 13 5 14 6c1 1 1.5 1.5 2 3 .5 1.5.5 3 0 4-1 2-3 3.5-5 4 0 0-2 1.5-2 3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/><path d="M9.5 11.5L19 4" stroke="currentColor" stroke-width="2" stroke-linecap="round" fill="none"/>`,
  python: `<rect x="6" y="3" width="12" height="18" rx="2" stroke="currentColor" stroke-width="2" fill="none"/><path d="M9 8h6M9 16h6M9 12h6" stroke="currentColor" stroke-width="2" stroke-linecap="round" fill="none"/>`,
  users: `<path d="M17 21v-2a4 4 0 00-4-4H7a4 4 0 00-4 4v2" stroke="currentColor" stroke-width="2" stroke-linecap="round" fill="none"/><circle cx="10" cy="7" r="4" stroke="currentColor" stroke-width="2" fill="none"/><path d="M23 21v-2a4 4 0 00-3-3.87M16 3.13a4 4 0 010 7.75" stroke="currentColor" stroke-width="2" stroke-linecap="round" fill="none"/>`,
  clock: `<circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="2" fill="none"/><path d="M12 7v5l3 2" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  flame: `<path d="M8.5 14.5A2.5 2.5 0 0011 12c0-1.38-.5-2-1-3-1.072-2.143-.224-4.054 2-6 .5 2.5 2 4.9 4 6.5 2 1.6 3 3.5 3 5.5a7 7 0 11-14 0c0-1.153.433-2.294 1-3a2.5 2.5 0 002.5 2.5z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  snowflake: `<path d="M12 2v20M2 12h20M4.93 4.93l14.14 14.14M19.07 4.93L4.93 19.07" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" fill="none"/>`,
  tv: `<rect x="2" y="7" width="20" height="14" rx="2" stroke="currentColor" stroke-width="2" fill="none"/><path d="M8 2.5l4 5 4-5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  car: `<path d="M4 11l2-5a1 1 0 011-1h10a1 1 0 011 1l2 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/><path d="M3 11h18v5a1 1 0 01-1 1h-1v2h-3v-2H8v2H5v-2H4a1 1 0 01-1-1v-5z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/><circle cx="7.5" cy="15.5" r="1" fill="currentColor"/><circle cx="16.5" cy="15.5" r="1" fill="currentColor"/>`,
  download: `<path d="M12 3v12m0 0l-4-4m4 4l4-4M4 21h16" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
  copy: `<rect x="9" y="9" width="12" height="12" rx="2" stroke="currentColor" stroke-width="2" fill="none"/><path d="M5 15H4a2 2 0 01-2-2V4a2 2 0 012-2h9a2 2 0 012 2v1" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>`,
}

export function Icon({ name, size = 24, className = '', style }: IconProps) {
  return (
    <svg
      width={size}
      height={size}
      viewBox="0 0 24 24"
      className={className}
      style={style}
      aria-hidden="true"
      dangerouslySetInnerHTML={{ __html: paths[name] }}
    />
  )
}

export function FeatureIcon({ name, className = '' }: { name: IconName; className?: string }) {
  return (
    <span className={`feature-card__icon ${className}`} aria-hidden="true">
      <Icon name={name} size={24} />
    </span>
  )
}

export function StepIcon({ name }: { name: IconName }) {
  return (
    <span className="step__icon" aria-hidden="true">
      <Icon name={name} size={26} />
    </span>
  )
}

export function TechIcon({ name }: { name: IconName }) {
  return (
    <span className="tech-card__icon" aria-hidden="true">
      <Icon name={name} size={32} />
    </span>
  )
}

export function AvatarIcon({ name }: { name: IconName }) {
  return (
    <div className="usecase-card__avatar" aria-hidden="true">
      <Icon name={name} size={20} />
    </div>
  )
}

export type { IconName }
