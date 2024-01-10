(ns deviceOrientation)

(def access-granted (atom "Waiting for acces"))

(defn registerDeviceMotionListener [handler]
  (reset! access-granted "Access granted")
  (js/window.addEventListener "deviceorientation" handler))

(defn handlePermissionResponse [state handler]
  (if (= state "granted")
    (registerDeviceMotionListener handler)
    (do (js/console.error "Request to access orientation was rejected")
        (reset! access-granted "Rejected access"))))

(defn orientationInit [handler]
  (if (= (js/typeof js/DeviceMotionEvent.requestPermission) "function")
    (-> (js/DeviceMotionEvent.requestPermission)
        (.then (fn [state] (handlePermissionResponse state handler))))
    (registerDeviceMotionListener handler)))

(defn degree->radians [degrees]
  (* degrees (/ js/Math.PI 180)))
