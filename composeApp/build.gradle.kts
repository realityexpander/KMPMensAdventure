import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.jetbrainsCompose)
}

kotlin {
	@OptIn(ExperimentalWasmDsl::class)
	wasmJs {
		moduleName = "composeApp"
		browser {
			commonWebpackConfig {
				outputFileName = "composeApp.js"
				devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
					static = (static ?: mutableListOf()).apply {
						// Serve sources to debug inside browser
						add(project.projectDir.path)
					}
				}
			}
		}
		binaries.executable()
	}

	jvm("desktop")

	sourceSets {
		val desktopMain by getting

		commonMain.dependencies {
			implementation(compose.runtime)
			implementation(compose.foundation)
			implementation(compose.material3)
			implementation(compose.ui)
			implementation(compose.components.resources)
			implementation(compose.components.uiToolingPreview)

			implementation("io.github.koalaplot:koalaplot-core:0.5.2")
			implementation("dev.chrisbanes.material3:material3-window-size-class-multiplatform:0.5.0")
		}
		desktopMain.dependencies {
			implementation(compose.desktop.currentOs)
		}
	}
}

compose.experimental {
	web.application {}
}

compose.desktop {
	application {
		mainClass = "MainKt"

		nativeDistributions {
			buildTypes.release {
				proguard {
					configurationFiles.from("compose-desktop.pro")
				}
			}

			targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
			packageName = "Mans Life"
			packageVersion = "1.0.0"
		}
	}
}

// Task to copy from ./composeApp/build/dist/wasmJs/productionExecutable to ./docs
tasks.register("copyWasmJsToDocs") {
	group = "build"
	doLast {
		val wasmJsDir = project.file("./build/dist/wasmJs/productionExecutable")
		val docsDir = file(rootDir.path + "/docs")
		wasmJsDir.copyRecursively(docsDir, overwrite = true)
	}
}
tasks.getByName("wasmJsBrowserDistribution").finalizedBy("copyWasmJsToDocs")


// Task to clean ./docs (docs is the dir that will be published to GitHub Pages)
tasks.register("cleanDocs") {
	group = "build"
	doLast {
		val docsDir = file(rootDir.path + "/docs")
		docsDir.deleteRecursively()
	}
}
tasks.getByName("clean").dependsOn("cleanDocs")
