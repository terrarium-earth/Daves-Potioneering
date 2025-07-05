plugins {
    id("earth.terrarium.cloche") version "0.10.16"
}

repositories {
    cloche.librariesMinecraft()

    mavenCentral()

    cloche {
        main()

        mavenFabric()
        mavenNeoforgedMeta()
        mavenNeoforged()
    }
	
	maven(url = "https://maven.blamejared.com") {
        name = "BlameJared Maven (JEI / CraftTweaker / Bookshelf)"
    }
    maven(url = "https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") {//geckolib
        content {
			includeGroup("software.bernie.geckolib")
		}
    }
    maven (url = "https://maven.shedaniel.me/")
    maven (url = "https://maven.terraformersmc.com/releases/")
    maven (url = "https://cursemaven.com")
}

cloche {
    minecraftVersion = "1.21.1"

    metadata {
        modId = "davespotioneering"
        name = "Dave's Potioneering"
        license = "Unlicense"
        description = "Make potions more useful! In loving memory of SoterDave"

        sources = "https://github.com/terrarium-earth/Daves-Potioneering"
        issues = "https://github.com/terrarium-earth/Daves-Potioneering/issues"

        dependency {
            modId = "geckolib"
            required = true
        }

        author("SoterDave")
        author("Tfarcenim")
        contributor("PricelessKoala")
    }

    common {
        accessWideners.from("src/common/main/resources/davespotioneering.accesswidener")
    }

    neoforge {
        loaderVersion = "21.1.135"

        data()
		
		dependencies {
			// Tetra not updated yet
			// implementation fg.deobf("curse.maven:tetra-289712:4857842")
			// implementation fg.deobf("curse.maven:mutil-351914:4706136")
			
			modImplementation("software.bernie.geckolib:geckolib-neoforge-1.21.1:4.7.6")
		}

        mixins.from("src/common/main/resources/davespotioneering.mixins.json",
            "src/neoforge/main/resources/davespotioneering.neoforge.mixins.json")

        runs {
            server()
            client()

            data()
        }
    }

    fabric {
        loaderVersion = "0.16.10"

        metadata {
            entrypoint("main", "tfar.davespotioneering.DavesPotioneeringFabric")
        }

        data()
        client()

        dependencies {
            fabricApi("0.115.2")
			modApi(module("me.shedaniel.cloth:cloth-config-fabric:15.0.140"))

//			modApi("org.jetbrains:annotations:19.0.0")
			modCompileOnly("com.terraformersmc:modmenu:11.0.3") {
                isTransitive = false
			}
			modRuntimeOnly("com.terraformersmc:modmenu:11.0.3") {
				isTransitive = false
			}

			modImplementation("software.bernie.geckolib:geckolib-fabric-1.21.1:4.7.6")
        }

        mixins.from("src/common/main/resources/davespotioneering.mixins.json",
            "src/fabric/main/resources/davespotioneering.fabric.mixins.json")

        runs {
            server()
            client()
            data()
        }
    }
}