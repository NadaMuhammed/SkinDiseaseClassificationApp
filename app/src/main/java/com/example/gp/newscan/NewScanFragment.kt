package com.example.gp.newscan

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.*
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.gp.R
import com.example.gp.utils.RealPathUtil
import com.example.gp.ml.Model1
import com.example.gp.ml.Model2
import com.example.gp.ml.Model3
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage

class NewScanFragment : Fragment() {
    private val CAMERA_REQUEST = 1888
    private var imageView: ImageView? = null
    private val MY_CAMERA_PERMISSION_CODE = 100
    lateinit var path: String
    lateinit var bitmap: Bitmap
    val imageSize = 128
    lateinit var resultTextView: TextView
    var result: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_scan, container, false)
        displayPopUpDialog()

        val labels: Array<String> = arrayOf(
            "Disorders of Pigmentation",
            "Connective Tissue diseases",
            "Acne and Rosacea",
            "Systemic Disease",
            "Poison Ivy (Contact Dermatitis)",
            "Vascular Tumors",
            "Urticaria (Hives)",
            "Atopic Dermatitis",
            "Bullous Disease",
            "Normal Skin",
            "Hair Loss Photos (Allopecia)",
            "Fungal Infections (tinea ringworm, candidasis)",
            "papulo-squamous diseases",
            "Melanoma Skin Cancer Nevi and Moles",
            "Nail Fungus",
            "Scabies Lyme Disease and other Infestations and Bites",
            "Eczema",
            "Drug Eruptions",
            "Not Skin Image",
            "Viral infection and STDS",
            "Seborrheic Keratoses and other Benign Tumors",
            "Actinic Keratosis Basal Cell Carcinoma and other Malignant Lesions",
            "Vasculitis",
            "Bacterial Infections",
            "Warts Molluscum and other Viral Infections"
        )

//        var cnt = 0
//        try {
//            val bufferReader = BufferedReader(InputStreamReader(requireContext().assets.open("labels.txt")))
//            var line = bufferReader.readLine()
//            while (line!=null){
//                labels[cnt] = line
//                cnt++
//                line = bufferReader.readLine()
//            }
//            Log.v("labels", labels.toString())
//        }catch (e: java.lang.Exception){
//            Log.v("e", e.message.toString())
//        }
        imageView = view.findViewById(R.id.add_img)
        val cameraButton = view.findViewById<Button>(R.id.openCamera_btn)
        val filesButton = view.findViewById<Button>(R.id.browseFiles_btn)
        resultTextView = view.findViewById<TextView>(R.id.result)
        val scanButton = view.findViewById<Button>(R.id.scanBtn)

        setHasOptionsMenu(true)

        filesButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent()
                intent.setType("image/*")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(intent, 10)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
        }

        cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.CAMERA),
                    MY_CAMERA_PERMISSION_CODE
                )
            } else {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        }

        scanButton.setOnClickListener {
            try {
//                val model = Model1.newInstance(requireContext())
//                bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true)


//            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 3), DataType.FLOAT32)
//            inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).buffer)

//                val inputFeature0 = TensorImage(DataType.FLOAT32)
//                inputFeature0.load(bitmap)
//
//                val outputs = model.process(inputFeature0.tensorBuffer)
//                val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray


                /////////////////////////////////Model2///////////////////////////////////////////
                val model = Model3.newInstance(requireContext())
                bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true)

                val inputFeature0 = TensorImage(DataType.FLOAT32)
                inputFeature0.load(bitmap)

                val outputs = model.process(inputFeature0.tensorBuffer)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
                model.close()

//                resultTextView.text = labels[getMax(outputFeature0)].toString()
                val handler = Handler()
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setMessage(labels[getMax(outputFeature0)])
                result = labels[getMax(outputFeature0)]
                builder.setTitle("Scan Result")
                val dialog: AlertDialog = builder.create()
                builder.setPositiveButton("Ok") { _, _ -> }
                dialog.show()

                model.close()
            } catch (exception: Exception) {
                Log.v("ex", exception.toString())
            }
        }
        return view
    }

    private fun displayPopUpDialog() {
        var popupDialog = Dialog(requireContext())
        popupDialog.setCancelable(false)
        popupDialog.setContentView(R.layout.popup_dialog)
        popupDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        var addphoto = popupDialog.findViewById<Button>(R.id.addPhoto)
        addphoto.setOnClickListener {
            popupDialog.dismiss()
        }
        popupDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            val context = this
            path = RealPathUtil.getRealPath(requireContext(), uri!!).toString()
            var bitmapp = BitmapFactory.decodeFile(path)
            imageView?.setImageBitmap(bitmapp)
            imageView?.visibility = View.VISIBLE
            bitmap = bitmapp
            Log.v("path", path)
        }
        if (requestCode === CAMERA_REQUEST && resultCode === Activity.RESULT_OK) {
            var bitmapp = data?.getExtras()?.get("data")
            imageView!!.setImageBitmap(bitmapp as Bitmap?)
            imageView?.visibility = View.VISIBLE
            bitmap = bitmapp as Bitmap
        }
    }


    private fun getMax(arr: FloatArray): Int {
        var max = 0
        for (i in arr.indices) {
            if (arr[i] > arr[max]) {
                max = i
            }
        }
        return max
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode === MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "camera permission granted", Toast.LENGTH_LONG)
                    .show()
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Toast.makeText(requireContext(), "camera permission denied", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share_menu, menu)
        val share = menu.findItem(R.id.menu_share)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_share) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, result)
            startActivity(Intent.createChooser(intent, "Share Result: "))
        }
        return super.onOptionsItemSelected(item)
    }
}