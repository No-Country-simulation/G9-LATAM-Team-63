import { api } from '../../../../data/api/client'
import type { LoginInput, RegisterInput, AuthResponse, RegisterResponse } from '../types/auth'

// POST /api/auth/login → { token }
export function login(input: LoginInput): Promise<AuthResponse> {
  return api.post<AuthResponse>('/auth/login', input)
}

// POST /api/auth/register → { id, username, mensaje, token }
export function register(input: RegisterInput): Promise<RegisterResponse> {
  return api.post<RegisterResponse>('/auth/register', input)
}
