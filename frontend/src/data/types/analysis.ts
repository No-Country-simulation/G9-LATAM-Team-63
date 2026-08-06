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
}

export interface HistoryEntry {
  id: string
  input: AnalysisInput
  result: AnalysisResult
  created_at: string
}
