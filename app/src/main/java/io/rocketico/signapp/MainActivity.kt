package io.rocketico.signapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import devliving.online.securedpreferencestore.SecuredPreferenceStore
import io.rocketico.signapp.core.TransactionHelper
import io.rocketico.signapp.utils.Cc
import io.rocketico.signapp.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.web3j.crypto.RawTransaction
import java.math.BigInteger
import java.util.regex.Pattern


class MainActivity : Activity() {
    val REQUEST_CODE_CHOOSE_WALLET = 101;
    var prefs = SecuredPreferenceStore.getSharedInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chooseWallet.setOnClickListener { startWalletChooseDialog() }
        generateHash.setOnClickListener { generateHash() }

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
                toast("Hash: $hash")
            }
        }.show()
    }

    private fun startWalletChooseDialog() {
        MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(REQUEST_CODE_CHOOSE_WALLET)
                .withFilter(Pattern.compile(".*\\.json$")) // Filtering files and directories by file name using regexp
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
}
