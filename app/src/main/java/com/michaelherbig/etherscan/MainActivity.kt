package com.michaelherbig.etherscan

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.michaelherbig.etherscanapi.Etherscan
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var api = Etherscan("YOUR_API_KEY")

        api.getContractABI("0xBB9bc244D798123fDe783fCc1C72d3Bb8C189413")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            hello.text = result.result
                        },
                        { e ->
                            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
                        }
                )
    }
}
