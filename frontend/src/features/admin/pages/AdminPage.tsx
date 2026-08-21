// ================================================================
// Panel de Administración de Usuarios
// Ruta: /admin (protegida — solo rol ADMIN)
// Endpoints usados:
//   GET  /api/usuarios         → lista todos los usuarios
//   POST /api/usuarios         → crea un nuevo usuario con roles
//   DELETE /api/usuarios/{id}  → elimina un usuario
// ================================================================

import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { getUsers, createUser, deleteUser } from '../../../data/api/users'
import type { CreateUserRequest } from '../../../data/types/users'
import { Icon } from '../../../shared/components/Icons'
import Button from '../../../shared/components/Button'
import Loader from '../../../shared/components/Loader'

const AVAILABLE_ROLES = ['USER', 'ADMIN']

export default function AdminPage() {
  const queryClient = useQueryClient()
  const [showForm, setShowForm] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState<number | null>(null)
  const [formData, setFormData] = useState<CreateUserRequest>({
    username: '',
    password: '',
    roles: ['USER'],
  })
  const [formError, setFormError] = useState<string | null>(null)

  // ─── Consulta de usuarios ────────────────────────────────────────
  const { data: users, isLoading, isError, refetch } = useQuery({
    queryKey: ['admin-users'],
    queryFn: getUsers,
    staleTime: 30_000,
  })

  // ─── Mutación: crear usuario ─────────────────────────────────────
  const createMutation = useMutation({
    mutationFn: createUser,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['admin-users'] })
      setShowForm(false)
      setFormData({ username: '', password: '', roles: ['USER'] })
      setFormError(null)
    },
    onError: (err: Error) => {
      setFormError(err.message || 'Error al crear el usuario.')
    },
  })

  // ─── Mutación: eliminar usuario ──────────────────────────────────
  const deleteMutation = useMutation({
    mutationFn: deleteUser,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['admin-users'] })
      setDeleteTarget(null)
    },
  })

  const handleRoleToggle = (role: string) => {
    setFormData((prev) => ({
      ...prev,
      roles: prev.roles.includes(role)
        ? prev.roles.filter((r) => r !== role)
        : [...prev.roles, role],
    }))
  }

  const handleCreate = (e: React.FormEvent) => {
    e.preventDefault()
    setFormError(null)
    if (!formData.username.trim()) return setFormError('El nombre de usuario es requerido.')
    if (formData.password.length < 6) return setFormError('La contraseña debe tener al menos 6 caracteres.')
    if (formData.roles.length === 0) return setFormError('Selecciona al menos un rol.')
    createMutation.mutate(formData)
  }

  return (
    <section className="admin-page">
      <div className="container">
        {/* ─── Header ───────────────────────────────────────── */}
        <div className="admin-page__header">
          <div className="section-label">Administración</div>
          <h1 className="section-title">
            Panel de{' '}
            <span className="gradient-text">usuarios</span>
          </h1>
          <p className="section-subtitle" style={{ margin: '0 auto' }}>
            Gestiona los usuarios de la plataforma: crea nuevas cuentas y administra los roles de acceso.
          </p>
        </div>

        {/* ─── Barra de acciones ────────────────────────────── */}
        <div className="admin-toolbar">
          <div className="admin-toolbar__info">
            {users && (
              <span className="admin-toolbar__count">
                <Icon name="users" size={16} />
                {users.length} {users.length === 1 ? 'usuario' : 'usuarios'} registrados
              </span>
            )}
          </div>
          <div className="admin-toolbar__actions">
            <Button variant="secondary" onClick={() => refetch()}>
              <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6 }}>
                <Icon name="zap" size={14} />
                Actualizar
              </span>
            </Button>
            <Button variant="primary" onClick={() => setShowForm((v) => !v)}>
              <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6 }}>
                <Icon name="plug" size={14} />
                {showForm ? 'Cancelar' : 'Nuevo usuario'}
              </span>
            </Button>
          </div>
        </div>

        {/* ─── Formulario de creación ───────────────────────── */}
        {showForm && (
          <div className="glass-card admin-create-form">
            <p className="result-card__label">Crear nuevo usuario</p>
            <form onSubmit={handleCreate} noValidate>
              {formError && (
                <div className="admin-form-error" role="alert">
                  <Icon name="alert" size={16} />
                  {formError}
                </div>
              )}
              <div className="admin-form-grid">
                <div className="admin-form-field">
                  <label className="admin-form-label" htmlFor="new-username">
                    Nombre de usuario
                  </label>
                  <input
                    id="new-username"
                    type="text"
                    className="admin-form-input"
                    placeholder="Ej: juan.perez"
                    value={formData.username}
                    onChange={(e) => setFormData((p) => ({ ...p, username: e.target.value }))}
                    autoComplete="off"
                  />
                </div>
                <div className="admin-form-field">
                  <label className="admin-form-label" htmlFor="new-password">
                    Contraseña
                  </label>
                  <input
                    id="new-password"
                    type="password"
                    className="admin-form-input"
                    placeholder="Mínimo 6 caracteres"
                    value={formData.password}
                    onChange={(e) => setFormData((p) => ({ ...p, password: e.target.value }))}
                    autoComplete="new-password"
                  />
                </div>
              </div>

              <div className="admin-form-field" style={{ marginTop: 12 }}>
                <label className="admin-form-label">Roles</label>
                <div className="admin-roles">
                  {AVAILABLE_ROLES.map((role) => (
                    <label key={role} className={`admin-role-tag ${formData.roles.includes(role) ? 'admin-role-tag--active' : ''}`}>
                      <input
                        type="checkbox"
                        style={{ display: 'none' }}
                        checked={formData.roles.includes(role)}
                        onChange={() => handleRoleToggle(role)}
                      />
                      {role}
                    </label>
                  ))}
                </div>
              </div>

              <div className="admin-form-actions">
                <Button
                  type="submit"
                  variant="primary"
                  disabled={createMutation.isPending}
                >
                  {createMutation.isPending ? 'Creando...' : 'Crear usuario'}
                </Button>
                <Button variant="secondary" onClick={() => { setShowForm(false); setFormError(null) }}>
                  Cancelar
                </Button>
              </div>
            </form>
          </div>
        )}

        {/* ─── Estado de carga / error ──────────────────────── */}
        {isLoading && <Loader text="Cargando usuarios..." />}

        {isError && (
          <div className="history-empty">
            <div className="history-empty__icon">
              <Icon name="alert" size={40} />
            </div>
            <h2 className="history-empty__title">Error al cargar</h2>
            <p className="history-empty__text">
              No se pudo conectar con el servidor. Verifica tu sesión.
            </p>
          </div>
        )}

        {/* ─── Tabla de usuarios ────────────────────────────── */}
        {users && users.length === 0 && (
          <div className="history-empty">
            <div className="history-empty__icon">
              <Icon name="users" size={40} />
            </div>
            <h2 className="history-empty__title">Sin usuarios</h2>
            <p className="history-empty__text">No hay usuarios registrados aún.</p>
          </div>
        )}

        {users && users.length > 0 && (
          <div className="history-table">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Usuario</th>
                  <th>Roles</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.id} className="history-table__row">
                    <td>
                      <span className="admin-user-id">#{user.id}</span>
                    </td>
                    <td>
                      <span className="admin-user-name">{user.username}</span>
                    </td>
                    <td>
                      <div className="admin-user-roles">
                        {user.roles.map((role) => (
                          <span
                            key={role}
                            className={`admin-role-badge ${role === 'ADMIN' ? 'admin-role-badge--admin' : 'admin-role-badge--user'}`}
                          >
                            {role}
                          </span>
                        ))}
                      </div>
                    </td>
                    <td className="history-table__action">
                      {deleteTarget === user.id ? (
                        <div className="admin-confirm-delete">
                          <span className="admin-confirm-text">¿Confirmar?</span>
                          <button
                            className="admin-confirm-yes"
                            onClick={() => deleteMutation.mutate(user.id)}
                            disabled={deleteMutation.isPending}
                          >
                            {deleteMutation.isPending ? '...' : 'Sí'}
                          </button>
                          <button
                            className="admin-confirm-no"
                            onClick={() => setDeleteTarget(null)}
                          >
                            No
                          </button>
                        </div>
                      ) : (
                        <button
                          className="admin-delete-btn"
                          onClick={() => setDeleteTarget(user.id)}
                          aria-label={`Eliminar usuario ${user.username}`}
                        >
                          <Icon name="logout" size={14} />
                          Eliminar
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </section>
  )
}
