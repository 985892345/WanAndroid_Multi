package com.ndhzs.module.login.page.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.ndhzs.lib.base.ui.BaseBindActivity
import com.ndhzs.lib.config.route.LOGIN_ENTRY
import com.ndhzs.lib.utils.extensions.setOnSingleClickListener
import com.ndhzs.module.login.databinding.LoginActivityLoginBinding
import com.ndhzs.module.login.page.viewmodel.LoginViewModel
import com.ndhzs.module.login.utils.textwatcher.BaseTextWatcher

@Route(path = LOGIN_ENTRY)
class LoginActivity : BaseBindActivity<LoginActivityLoginBinding>() {
  
  // 官方写的获取 ViewModel 的扩展函数
  // 如果需要带参数，你可以看看 BaseActivity 上的头注释
  private val mViewModel by viewModels<LoginViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // 分离每个初始化功能，别只会全写在 onCreate 中
    initView()
    initClick()
    initEvent()
    initObserve()
  }
  
  private fun initView() {
    binding.loginCbRemember.isChecked = mViewModel.isRememberPassword()
  }
  
  private fun initClick() {
    binding.loginCbRemember.setOnCheckedChangeListener { _, isChecked ->
      mViewModel.changeRememberPassword(isChecked)
    }
  
    // 使用 setOnSingleClickListener 防抖 (即 500 毫秒内的多次点击无效)
    binding.loginBtnLogin.setOnSingleClickListener {
      val username = binding.loginEtUsername.text?.toString()
      val password = binding.loginEtPassword.text?.toString()
      if (username.isNullOrBlank() || password.isNullOrBlank()) {
        toast("请输入完整!")
      } else {
        mViewModel.login(username, password)
      }
    }
    
    binding.loginBtnRegister.setOnSingleClickListener {
    
    }
    
    binding.loginBtnForgetPassword.setOnSingleClickListener {
      toast("wanAndroid好像没得这个功能")
    }
    
    binding.loginBtnQq.setOnSingleClickListener {
      toast("摆设作用，未实现")
    }
    
    binding.loginBtnWechat.setOnSingleClickListener {
      toast("摆设作用，未实现")
    }
  }
  
  private fun initEvent() {
    binding.loginEtUsername.addTextChangedListener(BaseTextWatcher(binding.loginTilUsername))
    binding.loginEtPassword.addTextChangedListener(BaseTextWatcher(binding.loginTilPassword))
  }
  
  private fun initObserve() {
    mViewModel.userName.observe {
      binding.loginEtUsername.setText(it)
    }
    
    mViewModel.password.observe {
      binding.loginEtPassword.setText(it)
    }
    
    mViewModel.loginEvent.collectSuspend {
      when (it) {
        is LoginViewModel.LoginEvent.ApiFail -> {
          toast(it.error.errorMsg)
        }
        is LoginViewModel.LoginEvent.HttpFail -> {
          toast(it.error.message ?: "未知错误")
        }
        is LoginViewModel.LoginEvent.Success -> {
          toast("欢迎回来 ${it.bean.username}")
          finishAfterTransition() // 其他页面可能返回动画，所以使用这个方法
        }
      }
    }
  }
}