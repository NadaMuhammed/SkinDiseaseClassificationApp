package com.example.gp.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.example.gp.R

class DiseaseFragment : Fragment() {
    val args by navArgs<DiseaseFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_disease, container, false)
        arguments.let {
            view.findViewById<ImageView>(R.id.diseaseImage).setImageResource(args.disease.image)
            view.findViewById<TextView>(R.id.diseaseName).text = args.disease.diseaseName
            view.findViewById<TextView>(R.id.diseaseDescription).text = args.disease.diseaseDescription
        }
        return view
    }
}