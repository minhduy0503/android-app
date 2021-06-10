package com.dev.fitface.view.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dev.fitface.R
import com.dev.fitface.utils.AppUtils
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.SharedPrefs
import kotlinx.android.synthetic.main.activity_collection.*

class CollectionActivity : AppCompatActivity(), View.OnClickListener {

    private var isValidCollection: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        initListener()
    }

    private fun initListener() {
        collectionLayout.setOnClickListener {
            edtCollection.clearFocus()
            AppUtils.hideSoftKeyboard(it)
        }

        edtCollection.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvErrorCollection.visibility =
                    if (tvErrorCollection.visibility == View.VISIBLE && s.isNullOrBlank()) View.VISIBLE else View.INVISIBLE

            }

            override fun afterTextChanged(s: Editable?) {
                isValidCollection = !s.isNullOrBlank() && s.contains("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?\$") == false
            }

        })

        btnConfirm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnConfirm -> {
                if (!isValidCollection){
                    showError()
                }
                else {
                    val collection = edtCollection.text.toString()
                    SharedPrefs.instance.put(Constants.Param.collection, collection)
                    AppUtils.startActivity(this, LoginActivity::class.java)
                }
            }
        }
    }

    private fun showError() {
        tvErrorCollection.visibility = if (!isValidCollection) View.VISIBLE else View.INVISIBLE
    }

}