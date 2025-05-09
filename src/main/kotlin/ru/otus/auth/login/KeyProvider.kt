package ru.otus.auth.login

import org.springframework.stereotype.Component
import java.io.InputStream
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

private const val PRIVATE_KEY_PATH = "/keys/private.pem"
private const val PUBLIC_KEY_PATH = "/keys/public.pem"

@Component
class KeyProvider {

    private val keyFactory = KeyFactory.getInstance("RSA")

    val privateKey: PrivateKey = run {
        val pem: ByteArray = getPem(PRIVATE_KEY_PATH)
        val keySpec = PKCS8EncodedKeySpec(pem)
        keyFactory.generatePrivate(keySpec)
    }

    val publicKey: PublicKey = run {
        val pem: ByteArray = getPem(PUBLIC_KEY_PATH)
        val spec = X509EncodedKeySpec(pem)
        keyFactory.generatePublic(spec)
    }

    private fun getPem(path: String): ByteArray {
        val resource: InputStream = javaClass.getResourceAsStream(path)!!
        val keyBytes: ByteArray = resource.use { it.readAllBytes() }
        return parsePem(keyBytes)
    }

    private fun parsePem(pemBites: ByteArray): ByteArray {
        val pemStr = String(pemBites)
            .replace("-----BEGIN.*?-----".toRegex(), "")
            .replace("-----END.*?-----".toRegex(), "")
            .replace("\\s+".toRegex(), "")
        return Base64.getDecoder().decode(pemStr)
    }
}