package com.dev.fitface.view

import android.os.Bundle
import android.util.Log
import android.view.View
import com.dev.fitface.api.api_utils.ApiStatus
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.BaseResponse
import com.dev.fitface.viewmodel.BaseViewModel

/**
 * Created by Dang Minh Duy on 10,May,2021
 */
abstract class BaseActivity<ViewModel : BaseViewModel> : BaseApplication() {

    //Live data
    lateinit var viewModel: ViewModel
    abstract fun createViewModel(): ViewModel
    abstract fun fetchData()
    abstract fun handleError(statusCode: Int?, message: String?, bundle: Bundle? = null)
    abstract fun observeData()

    abstract fun setLoadingView(): View?

    private var loadingView: View? = null
    protected fun hideProgressView() {
        loadingView?.visibility = View.GONE
    }

    protected fun showProgressView() {
        loadingView?.visibility = View.VISIBLE
    }

    private fun initViewBaseOnApiStatus(status: ApiStatus, apiStatus: Resource<Any>?, message: String?, bundle: Bundle?) {
        when (status) {
            ApiStatus.LOADING -> {
                showProgressView()
            }
            ApiStatus.SUCCESS -> {
                hideProgressView()
            }
            ApiStatus.ERROR -> {
                hideProgressView()
                //-------------------
                //không phải chỗ nào cũng toast, nếu cần thiết toast thì xử lý trong handleError
//                if(bundle?.getBoolean(Constants.BundleParam.notShowMessage, false) != true) {
//                    toastError(message)
//                }
                //-------------------
                handleError(processResponse(apiStatus)?.status ?: -1, message, bundle)
                Log.i("Debug"," -> ${processResponse(apiStatus)} -- $message -- $bundle")
            }
        }
    }

    private fun processResponse(response: Resource<Any>?): BaseResponse? {
        response?.resource?.let {
            return it as BaseResponse
        }
        return null
    }

    override fun onActivityCreated() {
        loadingView = setLoadingView()
        viewModel = createViewModel()
        viewModel?.apiStatus?.observe(this,
                { status ->
                    initViewBaseOnApiStatus(status.status, viewModel.apiStatus.value, status.message, status.bundle)
                }
        )
        observeData()
        fetchData()
    }


}