package ir.h_niknam.circuitsolver

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import ir.h_niknam.circuitsolver.equation.CircuitSolver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast


class MainActivity : AppCompatActivity(),View.OnClickListener {
    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.ic_current->{
                activeIcon = Constatnts.CURRENT
                pv.setType(Constatnts.CURRENT);
                selectedIcon()
            }
            R.id.ic_res->{
                activeIcon = Constatnts.RES
                pv.setType(Constatnts.RES);
                selectedIcon()
            }
            R.id.ic_volt->{
                activeIcon = Constatnts.VOLT
                pv.setType(Constatnts.VOLT);
                selectedIcon()
            }
            R.id.ic_wire->{
                activeIcon = Constatnts.WIRE
                pv.setType(Constatnts.WIRE);
                selectedIcon()
            }


        }
    }

    var activeIcon = Constatnts.WIRE;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        ic_current.setOnClickListener(this)
        ic_volt.setOnClickListener(this)
        ic_wire.setOnClickListener(this)
        ic_res.setOnClickListener(this)

        button.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                pv.undo()
            }
        })
        make.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {

                showDialog(pv.endDesign())
            }
        })


    }

    fun selectedIcon(){

        ic_current.setBackgroundColor(Color.TRANSPARENT)
        ic_volt.setBackgroundColor(Color.TRANSPARENT)
        ic_wire.setBackgroundColor(Color.TRANSPARENT)
        ic_res.setBackgroundColor(Color.TRANSPARENT)

        when(activeIcon){
            Constatnts.CURRENT->{
                ic_current.setBackgroundColor(resources.getColor(R.color.colorSelected))
            }
            Constatnts.VOLT->{
                ic_volt.setBackgroundColor(resources.getColor(R.color.colorSelected))
            }
            Constatnts.WIRE->{
                ic_wire.setBackgroundColor(resources.getColor(R.color.colorSelected))
            }
            Constatnts.RES->{
                ic_res.setBackgroundColor(resources.getColor(R.color.colorSelected))
            }
        }
    }

    fun showDialog(eq:Array<String>){
        val di2 = Dialog(this@MainActivity)
        di2.requestWindowFeature(Window.FEATURE_NO_TITLE)

        di2.setContentView(R.layout.dialog)
        di2.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        val window = di2.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp

        di2.equation.text = "equations: \n"+eq[0]

        di2.noNeedHelpTv.text = "no need helps: \n"+eq[1]

        var cs = CircuitSolver(eq[0])
        var ans = cs.getAnswers()
        var t = ""
        for(i in 0..ans.size-1){
            t+= ans[i]
            t+="\n"
        }
        di2.answer.text = "answers: \n"+t


        di2.icCopy.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", di2.equation.text.toString() +"\n" +di2.answer.text.toString()+"\n"+di2.noNeedHelpTv.text.toString())
                clipboard!!.setPrimaryClip(clip)

                Toast.makeText(this@MainActivity,"Copied",Toast.LENGTH_SHORT).show()
            }

        })

        di2.setCanceledOnTouchOutside(true)
        di2.setCancelable(true)
        di2.show()

    }
}
