import * as THREE from "three"

const HeroLights = () => {
  return (
    <>
      {/* Key light – principal */}
      <spotLight
        position={[6, 6, 6]}
        target-position={[0, -2, 0]}
        angle={0.3}
        penumbra={0.7}
        intensity={130}
        color="#f1e8d9"
        castShadow
        shadow-mapSize={[2048, 2048]}
      />

      {/* Rim light – recorte atrás */}
      <spotLight
        position={[-7, 5, -6]}
        angle={0.4}
        penumbra={1}
        intensity={80}
        color="#6c8cff"
      />

      {/* Fill light – bem sutil */}
      <pointLight position={[0, 2, 6]} intensity={18} color="#ffffff" />

      {/* Luz de fundo – profundidade */}
      <pointLight position={[0, 1, -6]} intensity={10} color="#1b1f3b" />

      {/* Area light – efeito tela/janela */}
      <primitive
        object={new THREE.RectAreaLight("#ffffff", 5, 2.5)}
        position={[0, 2.5, 5]}
        intensity={16}
        rotation={[0, Math.PI, 0]}
      />

      {/* Ambiente mínimo */}
      <ambientLight intensity={0.12} />
    </>
  )
}

export default HeroLights
