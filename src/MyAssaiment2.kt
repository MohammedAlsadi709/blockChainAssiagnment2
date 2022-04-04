import com.google.gson.GsonBuilder
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList

fun main() {

    //student: Mohammed Alsadi 120181601

    // block 1
    val genesisBlockChain = BlockChain("block 1", "0")
    BlockChain.blockChain.add(genesisBlockChain)
    BlockChain.blockChain[0].mineBlock(BlockChain.difficulty)


    // block 2
    val secondBlockChain = BlockChain("block 2", BlockChain.blockChain[BlockChain.blockChain.size - 1].hash)
    BlockChain.blockChain.add(secondBlockChain)
    BlockChain.blockChain[1].mineBlock(BlockChain.difficulty)


    // block 3
    val thirdBlockChain = BlockChain("block 3", BlockChain.blockChain[BlockChain.blockChain.size - 1].hash)
    BlockChain.blockChain.add(thirdBlockChain)
    BlockChain.blockChain[2].mineBlock(BlockChain.difficulty)


    // convert to json if blockchain is valid
    if(BlockChain.isValidBlockChain()){
        val blockChainJson: String = GsonBuilder().setPrettyPrinting().create().toJson(BlockChain.blockChain)
        println(blockChainJson)

    }else{
        println("invalid blockchain")
    }



}

class BlockChain(private var data: String, var previousHash: String) {
    var hash: String
    private var timeStamp: Long = Date().time
    private var nonce = 0

    init {
        hash = get()
    }

    companion object{
        var blockChain = ArrayList<BlockChain>()
        const val difficulty = 5


        fun getHash(input: String): String {
            return try {
                val digest = MessageDigest.getInstance("SHA-256")
                val hash = digest.digest(input.toByteArray(charset("UTF-8")))
                val hexString = StringBuffer()
                for (i in hash.indices) {
                    val hex = Integer.toHexString(0xff and hash[i].toInt())
                    if (hex.length == 1) hexString.append("0")
                    hexString.append(hex)
                }
                hexString.toString()
            } catch (e: Exception) {
                throw RuntimeException("Something Error While We are Getting Hash !!!")
            }
        }

        fun isValidBlockChain(): Boolean {
            // to validate if hash and previous hash is the same thing

            if (blockChain.isEmpty() || blockChain.size == 1) return true

            for (i in blockChain.size - 1 downTo 1) {
                if (blockChain[i].previousHash != blockChain[i - 1].hash) return false
            }
            return true
        }
    }



    private fun get(): String {
        return getHash(previousHash + timeStamp.toString() + nonce.toString() + data)
    }

    fun mineBlock(difficulty: Int) {
        val target = String(CharArray(difficulty)).replace('\u0000', '0') //Create a string with difficulty * "0"
        while (hash.substring(0, difficulty) != target) {
            nonce++
            hash = get()
        }
    }

}