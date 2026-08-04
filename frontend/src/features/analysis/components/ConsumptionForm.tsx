import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useNavigate } from 'react-router-dom'
import { analysisSchema, type AnalysisFormData } from '../schema'
import { useAnalysisMutation } from '../../../data/hooks/useAnalysis'
import { useAnalysisStore } from '../../../store/analysisStore'
import Input from '../../../shared/components/Input'
import Button from '../../../shared/components/Button'

const inmuebles = ['Casa', 'Apartamento', 'Local', 'Oficina']

export default function ConsumptionForm() {
  const navigate = useNavigate()
  const setLastInput = useAnalysisStore((s) => s.setLastInput)
  const mutation = useAnalysisMutation()

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<AnalysisFormData>({
    resolver: zodResolver(analysisSchema),
    defaultValues: {
      consumoKwh: 420,
      usoHorarioPico: true,
      cantidadEquipos: 10,
      tipoInmueble: 'Casa',
      horasAltoConsumo: 8,
    },
  })

  const onSubmit = async (data: AnalysisFormData) => {
    setLastInput(data)
    const result = await mutation.mutateAsync(data)
    if (result) {
      navigate('/resultados')
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} noValidate>
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

      <div className="form-field">
        <label htmlFor="tipoInmueble" className="form-field__label">Tipo de inmueble</label>
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
        {errors.tipoInmueble && <p className="form-field__error">{errors.tipoInmueble.message}</p>}
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

      <div className="form-field form-field--checkbox">
        <input
          id="usoHorarioPico"
          type="checkbox"
          className="form-field__input"
          style={{ width: 'auto' }}
          {...register('usoHorarioPico')}
        />
        <label htmlFor="usoHorarioPico" className="form-field__label" style={{ marginBottom: 0 }}>
          Consumo en horario pico (18:00 – 22:00)
        </label>
      </div>

      <div className="form-card__actions">
        <Button type="submit" variant="primary" disabled={isSubmitting}>
          {isSubmitting ? 'Analizando...' : 'Analizar consumo'}
        </Button>
      </div>
    </form>
  )
}
