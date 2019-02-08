rootProject.name = "zap-core-help"

val addOnsProjectName = "addOns"
include(addOnsProjectName)
include("commonFiles")

// Keep the add-ons in alphabetic order.
var addOns = listOf(
    "help",
    "help_ar_SA",
    "help_az_AZ",
    "help_bs_BA",
    "help_da_DK",
    "help_de_DE",
    "help_el_GR",
    "help_es_ES",
    "help_fa_IR",
    "help_fil_PH",
    "help_fr_FR",
    "help_hi_IN",
    "help_hr_HR",
    "help_hu_HU",
    "help_id_ID",
    "help_it_IT",
    "help_ja_JP",
    "help_ko_KR",
    "help_ms_MY",
    "help_pl_PL",
    "help_pt_BR",
    "help_ro_RO",
    "help_ru_RU",
    "help_si_LK",
    "help_sk_SK",
    "help_sl_SI",
    "help_sq_AL"
)

addOns.forEach { include("$addOnsProjectName:$it") }

rootProject.children.forEach { project -> setUpProject(settingsDir, project) }

fun setUpProject(parentDir: File, project: ProjectDescriptor) {
    project.projectDir = File(parentDir, project.name)
    project.buildFileName = "${project.name}.gradle.kts"

    if (!project.projectDir.isDirectory) {
        throw AssertionError("Project ${project.name} has no directory: ${project.projectDir}")
    }
    if (!project.buildFile.isFile) {
        throw AssertionError("Project ${project.name} has no build file: ${project.buildFile}")
    }
    project.children.forEach { project -> setUpProject(project.parent!!.projectDir, project) }
}
