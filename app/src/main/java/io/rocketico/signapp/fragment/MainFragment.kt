package io.rocketico.signapp.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import devliving.online.securedpreferencestore.SecuredPreferenceStore
import io.rocketico.signapp.R
import io.rocketico.signapp.core.TransactionHelper
import io.rocketico.signapp.utils.Cc
import io.rocketico.signapp.utils.Utils
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.jetbrains.anko.customView
import org.jetbrains.anko.editText
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import java.math.BigInteger

class MainFragment : Fragment() {
    private var mListener: OnFragmentInteractionListener? = null
    val REQUEST_CODE_CHOOSE_WALLET = 101;
    var prefs = SecuredPreferenceStore.getSharedInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_main, container, false)
        view.chooseWallet.setOnClickListener { startWalletChooseDialog() }
        view.generateHash.setOnClickListener { generateHash() }
        view.currentWallet.text = prefs.getString(Cc.PREFS_KEY_CURRENT_WALLET, "")

        return view;
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun generateHash() {
        val wallet = prefs.getString(Cc.PREFS_KEY_CURRENT_WALLET, "")
        alert {
            var password: EditText? = null
            customView {
                password = editText()
            }
            password!!.setText("111111111")
            yesButton {
                val n = BigInteger.ONE
                val rawtx = RawTransaction.createTransaction(n, n, n, "", "")
                val hash = TransactionHelper.signTransaction(wallet, password?.text.toString(), rawtx)
                val creds = WalletUtils.loadCredentials("111111111", wallet)
                val message = TransactionEncoder.signMessage(rawtx, creds)
                toast("Hash: ${Utils.toHexString(message)}")
                val w: Web3j

            }
        }.show()
    }

    private fun startWalletChooseDialog() {
        MaterialFilePicker()
                .withActivity(context as Activity?)
                .withRequestCode(REQUEST_CODE_CHOOSE_WALLET)
                .withFilterDirectories(true) // Set directories filterable (false by default)
                .withHiddenFiles(true) // Show hidden files and folders
                .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE_WALLET && resultCode == Activity.RESULT_OK) {
            val selectedFile = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            prefs.edit().putString(Cc.PREFS_KEY_CURRENT_WALLET, selectedFile).apply()
            currentWallet.text = prefs.getString(Cc.PREFS_KEY_CURRENT_WALLET, "")
        }
    }

    companion object {
        fun newInstance(): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
