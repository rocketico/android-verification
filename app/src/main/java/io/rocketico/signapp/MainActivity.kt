package io.rocketico.signapp

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import org.jetbrains.anko.doAsync
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.http.HttpService
import java.io.File
import java.math.BigInteger


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            doAsync {
                val web3 = Web3jFactory.build(HttpService("https://kovan.infura.io/fd28b54b765d41c8d352d092576bb125"))
                val creds = WalletUtils.loadCredentials("111111111", File(Environment.getExternalStorageDirectory(), "wallet.json"))
                val abi2: Abi2 = Abi2.load(
                        "0x9d3d8e66bf52a38b660c2b0224c9617fc73e8460",
                        web3,
                        creds,
                        BigInteger.valueOf(500000),
                        BigInteger.valueOf(4700000)
                )
                val result = abi2.echoFunction().send()
                Log.e("RESLT", result.toString());
            }

        }

    }
}
