import { z } from 'zod'

// Este schema replica exactamente el contrato de AnalisisRequest del backend:
// campos en camelCase, tipoInmueble restringido y rangos de validación iguales
// (cantidadEquipos 1-50, horasAltoConsumo 0-24, consumoKwh > 0).
export const analysisSchema = z.object({
  consumoKwh: z
    .number({ message: 'Debe ser un número' })
    .positive('Debe ser mayor a 0')
    .max(9999, 'Valor muy alto'),
  usoHorarioPico: z.boolean(),
  cantidadEquipos: z
    .number({ message: 'Debe ser un número' })
    .int('Debe ser un número entero')
    .min(1, 'Debe haber al menos 1 equipo')
    .max(50, 'La cantidad máxima de equipos es 50'),
  tipoInmueble: z.enum(
    ['Casa', 'Apartamento', 'Local', 'Oficina'],
    { message: 'Selecciona un tipo de inmueble' }
  ),
  horasAltoConsumo: z
    .number({ message: 'Debe ser un número' })
    .int('Debe ser un número entero')
    .min(0, 'Las horas no pueden ser negativas')
    .max(24, 'Las horas no pueden exceder 24'),
  // Campos nuevos — igual que el backend
  numeroHabitantes: z
    .number({ message: 'Debe ser un número' })
    .int('Debe ser un número entero')
    .min(1, 'Debe haber al menos 1 habitante')
    .max(6, 'El máximo permitido es 6 habitantes'),
  antiguedadInmueble: z
    .number({ message: 'Debe ser un número' })
    .int('Debe ser un número entero')
    .min(0, 'La antigüedad no puede ser negativa')
    .max(50, 'El máximo permitido es 50 años'),
  calefaccion: z.boolean(),
  aireAcondicionado: z.boolean(),
})

export type AnalysisFormData = z.infer<typeof analysisSchema>
