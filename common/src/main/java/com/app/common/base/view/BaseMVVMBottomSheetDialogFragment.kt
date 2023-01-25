package com.app.common.base.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseMVVMBottomSheetDialogFragment<VBinding : ViewDataBinding, VM : ViewModel>(
    private val layoutId: Int,
    private val viewModelClass: Class<VM>
)  : BottomSheetDialogFragment() {

    private lateinit var baseActivity: BaseActivity

    open var useSharedViewModel: Boolean = false

    lateinit var viewModel: VM

    private var _vBinding: VBinding? = null
    val binding: VBinding get() = _vBinding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            val activity: BaseActivity = context
            this.baseActivity = activity
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }


    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (_vBinding==null)_vBinding = DataBindingUtil.inflate(layoutInflater, layoutId, null, false)
        dialog.setContentView(binding.root)

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


    override fun onCancel(dialog: DialogInterface) {
        _vBinding = null
        super.onCancel(dialog)
    }



    fun showToast(message: String, useToast: Boolean = false) {
        baseActivity.show(message, useToast)
    }



}