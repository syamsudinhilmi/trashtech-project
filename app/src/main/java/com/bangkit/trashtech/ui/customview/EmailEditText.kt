package com.bangkit.trashtech.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.bangkit.trashtech.R

class EmailEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                error = null
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = null
            }

            override fun afterTextChanged(p0: Editable?) {
                if (!isValidEmail(p0.toString())) {
                    setError(context.getString(R.string.email_alert), null)
                }
            }
        })
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
