import * as squint_core from 'squint-cljs/core.js';
;
var registerDeviceMotionListener = function (handler) {
return window.addEventListener("deviceorientation", handler)
};
var handlePermissionResponse = function (state, handler) {
if ((state === "granted")) {
return registerDeviceMotionListener(handler)} else {
return console.error("Request to access orientation was rejected")}
};
var orientationInit = function (handler) {
if ((typeof DeviceMotionEvent.requestPermission === "function")) {
return DeviceMotionEvent.requestPermission().then((function (state) {
return handlePermissionResponse(state, handler)
}))} else {
return registerDeviceMotionListener(handler)}
};
var degree__GT_radians = function (degrees) {
return (degrees * (Math.PI / 180))
};

export { registerDeviceMotionListener, handlePermissionResponse, orientationInit, degree__GT_radians }
