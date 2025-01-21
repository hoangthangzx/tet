package com.metalsensor.gold.detector.finder.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.metalsensor.gold.detector.finder.R

class Saving_dialog(context: Context, style: Int, msg: String) : Dialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_saving)

        // Đặt kích thước dialog là match_parent
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }
}
