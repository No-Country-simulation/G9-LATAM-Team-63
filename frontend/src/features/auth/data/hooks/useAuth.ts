import { useMutation } from '@tanstack/react-query'
import { login, register } from '../api/auth'
import { useAuthStore } from '../../../../store/authStore'
import type { LoginInput, RegisterInput } from '../types/auth'

export function useLoginMutation() {
  const authLogin = useAuthStore((s) => s.login)

  return useMutation({
    mutationFn: (input: LoginInput) => login(input),
    onSuccess: (data, variables) => {
      // El backend devuelve solo { token } en login;
      // usamos el username que el usuario escribió en el formulario
      authLogin(data.token, variables.username)
    },
  })
}

export function useRegisterMutation() {
  const authLogin = useAuthStore((s) => s.login)

  return useMutation({
    mutationFn: (input: RegisterInput) => register(input),
    onSuccess: (data) => {
      // El registro ya devuelve el token — login automático sin pasar por /login
      authLogin(data.token, data.username)
    },
  })
}
