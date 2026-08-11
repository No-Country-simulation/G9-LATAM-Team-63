import { z } from 'zod'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useLoginMutation } from '../data/hooks/useAuth'
import { ApiError } from '../../../data/api/client'
import { Icon } from '../../../shared/components/Icons'
import Input from '../../../shared/components/Input'
import Button from '../../../shared/components/Button'

const loginSchema = z.object({
  username: z
    .string()
    .min(3, 'El usuario debe tener al menos 3 caracteres')
    .max(50, 'El usuario no puede superar los 50 caracteres'),
  password: z
    .string()
    .min(6, 'La contraseña debe tener al menos 6 caracteres')
    .max(100, 'La contraseña no puede superar los 100 caracteres'),
})

type LoginFormData = z.infer<typeof loginSchema>

export default function LoginPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const from = (location.state as { from?: string })?.from || '/analizar'
  const mutation = useLoginMutation()

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  })

  const onSubmit = async (data: LoginFormData) => {
    try {
      await mutation.mutateAsync(data)
      navigate(from, { replace: true })
    } catch {
      // El error ya está en mutation.error
    }
  }

  const errorMessage =
    mutation.error instanceof ApiError
      ? mutation.error.message
      : mutation.error
      ? 'Error al iniciar sesión. Intenta de nuevo.'
      : null

  return (
    <div className="auth-page">
      <div className="auth-card">
        {/* Encabezado */}
        <div className="auth-card__header">
          <div className="section-label">
            <Icon name="lock" size={12} />
            Acceso seguro
          </div>
          <h1 className="auth-card__title">
            Iniciar <span className="gradient-text">sesión</span>
          </h1>
          <p className="auth-card__subtitle">
            Ingresa tus credenciales para acceder a tu análisis energético.
          </p>
        </div>

        {/* Error del backend */}
        {errorMessage && (
          <div className="auth-error" role="alert">
            <Icon name="alert" size={16} />
            <span>{errorMessage}</span>
          </div>
        )}

        <form onSubmit={handleSubmit(onSubmit)} noValidate>
          <Input
            id="login-username"
            label="Usuario"
            type="text"
            autoComplete="username"
            placeholder="Tu nombre de usuario"
            error={errors.username?.message}
            {...register('username')}
          />
          <Input
            id="login-password"
            label="Contraseña"
            type="password"
            autoComplete="current-password"
            placeholder="••••••••"
            error={errors.password?.message}
            {...register('password')}
          />

          <div className="auth-card__actions">
            <Button
              type="submit"
              variant="primary"
              disabled={isSubmitting || mutation.isPending}
            >
              {isSubmitting || mutation.isPending ? 'Ingresando...' : 'Iniciar sesión'}
            </Button>
          </div>
        </form>

        <div className="auth-card__footer">
          <p>
            ¿No tienes cuenta?{' '}
            <Link to="/registro" className="auth-card__link">
              Registrarse
            </Link>
          </p>
        </div>
      </div>
    </div>
  )
}
