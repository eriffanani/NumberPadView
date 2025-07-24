package com.erif.numberpaddemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.erif.numberpad.NumberPadListener
import com.erif.numberpad.NumberPadView

class FrgPasswordInput : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frg_password_input, container, false)
        val numberPad: NumberPadView = view.findViewById(R.id.frg_input_numberPadView)
        numberPad.addListener(object : NumberPadListener {
            override fun onClickNumber(number: String) {
                toast(number)
            }

            override fun onClickLeftPane() {
                toast("Left Pane")
            }

            override fun onClickBackSpace() {
                toast("Backspace")
            }

        })
        return view
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}