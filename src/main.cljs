(ns main
  (:require ["three" :as three]
            ["three/addons/controls/OrbitControls.js" :refer [OrbitControls]]
            ["three/addons/postprocessing/EffectComposer.js" :refer [EffectComposer]]
            ["three/addons/postprocessing/ShaderPass.js" :refer [ShaderPass]]
            ["three/addons/postprocessing/RenderPass.js" :refer [RenderPass]]

            ["three/addons/shaders/RGBShiftShader.js" :refer [RGBShiftShader]]
            ["three/addons/shaders/DotScreenShader.js" :refer [DotScreenShader]]

            ["three/addons/postprocessing/OutputPass.js" :refer [OutputPass]]
            [deviceOrientation :as d]))

(def cubeOrientation (atom {:a 0 :b 0 :g 0}))

(def scene (three.Scene.))
(def camera (three.PerspectiveCamera.
             75
             (/ (.-innerWidth js/window)
                (.-innerHeight js/window))
             0.1
             100))

(.set camera.position 0 10 20)

(def renderer (three.WebGLRenderer.))
(.setSize renderer (.-innerWidth js/window) (.-innerHeight js/window))

(js/document.body.appendChild (.-domElement renderer))

(def controls (OrbitControls. camera renderer.domElement))
(.set (.-target controls) 0 5 0)
(.update controls)

(defn create-cube []
  (let [geometry (three.BoxGeometry. 1 1 1)
        material (three.MeshPhongMaterial. {:color 0x00ff00})
        cube (three.Mesh. geometry material)]
    (.set cube.position 0 5 0)
    cube))

(def cube (create-cube))
(.add scene cube)

(defn create-plane []
  (let [planeSize 40
        repeats (/ planeSize 2)
        loader (three.TextureLoader.)
        texture (.load loader "checker.png")
        planeGeo (three.PlaneGeometry. planeSize planeSize)
        planeMat (three.MeshPhongMaterial. {:map texture
                                            :side three.DoubleSide})
        mesh (three.Mesh. planeGeo planeMat)]
    (set! (.-wrapS texture) three.RepeatWrapping)
    (set! (.-wrapT texture) three.RepeatWrapping)
    (set! (.-magFilter texture) three.NearestFilter)
    (set! (.-colorSpace texture) three.SRGBColorSpace)
    (. (.. texture -repeat) set repeats repeats)
    (set! (.. mesh -rotation -x) (* js/Math.PI -0.5))
    mesh))

(def plane (create-plane))
(.add scene plane)

(defn create-ambient-light []
  (let [color 0xFFFFFF
        intensity 1
        light (three.AmbientLight. color intensity)]
    light))

(.add scene (create-ambient-light))

(def light (three.PointLight. 0x404080 950))
(.set light.position 0 7 0)
(set! (.-castShadow light) true)

(def helper (three.PointLightHelper. light))


(.add scene light)
(.add scene helper)

(def composer (EffectComposer. renderer))
(.addPass composer (RenderPass. scene camera))

(def effect (ShaderPass. DotScreenShader))
(set! effect.uniforms.scale.value 4)
(.addPass composer effect)

(def effect1 (ShaderPass. RGBShiftShader))
(set! effect1.uniforms.amount.value 0.0015)
(.addPass composer effect1)

(.addPass composer (OutputPass.))

(defn animate []
  (js/requestAnimationFrame animate)

  (let [alphaDegrees (:a @cubeOrientation)
        betaDegrees (:b @cubeOrientation)
        gammaDegrees (:g @cubeOrientation)
        a (d/degree->radians alphaDegrees)
        b (d/degree->radians betaDegrees)
        g (* -1 (d/degree->radians gammaDegrees))
        euler (three.Euler. b a g "XYZ")
        quaternion (.setFromEuler (three.Quaternion.) euler)]
    (.setRotationFromQuaternion cube quaternion))

  (.render composer))

(animate)

(defn orientationHandler [e]
  (js/console.log e)
  (reset! cubeOrientation {:a e.alpha :b e.beta :g e.gamma}))

(defn requestOrientationAccess []
  (d/orientationInit orientationHandler)
  (set! (js/document.querySelector "#access") -innerText @d/access-granted))

(-> (js/document.querySelector "#request-access")
    (.addEventListener "click" requestOrientationAccess))