import { forwardRef } from 'react'

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string
  error?: string
}

const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, id, ...props }, ref) => {
    const inputId = id || label.toLowerCase().replace(/\s+/g, '-')
    return (
      <div className="form-field">
        <label htmlFor={inputId} className="form-field__label">{label}</label>
        <input
          ref={ref}
          id={inputId}
          className={`form-field__input ${error ? 'form-field__input--error' : ''}`}
          {...props}
        />
        {error && <p className="form-field__error">{error}</p>}
      </div>
    )
  }
)

Input.displayName = 'Input'
export default Input
