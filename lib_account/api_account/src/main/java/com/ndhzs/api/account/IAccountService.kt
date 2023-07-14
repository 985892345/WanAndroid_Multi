package com.ndhzs.api.account

import com.ndhzs.api.account.utils.Value
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.io.Serializable

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/5/29 22:17
 */
interface IAccountService {
  
  /**
   * 观察学号的改变（状态）
   *
   * 有数据倒灌的 Observable，每次订阅会发送之前的最新值
   *
   * ## 注意
   * ### 1、Activity 和 Fragment 中使用一般需要切换线程
   * ```
   * observeOn(AndroidSchedulers.mainThread())
   * ```
   *
   * ### 2、生命周期问题
   * 模块中 BaseActivity 已自带了 safeSubscribeBy() 方法用于关联生命周期
   *
   * ## 其他问题
   * ### 1、为什么使用 Rxjava，不使用 Flow ?
   * Flow 目前还有很多 api 处于测试阶段，不如 Rxjava 稳定
   *
   * ### 2、单一流装换为多流
   * 如果你想对于不同学号返回给下游不同的 Observable，**需要使用 [Observable.switchMap]**，因为它可以自动停止上一个发送的 Observable
   * ```
   * 写法如下：
   * observeStuNumState()
   *   .observeOn(Schedulers.io()) // 注意：你需要使用 observeOn 才能切换线程，subscribeOn 无法切换发送源的线程
   *   .switchMap { value ->
   *     // switchMap 可以在上游发送新的数据时自动关闭上一次数据生成的 Observable
   *     value.nullUnless(Observable.never()) {
   *       if (stuNum.isEmpty()) Observable.never()
   *       else LessonDataBase.INSTANCE.getStuLessonDao() // 数据库
   *         .observeAllLesson(stuNum) // 观察数据库的数据变动，这是 Room 的响应式编程
   *         .distinctUntilChanged() // 必加，因为 Room 每次修改都会回调，所以需要加这个去重
   *         .doOnSubscribe {
   *           getLesson(stuNum, isNeedOldList).safeSubscribeBy() // 在开始订阅时请求一次云端数据
   *         }.map { StuResult(stuNum, it) }
   *         .subscribeOn(Schedulers.io())
   *     }
   *   }
   * ```
   *
   * ### 3、为什么使用 Value 包裹 ?
   * 因为 Rxjava 不允许数据为空值，所以使用 Value 包裹了一层
   *
   * - 更多注意事项请看 [observeUserInfoEvent]
   */
  fun observeUserInfoState(): Observable<Value<LoginBean>>
  
  /**
   * 观察学号的改变（事件）
   *
   * 没有数据倒灌的 Observable，即每次订阅不会发送之前的最新值
   *
   * ## 更多注意事项请看 [observeUserInfoState]
   */
  fun observeUserInfoEvent(): Observable<Value<LoginBean>>
  
  fun getUserInfo(): LoginBean?
  
  fun isLogin(): Boolean
  
  fun login(
    username: String,
    password: String
  ): Single<LoginBean>
  
  fun logout(): Completable
  
  fun register(
    username: String,
    password: String,
    rePassword: String
  ): Single<LoginBean>
  
  data class LoginBean(
    val admin: Boolean,
    val chapterTops: List<Any>,
    val coinCount: Int,
    val collectIds: List<Int>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
  ) : Serializable
}