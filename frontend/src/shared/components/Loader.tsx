interface LoaderProps {
  text?: string
}

export default function Loader({ text = 'Cargando...' }: LoaderProps) {
  return (
    <div className="loader" role="status" aria-label={text}>
      <div className="loader__spinner" />
      <p className="loader__text">{text}</p>
    </div>
  )
}
