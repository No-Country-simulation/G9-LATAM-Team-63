import type { IconName } from '../../../shared/components/Icons'

interface Tech {
  icon: IconName
  name: string
  desc: string
}

export const techs: Tech[] = [
  {
    icon: 'python',
    name: 'Python & Scikit-Learn',
    desc: 'Modelos supervisados: Regresión Logística, Random Forest y Árboles de Decisión para clasificación de perfiles energéticos.',
  },
  {
    icon: 'cloud',
    name: 'Oracle Cloud (OCI)',
    desc: 'Object Storage para modelos, OCI Compute para la API y OCI Functions para procesamiento escalable bajo demanda.',
  },
  {
    icon: 'coffee',
    name: 'Java Spring Boot',
    desc: 'API REST robusta con validación de entrada, manejo de errores, documentación OpenAPI y alta capacidad de respuesta.',
  },
  {
    icon: 'atom',
    name: 'React + TypeScript',
    desc: 'Interfaz moderna con componentes reutilizables, tipado estático y visualización dinámica de resultados de análisis.',
  },
]
