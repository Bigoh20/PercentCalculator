package com.bigoblog.percentcalculator

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigoblog.percentcalculator.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    //Crear el layout manager y el adapter del recyclerview.
    private lateinit var mManager : LinearLayoutManager
    private lateinit var mAdapter : Adapter
    private lateinit var binding: ActivityMainBinding
    //Crear los sonidos.
    private var mp1 : MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //Poner arriba al recyclerView.
        setupRv()
        //Inicializar sonidos.
        setupSounds()
        //Recuperar el evento de los botones.
        binding.buttonAdd.setOnClickListener {
            getPercent()
        }
        binding.buttonClear.setOnClickListener {  deleteData() }


        //Evento del segundo porcentaje.
        binding.buttonCalculate.setOnClickListener {
            calculatePercent()
        }


    }

    private fun calculatePercent() {
        try{


        //Recuperar los textos de ambos.
        val num = binding.TIETNumber2.text.toString()
        val per = binding.TIETPercent.text.toString()
        //Validar que no estén vacíos.
        when {
            num.isEmpty() -> {
                binding.TILNumber2.error = getString(R.string.text_Error_empty)
            }
            per.isEmpty() -> {
                binding.TILPercent.error = getString(R.string.text_Error_empty)
            }
            else -> {
                //Reponer sus errores a null
                binding.TILNumber2.error = null
                binding.TILPercent.error = null
                //Calcular:
                val result = num.toDouble() * (per.toDouble() / 100)
                //Ubicar el resultado.
                binding.tvResult2.text = "Resultado: El $per % de $num es: $result"
                //Reiniciar los campos.
                binding.TIETNumber2.setText("")
                binding.TIETPercent.setText("")
            }
        }
        }catch (e : NumberFormatException){
            Snackbar.make(binding.root, "Ha ocurrido un error sintáctico", Snackbar.LENGTH_SHORT)
                .setAnchorView(binding.buttonCalculate)
                .show()
        }
    }


    private fun setupSounds() {
        mp1 = MediaPlayer.create(this, R.raw.sound_click_two)
    }

    private fun deleteData() {
       val success = mAdapter.delete()
        if(success) {
            Toast.makeText(this, "Información eliminada correctamente", Toast.LENGTH_SHORT).show()
            //Reponer el texto por defecto.
            binding.tvTotal.text = getString(R.string.text_total)
        }
        else Toast.makeText(this, "No hay items que eliminar", Toast.LENGTH_SHORT).show()


    }


    private fun setupRv() {
        mAdapter = Adapter(mutableListOf(Numbers(45.4, 5.9)))
        mManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = mManager
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.setHasFixedSize(true)

    }




    private fun getPercent() {
        //Primero verificar que tenga algo el TIET.
        if(binding.TIETNumber.text.toString().isEmpty()){
            //En caso de no, marcar el error.
            binding.TILNumber.error = getString(R.string.text_Error_empty)
        }else{
            //Reiniciar el error si tuvo uno antes.
            binding.TILNumber.error = null
            //Conseguir el número actual.
            try {


                val currentNumber = binding.TIETNumber.text.toString().toDouble()
                //Calcular el total y colocarlo en el textview. (El calculate retorna el total de la suma de los numeros)
                val textTotal = mAdapter.calculate(currentNumber, binding.checkBoxDetail.isChecked)
                //Reproducir el sonido.
                mp1?.start()

                binding.tvTotal.text = "Total: $textTotal"
                binding.TIETNumber.setText("")
            }catch (e : NumberFormatException){
                binding.TILNumber.error = getString(R.string.text_sintax_error)
            }
        }
    }




}