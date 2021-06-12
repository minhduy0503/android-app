package com.dev.fitface.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.fitface.R

/**
 * A simple [Fragment] subclass.
 * Use the [LockPasscodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LockPasscodeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lock_passcode, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            LockPasscodeFragment().apply {
                arguments = bundle
            }
    }
}