import type { IconName } from '../../../shared/components/Icons'

interface Tech {
  icon: IconName
  name: string
  desc: string
}

export const techs: Tech[] = [
  {
    icon: 'python',
    name: 'Python & XGBoost',
    desc: 'Modelo XGBoost entrenado con datos reales para clasificación de perfiles energéticos. Devuelve categoría, probabilidad exacta y distribución por clase (distancias).',
  },
  {
    icon: 'cloud',
    name: 'Cloud Computing',
    desc: 'Almacenamiento de modelos serializados, procesamiento escalable y alojamiento de alta disponibilidad para la API en la nube.',
  },
  {
    icon: 'coffee',
    name: 'Java Spring Boot',
    desc: 'API REST robusta que actúa como proxy hacia el servicio Python: valida entradas, maneja errores, expone historial y gestión de usuarios con JWT.',
  },
  {
    icon: 'atom',
    name: 'React + TypeScript',
    desc: 'Interfaz moderna con componentes reutilizables, tipado estático y visualización dinámica de resultados de análisis.',
  },
]
