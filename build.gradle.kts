plugins {
    `java-library`
}

// Common Configure
val zxingVersion       = "3.3.3"
val lombokVersion      = "1.18.4"
val javaVersion        = JavaVersion.VERSION_1_8

// 项目信息
group = "org.cat73"
version = "1.0.0-SNAPSHOT"

// Java 版本
configure<JavaPluginConvention> {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

// 编码
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// 仓库配置
repositories {
    jcenter()
}

// 公共依赖
dependencies {
    api                     ("com.google.zxing:core:$zxingVersion")
    annotationProcessor     ("org.projectlombok:lombok:$lombokVersion")
    compileOnly             ("org.projectlombok:lombok:$lombokVersion")
}
