import type { ReactNode } from 'react'
import { Link } from 'react-router-dom'

type ButtonVariant = 'primary' | 'secondary'

type ButtonProps = {
  variant?: ButtonVariant
  children: ReactNode
  className?: string
}

type ButtonAsButton = ButtonProps & { type?: 'button' | 'submit' | 'reset'; disabled?: boolean; onClick?: () => void }
type ButtonAsAnchor = ButtonProps & { href: string; target?: string; rel?: string }
type ButtonAsRouterLink = ButtonProps & { to: string }

export default function Button(props: ButtonAsButton | ButtonAsAnchor | ButtonAsRouterLink) {
  const { variant = 'primary', children, className = '' } = props
  const cls = `btn-${variant} ${className}`.trim()

  if ('to' in props) {
    return <Link to={props.to} className={cls}>{children}</Link>
  }

  if ('href' in props) {
    return <a href={props.href} className={cls}>{children}</a>
  }

  return (
    <button
      type={props.type ?? 'button'}
      disabled={props.disabled}
      onClick={props.onClick}
      className={cls}
    >
      {children}
    </button>
  )
}
