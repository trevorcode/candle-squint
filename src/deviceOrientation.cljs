(ns deviceOrientation)


(defn registerDeviceMotionListener [handler]
  (js/window.addEventListener "deviceorientation" handler))

(defn handlePermissionResponse [state handler]
  (if (= state "granted")
    (registerDeviceMotionListener handler)
    (js/console.error "Request to access orientation was rejected")))

(defn orientationInit [handler]
  (if (= (js/typeof js/DeviceMotionEvent.requestPermission) "function")
    (-> (js/DeviceMotionEvent.requestPermission)
        (.then (fn [state] (handlePermissionResponse state handler))))
    (registerDeviceMotionListener handler)))

(defn degree->radians [degrees]
    (* degrees (/ js/Math.PI 180)))
