package com.devapp.trainning

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.*
import java.lang.StringBuilder
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ReadDataTask(val context: Context): AsyncTask<String,String,String>() {
    private val progressDialog = ProgressDialog(context)
    override fun onPreExecute() {
        showDialog()
        super.onPreExecute()
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun doInBackground(vararg params: String?): String {
        var content = StringBuilder()
        try {
            val url = URL(params[0])
            val httpsURLConnection = url.openConnection() as HttpsURLConnection
            httpsURLConnection.connectTimeout = 10000
            httpsURLConnection.readTimeout = 5000
            httpsURLConnection.connect()
            var inputStream = url.openStream()
            var bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            var total:Long?=0
            if (total != null) {
                while (bufferedReader.readLine().let { line=it;it!=null }){
                    total+= line!!.length
                }
            }
            bufferedReader.close()
            inputStream = url.openStream()
            bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var count =0
            while (bufferedReader.readLine().let { line=it;it!=null }){
                count+=line?.length!!
                content.append(line+"\n")
                publishProgress(((count*100/ total!!)).toString())

            }
            bufferedReader.close()
            writeToFile(content.toString())
        }catch (e:Exception){
            e.printStackTrace()
        }

        return content.toString()
    }

    private fun writeToFile(content:String) {
        val filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File("$filePath/json.txt")
        if(!file.exists()) file.createNewFile()
        val buffredWriter = BufferedWriter(FileWriter(file,false))
        buffredWriter.write(content)
        buffredWriter.close()
    }

    override fun onPostExecute(result: String?) {
        cancelDialog()
        Log.d("TAG", "onPostExecute: $result")
        super.onPostExecute(result)
    }

    override fun onProgressUpdate(vararg values: String?) {
        Log.d("TAG", "onProgressUpdate: ${values[0]}")
        progressDialog.progress = values[0]!!.toInt()
        super.onProgressUpdate(*values)
    }
    private fun showDialog(){
        progressDialog.isIndeterminate = false
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.max = 100
        progressDialog.setTitle("Downloading...")
        progressDialog.setMessage("Download Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }
    private fun cancelDialog(){
        progressDialog.dismiss()
    }
}