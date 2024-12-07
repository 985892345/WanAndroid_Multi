import com.android.build.api.dsl.ApplicationBuildFeatures
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryBuildFeatures
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * 使用 ARouter
 *
 * 单独给每个模块都添加而不是直接在 build-logic 中全部添加的原因:
 * - 为了按需引入 kapt
 * - 部分 lib 模块只使用依赖，不包含注解
 *
 * @param isNeedKsp 是否需要处理注解，对于非实现模块是不需要处理注解的，比如 api 模块
 */
fun Project.useARouter(isNeedKsp: Boolean = !name.startsWith("api_")) {
  if (isNeedKsp) {
    // kapt 按需引入
    apply(plugin = "com.google.devtools.ksp")
    extensions.configure<KspExtension> {
      arg("AROUTER_MODULE_NAME", project.name)
    }
    dependencies {
      "ksp"(libsEx.`arouter-compiler`)
    }
  }
  dependencies {
    "implementation"(libsEx.`arouter`)
  }
}

/**
 * 使用 DataBinding
 * @param isNeedKapt 是否只依赖而不开启 DataBinding，默认开启 DataBinding
 */
fun Project.useDataBinding(isNeedKapt: Boolean = !name.startsWith("api_")) {
  if (isNeedKapt) {
    // kapt 按需引入
    apply(plugin = "org.jetbrains.kotlin.kapt")
    extensions.configure(CommonExtension::class.java) {
      buildFeatures {
        when (this) {
          is LibraryBuildFeatures -> dataBinding = true // com.android.library 插件的配置
          is ApplicationBuildFeatures -> dataBinding = true // com.android.application 插件的配置
        }
      }
    }
  }
  dependencies {
    "implementation"(libsEx.`androidx-databinding`)
    "implementation"(libsEx.`androidx-databinding-ktx`)
  }
}

/**
 * 使用 AutoService
 * todo 感觉可以切换为我的 KtProvider https://github.com/985892345/KtProvider
 */
fun Project.useAutoService(isNeedKapt: Boolean = !name.startsWith("api_")) {
  if (isNeedKapt) {
    // kapt 按需引入
    apply(plugin = "org.jetbrains.kotlin.kapt")
    dependencies {
      "kapt"(libsEx.`autoService-compiler`)
    }
  }
  dependencies {
    // 谷歌官方的一种动态加载库 https://github.com/google/auto/tree/main/service
    "compileOnly"(libsEx.autoService)
  }
}

/**
 * 使用 Room，已默认支持与 Kt 协程一起使用
 * @param rxjava 依赖 room-rxjava
 * @param paging 依赖 room-paging
 */
fun Project.useRoom(
  rxjava: Boolean = false,
  paging: Boolean = false,
) {
  // ksp 按需引入
  apply(plugin = "com.google.devtools.ksp")
  extensions.configure<KspExtension> {
    arg("room.schemaLocation", "${project.projectDir}/schemas") // room 的架构导出目录
    // https://developer.android.com/jetpack/androidx/releases/room#compiler-options
    // 启用 Gradle 增量注释处理器
    arg("room.incremental", "true")
  }
  dependencies {
    "implementation"(libsEx.`androidx-room`)
    "implementation"(libsEx.`androidx-room-ktx`)
    "ksp"(libsEx.`androidx-room-compiler`)
    if (rxjava) {
      "implementation"(libsEx.`androidx-room-rxjava`)
    }
    if (paging) {
      "implementation"(libsEx.`androidx-room-paging`)
    }
  }
}


/**
 * glide 的配置，我看了一下目前只有 module_map 模块在用，而且只是用来下载文件获取进度，为什么不直接用 okhttp 下载呢？
 * 你们啥时候去把这个逻辑干掉，那就不需要使用这个 kapt 了
 */
@Deprecated("仅限 module_map 使用，其他模块请直接依赖 glide 即可")
fun Project.useGlide() {
  //kapt 按需引入
  apply(plugin = "org.jetbrains.kotlin.kapt")
  dependencies {
    "implementation"(libsEx.glide)
    "kapt"(libsEx.`glide-compiler`)
  }
}