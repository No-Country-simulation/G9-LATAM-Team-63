// Contrato exacto con los DTOs del backend:
// AnalisisRequest (entrada) y AnalisisResponse (salida)
export interface AnalysisInput {
  consumoKwh: number
  usoHorarioPico: boolean
  cantidadEquipos: number
  tipoInmueble: 'Casa' | 'Apartamento' | 'Local' | 'Oficina'
  horasAltoConsumo: number
  // Campos nuevos — requeridos por el backend
  numeroHabitantes: number
  antiguedadInmueble: number
  calefaccion: boolean
  aireAcondicionado: boolean
}

export interface AnalysisResult {
  categoria: 'Eficiente' | 'Moderado' | 'Ineficiente'
  probabilidad: number
  recomendaciones: string[]
  // El backend serializa este campo con guion bajo (@JsonProperty)
  costo_estimado_mensual: number
  // Siempre presente en respuesta real del backend
  idAnalisis: number
  // Contrato del modelo de ciencia de datos (API Python):
  // probabilidad asignada a CADA clase. Permite mostrar la distribución
  // completa del modelo. Ausente en respuestas de Spring Boot.
  distancias?: Record<'Eficiente' | 'Moderado' | 'Ineficiente', number>
}

export interface HistoryEntry {
  id: string
  input: AnalysisInput
  result: AnalysisResult
  created_at: string
}

// Contrato exacto con AnalisisHistorialResponse del backend:
// usado por GET /api/analisis/historial y GET /api/analisis/{id}
export interface HistorialEntryDto {
  idAnalisis: number
  consumoKwh: number
  usoHorarioPico: boolean
  cantidadEquipos: number
  tipoInmueble: 'Casa' | 'Apartamento' | 'Local' | 'Oficina'
  numeroHabitantes: number
  antiguedadInmueble: number
  calefaccion: boolean
  aireAcondicionado: boolean
  horasAltoConsumo: number
  categoria: 'Eficiente' | 'Moderado' | 'Ineficiente'
  probabilidad: number
  recomendaciones: string[]
  costo_estimado_mensual: number
  fecha_creacion: string
}
