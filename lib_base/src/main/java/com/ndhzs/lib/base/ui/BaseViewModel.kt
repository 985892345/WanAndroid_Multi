package com.ndhzs.lib.base.ui

import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.*
import com.ndhzs.lib.base.utils.RxjavaLifecycle
import com.ndhzs.lib.base.BaseApp
import com.ndhzs.lib.base.utils.ToastUtils
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 *
 * ## 一、Flow 相关 collect 封装
 * ### collectLaunch()
 * ```
 * mViewModel.flow
 *     .collectLaunch {
 *         // 配合生命周期的收集方法，可以少写一个 lifecycleScope.launch {} 包在外面
 *     }
 * ```
 *
 * ## 二、Rxjava 绑定生命周期
 * ```
 * IXXXApiService::class.api
 *     .getXXX()
 *     .safeSubscribeBy {
 *         // 使用 safeSubscribeBy() 将 Rxjava 流与生命周期相关联，实现自动取消
 *     }
 * ```
 *
 * ## 三、区分 状态 与 事件
 * ### 状态
 * 一般是指 UI 状态，特点是 转屏/黑夜模式切换 后导致 重建的 Activity 能够恢复以前的 UI 状态
 *
 * ### 事件
 * 比如购买成功后弹出的 toast 就是事件，特点：只需要消费一次，重建后的 Activity 不需要再次消费
 *
 * ### 使用上的注意点
 * - LiveData 一般用于状态
 * - SharedFlow 一般用于事件（不带缓存的 SharedFlow，带有缓存的就表状态）
 *
 * 但是往往存在模棱两可的情况
 *
 * 比如：我有个观察回调，用于记录数据请求是否成功。回调中既需要设置 UI 状态，也需要弹 toast。这种情况下回调中既有状态也有事件
 *
 * ### 推荐做法
 * - ViewModel 写两个变量
 * - 一个是 `xxxState: LiveData` 用于表示状态
 * - 另一个 `xxxEvent: SharedFlow` 用于表示事件
 * - 为了减少数据源的操作量，可以这样： `xxxEvent = xxxState.asSharedFlow()`
 * - 然后只需要发送数据给 `xxxState` 即可 (`xxxEvent` 会自动接收新数据)
 *
 *
 * # 更多封装请往父类和接口查看
 */
abstract class BaseViewModel : ViewModel(), RxjavaLifecycle, ToastUtils {
  
  val appContext: Context
    get() = BaseApp.baseApp
  
  private val mDisposableList = mutableListOf<Disposable>()
  
  @CallSuper
  override fun onCleared() {
    super.onCleared()
    mDisposableList
      .filter { !it.isDisposed }
      .forEach { it.dispose() }
    mDisposableList.clear()
  }
  
  /**
   * 开启协程并收集 Flow
   */
  protected fun <T> Flow<T>.collectLaunch(action: suspend (value: T) -> Unit) {
    viewModelScope.launch {
      collect{ action.invoke(it) }
    }
  }
  
  /**
   * 返回一个缓存值为 0 表示事件的 SharedFlow，不会因为 Activity 重建而出现数据倒灌问题
   *
   * [关于状态跟事件的区别可以看这篇文章](https://juejin.cn/post/7046191406825603109)
   */
  protected fun <T> LiveData<T>.asShareFlow(): SharedFlow<T> {
    val sharedFlow = MutableSharedFlow<T>()
    observeForever {
      viewModelScope.launch {
        sharedFlow.emit(it)
      }
    }
    return sharedFlow
  }
  
  
  
  
  
  
  
  
  /**
   * 实现 [RxjavaLifecycle] 的方法，用于带有生命周期的调用
   */
  final override fun onAddRxjava(disposable: Disposable) {
    mDisposableList.add(disposable)
  }
}