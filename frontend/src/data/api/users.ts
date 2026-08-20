// ================================================================
// Capa de comunicación con el backend para gestión de usuarios.
// Endpoints exclusivos para ADMIN: POST y DELETE.
// GET /api/usuarios está disponible para ADMIN y USER.
// ================================================================

import { api } from './client'
import { API_BASE_URL } from '../../config/constants'
import type { UserDto, CreateUserRequest } from '../types/users'

// ================================================================
// 1. Listar todos los usuarios
// GET /api/usuarios → 200 OK (requiere ADMIN o USER)
// ================================================================
export function getUsers(): Promise<UserDto[]> {
  return api.get<UserDto[]>('/usuarios')
}

// ================================================================
// 2. Crear usuario con roles
// POST /api/usuarios → 201 Created (requiere ADMIN)
// ================================================================
export function createUser(data: CreateUserRequest): Promise<UserDto> {
  return api.post<UserDto>('/usuarios', data)
}

// ================================================================
// 3. Eliminar usuario por ID
// DELETE /api/usuarios/{id} → 204 No Content (requiere ADMIN)
// ================================================================
export async function deleteUser(id: number): Promise<void> {
  const token = localStorage.getItem('energiai_token')
  const response = await fetch(`${API_BASE_URL}/usuarios/${id}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
  })
  if (!response.ok && response.status !== 204) {
    throw new Error(`Error al eliminar usuario: ${response.status}`)
  }
}
