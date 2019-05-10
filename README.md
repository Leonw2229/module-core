# ModuleCore
[![](https://jitpack.io/v/Leonw2229/ModuleCore.svg)](https://jitpack.io/#Leonw2229/ModuleCore)
一个项目基础架构的简单抽象

### 1、设置jitpack仓库
```gradle
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```
### 2、添加仓库依赖
```gradle
implementation 'com.github.Leonw2229.module-core:module_arch:v0.0.1'
```
### 3、已包含的依赖列表
```gradle
##  module_arch
api 'io.reactivex.rxjava2:rxandroid:2.1.1'
api 'io.reactivex.rxjava2:rxjava:2.2.8'

api "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.31"
api 'org.jetbrains.anko:anko-commons:0.10.8'
api 'androidx.core:core-ktx:1.1.0-alpha05'
api 'androidx.lifecycle:lifecycle-extensions:2.1.0-alpha04'
api 'com.github.lmyDevs:sweet-alert-dialog:v1.4.4'
api 'com.gyf.immersionbar:immersionbar:2.3.3'
api 'com.alibaba:arouter-api:1.4.1'
api 'com.blankj:utilcodex:1.23.7'
  
implementation 'androidx.appcompat:appcompat:1.1.0-alpha04'
    
##  module_network
api 'com.google.code.gson:gson:2.8.5'
api 'com.squareup.okhttp3:okhttp:3.14.1'
api 'com.squareup.okhttp3:logging-interceptor:3.14.1'
api 'com.squareup.retrofit2:retrofit:2.5.0'
api 'com.squareup.retrofit2:converter-gson:2.5.0'
api 'com.squareup.retrofit2:adapter-rxjava2:2.5.0'
api 'io.reactivex.rxjava2:rxandroid:2.1.1'
api 'io.reactivex.rxjava2:rxjava:2.2.8'
```
