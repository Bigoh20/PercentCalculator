package com.bigoblog.percentcalculator

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bigoblog.percentcalculator.databinding.ItemRvBinding

class Adapter(private var items : MutableList<Numbers>) : RecyclerView.Adapter<Adapter.AdapteViewHolder>() {

    //Crear el contexto.
    private var numbers : MutableList<Double> = mutableListOf()
    private var firstTime = true
    private lateinit var context : Context


    private var detail = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapteViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_rv, parent, false)
        return AdapteViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapteViewHolder, position: Int) {
            holder.render(position)
    }

    override fun getItemCount(): Int = items.size


    //Retorna el total de todos los numeros
    fun calculate(currentNumber: Double, enableDetail : Boolean) : Double{

        //Agregar al detalle si tiene que mostrar los porcentajes cortos o largos.
        detail = enableDetail
        //Limpiar todo el array.
        items.clear()
        //Agregar el nuevo numero
        numbers.add(currentNumber)

        //Rellenar todos de nuevo con el nuevo numero..
        val total = getTotal()
        for(num in numbers){
            val percent = (num * 100) / total
            items.add(Numbers(num, percent))
        }
        //Notificar que toda la data fue cambiada.
        notifyDataSetChanged()
        //Colocar que ya no es firstTime
        firstTime = false
        //Enviar al mainActivity para que actualice el total
       return total


    }

    private fun getTotal(): Double {
        var num = 0.0
     for(number in numbers) num += number
        return num
    }


    fun delete() : Boolean{


        var size = items.size
        return if(size == 0 || firstTime) false

        else{
            //Colocar que es primer tiempo para que aparezca el mesnaje de inicio
            firstTime = true
            while(size != 0){
                size--
                items.removeAt(size)
                notifyItemRemoved(size)
            }
            //Eliminar el array de numeros tmb.
            numbers.clear()


            //Rellenar el array por defecto para que pueda pasar por render de nuevo.
            items.add(Numbers(1.0, 1.0))
            notifyDataSetChanged()
           true
        }


    }


    inner class AdapteViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRvBinding.bind(view)

        fun render(pos : Int){
                val item = items[pos]
            //Crear un objeto del main activity para enviar el total.

            if(!firstTime){
                if(!detail){
                    binding.tvNumber.text = String.format("%.2f", item.number)
                    binding.tvPercent.text = String.format("%.2f", item.percent) + "%"

                }else{
                    binding.tvNumber.text = item.number.toString()
                    binding.tvPercent.text = item.percent.toString() + "%"
                }

            }else{
                binding.tvNumber.text = "Número."
                binding.tvPercent.text = "Porcentaje."
            }


        }
    }
}