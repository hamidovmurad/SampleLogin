package com.app.common.base.view

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseVBBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var baseActivity: BaseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            val activity: BaseActivity = context
            this.baseActivity = activity
        }

    }

    fun showToast(message: String, useToast: Boolean = false) {
        baseActivity.show(message, useToast)
    }

}