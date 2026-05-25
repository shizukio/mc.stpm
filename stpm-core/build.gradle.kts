plugins {
    kotlin("jvm")
}

dependencies {
    // core は loader 非依存にします。
    // サーバー実装や command framework への依存は loader module 側へ置きます。
    implementation(kotlin("stdlib"))
}

kotlin {
    jvmToolchain(21)
}
