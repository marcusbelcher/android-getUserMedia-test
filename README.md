# README
This repo is a simple test case for anyone to use - as is - to check JavaScript WebRTC / Camera access via getUserMedia inside a WebView in an Android application. The Application - as default - views another test application I have hosted on GitHub pages:(https://github.com/marcusbelcher/wasm-asm-camera-webgl-test). 

## Build
- Checkout this repo
- Open with Android Studio 3.0.1
- Connect an actual device (couldnt get it working in an emulator)
- Build to actual device
- Enable permissions 
- Run app

## 3rd Parties
- https://webrtc.org/native-code/android/

## Issues encountered
- Java Versions: API 21, Androud Studio 3+, WebRTC dependency requires Graddle build settings need the following to allow API level 21 to 26.  
    `compileOptions {
        targetCompatibility 1.8
        sourceCompatibility 1.8
    }`
- WebRTC / getUserMedia access: Min API version of 21, this is for runtime onPermissionRequest

## Useful links
- https://github.com/webrtc/adapter (https://webrtc.github.io/adapter/adapter-latest.js) 
- https://www.webrtc-experiment.com/DetectRTC/
- https://stackoverflow.com/questions/48775154/notreadableerror-could-not-start-source
- https://github.com/googlesamples/android-PermissionRequest
- https://github.com/googlearchive/chromium-webview-samples/tree/master/webrtc-example
- https://bintray.com/package/readme/google/webrtc/google-webrtc