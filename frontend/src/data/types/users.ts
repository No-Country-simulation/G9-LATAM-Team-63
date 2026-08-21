// Tipos para gestión de usuarios — contratos con el backend
// UsuarioResponse: { id, username, roles }
// UsuarioRequest:  { username, password, roles }

export interface UserDto {
  id: number
  username: string
  roles: string[]
}

export interface CreateUserRequest {
  username: string
  password: string
  roles: string[]
}
