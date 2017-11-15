package io.rocketico.signapp.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import devliving.online.securedpreferencestore.SecuredPreferenceStore
import io.rocketico.signapp.R
import io.rocketico.signapp.utils.Cc
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.alert
import org.spongycastle.util.encoders.Hex
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.protocol.http.HttpService
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
                doAsync {
                    val web3: Web3j = Web3jFactory.build(HttpService("https://ropsten.infura.io/fd28b54b765d41c8d352d092576bb125"))

                    val function = Function(
                            "",
                            listOf("asd", "asd") as MutableList<Type<Any>>?,
                            listOf("ac", 1) as MutableList<TypeReference<*>>?
                    )

                    val encodedFunction = FunctionEncoder.encode(function)
                    val transaction = Transaction.createFunctionCallTransaction(
                            "",
                            BigInteger.ZERO,
                            BigInteger.ZERO,
                            BigInteger.ZERO,
                            "",
                            encodedFunction
                    );
                    val transactionResponse = web3.ethSendTransaction(transaction).sendAsync().get()
                    Log.e("ETH TX HASH", transactionResponse.transactionHash)

                    return@doAsync;
                    val ethGetTransactionCount = web3.ethGetTransactionCount(
                            "0x86738731A0cbFB07E103ae235403037346eA602e",
                            DefaultBlockParameterName.LATEST
                    ).sendAsync().get();

                    val nonce = ethGetTransactionCount.getTransactionCount();
                    Log.e("NONCE", nonce.toString())
                    val rawtx = RawTransaction.createTransaction(
                            nonce,
                            BigInteger.valueOf(39000000000),
                            BigInteger.valueOf(69000000000),
                            "0x070DD6eb24D375669fc0DEfA253501A3158BEB2B",
                            "0xa9059cbb0000000000000000000000001ca815abdd308caf6478d5e80bfc11a6556ce0ed000000000000000000000000000000000000000000000000002386f26fc10000"
                    )
                    val creds = WalletUtils.loadCredentials("111111111", wallet)
                    val message = TransactionEncoder.signMessage(rawtx, creds)
                    val ethSendTransaction: EthSendTransaction = web3.ethSendRawTransaction(Hex.toHexString(message)).sendAsync().get()

                    uiThread {
                        if (ethSendTransaction.transactionHash != null) {
                            Log.e("hash", ethSendTransaction.transactionHash)
                        } else {
                            Log.e("hash", "NULL")
                        }
                    }
                }
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
