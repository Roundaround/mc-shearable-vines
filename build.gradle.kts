plugins {
  id("me.roundaround.allay")
}

allay {
  displayName.set("Shearable Vines")
  description.set("Use shears on vines to stop them from growing!")
  authors.set(listOf("Roundaround"))
  license.set("MIT")
  homepage.set("https://modrinth.com/mod/shearable-vines")
  repository.set("https://github.com/Roundaround/mc-shearable-vines")
  issues.set("https://github.com/Roundaround/mc-shearable-vines/issues")
  logoFile.set("assets/shearablevines/banner.png")

  modrinth {
    projectId.set("shearable-vines")
  }

  curseforge {
    projectId.set(1502548)
  }

  release {
    versionType.set("release")
    minecraftVersions("26.1".."26.1.2")
    changelogDir.set(file("changelogs"))
  }
}
