import * as squint_core from 'squint-cljs/core.js';
import * as three from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { EffectComposer } from 'three/addons/postprocessing/EffectComposer.js';
import { ShaderPass } from 'three/addons/postprocessing/ShaderPass.js';
import { RenderPass } from 'three/addons/postprocessing/RenderPass.js';
import { RGBShiftShader } from 'three/addons/shaders/RGBShiftShader.js';
import { DotScreenShader } from 'three/addons/shaders/DotScreenShader.js';
import { OutputPass } from 'three/addons/postprocessing/OutputPass.js';
import * as d from './deviceOrientation.mjs';
var cubeOrientation = squint_core.atom(({ "a": 0, "b": 0, "g": 0 }));
var scene = new three.Scene();
var camera = new three.PerspectiveCamera(75, (window.innerWidth / window.innerHeight), 0.1, 100);
camera.position.set(0, 10, 20);
var renderer = new three.WebGLRenderer();
renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);
var controls = new OrbitControls(camera, renderer.domElement);
controls.target.set(0, 5, 0);
controls.update();
var create_cube = function () {
let geometry1 = new three.BoxGeometry(1, 1, 1);
let material2 = new three.MeshPhongMaterial(({ "color": 65280 }));
let cube3 = new three.Mesh(geometry1, material2);
cube3.position.set(0, 5, 0);
return cube3
};
var cube = create_cube();
scene.add(cube);
var create_plane = function () {
let planeSize1 = 40;
let repeats2 = (planeSize1 / 2);
let loader3 = new three.TextureLoader();
let texture4 = loader3.load("checker.png");
let planeGeo5 = new three.PlaneGeometry(planeSize1, planeSize1);
let planeMat6 = new three.MeshPhongMaterial(({ "map": texture4, "side": three.DoubleSide }));
let mesh7 = new three.Mesh(planeGeo5, planeMat6);
texture4.wrapS = three.RepeatWrapping;
texture4.wrapT = three.RepeatWrapping;
texture4.magFilter = three.NearestFilter;
texture4.colorSpace = three.SRGBColorSpace;
texture4.repeat.set(repeats2, repeats2);
mesh7.rotation.x = (Math.PI * -0.5);
return mesh7
};
var plane = create_plane();
scene.add(plane);
var create_ambient_light = function () {
let color1 = 16777215;
let intensity2 = 1;
let light3 = new three.AmbientLight(color1, intensity2);
return light3
};
scene.add(create_ambient_light());
var light = new three.PointLight(4210816, 950);
light.position.set(0, 7, 0);
light.castShadow = true;
var helper = new three.PointLightHelper(light);
scene.add(light);
scene.add(helper);
var composer = new EffectComposer(renderer);
composer.addPass(new RenderPass(scene, camera));
var effect = new ShaderPass(DotScreenShader);
effect.uniforms.scale.value = 4;
composer.addPass(effect);
var effect1 = new ShaderPass(RGBShiftShader);
effect1.uniforms.amount.value = 0.0015;
composer.addPass(effect1);
composer.addPass(new OutputPass());
var animate = function () {
requestAnimationFrame(animate);
let alphaDegrees1 = squint_core.get(squint_core.deref(cubeOrientation), "a");
let betaDegrees2 = squint_core.get(squint_core.deref(cubeOrientation), "b");
let gammaDegrees3 = squint_core.get(squint_core.deref(cubeOrientation), "g");
let a4 = d.degree__GT_radians(alphaDegrees1);
let b5 = d.degree__GT_radians(betaDegrees2);
let g6 = (-1 * d.degree__GT_radians(gammaDegrees3));
let euler7 = new three.Euler(b5, a4, g6, "XYZ");
let quaternion8 = new three.Quaternion().setFromEuler(euler7);
cube.setRotationFromQuaternion(quaternion8);
return composer.render()
};
animate();
var orientationHandler = function (e) {
console.log(e);
return squint_core.reset_BANG_(cubeOrientation, ({ "a": e.alpha, "b": e.beta, "g": e.gamma }))
};
d.orientationInit(orientationHandler);

export { create_cube, light, renderer, orientationHandler, effect1, create_ambient_light, camera, cube, animate, helper, composer, plane, effect, controls, create_plane, cubeOrientation, scene }
