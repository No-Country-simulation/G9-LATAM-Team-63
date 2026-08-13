import { z } from 'zod'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { Link, useNavigate } from 'react-router-dom'
import { useRegisterMutation } from '../data/hooks/useAuth'
import { ApiError } from '../../../data/api/client'
import { Icon } from '../../../shared/components/Icons'
import Input from '../../../shared/components/Input'
import Button from '../../../shared/components/Button'

const registerSchema = z
  .object({
    username: z
      .string()
      .min(3, 'El usuario debe tener al menos 3 caracteres')
      .max(50, 'El usuario no puede superar los 50 caracteres'),
    password: z
      .string()
      .min(6, 'La contraseña debe tener al menos 6 caracteres')
      .max(100, 'La contraseña no puede superar los 100 caracteres'),
    confirmPassword: z.string(), // ← corregido: debe coincidir con el backend
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: 'Las contraseñas no coinciden',
    path: ['confirmPassword'],
  })

type RegisterFormData = z.infer<typeof registerSchema>

export default function RegisterPage() {
  const navigate = useNavigate()
  const mutation = useRegisterMutation()

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
  })

  const onSubmit = async (data: RegisterFormData) => {
    try {
      // Enviar los 3 campos que espera el backend
      await mutation.mutateAsync({
        username: data.username,
        password: data.password,
        confirmPassword: data.confirmPassword, // ← agregado
      })
      navigate('/analizar', { replace: true })
    } catch {
      // El error ya está en mutation.error
    }
  }

  const errorMessage =
    mutation.error instanceof ApiError
      ? mutation.error.message
      : mutation.error
      ? 'Error al registrarse. Intenta de nuevo.'
      : null

  return (
    <div className="auth-page">
      <div className="auth-card">
        <div className="auth-card__header">
          <div className="section-label">
            <Icon name="zap" size={12} />
            Crear cuenta
          </div>
          <h1 className="auth-card__title">
            Únete a <span className="gradient-text">EnergiAI</span>
          </h1>
          <p className="auth-card__subtitle">
            Crea tu cuenta y comienza a optimizar tu consumo eléctrico hoy.
          </p>
        </div>

        {errorMessage && (
          <div className="auth-error" role="alert">
            <Icon name="alert" size={16} />
            <span>{errorMessage}</span>
          </div>
        )}

        <form onSubmit={handleSubmit(onSubmit)} noValidate>
          <Input
            id="register-username"
            label="Nombre de usuario"
            type="text"
            autoComplete="username"
            placeholder="Ej: usuario123"
            error={errors.username?.message}
            {...register('username')}
          />
          <Input
            id="register-password"
            label="Contraseña"
            type="password"
            autoComplete="new-password"
            placeholder="Mínimo 6 caracteres"
            error={errors.password?.message}
            {...register('password')}
          />
          <Input
            id="register-confirm-password"
            label="Confirmar contraseña"
            type="password"
            autoComplete="new-password"
            placeholder="Repite tu contraseña"
            error={errors.confirmPassword?.message} // ← corregido
            {...register('confirmPassword')} // ← corregido
          />

          <div className="auth-card__actions">
            <Button
              type="submit"
              variant="primary"
              disabled={isSubmitting || mutation.isPending}
            >
              {isSubmitting || mutation.isPending ? 'Creando cuenta...' : 'Crear cuenta'}
            </Button>
          </div>
        </form>

        <div className="auth-card__footer">
          <p>
            ¿Ya tienes cuenta?{' '}
            <Link to="/login" className="auth-card__link">
              Iniciar sesión
            </Link>
          </p>
        </div>
      </div>
    </div>
  )
}