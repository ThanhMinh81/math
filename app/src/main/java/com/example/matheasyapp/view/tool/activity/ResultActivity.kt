package com.example.matheasyapp.view.tool.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.databinding.ActivityResultBinding
import com.example.matheasyapp.view.tool.adapter.LoanAdapter
import com.example.matheasyapp.view.tool.model.LoanPayment


class ResultActivity : AppCompatActivity() {


private lateinit var binding: ActivityResultBinding
    private lateinit var  loanAdapter : LoanAdapter
    private  lateinit var  list : ArrayList<LoanPayment?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityResultBinding.inflate(layoutInflater)
     setContentView(binding.root)
        list = ArrayList<LoanPayment?>()

        // Nhận dữ liệu từ Intent trong Activity thứ 2
        val listData: ArrayList<LoanPayment>? = intent.getParcelableArrayListExtra("myObjects")

        // Kiểm tra và sử dụng dữ liệu nếu có
        if (listData != null) {
            // Bây giờ bạn có thể sử dụng listData như là ArrayList<LoanPayment>
            for (loanPayment in listData) {
                // Đối với mỗi đối tượng LoanPayment, làm gì đó ở đây
                println("Loan payment: $loanPayment")
            }
        }
        list!!.addAll(listData!!)
        loanAdapter = LoanAdapter(this,list)
        binding.tvRcvResult.adapter = loanAdapter
        binding.tvRcvResult.layoutManager = LinearLayoutManager(this)



    }


}