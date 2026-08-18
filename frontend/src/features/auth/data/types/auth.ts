export interface LoginInput {
  username: string
  password: string
}

export interface RegisterInput {
  username: string
  password: string
  confirmPassword: string
}

export interface AuthResponse {
  token: string
}

export interface RegisterResponse {
  id: number
  username: string
  mensaje: string
  token: string
}
