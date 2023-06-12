package com.senaaslantas.retrotifkotlin



import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.senaaslantas.retrofitkotlin.adapter.RecyclerViewAdapter
import com.senaaslantas.retrotifkotlin.databinding.ActivityMainBinding
import com.senaaslantas.retrotifkotlin.model.CriptoModel
import com.senaaslantas.retrotifkotlin.service.CriptoApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {
    private lateinit var binding: ActivityMainBinding

    private val BASE_URL = "https://raw.githubusercontent.com"
    private var cryptoModels: ArrayList<CriptoModel>? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null//https://raw.githubusercontent.com

    //Disposable
    private var compositeDisposable: CompositeDisposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        compositeDisposable = CompositeDisposable()

        //RecyclerView

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.RecyclerView.layoutManager = layoutManager

        loadData()

    }

    private fun loadData() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CriptoApi::class.java)


        compositeDisposable?.add(
            retrofit.getData()

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)

        )
    }


        private fun handleResponse(cryptoList: List<CriptoModel>) {
            cryptoModels = ArrayList(cryptoList)

            cryptoModels?.let {
                recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
                binding.RecyclerView.adapter = recyclerViewAdapter
            }
        }

        override fun onItemClick(cryptoModel: CriptoModel) {
            Toast.makeText(this, "Clicked : ${cryptoModel.currency}", Toast.LENGTH_LONG).show()
        }

        override fun onDestroy() {
            super.onDestroy()

            compositeDisposable?.clear()
        }

    }












