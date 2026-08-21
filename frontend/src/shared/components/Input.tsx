import React from 'react'

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string
  error?: string
}

const Input = React.forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, className = '', ...rest }, ref) => {
    return (
      <div className={`form-field ${className}`}>
        {label && (
          <label className="form-field__label" htmlFor={rest.id}>
            {label}
          </label>
        )}
        <input
          ref={ref}
          className={`form-field__input ${error ? 'form-field__input--error' : ''}`}
          {...rest}
        />
        {error && <span className="form-field__error">{error}</span>}
      </div>
    )
  }
)

Input.displayName = 'Input'
export default Input