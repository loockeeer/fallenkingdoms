import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.tools.ant.filters.FixCrLfFilter
import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.5.10"

	id("com.github.johnrengelman.shadow") version "7.0.0"
	id("com.github.gmazzo.buildconfig") version "3.0.0"
}

group = "fr.loockeeer"
version = "1.0.0"

repositories {
	mavenLocal()
	google()
	jcenter()
	mavenCentral()
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
	maven("https://oss.sonatype.org/content/repositories/snapshots")
	maven("https://oss.sonatype.org/content/repositories/central")
	maven("https://maven.enginehub.org/repo/")
}

val minecraft_version: String by project

dependencies {
	compileOnly("org.spigotmc", "spigot-api", "$minecraft_version-R0.1-SNAPSHOT")
	compileOnly("net.luckperms", "api", "5.3")
	implementation("fr.mrmicky", "fastboard", "1.2.0")
}

buildConfig {
	className("BuildConfig")
	packageName("$group.$name")
}

tasks {
	processResources {
		filter(FixCrLfFilter::class)
		filter(ReplaceTokens::class, "tokens" to mapOf("version" to project.version))
		filteringCharset = "UTF-8"
	}
	jar {
		enabled = false
	}
	build {
		dependsOn(shadowJar)
	}

	shadowJar {
		relocate("fr.mrmicky.fastboard", "fr.loockeeer.fallenkingdoms.fastboard")
	}

	withType<KotlinCompile> {
		kotlinOptions {
			jvmTarget = "16"
		}
	}
}