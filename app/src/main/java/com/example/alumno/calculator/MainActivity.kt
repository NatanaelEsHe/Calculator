package com.example.alumno.calculator

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_main.*
import android.view.Surface
import android.view.WindowManager


class MainActivity : AppCompatActivity() {


    private var sw : Boolean = false
    private var dec : Boolean = true
    private var bin : Boolean = false
    private var hex : Boolean = false
    private var sw2 : Boolean = false
    private var mem1 : Double = 0.0
    private var signo : String = ""
    private var numero : String = ""
    private var memoria : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        deshabilitar(false)

        dec = true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if(outState != null){
            if(bin)
                try {
                    numero = binDecimal(tv_display.text.toString().toInt()).toString()
                } catch (e:NumberFormatException){}
            if(hex)
                numero = hexDecimal(tv_display.text.toString()).toString()
            if(dec)
                numero = tv_display.text.toString()
            outState.putString("numero", numero)

            memoria = tv_memo.text.toString()
            outState.putString("memoria",memoria)

            outState.putBoolean("sw2",sw2)
            outState.putBoolean("dec",dec)

            outState.putBoolean("bPunto",b_punto.isEnabled)
            outState.putBoolean("bPor",b_por.isEnabled)
            outState.putBoolean("bDiv",b_div.isEnabled)
            outState.putBoolean("bMul",b_mul.isEnabled)
            outState.putBoolean("bRes",b_restar.isEnabled)
            outState.putBoolean("bSum",b_sumar.isEnabled)
            outState.putBoolean("bIgual",b_res.isEnabled)
            outState.putDouble("mem1",mem1)
            outState.putString("signo",signo)
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState != null) {
            numero = savedInstanceState.getString("numero")
            tv_display.text = numero

            dec = savedInstanceState.getBoolean("dec")

            if(getRotation(this).equals("vertical")){
                if(dec) {
                    memoria = savedInstanceState.getString("memoria")
                    tv_memo.text = memoria
                } else {
                    tv_memo.text = numero
                }
                dec = true
                bin = false
                hex = false
            } else{
                memoria = savedInstanceState.getString("memoria")
                tv_memo.text = memoria
            }
            b_punto.isEnabled = savedInstanceState.getBoolean("bPunto")
            b_por.isEnabled = savedInstanceState.getBoolean("bPor")
            b_div.isEnabled = savedInstanceState.getBoolean("bDiv")
            b_mul.isEnabled = savedInstanceState.getBoolean("bMul")
            b_res.isEnabled = savedInstanceState.getBoolean("bRes")
            b_sumar.isEnabled = savedInstanceState.getBoolean("bSum")
            b_res.isEnabled = savedInstanceState.getBoolean("bIgual")
            mem1 = savedInstanceState.getDouble("mem1")
            signo = savedInstanceState.getString("signo")

            sw2 = savedInstanceState.getBoolean("sw2")
        }

    }

    fun getRotation(context: Context): String {
        val rotation = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.orientation
        when (rotation) {
            Surface.ROTATION_0,
            Surface.ROTATION_180 -> return "vertical"
            Surface.ROTATION_90 -> return "horinzontal"
            else -> {
                return "horizontal"
            }
        }
    }

    fun numero(v : View){
        val num = findViewById<Button>(v.id)
        val textNum = num.text.toString()

        when (num){
            b_ac -> {
                reset()
            }

            b_masmenos -> {
                if(!sw) {
                    tv_display.text = "-${tv_display.text.toString()}"
                    sw = true
                } else {
                    tv_display.text = tv_display.text.toString().replace("-","")
                    sw = false
                }
            }

            b_por -> {
                if(!comprobarDisplay()){
                    tv_memo.text = "${tv_memo.text.toString()}%"
                    tv_display.text = (tv_display.text.toString().toDouble() /100).toString()
                    b_por.isEnabled = false
                }
            }

            b_div -> {
                if(!comprobarDisplay()){
                    try {
                        tv_memo.text = "${tv_memo.text.toString()}÷"
                        if(bin){
                            var numBin = tv_display.text.toString().toInt()
                            var numDec = binDecimal(numBin).toDouble()
                            leerMem1("/", numDec)
                        }
                        if(hex){
                            var numHex = tv_display.text.toString()
                            var numDec = hexDecimal(numHex).toDouble()
                            leerMem1("/",numDec)
                        }
                        if(dec) {
                            leerMem1("/", tv_display.text.toString().toDouble())
                            b_por.isEnabled = false
                            b_punto.isEnabled = true
                        }
                        b_div.isEnabled = false
                        b_mul.isEnabled = false
                        b_restar.isEnabled = false
                        b_sumar.isEnabled = false
                        b_res.isEnabled = false
                    } catch(e:Exception){}
                }
            }

            b_mul -> {
                if(!comprobarDisplay()){
                    try {
                        tv_memo.text = "${tv_memo.text.toString()}x"
                        if(bin){
                            var numBin = tv_display.text.toString().toInt()
                            var numDec = binDecimal(numBin).toDouble()
                            leerMem1("x", numDec)
                        }
                        if(hex){
                            var numHex = tv_display.text.toString()
                            var numDec = hexDecimal(numHex).toDouble()
                            leerMem1("x",numDec)
                        }
                        if(dec) {
                            leerMem1("x", tv_display.text.toString().toDouble())
                            b_por.isEnabled = false
                            b_punto.isEnabled = true
                        }
                        b_div.isEnabled = false
                        b_mul.isEnabled = false
                        b_restar.isEnabled = false
                        b_sumar.isEnabled = false
                        b_res.isEnabled = false
                    } catch(e:Exception){}
                }
            }

            b_restar -> {
                if(!comprobarDisplay()){
                    try {

                        tv_memo.text = "${tv_memo.text.toString()}-"
                        if(bin){
                            var numBin = tv_display.text.toString().toInt()
                            var numDec = binDecimal(numBin).toDouble()
                            leerMem1("-", numDec)
                        }
                        if(hex){
                            var numHex = tv_display.text.toString()
                            var numDec = hexDecimal(numHex).toDouble()
                            leerMem1("-",numDec)
                        }
                        if(dec) {
                            leerMem1("-", tv_display.text.toString().toDouble())
                            b_por.isEnabled = false
                            b_punto.isEnabled = true
                        }
                        b_div.isEnabled = false
                        b_mul.isEnabled = false
                        b_restar.isEnabled = false
                        b_sumar.isEnabled = false
                        b_res.isEnabled = false
                    } catch(e:Exception){}
                }
            }

            b_sumar -> {
                if(!comprobarDisplay()){
                    try {
                        tv_memo.text = "${tv_memo.text.toString()}+"
                        if(bin){
                            var numBin = tv_display.text.toString().toInt()
                            var numDec = binDecimal(numBin).toDouble()
                            leerMem1("+", numDec)
                        }
                        if(hex){
                            var numHex = tv_display.text.toString()
                            var numDec = hexDecimal(numHex).toDouble()
                            leerMem1("+",numDec)
                        }
                        if(dec) {
                            leerMem1("+", tv_display.text.toString().toDouble())
                            b_por.isEnabled = false
                            b_punto.isEnabled = true
                        }
                        b_div.isEnabled = false
                        b_mul.isEnabled = false
                        b_restar.isEnabled = false
                        b_sumar.isEnabled = false
                        b_res.isEnabled = false
                    } catch(e:Exception){}
                }
            }

            b_A, b_B, b_C, b_D, b_E, b_F -> {
                if(tv_display.text.length <= 14) {
                    tv_display.text = "${tv_display.text.toString()}${textNum}"
                    tv_memo.text = " ${tv_memo.text.toString()}${textNum}"
                    if(!sw2) {
                        b_div.isEnabled = true
                        b_mul.isEnabled = true
                        b_restar.isEnabled = true
                        b_sumar.isEnabled = true
                        b_res.isEnabled = false

                        sw2 = true
                    } else
                        b_res.isEnabled = true
                }
            }

            b_7, b_8, b_9, b_4, b_5, b_6, b_1, b_2, b_3, b_0  -> {
                if(tv_display.text.length <= 14) {
                    tv_display.text = "${tv_display.text.toString()}${textNum}"
                    tv_memo.text = " ${tv_memo.text.toString()}${textNum}"
                    if(!sw2) {
                        if(dec) {
                            b_por.isEnabled = true
                            b_punto.isEnabled = true
                        }
                        b_div.isEnabled = true
                        b_mul.isEnabled = true
                        b_restar.isEnabled = true
                        b_sumar.isEnabled = true
                        b_res.isEnabled = false

                        sw2 = true
                    } else
                        b_res.isEnabled = true
                }
            }

            b_res -> {
                if(!comprobarDisplay()) {
                    if(bin){
                        var numBin = tv_display.text.toString().toInt()
                        var numDec = binDecimal(numBin)
                        leerMem1(signo, numDec.toDouble())

                        var numBin2 = decimalBinario(mem1.toInt())
                        tv_memo.text = "${tv_memo.text.toString()} = $numBin2"
                        tv_display.text = "$numBin2"
                    }
                    if(hex){
                        var numHex = tv_display.text.toString()
                        var numDec = hexDecimal(numHex).toDouble()
                        leerMem1(signo, numDec)

                        var numHex2 = decimalHexadecimal(mem1.toInt())
                        tv_memo.text = "${tv_memo.text.toString()} = $numHex2"
                        tv_display.text = "$numHex2"
                    }
                    if(dec) {
                        leerMem1(signo, tv_display.text.toString().toDouble())
                        tv_memo.text = "${tv_memo.text.toString()} = ${Math.round(mem1 * 100) / 100.00}"
                        tv_display.text = "$mem1"

                        b_por.isEnabled = true
                        b_punto.isEnabled = false
                    }
                    b_div.isEnabled = true
                    b_mul.isEnabled = true
                    b_restar.isEnabled = true
                    b_sumar.isEnabled = true
                    b_res.isEnabled = false
                    reset2()
                }

            }

            b_punto -> {
                if(b_punto.isEnabled) {
                    tv_display.text = "${tv_display.text.toString()}."
                    tv_memo.text = "${tv_memo.text.toString()}."
                    b_punto.isEnabled = false
                }
            }
        }
    }

    fun leerMem1( signo : String, num : Double){
        this.signo = signo
        when (signo){
            "+" -> {
                mem1 = mem1 +num
            }

            "-" -> {
                if(mem1 == 0.0)
                    mem1 = num
                else
                    mem1 = mem1 - num
            }

            "x" -> {
                if(mem1 == 0.0)
                    mem1 = num
                else
                    mem1 = mem1 * num
            }

            "/" -> {
                if(mem1 == 0.0)
                    mem1 = num
                else
                    mem1 = mem1 / num
            }
        }
        tv_display.text = ""
    }

    fun reset(){
        tv_display.text = ""
        tv_memo.text = ""
        mem1 = 0.0
        signo = ""
        sw2 = false
        deshabilitar(false)
    }

    fun reset2(){
        mem1 = 0.0
        signo = ""
    }

    fun deshabilitar(sw : Boolean){
        if(dec)
            b_por.isEnabled = !sw
        b_div.isEnabled = sw
        b_mul.isEnabled = sw
        b_restar.isEnabled = sw
        b_sumar.isEnabled = sw
        b_res.isEnabled = sw
    }

    fun comprobarDisplay() : Boolean{
        if(tv_display.text.toString().isEmpty() || tv_display.text.toString().equals("."))
            return true
        return false
    }

    fun cambiarModo(v:View){
        val modo = findViewById<RadioButton>(v.id)
        when(modo){
            b_dec -> {
                deshabilitarHorizontal()
                deshabilitarNumeros(true)
                hex = false
                bin = false
                dec = true
                reset()
            }

            b_bin -> {
                deshabilitarHorizontal()
                deshabilitarNumeros(false)
                dec = false
                hex = false
                bin = true
                reset()
            }

            b_hexa -> {
                habilitarHorizontal()
                deshabilitarFun(false)
                deshabilitarNumeros(true)
                b_punto.isEnabled = false
                dec = false
                bin = false
                hex = true
                reset()
            }
        }
    }

    fun deshabilitarNumeros(sw:Boolean){
        b_9.isEnabled = sw
        b_8.isEnabled = sw
        b_7.isEnabled = sw
        b_6.isEnabled = sw
        b_5.isEnabled = sw
        b_4.isEnabled = sw
        b_3.isEnabled = sw
        b_2.isEnabled = sw
        deshabilitarFun(sw)
    }

    fun deshabilitarFun(sw:Boolean){
        b_masmenos.isEnabled = sw
        b_por.isEnabled = sw
        b_punto.isEnabled = sw
    }

    fun deshabilitarHorizontal() {
        b_A.visibility = View.INVISIBLE
        b_B.visibility = View.INVISIBLE
        b_C.visibility = View.INVISIBLE
        b_D.visibility = View.INVISIBLE
        b_E.visibility = View.INVISIBLE
        b_F.visibility = View.INVISIBLE
    }

    fun habilitarHorizontal() {
        b_A.visibility = View.VISIBLE
        b_B.visibility = View.VISIBLE
        b_C.visibility = View.VISIBLE
        b_D.visibility = View.VISIBLE
        b_E.visibility = View.VISIBLE
        b_F.visibility = View.VISIBLE
    }

    fun decimalBinario(numero : Int) : Int{
        var binario = 0
        var bin : Double
        var digito : Int
        var exp = 0.0
        var numero = numero
        while(numero != 0){
            digito = numero % 2
            bin = binario + digito * Math.pow(10.0, exp)
            binario = bin.toInt()
            exp++
            numero /= 2
        }
        return binario
    }

    fun decimalHexadecimal(decimal : Int): String{
        var decimal= decimal
        var resto : Int
        var hexa = ""
        while(decimal>0) {
            resto=(decimal%16)
            if(resto>9)
                hexa = letras(resto)+hexa
            else
                hexa = resto.toString() + hexa
            decimal /= 16
        }
        return hexa
    }

    fun letras(n: Int): String {
        var letra = ""
        when (n) {
            10 -> letra = "A"
            11 -> letra = "B"
            12 -> letra = "C"
            13 -> letra = "D"
            14 -> letra = "E"
            15 -> letra = "F"
        }
        return letra
    }

    fun hexDecimal(s: String): Int {
        var s = s
        val digits = "0123456789ABCDEF"
        s = s.toUpperCase()
        var valor = 0
        for (i in 0 until s.length) {
            var c : Char = s[i]
            var d : Int = digits.indexOf(c)
            valor = 16 * valor + d
        }
        return valor
    }

    fun binDecimal(number: Int): Int {
        var decimal = 0
        var binario = number
        var exp = 0

        while (binario != 0) {
            val ultimo = binario % 10
            decimal += (ultimo * Math.pow(2.0, exp.toDouble())).toInt()
            exp++
            binario = binario / 10
        }
        return decimal
    }
}
