import { Link } from 'react-router-dom'
import { useHistory } from '../../../data/hooks/useHistory'
import { Icon } from '../../../shared/components/Icons'
import Loader from '../../../shared/components/Loader'

export default function HistoryPage() {
  const { data: history, isLoading, isError } = useHistory()

  return (
    <section className="history-page">
      <div className="container">
        <div className="history-page__header">
          <div className="section-label">Historial</div>
          <h1 className="section-title">
            Historial de{' '}
            <span className="gradient-text">análisis</span>
          </h1>
          <p className="section-subtitle" style={{ margin: '0 auto' }}>
            Revisa tus análisis anteriores y da seguimiento a tu eficiencia energética.
          </p>
        </div>

        {isLoading && <Loader text="Cargando historial..." />}

        {isError && (
          <div className="history-empty">
            <div className="history-empty__icon">
              <Icon name="alert" size={40} />
            </div>
            <h2 className="history-empty__title">Error al cargar</h2>
            <p className="history-empty__text">
              No pudimos conectar con el servidor. Intenta de nuevo más tarde.
            </p>
            <Link to="/" className="btn-secondary">Volver al inicio</Link>
          </div>
        )}

        {history && history.length === 0 && (
          <div className="history-empty">
            <div className="history-empty__icon">
              <Icon name="clipboard" size={40} />
            </div>
            <h2 className="history-empty__title">Sin análisis aún</h2>
            <p className="history-empty__text">
              Aún no has realizado ningún análisis. ¡Comienza ahora!
            </p>
            <Link to="/analizar" className="btn-primary">Analizar consumo</Link>
          </div>
        )}

        {history && history.length > 0 && (
          <div className="history-table">
            <table>
              <thead>
                <tr>
                  <th>Fecha</th>
                  <th>Consumo</th>
                  <th>Equipos</th>
                  <th>Perfil</th>
                  <th>Costo est.</th>
                  <th>Confianza</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {history.map((entry) => (
                  <tr key={entry.id} className="history-table__row">
                    <td>{new Date(entry.created_at).toLocaleDateString()}</td>
                    <td>{entry.input.consumoKwh} kWh</td>
                    <td>{entry.input.cantidadEquipos}</td>
                    <td>
                      <span className={`profile-badge profile-badge--${entry.result.categoria === 'Eficiente' ? 'eficiente' : entry.result.categoria === 'Moderado' ? 'moderado' : 'ineficiente'}`}>
                        {entry.result.categoria}
                      </span>
                    </td>
                    <td>${entry.result.costo_estimado_mensual.toFixed(2)}</td>
                    <td>
                      <span className="history-confidence">
                        {(entry.result.probabilidad * 100).toFixed(5)}%
                      </span>
                    </td>
                    <td className="history-table__action">
                      <Link
                        to={`/resultados/${entry.id}`}
                        className="history-table__view"
                        aria-label={`Ver detalle del análisis ${entry.id}`}
                      >
                        Ver detalle
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </section>
  )
}
