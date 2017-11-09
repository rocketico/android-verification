package io.rocketico.signapp

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import io.rocketico.signapp.core.TransactionHelper
import org.jetbrains.anko.toast
import org.web3j.crypto.RawTransaction
import java.io.File
import java.math.BigInteger


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            val wallet = File(Environment.getExternalStorageDirectory(), "wallet.json").absolutePath
            val n = BigInteger.ONE
            val rawtx = RawTransaction.createTransaction(n, n, n, "", "")
            val hash = TransactionHelper.signTransaction(wallet, "111111111", rawtx)
            toast("Hash: $hash")
        }

    }
}
