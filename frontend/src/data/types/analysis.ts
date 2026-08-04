// Contrato exacto con los DTOs del backend:
// AnalisisRequest (entrada) y AnalisisResponse (salida)
export interface AnalysisInput {
  consumoKwh: number
  usoHorarioPico: boolean
  cantidadEquipos: number
  tipoInmueble: 'Casa' | 'Apartamento' | 'Local' | 'Oficina'
  horasAltoConsumo: number
}

export interface AnalysisResult {
  categoria: 'Eficiente' | 'Moderado' | 'Ineficiente'
  probabilidad: number
  recomendaciones: string[]
  // El backend serializa este campo con guion bajo (@JsonProperty)
  costo_estimado_mensual: number
  // Solo lo devuelve el backend; el fallback local no lo genera
  idAnalisis?: number
}

export interface HistoryEntry {
  id: string
  input: AnalysisInput
  result: AnalysisResult
  created_at: string
}
