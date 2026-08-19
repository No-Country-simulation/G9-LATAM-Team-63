import React from 'react'

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string
  error?: string
}

const Input = React.forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, className = '', ...rest }, ref) => {
    return (
      <div className={`input-wrapper ${className}`}>
        {label && <label htmlFor={rest.id}>{label}</label>}
        <input ref={ref} {...rest} />
        {error && <span className="input-error">{error}</span>}
      </div>
    )
  }
)

Input.displayName = 'Input'
export default Input