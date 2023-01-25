package com.app.common.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseMVVMFragment<VBinding : ViewDataBinding, VM : ViewModel>(
    private val layoutId: Int,
    private val viewModelClass: Class<VM>
) :
    BaseFragment() {

    open var useSharedViewModel: Boolean = false
    open var isStateSave: Boolean = false

    lateinit var viewModel: VM

    private var _vBinding: VBinding? = null
    val binding: VBinding get() = _vBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_vBinding==null)_vBinding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        observeData()
    }

    open fun setUpViews() {}

    open fun observeData() {}

    private fun init() {
        viewModel = if (useSharedViewModel) {
            ViewModelProvider(requireActivity()).get(viewModelClass)
        } else {
            ViewModelProvider(this).get(viewModelClass)
        }
    }


    override fun onDestroyView() {
        if (!isStateSave) _vBinding = null
        super.onDestroyView()
    }

}