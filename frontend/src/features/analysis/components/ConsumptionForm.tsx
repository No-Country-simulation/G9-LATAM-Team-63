import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useNavigate } from 'react-router-dom'
import { analysisSchema, type AnalysisFormData } from '../schema'
import { useAnalysisMutation } from '../../../data/hooks/useAnalysis'
import { useAnalysisStore } from '../../../store/analysisStore'
import { ApiError } from '../../../data/api/client'
import Input from '../../../shared/components/Input'
import Button from '../../../shared/components/Button'

const inmuebles = ['Casa', 'Apartamento', 'Local', 'Oficina'] as const

export default function ConsumptionForm() {
  const navigate = useNavigate()
  const setLastInput = useAnalysisStore((s) => s.setLastInput)
  const mutation = useAnalysisMutation()
  const [generalError, setGeneralError] = useState<string | null>(null)

  const {
    register,
    handleSubmit,
    setError,
    formState: { errors, isSubmitting },
  } = useForm<AnalysisFormData>({
    resolver: zodResolver(analysisSchema),
    defaultValues: {
      consumoKwh: 420,
      usoHorarioPico: true,
      cantidadEquipos: 10,
      tipoInmueble: 'Casa',
      horasAltoConsumo: 8,
      numeroHabitantes: 3,
      antiguedadInmueble: 10,
      calefaccion: false,
      aireAcondicionado: true,
    },
  })

  const onSubmit = async (data: AnalysisFormData) => {
    setGeneralError(null)
    setLastInput(data)
    try {
      const result = await mutation.mutateAsync(data)
      if (result) {
        navigate('/resultados')
      }
    } catch (err) {
      if (err instanceof ApiError) {
        if (err.status === 400 && err.details) {
          // Mapear errores de campo del backend al formulario
          const fieldMap: Record<string, keyof AnalysisFormData> = {
            consumoKwh: 'consumoKwh',
            usoHorarioPico: 'usoHorarioPico',
            cantidadEquipos: 'cantidadEquipos',
            tipoInmueble: 'tipoInmueble',
            horasAltoConsumo: 'horasAltoConsumo',
            numeroHabitantes: 'numeroHabitantes',
            antiguedadInmueble: 'antiguedadInmueble',
            calefaccion: 'calefaccion',
            aireAcondicionado: 'aireAcondicionado',
          }
          let hasFieldError = false
          Object.entries(err.details).forEach(([campo, mensaje]) => {
            const fieldName = fieldMap[campo]
            if (fieldName) {
              setError(fieldName, { message: mensaje })
              hasFieldError = true
            }
          })
          if (!hasFieldError) {
            setGeneralError(err.message)
          }
        } else {
          setGeneralError(err.message)
        }
      } else {
        setGeneralError('Ocurrió un error inesperado. Intenta de nuevo.')
      }
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} noValidate>
      {/* Error general del backend */}
      {generalError && (
        <div className="form-error-general" role="alert">
          {generalError}
        </div>
      )}

      {/* Fila: Consumo + Cantidad de equipos */}
      <div className="form-row">
        <Input
          label="Consumo mensual (kWh)"
          type="number"
          step="any"
          placeholder="Ej: 420"
          error={errors.consumoKwh?.message}
          {...register('consumoKwh', { valueAsNumber: true })}
        />
        <Input
          label="Cantidad de equipos"
          type="number"
          min={1}
          max={50}
          placeholder="Ej: 10"
          error={errors.cantidadEquipos?.message}
          {...register('cantidadEquipos', { valueAsNumber: true })}
        />
      </div>

      {/* Fila: Número de habitantes + Antigüedad */}
      <div className="form-row">
        <Input
          label="Número de habitantes"
          type="number"
          min={1}
          max={6}
          placeholder="Ej: 3"
          error={errors.numeroHabitantes?.message}
          {...register('numeroHabitantes', { valueAsNumber: true })}
        />
        <Input
          label="Antigüedad del inmueble (años)"
          type="number"
          min={0}
          max={50}
          placeholder="Ej: 10"
          error={errors.antiguedadInmueble?.message}
          {...register('antiguedadInmueble', { valueAsNumber: true })}
        />
      </div>

      {/* Fila: Tipo de inmueble + Horas de alto consumo */}
      <div className="form-row">
        <div className="form-field">
          <label htmlFor="tipoInmueble" className="form-field__label">
            Tipo de inmueble
          </label>
          <select
            id="tipoInmueble"
            className={`form-field__input ${errors.tipoInmueble ? 'form-field__input--error' : ''}`}
            {...register('tipoInmueble')}
          >
            <option value="">Seleccionar...</option>
            {inmuebles.map((t) => (
              <option key={t} value={t}>{t}</option>
            ))}
          </select>
          {errors.tipoInmueble && (
            <p className="form-field__error">{errors.tipoInmueble.message}</p>
          )}
        </div>

        <Input
          label="Horas de alto consumo al día"
          type="number"
          min={0}
          max={24}
          placeholder="Ej: 8"
          error={errors.horasAltoConsumo?.message}
          {...register('horasAltoConsumo', { valueAsNumber: true })}
        />
      </div>

      {/* Sección de checkboxes */}
      <div className="form-checkboxes">
        <div className="form-field form-field--checkbox">
          <input
            id="usoHorarioPico"
            type="checkbox"
            className="form-field__checkbox"
            {...register('usoHorarioPico')}
          />
          <label htmlFor="usoHorarioPico" className="form-field__label form-field__label--inline">
            Consumo en horario pico (18:00 – 22:00)
          </label>
        </div>

        <div className="form-field form-field--checkbox">
          <input
            id="calefaccion"
            type="checkbox"
            className="form-field__checkbox"
            {...register('calefaccion')}
          />
          <label htmlFor="calefaccion" className="form-field__label form-field__label--inline">
            Calefacción eléctrica
          </label>
        </div>

        <div className="form-field form-field--checkbox">
          <input
            id="aireAcondicionado"
            type="checkbox"
            className="form-field__checkbox"
            {...register('aireAcondicionado')}
          />
          <label htmlFor="aireAcondicionado" className="form-field__label form-field__label--inline">
            Aire acondicionado
          </label>
        </div>
      </div>

      <div className="form-card__actions">
        <Button type="submit" variant="primary" disabled={isSubmitting || mutation.isPending}>
          {isSubmitting || mutation.isPending ? 'Analizando...' : 'Analizar consumo'}
        </Button>
      </div>
    </form>
  )
}
