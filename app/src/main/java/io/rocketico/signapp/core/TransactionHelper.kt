package io.rocketico.signapp.core

import io.rocketico.signapp.utils.Utils
import org.spongycastle.util.encoders.Hex
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionUtils
import org.web3j.crypto.WalletUtils

object TransactionHelper {
    fun signTransaction(walletFile: String, password: String, transaction: RawTransaction): String {
        val credentials = WalletUtils.loadCredentials(password, walletFile);
        return Hex.toHexString(TransactionUtils.generateTransactionHash(transaction, credentials))
    }
}
