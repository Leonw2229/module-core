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
implementation 'androidx.appcompat:appcompat:1.1.0-alpha05'
implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
implementation 'io.reactivex.rxjava2:rxjava:2.2.8'

implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
implementation 'org.jetbrains.anko:anko-commons:0.10.8'
implementation 'androidx.core:core-ktx:1.2.0-alpha01'
implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0-alpha01'
implementation 'com.github.lmyDevs:sweet-alert-dialog:v1.4.4'
implementation 'com.gyf.immersionbar:immersionbar:2.3.3'
implementation 'com.alibaba:arouter-api:1.4.1'
implementation 'com.blankj:utilcodex:1.23.7'
    
##  module_network
implementation 'com.google.code.gson:gson:2.8.5'
implementation 'com.squareup.okhttp3:okhttp:3.14.1'
implementation 'com.squareup.okhttp3:logging-interceptor:3.14.1'
implementation 'com.squareup.retrofit2:retrofit:2.5.0'
implementation 'com.squareup.retrofit2:converter-gson:2.5.0'
implementation 'com.squareup.retrofit2:adapter-rxjava2:2.5.0'
implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
implementation 'io.reactivex.rxjava2:rxjava:2.2.8'
implementation"org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
implementation 'org.jetbrains.anko:anko-commons:0.10.8'

implementation 'androidx.appcompat:appcompat:1.1.0-alpha05'
```
