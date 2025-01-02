import toni.blahaj.*
import toni.blahaj.api.*

val templateSettings = object : BlahajSettings {
	// -------------------- Dependencies ---------------------- //
	override val depsHandler: BlahajDependencyHandler get() = object : BlahajDependencyHandler {
		override fun addGlobal(mod : ModData, deps: DependencyHandler) {

		}

		override fun addFabric(mod : ModData, deps: DependencyHandler) {
			deps.modImplementation(modrinth("sodium", when (mod.mcVersion) {
				"1.21.4" -> "mc1.21.4-0.6.6-fabric"
				"1.21.1" -> "mc1.21.1-0.6.5-fabric"
				"1.20.1" -> "mc1.20.1-0.5.11"
				else -> ""
			}))
		}

		override fun addForge(mod : ModData, deps: DependencyHandler) {
			deps.modImplementation(modrinth("embeddium", "0.3.31+mc1.20.1"))
			deps.compileOnly(deps.annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")!!)
			deps.include(deps.implementation("io.github.llamalad7:mixinextras-forge:0.3.5")!!)
		}

		override fun addNeo(mod : ModData, deps: DependencyHandler) {

			when (mod.mcVersion)
			{
				"1.21.4" -> {
					deps.implementation(modrinth("sodium", "mc1.21.4-0.6.6-neoforge"))

					deps.compileOnly("org.sinytra.forgified-fabric-api:fabric-api-base:0.4.42+d1308ded19")
					deps.compileOnly("org.sinytra.forgified-fabric-api:fabric-renderer-api-v1:5.0.0+babc52e504")
					deps.compileOnly("org.sinytra.forgified-fabric-api:fabric-rendering-data-attachment-v1:0.3.48+73761d2e19")
					deps.compileOnly("org.sinytra.forgified-fabric-api:fabric-block-view-api-v2:1.0.10+9afaaf8c19")
				}
				"1.21.1" -> {
					deps.implementation(modrinth("sodium", "mc1.21.1-0.6.5-neoforge"))

					deps.compileOnly("org.sinytra.forgified-fabric-api:fabric-api-base:0.4.42+d1308dedd1")
					deps.compileOnly("org.sinytra.forgified-fabric-api:fabric-renderer-api-v1:3.4.0+acb05a39d1")
				}
			}
		}
	}

	// ---------- Curseforge/Modrinth Configuration ----------- //
	// For configuring the dependecies that will show up on your mod page.
	override val publishHandler: BlahajPublishDependencyHandler get() = object : BlahajPublishDependencyHandler {
		override fun addShared(mod : ModData, deps: DependencyContainer) {
			deps.requires("sodium-options-api")

			if (mod.isFabric) {
				deps.requires("fabric-api")
			}

			if (mod.isForge)
				deps.requires("embeddium")
			else
				deps.requires("sodium")
		}

		override fun addCurseForge(mod : ModData, deps: DependencyContainer) {

		}

		override fun addModrinth(mod : ModData, deps: DependencyContainer) {

		}
	}
}

plugins {
	`maven-publish`
	application
	id("toni.blahaj") version "1.0.15"
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("dev.kikugie.j52j") version "1.0"
	id("dev.architectury.loom")
	id("me.modmuss50.mod-publish-plugin")
	id("systems.manifold.manifold-gradle-plugin")
}

blahaj {
	sc = stonecutter
	settings = templateSettings
	init()
}

// Dependencies
repositories {
	maven("https://maven.pkg.github.com/ims212/ForgifiedFabricAPI") {
		credentials {
			username = "IMS212"
			// Read only token
			password = "ghp_" + "DEuGv0Z56vnSOYKLCXdsS9svK4nb9K39C1Hn"
		}
	}
	maven("https://www.cursemaven.com")
	maven("https://api.modrinth.com/maven")
	maven("https://thedarkcolour.github.io/KotlinForForge/")
	maven("https://maven.kikugie.dev/releases")
	maven("https://maven.txni.dev/releases")
	maven("https://jitpack.io")
	maven("https://maven.neoforged.net/releases/")
	maven("https://maven.terraformersmc.com/releases/")
	maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
	maven("https://maven.parchmentmc.org")
	maven("https://maven.su5ed.dev/releases")
	maven("https://maven.su5ed.dev/releases")
	maven("https://maven.fabricmc.net")
	maven("https://maven.shedaniel.me/")
}
