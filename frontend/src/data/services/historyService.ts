import type { HistorialEntryDto, HistoryEntry } from '../types/analysis'

// El historial vive en el backend (tabla analisis_energetico, filtrada por usuario):
// GET /api/analisis/historial para la lista y GET /api/analisis/{id} para el detalle.

// Mapea el DTO del backend (AnalisisHistorialResponse) al shape local HistoryEntry
export function mapHistorialEntry(dto: HistorialEntryDto): HistoryEntry {
  return {
    id: String(dto.idAnalisis),
    input: {
      consumoKwh: dto.consumoKwh,
      usoHorarioPico: dto.usoHorarioPico,
      cantidadEquipos: dto.cantidadEquipos,
      tipoInmueble: dto.tipoInmueble,
      horasAltoConsumo: dto.horasAltoConsumo,
      numeroHabitantes: dto.numeroHabitantes,
      antiguedadInmueble: dto.antiguedadInmueble,
      calefaccion: dto.calefaccion,
      aireAcondicionado: dto.aireAcondicionado,
    },
    result: {
      categoria: dto.categoria,
      probabilidad: dto.probabilidad,
      recomendaciones: dto.recomendaciones,
      costo_estimado_mensual: dto.costo_estimado_mensual,
      idAnalisis: dto.idAnalisis,
    },
    created_at: dto.fecha_creacion,
  }
}
