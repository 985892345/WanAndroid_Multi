package rule

import org.gradle.api.Project
import java.io.File

/**
 * 项目命名空间检查，主要用于新建项目的时候没有按规范写好模块包名
 *
 * @author 985892345
 * 2022/12/20 17:42
 */
object ModuleNamespaceCheckRule : AndroidProjectChecker.ICheckRule {

  /**
   * 得到正确的 namespace
   *
   * TODO 如果你用该项目进行其他项目开发，但又不想使用本项目统一的包名，你可以改一下该方法
   */
  fun getCorrectNamespace(project: Project): String {
    return getPackagePrefix(project) + project.name.replace("_", ".")
  }

  // 得到包命名前缀
  private fun getPackagePrefix(project: Project): String {
    return "com.ndhzs."
  }
  
  override fun onConfig(project: Project) {
    val namespace = getCorrectNamespace(project)
    val file = project.projectDir
      .resolve("src")
      .resolve("main")
      .resolve("java")
      .resolve(namespace.replace(".", File.separator))
    if (!file.exists()) {
      val rule = """
        
        模块包名命名规范：
        1、一级模块
          一级模块以 com.mredrock.cyxbs.xxx 包名命名。如：module_course 为 com.mredrock.cyxbs.course
        2、二级模块
          一级模块以 com.mredrock.cyxbs.[module|lib|api].xxx 包名命名。如：api_course 为 com.mredrock.cyxbs.api.course
          
        你当前 ${project.name} 模块的包名应该改为：$namespace
        ${project.projectDir}
        
        TODO 如果你用该项目进行其他项目开发，但又不想使用本项目统一的包名，你可以修改以下该方法 build_logic/plugin/checker 模块下的 ModuleNamespaceCheckRule
        
      """.trimIndent()
      throw RuntimeException("${project.name} 模块包名错误！" + rule)
    }
  }
}