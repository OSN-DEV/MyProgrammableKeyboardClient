package tenpokei.java_conf.gr.jp.myprogrammablekeyboardclient

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : Activity() {

    // item of key
    private class Item(displayText: String, sendKey: String) {
        public val DisplayText: String = displayText
        public val SendKey: String = sendKey
    }

    // items of key set
    private class KeySet(displayText: String) {
        public val DisplayText: String = displayText
        public val Items = Array(4, { arrayOfNulls<Item>(9) })
    }

    // key type
    private val _keySet: Array<KeySet?> = arrayOfNulls(5);

    // button ids
    private var _keyIds1 = arrayOf(R.id.key1_1, R.id.key1_2, R.id.key1_3, R.id.key1_4, R.id.key1_5, R.id.key1_6, R.id.key1_7, R.id.key1_8, R.id.key1_9)
    private var _keyIds2 = arrayOf(R.id.key2_1, R.id.key2_2, R.id.key2_3, R.id.key2_4, R.id.key2_5, R.id.key2_6, R.id.key2_7, R.id.key2_8, R.id.key2_9)
    private var _keyIds3 = arrayOf(R.id.key3_1, R.id.key3_2, R.id.key3_3, R.id.key3_4, R.id.key3_5, R.id.key3_6, R.id.key3_7, R.id.key3_8, R.id.key3_9)
    private var _keyIds4 = arrayOf(R.id.key4_1, R.id.key4_2, R.id.key4_3, R.id.key4_4, R.id.key4_5, R.id.key4_6, R.id.key4_7, R.id.key4_8, R.id.key4_9)
    private var _keyIds = arrayOf(_keyIds1, _keyIds2, _keyIds3, _keyIds4)

    class Const {
        companion object {
            const val Url = "http://192.168.10.100:50000/mpk"
        }
    }

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setValues()
        this.prepareKeyType()
        this.prepareKey()

        findViewById<RadioButton>(R.id.key_type1).isChecked = true
    }

    /**
     * set value
     */
    private fun setValues() {
        // windows
        var keySet = KeySet("Windows")
        keySet.Items[0][0] = Item("F2", "f2")
        keySet.Items[0][1] = Item("F11", "f11")
        keySet.Items[0][2] = Item("F12", "f12")
        keySet.Items[0][8] = Item("PS", "ps")
        keySet.Items[1][0] = Item("F7", "f7")
        keySet.Items[1][1] = Item("F8", "f8")
        keySet.Items[1][2] = Item("F9", "f9")
        keySet.Items[2][7] = Item("Home", "home")
        keySet.Items[2][8] = Item("Page\nup", "pgup")
        keySet.Items[3][0] = Item("Copy", "ctrl+c")
        keySet.Items[3][1] = Item("Paste", "ctrl+v")
        keySet.Items[3][2] = Item("Task\nmanager", "shift+ctrl+esc")
        keySet.Items[3][7] = Item("End", "end")
        keySet.Items[3][8] = Item("Page\ndown", "pgdown")
        this._keySet[0] = keySet;

        // Visual Studio
        keySet = KeySet("Visual\nStudio")
        keySet.Items[0][0] = Item("Run", "f5")
        keySet.Items[0][1] = Item("Step\nin", "f11")
        keySet.Items[0][2] = Item("Step\nover", "f10")
        keySet.Items[1][0] = Item("Break\nPoint", "f9")
        keySet.Items[1][1] = Item("Move\nDefinition", "f12")
        keySet.Items[1][2] = Item("Search\nReference", "shift+f12")
        keySet.Items[1][3] = Item("Close\nTab", "ctrl+f4")

        keySet.Items[2][7] = Item("Home", "home")
        keySet.Items[2][8] = Item("Page\nup", "pgup")
        keySet.Items[3][7] = Item("End", "end")
        keySet.Items[3][8] = Item("Page\ndown", "pgdown")
        this._keySet[1] = keySet;

        // Android
        keySet = KeySet("Android")
        keySet.Items[0][0] = Item("Step\nin", "f7")
        keySet.Items[0][1] = Item("Step\nover", "f8")
        keySet.Items[2][7] = Item("Home", "home")
        keySet.Items[2][8] = Item("Page\nup", "pgup")
        keySet.Items[3][7] = Item("End", "end")
        keySet.Items[3][8] = Item("Page\ndown", "pgdown")
        this._keySet[2] = keySet;
    }

    /**
     * prepare key type
     */
    private fun prepareKeyType() {
        val ids = arrayOf(R.id.key_type1, R.id.key_type2, R.id.key_type3, R.id.key_type4, R.id.key_type5)
        for (i in ids.indices) {
            val keyType = findViewById<RadioButton>(ids[i])
            if (null == this._keySet[i]) {
                keyType.isEnabled = false;
                continue;
            }
            keyType.text = this._keySet[i]!!.DisplayText
            keyType.tag = i
            keyType.setOnCheckedChangeListener { radio, checked ->
                if (checked) {
                    this.showItem(Integer.parseInt(radio.tag.toString()))
                }
            }
        }
    }

    /**
     * prepare key
     */
    private fun prepareKey() {
        for (i in this._keyIds.indices) {
            for (j in this._keyIds[i].indices) {
                var key = findViewById<Button>(this._keyIds[i][j])
                key.setOnClickListener { button ->
                    this.itemClick(button.tag.toString())
                }
            }
        }
    }

    /**
     * show items
     */
    private fun showItem(index: Int) {
        val keySet = this._keySet[index]!!
        for (i in this._keyIds.indices) {
            for (j in this._keyIds[i].indices) {
                var item = keySet.Items[i][j]
                var key = findViewById<Button>(this._keyIds[i][j])
                if (null == item) {
                    key.text= null
                    key.isEnabled = false
                    key.tag = ""
                    continue
                }
                key.text = item.DisplayText
                key.tag = item.SendKey
                key.isEnabled = true
            }
        }
    }

    /**
     * item click
     */
    private fun itemClick(key: String) {
        SendRequestTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, key)
    }

    inner class SendRequestTask : AsyncTask<String, Void, Int>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String): Int {
            var conn: HttpURLConnection? = null
            var responseCode: Int = 0
            try {
                val url = URL(Const.Url)
                conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.instanceFollowRedirects = false
                conn.doOutput = true
                conn.connect()

                val printStream = PrintStream(conn.outputStream)
                printStream.print(params[0] + "=" + params[0])
                printStream.close()

                responseCode = conn.responseCode
            } catch (e: InterruptedException) {
            } catch (e: Exception) {
                return -1
            } finally {
                conn?.disconnect()
            }

            return responseCode
        }

        override fun onPostExecute(result: Int) {
            super.onPostExecute(result)
            if (200 != result) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "error:" + result, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
