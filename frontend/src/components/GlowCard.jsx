import { useRef } from "react"

const GlowCard = ({ card, children, index }) => {
  const cardRefs = useRef([])
  const handlerMouseMove = (index) => (e) => {
    const card = cardRefs.current[index]
    if (!card) return

    // get the position of the card
    const rect = card.getBoundingClientRect()
    const x = e.clientX - rect.left - rect.width / 2
    const y = e.clientY - rect.top - rect.height / 2

    // calculate the angle
    let angle = Math.atan2(y, x) * (180 / Math.PI)

    angle = (angle + 360) % 360

    card.style.setProperty("--start", angle + 60)
  }

  return (
    <div
      ref={(el) => (cardRefs.current[index] = el)}
      onMouseMove={handlerMouseMove(index)}
      className="card card-border timeline-card rounded-xl p-10"
    >
      <div className="glow" />
      <div className="flex items-center gap-1 mb-5">
        <h1 className="font-semibold text-lg">{card.tag}</h1>
      </div>
      <div className="mb-5">
        <p className="text-white-50 text-lg">{card.review}</p>
      </div>
      {children}
    </div>
  )
}

export default GlowCard
