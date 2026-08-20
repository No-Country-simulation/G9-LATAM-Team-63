// Estadísticas verificables con el funcionamiento real de la aplicación:
// modelo XGBoost, probabilidades por clase, API real y 9 variables.
export const stats = [
  {
    number: '3',
    label: 'Perfiles de eficiencia',
    desc: 'Eficiente, Moderado e Ineficiente',
    gradient: 'var(--gradient-accent)',
  },
  {
    number: '9',
    label: 'Variables analizadas',
    desc: 'consumo, equipos, horarios e inmueble (entre otras)',
    gradient: 'linear-gradient(135deg, #2563eb, #3b82f6)',
  },
  {
    number: 'XGBoost',
    label: 'Motor de clasificación',
    desc: 'con probabilidades por clase (distancias)',
    gradient: 'linear-gradient(135deg, #3b82f6, #6366f1)',
  },
  {
    number: '<1s',
    label: 'Tiempo de respuesta',
    desc: 'de la API REST en producción',
    gradient: 'linear-gradient(135deg, #6366f1, #7c3aed)',
  },
]
